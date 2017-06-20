package com.nalashaa.fhir.provider;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

/**
 * This is a resource provider which stores Immunization resources in memory
 * using a HashMap. This is obviously not a production-ready solution for many
 * reasons, but it is useful to help illustrate how to build a fully-functional
 * server.
 */
public class ImmunizationResourceProvider implements IResourceProvider {

	/**
	 * This map has a resource ID as a key, and each key maps to a Deque list
	 * containing all versions of the resource with that ID.
	 */
	private Map<Long, Deque<Immunization>> myIdToPatientVersions = new HashMap<Long, Deque<Immunization>>();

	/**
	 * This is used to generate new IDs
	 */
	private long myNextId = 1;

	/**
	 * Constructor, which pre-populates the provider with one resource instance.
	 */
	public ImmunizationResourceProvider() {
		long resourceId = myNextId++;

		Immunization immunization = new Immunization();
		immunization.setId(Long.toString(resourceId));
		immunization.addIdentifier();
		immunization.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
		immunization.getIdentifier().get(0).setValue("00002");
		immunization.addReaction().setReported(true);

		LinkedList<Immunization> list = new LinkedList<Immunization>();
		list.add(immunization);

		myIdToPatientVersions.put(resourceId, list);

	}

	/**
	 * Stores a new version of the patient in memory so that it can be retrieved
	 * later.
	 * 
	 * @param theImmunization
	 *            The patient resource to store
	 * @param theId
	 *            The ID of the patient to retrieve
	 */
	private void addNewVersion(Immunization theImmunization, Long theId) {
		InstantDt publishedDate;
		if (!myIdToPatientVersions.containsKey(theId)) {
			myIdToPatientVersions.put(theId, new LinkedList<Immunization>());
			publishedDate = InstantDt.withCurrentTime();
		} else {
			Immunization currentPatitne = myIdToPatientVersions.get(theId).getLast();
			Map<ResourceMetadataKeyEnum<?>, Object> resourceMetadata = currentPatitne.getResourceMetadata();
			publishedDate = (InstantDt) resourceMetadata.get(ResourceMetadataKeyEnum.PUBLISHED);
		}

		/*
		 * PUBLISHED time will always be set to the time that the first version
		 * was stored. UPDATED time is set to the time that the new version was
		 * stored.
		 */
		theImmunization.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, publishedDate);
		theImmunization.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, InstantDt.withCurrentTime());

		Deque<Immunization> existingVersions = myIdToPatientVersions.get(theId);

		// We just use the current number of versions as the next version number
		String newVersion = Integer.toString(existingVersions.size());

		// Create an ID with the new version and assign it back to the resource
		IdDt newId = new IdDt("Immunization", Long.toString(theId), newVersion);
		theImmunization.setId(newId);

		existingVersions.add(theImmunization);
	}

	/**
	 * The "@Create" annotation indicates that this method implements
	 * "create=type", which adds a new instance of a resource to the server.
	 */
	@Create()
	public MethodOutcome createPatient(@ResourceParam Immunization thePatient) {
		validateResource(thePatient);

		// Here we are just generating IDs sequentially
		long id = myNextId++;

		addNewVersion(thePatient, id);

		// Let the caller know the ID of the newly created resource
		return new MethodOutcome(new IdDt(id));
	}

	@Search
	public List<Immunization> findPatientsUsingArbitraryCtriteria() {
		LinkedList<Immunization> retVal = new LinkedList<Immunization>();

		for (Deque<Immunization> nextPatientList : myIdToPatientVersions.values()) {
			Immunization nextPatient = nextPatientList.getLast();
			retVal.add(nextPatient);
		}

		return retVal;
	}

	/**
	 * The getResourceType method comes from IResourceProvider, and must be
	 * overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Immunization> getResourceType() {
		return Immunization.class;
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
	public Immunization readPatient(@IdParam IdDt theId) {
		Deque<Immunization> retVal;
		try {
			retVal = myIdToPatientVersions.get(theId.getIdPartAsLong());
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
			for (Immunization nextVersion : retVal) {
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
	 *            This is the ID of the patient to update
	 * @param thePatient
	 *            This is the actual resource to save
	 * @return This method returns a "MethodOutcome"
	 */
	@Update()
	public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Immunization thePatient) {
		validateResource(thePatient);

		Long id;
		try {
			id = theId.getIdPartAsLong();
		} catch (DataFormatException e) {
			throw new InvalidRequestException("Invalid ID " + theId.getValue() + " - Must be numeric");
		}

		/*
		 * Throw an exception (HTTP 404) if the ID is not known
		 */
		if (!myIdToPatientVersions.containsKey(id)) {
			throw new ResourceNotFoundException(theId);
		}

		addNewVersion(thePatient, id);

		return new MethodOutcome();
	}

	/**
	 * This method just provides simple business validation for resources we are
	 * storing.
	 * 
	 * @param thePatient
	 *            The patient to validate
	 */
	private void validateResource(Immunization thePatient) {
		/*
		 * Our server will have a rule that patients must have a family name or
		 * we will reject them
		 */
		System.out.println("thePatient.getDoseQuantity()" + thePatient.getDoseQuantity());
		// if (thePatient.getDoseQuantity().isEmpty()) {
		// OperationOutcome outcome = new OperationOutcome();
		// outcome.addIssue().setSeverity(IssueSeverityEnum.FATAL).setDetails("No
		// dose quantity provided, Immunization resources must have dose
		// quantity.");
		// throw new UnprocessableEntityException(outcome);
		// }
	}

}
