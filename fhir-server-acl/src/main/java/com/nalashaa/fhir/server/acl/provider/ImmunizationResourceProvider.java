package com.nalashaa.fhir.server.acl.provider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nalashaa.fhir.server.acl.db.model.ImmunizationEn;
import com.nalashaa.fhir.server.acl.service.ImmunizationtService;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

/**
 * 
 * This is a resource provider for resource type Immunization
 * 
 * @author santosh
 *
 */
@Component
public class ImmunizationResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(ImmunizationResourceProvider.class);

	@Autowired
	private ImmunizationtService immunizationService;

	/**
	 * Constructor
	 */
	public ImmunizationResourceProvider() {
		logger.info("************************* ImmunizationResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * NOT SUPPORTED
	 * 
	 * Stores a new version of the Immunization so that it can be retrieved later.
	 * 
	 * @param theImmunization
	 *            The Immunization resource to store
	 * @param theId
	 *            The ID of the Immunization to retrieve
	 */
	private void addNewVersion(Immunization theImmunization, Long theId) {
	}

	/**
	 * Adds a new instance of Immunization resource to the server.
	 */
	@Create()
	public MethodOutcome createImmunization(@ResourceParam Immunization theImmunization) {
		// validateResource(theImmunization);

		logger.info("***** Creating Immunization resource *****");
		Integer immunizationId = immunizationService.saveImmunization(theImmunization);
		logger.debug("Immunization resource with Id : {} created successfully.", immunizationId);

		return new MethodOutcome(new IdDt(immunizationId), true);
	}

	/**
	 * This method searches by any parameter
	 * 
	 * @param addressCity
	 *            a string containing the name of the search criteria,
	 *            address-city
	 * 
	 * @return This method returns a list of Immunizations. This list may contain
	 *         multiple matching resources, or it may also be empty.
	 */
	@Search()
	public List<Immunization> findImmunizationsBy(@OptionalParam(name = Immunization.SP_DATE) StringDt date,
			@OptionalParam(name = Immunization.SP_DOSE_SEQUENCE) StringDt doseSequence,
			@OptionalParam(name = Immunization.SP_IDENTIFIER) StringDt identifier,
			@OptionalParam(name = Immunization.SP_LOCATION) StringDt location,
			@OptionalParam(name = Immunization.SP_LOT_NUMBER) StringDt lotNumber,
			@OptionalParam(name = Immunization.SP_MANUFACTURER) StringDt manufacturer,
			@OptionalParam(name = Immunization.SP_NOTGIVEN) StringDt notGiven,
			@OptionalParam(name = Immunization.SP_PATIENT) StringDt patient,
			@OptionalParam(name = Immunization.SP_PERFORMER) StringDt performer,
			@OptionalParam(name = Immunization.SP_REACTION) StringDt reaction,
			@OptionalParam(name = Immunization.SP_REACTION_DATE) StringDt reactionDate,
			@OptionalParam(name = Immunization.SP_REASON) StringDt reason,
			@OptionalParam(name = Immunization.SP_REASON_NOT_GIVEN) StringDt reasonNotGiven,
			@OptionalParam(name = Immunization.SP_REQUESTER) StringDt requester,
			@OptionalParam(name = Immunization.SP_STATUS) StringDt status,
			@OptionalParam(name = Immunization.SP_VACCINE_CODE) StringDt vaccineCode,
			@OptionalParam(name = Immunization.SP_RES_ID) StringDt resId,
			@OptionalParam(name = Immunization.SP_RES_LANGUAGE) StringDt resLanguage) {

		List<Immunization> retVal = new ArrayList<Immunization>();
		List<ImmunizationEn> ImmunizationEnListAll = new ArrayList<ImmunizationEn>();

		Map<String, String> paramMap = new ConcurrentHashMap<String, String>();

		if (identifier != null) {
			logger.debug("Search parameter identifier:{}", identifier.getValue());
			paramMap.put(Immunization.SP_IDENTIFIER, identifier.getValue());
		}
		
		if (manufacturer != null) {
			logger.debug("Search parameter manufacturer:{}", manufacturer.getValue());
			paramMap.put(Immunization.SP_MANUFACTURER, manufacturer.getValue());
		}
		
		if (lotNumber != null) {
			logger.debug("Search parameter lotNumber:{}", lotNumber.getValue());
			paramMap.put(Immunization.SP_LOT_NUMBER, lotNumber.getValue());
		}
		
		if (date != null) {
			logger.debug("Search parameter date:{}", date.getValue());
			paramMap.put(Immunization.SP_DATE, date.getValue());
		}

		if (doseSequence != null) {
			logger.debug("Search parameter doseSequence:{}", doseSequence.getValue());
			paramMap.put(Immunization.SP_DOSE_SEQUENCE, doseSequence.getValue());
		}

		retVal = immunizationService.findImmunizationByParamMap(paramMap);

		logger.debug("Found {} Immunization resource.", ImmunizationEnListAll.size());

		return retVal;
	}

	/**
	 * This method reads a Immunization resource.
	 * 
	 * <p>
	 * Read operations take a single parameter annotated with the
	 * {@link IdParam} paramater, and return a single resource instance.
	 * </p>
	 * 
	 * @param theId
	 *            the Id of Immunization resource to read
	 * 
	 * @return Returns a resource matching this identifier, or null if none
	 *         exists.
	 */
	@Read(version = true)
	public Immunization readImmunization(@IdParam IdDt theId) {

		logger.debug("Reading Immunization resource with Id:{}", theId.getIdPartAsLong());

		Immunization ImmunizationEn = immunizationService.findImmunizationById(theId.getIdPartAsLong().intValue());

		if (ImmunizationEn == null) {
			throw new ResourceNotFoundException("Resource not found : " + theId.getValueAsString());
		}

		// Immunization Immunization = entityToResourceConverter(ImmunizationEn);
		return ImmunizationEn;

	}

	/**
	 * This method deletes a Immunization resource.
	 * 
	 * @param theId
	 *            This is the ID of the Immunization to delete
	 * @return This method returns a "MethodOutcome"
	 */
	@Delete
	public MethodOutcome deleteImmunization(@IdParam IdDt theId) {

		logger.info("Deleting Immunization resource with Id:{}", theId.getValueAsString());

		immunizationService.deleteImmunizationById(theId.getIdPartAsLong().intValue());
		return new MethodOutcome();

	}

	/**
	 * The "@Update" annotation indicates that this method supports replacing an
	 * existing resource (by ID) with a new instance of that resource.
	 * 
	 * @param theId
	 *            This is the ID of the Immunization to update
	 * @param theImmunization
	 *            This is the actual resource to save
	 * @return This method returns a "MethodOutcome"
	 */
	@Update
	public MethodOutcome updateImmunization(@IdParam IdDt theId, @ResourceParam Immunization theImmunization) {

		logger.info("Updating Immunization resource with Id:{}", theId.getValueAsString());

		theImmunization.setId(theId.getIdPart());

		immunizationService.updateImmunization(theImmunization);

		// validateResource(theImmunization);

		// addNewVersion(theImmunization, id);

		return new MethodOutcome(theId);
	}

	/**
	 * This method just provides simple business validation for resources we are
	 * storing.
	 * 
	 * @param theImmunization
	 *            The Immunization to validate
	 */
	private void validateResource(Immunization theImmunization) {

	}

	/**
	 * The getResourceType method indicate what type of resource this provider
	 * supplies.
	 */
	@Override
	public Class<Immunization> getResourceType() {
		return Immunization.class;
	}

}
