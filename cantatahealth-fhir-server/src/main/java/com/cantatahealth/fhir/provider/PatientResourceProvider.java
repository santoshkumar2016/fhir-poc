package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.service.PatientService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

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
	@Read
	// (version = true)
	public Patient readPatient(@IdParam IdType theId) {

		logger.debug("Reading Patient resource with Id:{}", theId.getIdPartAsLong());

		Patient patient = patientService.findPatientById(theId.getIdPartAsLong());

		if (patient == null || patient.isEmpty()) {
			throw new ResourceNotFoundException("Resource not found : " + theId.getValueAsString());
		}

		return patient;

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
	/**
	 * 
	 * This method searches by any parameter
	 * 
	 * @param active
	 * @param address
	 * @param addressCity
	 * @param addressCountry
	 * @param addressPostalCode
	 * @param addressState
	 * @param addressUse
	 * @param animalBreed
	 * @param animalSpecies
	 * @param birthDate
	 * @param deceased
	 * @param email
	 * @param family
	 * @param gender
	 * @param given
	 * @param identifier
	 * @param language
	 * @param link
	 * @param name
	 * @param organization
	 * @param phone
	 * @param phonetic
	 * @param telecom
	 * @return This method returns a list of Patients. This list may contain
	 *         multiple matching resources, or it may also be empty.
	 */
	@Search()
	public List<Patient> findPatientsBy(
			// @OptionalParam(name = Patient.SP_BIRTHDATE) DateParam birthDate,
			@OptionalParam(name = Patient.SP_BIRTHDATE) DateRangeParam birthDateRange,
			@OptionalParam(name = Patient.SP_FAMILY) StringDt family,
			@OptionalParam(name = Patient.SP_GENDER) StringDt gender,
			@OptionalParam(name = Patient.SP_GIVEN) StringDt given,
			@OptionalParam(name = Patient.SP_IDENTIFIER) StringDt identifier,
			@OptionalParam(name = Patient.SP_TELECOM) StringDt telecom) {

		List<Patient> retVal = new ArrayList<Patient>();
		List<PatientDbEntity> PatientDbEntityListAll = new ArrayList<PatientDbEntity>();

		Map<String, String> paramMap = new ConcurrentHashMap<String, String>();

		if (family != null) {
			logger.debug("Search Patient family:{}", family.getValueAsString());
			paramMap.put(Patient.SP_FAMILY, family.getValue());
		}

		if (given != null) {
			logger.debug("Search Patient given:{}", given.getValueAsString());
			paramMap.put(Patient.SP_GIVEN, given.getValue());
		}
		if (identifier != null) {
			logger.debug("Search Patient identifier:{}", identifier.getValueAsString());
			paramMap.put(Patient.SP_IDENTIFIER, identifier.getValue());
		}

		if (gender != null) {
			logger.debug("Search parameter gender:{}", gender.getValue());
			paramMap.put(Patient.SP_GENDER, getCode(gender.getValue()));
		}

		// if (birthDate != null) {
		// String birthdate = birthDate.getValueAsString();
		// logger.debug("Search parameter birthDate:{}", birthDate.getValue());
		// paramMap.put(Patient.SP_BIRTHDATE, birthdate);
		// }

		if (birthDateRange != null) {			
			if (birthDateRange.getLowerBound() != null)
				paramMap.put(Patient.SP_BIRTHDATE + "_START", birthDateRange.getLowerBound().getValueAsString());
			if (birthDateRange.getUpperBound() != null)
				paramMap.put(Patient.SP_BIRTHDATE + "_END", birthDateRange.getUpperBound().getValueAsString());

		}

		if (telecom != null) {
			logger.debug("Search parameter telecom:{}", telecom.getValue());
			paramMap.put(Patient.SP_TELECOM, telecom.getValue());
		}

		retVal = patientService.findPatientByParamMap(paramMap);

		logger.debug("Found {} Patient resource.", PatientDbEntityListAll.size());

		return retVal;
	}

	/**
	 * 
	 * @param codeInitial
	 * @return
	 */
	private String getCode(String codeInitial) {
		String gender = "";
		switch (codeInitial.toLowerCase()) {
		case "male":
			gender = "M";
			break;
		case "female":
			gender = "F";
			break;
		case "unknown":
			gender = "U";
		}

		return gender;
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
