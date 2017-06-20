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

import com.nalashaa.fhir.server.acl.db.model.PatientEn;
import com.nalashaa.fhir.server.acl.service.PatientService;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
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
 * This is a resource provider for resource type Patient
 * 
 * @author santosh
 *
 */
@Component
public class PatientResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(PatientResourceProvider.class);

	@Autowired
	private PatientService patientService;

	/**
	 * Constructor
	 */
	public PatientResourceProvider() {
		logger.info("************************* PatientResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * NOT SUPPORTED
	 * 
	 * Stores a new version of the patient so that it can be retrieved later.
	 * 
	 * @param thePatient
	 *            The patient resource to store
	 * @param theId
	 *            The ID of the patient to retrieve
	 */
	private void addNewVersion(Patient thePatient, Long theId) {
	}

	/**
	 * Adds a new instance of Patient resource to the server.
	 */
	@Create()
	public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
		// validateResource(thePatient);

		logger.info("***** Creating Patient resource *****");
		Integer patientId = patientService.savePatient(thePatient);
		logger.debug("Patient resource with Id : {} created successfully.", patientId);

		return new MethodOutcome(new IdDt(patientId), true);
	}

	/**
	 * This method searches by any parameter
	 * 
	 * @param addressCity
	 *            a string containing the name of the search criteria,
	 *            address-city
	 * 
	 * @return This method returns a list of Patients. This list may contain
	 *         multiple matching resources, or it may also be empty.
	 */
	@Search()
	public List<Patient> findPatientsBy(@OptionalParam(name = Patient.SP_ACTIVE) StringDt active,
			@OptionalParam(name = Patient.SP_ADDRESS) StringDt address,
			@OptionalParam(name = Patient.SP_ADDRESS_CITY) StringDt addressCity,
			@OptionalParam(name = Patient.SP_ADDRESS_COUNTRY) StringDt addressCountry,
			@OptionalParam(name = Patient.SP_ADDRESS_POSTALCODE) StringDt addressPostalCode,
			@OptionalParam(name = Patient.SP_ADDRESS_STATE) StringDt addressState,
			@OptionalParam(name = Patient.SP_ADDRESS_USE) StringDt addressUse,
			@OptionalParam(name = Patient.SP_ANIMAL_BREED) StringDt animalBreed,
			@OptionalParam(name = Patient.SP_ANIMAL_SPECIES) StringDt animalSpecies,
			@OptionalParam(name = Patient.SP_BIRTHDATE) StringDt birthDate,
			@OptionalParam(name = Patient.SP_CAREPROVIDER) StringDt careProvider,
			@OptionalParam(name = Patient.SP_DEATHDATE) StringDt deathDate,
			@OptionalParam(name = Patient.SP_DECEASED) StringDt deceased,
			@OptionalParam(name = Patient.SP_EMAIL) StringDt email,
			@OptionalParam(name = Patient.SP_FAMILY) StringDt family,
			@OptionalParam(name = Patient.SP_GENDER) StringDt gender,
			@OptionalParam(name = Patient.SP_GIVEN) StringDt given,
			@OptionalParam(name = Patient.SP_IDENTIFIER) StringDt identifier,
			@OptionalParam(name = Patient.SP_LANGUAGE) StringDt language,
			@OptionalParam(name = Patient.SP_LINK) StringDt link,
			@OptionalParam(name = Patient.SP_NAME) StringDt name,
			@OptionalParam(name = Patient.SP_ORGANIZATION) StringDt organization,
			@OptionalParam(name = Patient.SP_PHONE) StringDt phone,
			@OptionalParam(name = Patient.SP_PHONETIC) StringDt phonetic,
			@OptionalParam(name = Patient.SP_TELECOM) StringDt telecom) {

		List<Patient> retVal = new LinkedList<Patient>();
		List<PatientEn> patientEnListAll = new LinkedList<PatientEn>();

		Map<String, String> paramMap = new ConcurrentHashMap<String, String>();

		if (active != null) {
			logger.debug("Search parameter active:{}", active.getValue());
			paramMap.put(Patient.SP_ACTIVE, active.getValue());
		}

		if (address != null) {
			logger.debug("Search parameter active:{}", active.getValue());
			paramMap.put(Patient.SP_ADDRESS, address.getValue());
		}

		if (address != null) {
			logger.debug("Search parameter active:{}", active.getValue());
			paramMap.put(Patient.SP_ADDRESS, address.getValue());
		}

		if (addressCity != null) {
			logger.debug("Search Patient addressCity:{}", addressCity.getValueAsString());
			paramMap.put(Patient.SP_ADDRESS_CITY, addressCity.getValue());
		}

		if (addressCountry != null) {
			logger.debug("Search Patient addressCountry:{}", addressCity.getValueAsString());
			paramMap.put(Patient.SP_ADDRESS_COUNTRY, addressCountry.getValue());

		}

		if (family != null) {
			logger.debug("Search Patient family:{}", family.getValueAsString());
			paramMap.put(Patient.SP_FAMILY, family.getValue());
		}

		retVal = patientService.findPatientByParamMap(paramMap);

		logger.debug("Found {} Patient resource.", patientEnListAll.size());

		return retVal;
	}

	@Search(compartmentName = "Condition")
	public List<IResource> searchCompartment(@IdParam IdDt thePatientId) {
		List<IResource> retVal = new ArrayList<IResource>();
		Condition condition = new Condition();
		condition.setId(new IdDt("1234"));
		retVal.add(condition);
		return retVal;
	}

	/**
	 * This method reads a patient resource.
	 * 
	 * <p>
	 * Read operations take a single parameter annotated with the
	 * {@link IdParam} paramater, and return a single resource instance.
	 * </p>
	 * 
	 * @param theId
	 *            the Id of patient resource to read
	 * 
	 * @return Returns a resource matching this identifier, or null if none
	 *         exists.
	 */
	@Read(version = true)
	public Patient readPatient(@IdParam IdDt theId) {

		logger.debug("Reading Patient resource with Id:{}", theId.getIdPartAsLong());

		Patient patient = patientService.findPatientById(theId.getIdPartAsLong().intValue());

		if (patient == null) {
			throw new ResourceNotFoundException("Resource not found : " + theId.getValueAsString());
		}

		// Patient patient = entityToResourceConverter(patientEn);
		return patient;

	}

	/**
	 * This method deletes a patient resource.
	 * 
	 * @param theId
	 *            This is the ID of the patient to delete
	 * @return This method returns a "MethodOutcome"
	 */
	@Delete
	public MethodOutcome deletePatient(@IdParam IdDt theId) {

		logger.info("Deleting Patient resource with Id:{}", theId.getValueAsString());

		patientService.deletePatientById(theId.getIdPartAsLong().intValue());
		return new MethodOutcome();

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
	@Update
	public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient) {

		logger.info("Updating Patient resource with Id:{}", theId.getValueAsString());

		thePatient.setId(theId.getIdPart());

		patientService.updatePatient(thePatient);

		// validateResource(thePatient);

		// addNewVersion(thePatient, id);

		return new MethodOutcome(theId);
	}

	/**
	 * This method just provides simple business validation for resources we are
	 * storing.
	 * 
	 * @param thePatient
	 *            The patient to validate
	 */
	private void validateResource(Patient thePatient) {
		/*
		 * Our server will have a rule that patients must have a family name or
		 * we will reject them
		 */
		if (thePatient.getNameFirstRep().getFamilyFirstRep().isEmpty()) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue().setSeverity(IssueSeverityEnum.FATAL)
					.setDetails("No family name provided, Patient resources must have at least one family name.");
			throw new UnprocessableEntityException(outcome);
		}
	}

	/**
	 * The getResourceType method indicate what type of resource this provider
	 * supplies.
	 */
	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

}
