package com.nalashaa.fhir.provider;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nalashaa.fhir.db.model.PatientEn;
import com.nalashaa.fhir.service.PatientService;
import com.nalashaa.fhir.service.impl.PatientServiceImpl;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
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

public class PatientResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(PatientResourceProvider.class);

	// @Autowired
	private PatientService patientService = new PatientServiceImpl();

	/**
	 * Constructor, which pre-populates the provider with one resource instance.
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
		Integer patientId = patientService.savePatient(resourceToEntityConverter(thePatient));
		logger.debug("Patient resource with Id : {} created successfully.", patientId);

		return new MethodOutcome(new IdDt(patientId), true);
	}

	/**
	 * This method searches by family name.
	 * 
	 * @param theFamilyName
	 *            a string containing the name of the search criteria, family.
	 * 
	 * @return This method returns a list of Patients. This list may contain
	 *         multiple matching resources, or it may also be empty.
	 */
//	@Search()
//	public List<Patient> findPatientsByName(@RequiredParam(name = Patient.SP_FAMILY) StringDt theFamilyName) {
//
//		logger.debug("Searching Patient resource with search parameter family:{}", theFamilyName.getValueAsString());
//		LinkedList<Patient> retVal = new LinkedList<Patient>();
//
//		List<PatientEn> patientEnList = patientService.findPatientByName(theFamilyName.getValue());
//
//		logger.debug("Found {} Patient resource.", patientEnList.size());
//
//		Iterator<PatientEn> itr = patientEnList.iterator();
//
//		while (itr.hasNext()) {
//			retVal.add(entityToResourceConverter(itr.next()));
//		}
//
//		return retVal;
//	}

	/**
	 * This method searches by address-city.
	 * 
	 * @param addressCity
	 *            a string containing the name of the search criteria,
	 *            address-city
	 * 
	 * @return This method returns a list of Patients. This list may contain
	 *         multiple matching resources, or it may also be empty.
	 */
//	@Search()
//	public List<Patient> findPatientsByAddressCity(
//			@RequiredParam(name = Patient.SP_ADDRESS_CITY) StringDt addressCity) {
//
//		logger.debug("Searching Patient resource with search parameter:value  address-city:{}",
//				addressCity.getValueAsString());
//
//		LinkedList<Patient> retVal = new LinkedList<Patient>();
//
//		List<PatientEn> patientEnList = patientService.findPatientByAddressCity(addressCity.getValue());
//
//		logger.debug("Found {} Patient resource.", patientEnList.size());
//
//		Iterator<PatientEn> itr = patientEnList.iterator();
//
//		while (itr.hasNext()) {
//			retVal.add(entityToResourceConverter(itr.next()));
//		}
//
//		return retVal;
//	}

	/**
	 * This method searches by address-city.
	 * 
	 * @param addressCity
	 *            a string containing the name of the search criteria,
	 *            address-city
	 * 
	 * @return This method returns a list of Patients. This list may contain
	 *         multiple matching resources, or it may also be empty.
	 */
	@Search()
	public List<Patient> findPatientsBy(
			@OptionalParam(name = Patient.SP_ACTIVE) StringDt active,
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

		logger.debug("Searching Patient resource with search parameter:value  address-city:{}",
				addressCity.getValueAsString());

		LinkedList<Patient> retVal = new LinkedList<Patient>();
		List<PatientEn> patientEnListAll = new LinkedList<PatientEn>();
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		if(family != null){
			paramMap.put("FNAME", family.getValue());
			//patientEnList = patientService.findPatientByName(family.getValue());
		}
		
		//patientEnListAll.addAll(patientEnList);
		
		if(addressCity != null){
			paramMap.put("CITY1", addressCity.getValue());
			//patientEnList = patientService.findPatientByAddressCity(addressCity.getValue());
		}
		
		
		patientEnListAll = patientService.findPatientByParamMap(paramMap);
		//patientEnListAll.addAll(patientEnList);

		logger.debug("Found {} Patient resource.", patientEnListAll.size());

		Iterator<PatientEn> itr = patientEnListAll.iterator();

		while (itr.hasNext()) {
			retVal.add(entityToResourceConverter(itr.next()));
		}

		return retVal;
	}
	
	@Search(compartmentName = "Condition")
	public List<IResource> searchCompartment(@IdParam IdDt thePatientId) {
		List<IResource> retVal = new ArrayList<IResource>();
		Condition condition = new Condition();
		condition.setId(new IdDt("665577"));
		retVal.add(condition);
		return retVal;
	}
	
	// @Search
	// this is not supported
	public List<Patient> findPatientsUsingArbitraryCtriteria() {
		LinkedList<Patient> retVal = new LinkedList<Patient>();

		List<PatientEn> patientEnList = patientService.findAllPatients();

		Iterator<PatientEn> itr = patientEnList.iterator();
		PatientEn patientEn = null;
		while (itr.hasNext()) {

			patientEn = itr.next();

			Patient patient = new Patient();
			patient.setId(patientEn.getCid().toString());
			patient.addAddress().setCity(patientEn.getAddress11());
			patient.addCommunication().setPreferred(false);
			patient.getCommunication().get(0)
					.setLanguage(new CodeableConceptDt("urn:language", patientEn.getLanguageCode()));
			patient.addCommunication().setPreferred(false);
			patient.getCommunication().get(1)
					.setLanguage(new CodeableConceptDt("urn:languagepre", patientEn.getLanguageCode()));
			patient.addContact().addTelecom().setValue(patientEn.getHphone());
			patient.getContact().get(0).addRelationship().setText("");
			patient.addAddress().addLine(patientEn.getCity1());
			patient.addCareProvider().setDisplay(patientEn.getCompany());
			patient.addName().addFamily(patientEn.getFname()).addGiven(patientEn.getLname());

			patient.setActive(patientEn.getAcuity() == 1 ? true : false);
			patient.setGender((patientEn.getSex() != null && !patientEn.getSex().isEmpty())
					? AdministrativeGenderEnum.valueOf(patientEn.getSex()) : AdministrativeGenderEnum.UNKNOWN);
			Date birthDate = new Date();
			try {
				birthDate = DateFormat.getDateInstance().parse(patientEn.getBirthday());
			} catch (ParseException e) {

				e.printStackTrace();
			}
			patient.setBirthDateWithDayPrecision(birthDate);

			if (patientEn.getMaritalStatusCode() != null) {
				patient.setMaritalStatus(MaritalStatusCodesEnum.valueOf(patientEn.getMaritalStatusCode()));
			}

			retVal.add(patient);

		}

		return retVal;
	}

	/**
	 * The getResourceType method indicate what type of resource this provider
	 * supplies.
	 */
	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
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

		PatientEn patientEn = patientService.findPatientById(theId.getIdPartAsLong().intValue());

		if (patientEn == null) {
			throw new ResourceNotFoundException("Resource not found : " + theId.getValueAsString());
		}

		Patient patient = entityToResourceConverter(patientEn);
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

		patientService.updatePatient(resourceToEntityConverter(thePatient));

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

	private PatientEn resourceToEntityConverter(Patient thePatient) {

		logger.debug("Converting Patient resource with Id:{} to Patient entity.", thePatient.getId());

		PatientEn patientEn = new PatientEn();

		if (thePatient.getId() != null && !thePatient.getId().isEmpty()) {
			patientEn.setCid(thePatient.getId().getIdPartAsLong().intValue());
		}

		// Ideally below fields should not be set explicitly as below and should
		// take default value

		if (thePatient.getAddress() != null && !thePatient.getAddress().isEmpty()) {
			patientEn.setCity1(thePatient.getAddress().get(0).getCity());
			patientEn.setAddress11(thePatient.getAddress().get(0).getLine().get(0).getValue());
		} else {
			patientEn.setCity1("DefCity1");
			patientEn.setAddress11("DefAddre11");
		}

		if (thePatient.getContact() != null && !thePatient.getContact().isEmpty()) {
			patientEn.setHphone(thePatient.getContact().get(0).getTelecom().get(0).getValue());
		} else {
			patientEn.setHphone("(800) 283-8334");
		}

		if (thePatient.getCareProvider() != null && !thePatient.getCareProvider().isEmpty()) {
			patientEn.setProvider(thePatient.getCareProvider().get(0).getDisplay().getValue());
		} else {
			patientEn.setProvider("DefProvider");
		}

		if (thePatient.getName() != null && !thePatient.getName().isEmpty()) {
			patientEn.setFname(thePatient.getName().get(0).getGivenAsSingleString());
			patientEn.setLname(thePatient.getName().get(0).getFamilyAsSingleString());
		} else {
			patientEn.setFname("DefFName");
			patientEn.setLname("DefLName");
		}

		if (thePatient.getBirthDateElement() != null && !thePatient.getBirthDateElement().isEmpty()) {
			patientEn.setBirthday(thePatient.getBirthDateElement().getValueAsString());
		} else {
			patientEn.setBirthday("2017-01-08");
		}

		if (!thePatient.getMaritalStatus().isEmpty()) {
			patientEn.setMaritalStatusCode(MaritalStatusCodesEnum
					.valueOf(thePatient.getMaritalStatus().getCoding().get(0).getCode()).toString());
		} else {
			patientEn.setMaritalStatusCode(MaritalStatusCodesEnum.UNK.toString());
		}

		patientEn.setAddress12("nn");
		patientEn.setAbstract_("NP");

		patientEn.setAcuity((byte) 0);
		patientEn.setAddress21("NP");
		patientEn.setAddress22("NP");
		patientEn.setAltid("NP");
		patientEn.setAudit("NP");

		patientEn.setBnid("NP");
		patientEn.setCashOnly("Y");
		patientEn.setCategory("NP");
		patientEn.setCity2("");
		patientEn.setCompanyContact("");
		patientEn.setCountry1("");
		patientEn.setCountry2("");
		patientEn.setCredentials("");
		patientEn.setDirid("");
		patientEn.setDoa("");
		patientEn.setDob("");
		patientEn.setEdoa("");
		patientEn.setEmail1("");
		patientEn.setEmail2("");
		patientEn.setEmailto(0);
		patientEn.setEmployer("");
		patientEn.setEthnic("");
		patientEn.setEthnicityId(0);
		patientEn.setFax1("");
		patientEn.setFax2("");
		patientEn.setFeeSchedule(0);
		patientEn.setGlobalid("");
		patientEn.setGrade("");
		patientEn.setImport_("");
		patientEn.setInactiveReason("");
		patientEn.setInitialcontact("");
		patientEn.setLanguageCode("");
		patientEn.setLiveswith("");
		patientEn.setLupdate("");
		patientEn.setMname("");
		patientEn.setMphone("");
		patientEn.setMpi(0);
		patientEn.setNextOfKin("");
		patientEn.setNickname("");
		patientEn.setOwner("");
		patientEn.setPgroup("");
		patientEn.setPhoneto(0);
		patientEn.setPin("");
		patientEn.setPrefix("");
		patientEn.setPricontact("");
		patientEn.setProvider2("");
		patientEn.setProvider3("");
		patientEn.setProvider4("");
		patientEn.setQualified(false);
		patientEn.setReadonly((byte) 0);
		patientEn.setReferrer("");
		patientEn.setReligion("");
		patientEn.setSalutation("");
		patientEn.setSchool("");
		patientEn.setSex("");
		patientEn.setSmokingStatusDate(new Date());
		patientEn.setSmokingStatusId(0);
		patientEn.setSpouse("");
		patientEn.setSsnum("");
		patientEn.setState1("");
		patientEn.setState2("");
		patientEn.setTitle("");
		patientEn.setTransgender(0);
		patientEn.setType("");
		patientEn.setUndeleteable((byte) 0);
		patientEn.setUrl("");
		patientEn.setWphone1("");
		patientEn.setWphone2("");
		patientEn.setZip1("");
		patientEn.setZip2("");
		patientEn.setEdod("");

		patientEn.setType("");

		patientEn.setActive(false);
		patientEn.setDoa("");
		patientEn.setDod("");
		patientEn.setCompany("");
		patientEn.setMpi(1);
		patientEn.setCommunicationPreference("NP");
		patientEn.setLanguageCode("NP");

		patientEn.setMotherMaidenName("NP");
		patientEn.setSmokingStatusDate(new Date());
		patientEn.setSmokingStatusId(0);
		patientEn.setEthnicityId(0);
		patientEn.setLocation("NP");
		patientEn.setAudit("NP");
		patientEn.setAbstract_("NP");
		patientEn.setComments("NP");

		return patientEn;

	}

	private Patient entityToResourceConverter(PatientEn patientEn) {

		logger.debug("Converting Patient entity with Id:{} to Patient resource.", patientEn.getCid());

		Patient patient = new Patient();
		patient.setId(patientEn.getCid().toString());

		//
		List<IdentifierDt> lsit = new LinkedList<IdentifierDt>();
		lsit.add(new IdentifierDt("system", patientEn.getCid().toString()));

		patient.setIdentifier(lsit).getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL)
				.setType(IdentifierTypeCodesEnum.EN);

		NarrativeDt ndt = new NarrativeDt(new XhtmlDt("Patient data"), NarrativeStatusEnum.GENERATED);
		patient.setText(ndt);

		patient.setActive(patientEn.isActive());
		patient.addName().addFamily(patientEn.getFname()).addGiven(patientEn.getLname()).setUse(NameUseEnum.OFFICIAL)
				.setText("Text representation of the name ");

		patient.setGender((patientEn.getSex() != null && !patientEn.getSex().isEmpty())
				? AdministrativeGenderEnum.valueOf(patientEn.getSex()) : AdministrativeGenderEnum.UNKNOWN);
		// patient.setPhoto(theValue);

		patient.addContact().addTelecom().setValue(patientEn.getHphone());
		patient.getContact().get(0).addRelationship().setText("Father");

		patient.addTelecom().setUse(ContactPointUseEnum.HOME).setValue(patientEn.getHphone());
		Date birthDate = new Date();
		try {
			birthDate = DateFormat.getDateInstance().parse(patientEn.getBirthday());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		patient.setBirthDateWithDayPrecision(birthDate);
		// patient.setDeceased(false);
		patient.addAddress().setCity(patientEn.getCity1());
		patient.getAddress().get(0).addLine(patientEn.getAddress11());
		if (patientEn.getMaritalStatusCode() != null) {
			patient.setMaritalStatus(MaritalStatusCodesEnum.valueOf(patientEn.getMaritalStatusCode()));
		} else {
			patient.setMaritalStatus(MaritalStatusCodesEnum.UNK);
		}

		// patient.setMultipleBirth(IDatatype);
		// patient.setPhoto(List<AttachmentDt>)

		// patient.getContact().get(0).setName("");
		// patient.setAnimal(Animal);
		patient.addCommunication().setPreferred(false);
		patient.getCommunication().get(0)
				.setLanguage(new CodeableConceptDt("urn:language", patientEn.getLanguageCode()));
		patient.addCommunication().setPreferred(true);
		patient.getCommunication().get(1)
				.setLanguage(new CodeableConceptDt("urn:language", patientEn.getCommunicationPreference()));
		patient.addCareProvider().setDisplay(patientEn.getProvider());
		patient.setManagingOrganization(new ResourceReferenceDt(new Organization()));// getManagingOrganization().getResource()
		// patient.setLink(theValue)

		return patient;
	}
}
