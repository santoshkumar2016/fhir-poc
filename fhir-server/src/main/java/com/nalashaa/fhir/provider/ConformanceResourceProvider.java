package com.nalashaa.fhir.provider;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class ConformanceResourceProvider implements IResourceProvider {

	/**
	 * This map has a resource ID as a key, and each key maps to a Deque list
	 * containing all versions of the resource with that ID.
	 */
	private Map<Long, Deque<Conformance>> myIdToConformanceVersions = new HashMap<Long, Deque<Conformance>>();

	/**
	 * This is used to generate new IDs
	 */
	private long myNextId = 1;

	/**
	 * Constructor, which pre-populates the provider with one resource instance.
	 */
	public ConformanceResourceProvider() {
		long resourceId = myNextId++;

		Conformance Conformance = new Conformance();
		Conformance.setId(Long.toString(resourceId));
		NarrativeDt nDt = new NarrativeDt();
		nDt.setDiv("This is NALASHAA FHIR Server. This server can serve Patient resource.");
		nDt.setStatus(NarrativeStatusEnum.GENERATED);
		Conformance.setText(nDt);
		

		LinkedList<Conformance> list = new LinkedList<Conformance>();
		list.add(Conformance);

		myIdToConformanceVersions.put(resourceId, list);

	}

	/**
	 * Stores a new version of the Conformance in memory so that it can be retrieved
	 * later.
	 * 
	 * @param theConformance
	 *            The Conformance resource to store
	 * @param theId
	 *            The ID of the Conformance to retrieve
	 */
	private void addNewVersion(Conformance theConformance, Long theId) {
		InstantDt publishedDate;
		if (!myIdToConformanceVersions.containsKey(theId)) {
			myIdToConformanceVersions.put(theId, new LinkedList<Conformance>());
			publishedDate = InstantDt.withCurrentTime();
		} else {
			Conformance currentPatitne = myIdToConformanceVersions.get(theId).getLast();
			Map<ResourceMetadataKeyEnum<?>, Object> resourceMetadata = currentPatitne.getResourceMetadata();
			publishedDate = (InstantDt) resourceMetadata.get(ResourceMetadataKeyEnum.PUBLISHED);
		}

		/*
		 * PUBLISHED time will always be set to the time that the first version
		 * was stored. UPDATED time is set to the time that the new version was
		 * stored.
		 */
		theConformance.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, publishedDate);
		theConformance.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, InstantDt.withCurrentTime());

		Deque<Conformance> existingVersions = myIdToConformanceVersions.get(theId);

		// We just use the current number of versions as the next version number
		String newVersion = Integer.toString(existingVersions.size());

		// Create an ID with the new version and assign it back to the resource
		IdDt newId = new IdDt("Conformance", Long.toString(theId), newVersion);
		theConformance.setId(newId);

		existingVersions.add(theConformance);
	}

	/**
	 * The "@Create" annotation indicates that this method implements
	 * "create=type", which adds a new instance of a resource to the server.
	 */
	@Create()
	public MethodOutcome createConformance(@ResourceParam Conformance theConformance) {
		validateResource(theConformance);

		// Here we are just generating IDs sequentially
		long id = myNextId++;

		addNewVersion(theConformance, id);

		// Let the caller know the ID of the newly created resource
		return new MethodOutcome(new IdDt(id));
	}

	@Search
	public List<Conformance> findConformancesUsingArbitraryCtriteria() {
		LinkedList<Conformance> retVal = new LinkedList<Conformance>();

		for (Deque<Conformance> nextConformanceList : myIdToConformanceVersions.values()) {
			Conformance nextConformance = nextConformanceList.getLast();
			retVal.add(nextConformance);
		}

		return retVal;
	}

	/**
	 * The getResourceType method comes from IResourceProvider, and must be
	 * overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Conformance> getResourceType() {
		return Conformance.class;
	}

	/**
	 * This is the "read" operation. The "@Read" annotation indicates that this
	 * method supports the read and/or vread operation.
	 * <p>
	 * Read operations take a single parameter annotated with the
	 * {@link IdParam} paramater, and should return a single resource instance.
	 * </p>
	 * 
	 * @param theId
	 *            The read operation takes one parameter, which must be of type
	 *            IdDt and must be annotated with the "@Read.IdParam"
	 *            annotation.
	 * @return Returns a resource matching this identifier, or null if none
	 *         exists.
	 */
	@Read(version = true)
	public Conformance readConformance(@IdParam IdDt theId) {
		Deque<Conformance> retVal;
		try {
			retVal = myIdToConformanceVersions.get(theId.getIdPartAsLong());
		} catch (NumberFormatException e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an
			 * unknown resource
			 */
			throw new ResourceNotFoundException(theId);
		}

		if (theId.hasVersionIdPart() == false) {
			return retVal.getLast();
		} else {
			for (Conformance nextVersion : retVal) {
				String nextVersionId = nextVersion.getId().getVersionIdPart();
				if (theId.getVersionIdPart().equals(nextVersionId)) {
					return nextVersion;
				}
			}
			// No matching version
			throw new ResourceNotFoundException("Unknown version: " + theId.getValue());
		}

	}

	/**
	 * The "@Update" annotation indicates that this method supports replacing an
	 * existing resource (by ID) with a new instance of that resource.
	 * 
	 * @param theId
	 *            This is the ID of the Conformance to update
	 * @param theConformance
	 *            This is the actual resource to save
	 * @return This method returns a "MethodOutcome"
	 */
	@Update()
	public MethodOutcome updateConformance(@IdParam IdDt theId, @ResourceParam Conformance theConformance) {
		validateResource(theConformance);

		Long id;
		try {
			id = theId.getIdPartAsLong();
		} catch (DataFormatException e) {
			throw new InvalidRequestException("Invalid ID " + theId.getValue() + " - Must be numeric");
		}

		/*
		 * Throw an exception (HTTP 404) if the ID is not known
		 */
		if (!myIdToConformanceVersions.containsKey(id)) {
			throw new ResourceNotFoundException(theId);
		}

		addNewVersion(theConformance, id);

		return new MethodOutcome();
	}

	/**
	 * This method just provides simple business validation for resources we are
	 * storing.
	 * 
	 * @param theConformance
	 *            The Conformance to validate
	 */
	private void validateResource(Conformance theConformance) {
		/*
		 * Our server will have a rule that Conformances must have a family name or
		 * we will reject them
		 */
		//System.out.println("theConformance.getDoseQuantity()" + theConformance.getDoseQuantity());
		// if (theConformance.getDoseQuantity().isEmpty()) {
		// OperationOutcome outcome = new OperationOutcome();
		// outcome.addIssue().setSeverity(IssueSeverityEnum.FATAL).setDetails("No
		// dose quantity provided, Conformance resources must have dose
		// quantity.");
		// throw new UnprocessableEntityException(outcome);
		// }
	}

}
