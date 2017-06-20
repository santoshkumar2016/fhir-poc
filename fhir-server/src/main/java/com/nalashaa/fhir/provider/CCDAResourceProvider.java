package com.nalashaa.fhir.provider;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.nalashaa.fhir.db.model.PatientEn;
import com.nalashaa.fhir.model.CCDA;
import com.nalashaa.fhir.service.PatientService;
import com.nalashaa.fhir.service.impl.PatientServiceImpl;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

/**
 * 
 * This is a resource provider for resource type CCDA
 * 
 * @author santosh
 *
 */

public class CCDAResourceProvider implements IResourceProvider {

	/**
	 * This map has a resource ID as a key, and each key maps to a Deque list
	 * containing all versions of the resource with that ID.
	 */
	private Map<Long, Deque<Patient>> myIdToPatientVersions = new HashMap<Long, Deque<Patient>>();

	// @Autowired
	private PatientService patientServiceImpl = new PatientServiceImpl();

	/**
	 * This is used to generate new IDs
	 */
	private long myNextId = 1;

	/**
	 * Constructor, which pre-populates the provider with one resource instance.
	 */
	public CCDAResourceProvider() {
		// long resourceId = myNextId++;
		//
		// Patient patient = new Patient();
		// patient.setId(Long.toString(resourceId));
		// patient.addIdentifier();
		// patient.getIdentifier().get(0).setSystem(new
		// UriDt("urn:hapitest:mrns"));
		// patient.getIdentifier().get(0).setValue("00002");
		// patient.addName().addFamily("Test");
		// patient.getName().get(0).addGiven("PatientOne");
		// patient.setGender(AdministrativeGenderEnum.FEMALE);
		//
		// LinkedList<Patient> list = new LinkedList<Patient>();
		// list.add(patient);
		//
		// myIdToPatientVersions.put(resourceId, list);

	}

	/**
	 * Stores a new version of the patient so that it can be retrieved later.
	 * 
	 * @param thePatient
	 *            The patient resource to store
	 * @param theId
	 *            The ID of the patient to retrieve
	 */
	private void addNewVersion(Patient thePatient, Long theId) {
		InstantDt publishedDate;
		if (!myIdToPatientVersions.containsKey(theId)) {
			myIdToPatientVersions.put(theId, new LinkedList<Patient>());
			publishedDate = InstantDt.withCurrentTime();
		} else {
			Patient currentPatitne = myIdToPatientVersions.get(theId).getLast();
			Map<ResourceMetadataKeyEnum<?>, Object> resourceMetadata = currentPatitne.getResourceMetadata();
			publishedDate = (InstantDt) resourceMetadata.get(ResourceMetadataKeyEnum.PUBLISHED);
		}

		/*
		 * PUBLISHED time will always be set to the time that the first version
		 * was stored. UPDATED time is set to the time that the new version was
		 * stored.
		 */
		thePatient.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, publishedDate);
		thePatient.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, InstantDt.withCurrentTime());

		Deque<Patient> existingVersions = myIdToPatientVersions.get(theId);

		// We just use the current number of versions as the next version number
		String newVersion = Integer.toString(existingVersions.size());

		// Create an ID with the new version and assign it back to the resource
		IdDt newId = new IdDt("Patient", Long.toString(theId), newVersion);
		thePatient.setId(newId);

		existingVersions.add(thePatient);
	}




	/**
	 * The getResourceType method comes from IResourceProvider, and must be
	 * overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<CCDA> getResourceType() {
		return CCDA.class;
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
	public CCDA readCCDA(@IdParam IdDt theId) {

		PatientEn patientEn = patientServiceImpl.findPatientById(theId.getIdPartAsLong().intValue());

		//Patient patient = entityToResourceConverter(patientEn);
		CCDA ccda = new CCDA();
		
		ccda.getPatient().setResource(new Patient());
		
		Patient patient = (Patient) ccda.getPatient().getResource();
		((Patient) ccda.getPatient().getResource()).setId(patientEn.getCid().toString());
		
		((Patient) ccda.getPatient().getResource()).setActive(patientEn.isActive());
		((Patient) ccda.getPatient().getResource()).addName().addFamily(patientEn.getFname()).addGiven(patientEn.getLname());
		// .setUse(NameUseEnum.OFFICIAL);

		((Patient) ccda.getPatient().getResource()).setGender((patientEn.getSex() != null && !patientEn.getSex().isEmpty())
				? AdministrativeGenderEnum.valueOf(patientEn.getSex()) : AdministrativeGenderEnum.UNKNOWN);
		// patient.setPhoto(theValue);

		((Patient) ccda.getPatient().getResource()).addContact().addTelecom().setValue(patientEn.getHphone());
		((Patient) ccda.getPatient().getResource()).getContact().get(0).addRelationship().setText("Father");

		((Patient) ccda.getPatient().getResource()).addTelecom();
		Date birthDate = new Date();
		try {
			birthDate = DateFormat.getDateInstance().parse(patientEn.getBirthday());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((Patient) ccda.getPatient().getResource()).setBirthDateWithDayPrecision(birthDate);
		// patient.setDeceased();
		((Patient) ccda.getPatient().getResource()).addAddress().setCity(patientEn.getCity1());
		((Patient) ccda.getPatient().getResource()).getAddress().get(0).addLine(patientEn.getAddress11());
		if (patientEn.getMaritalStatusCode() != null) {
			((Patient) ccda.getPatient().getResource()).setMaritalStatus(MaritalStatusCodesEnum.valueOf(patientEn.getMaritalStatusCode()));
		} else {
			((Patient) ccda.getPatient().getResource()).setMaritalStatus(MaritalStatusCodesEnum.UNK);
		}

	
		((Patient) ccda.getPatient().getResource()).addCommunication().setPreferred(false);
		((Patient) ccda.getPatient().getResource()).getCommunication().get(0)
				.setLanguage(new CodeableConceptDt("urn:language", patientEn.getLanguageCode()));
		((Patient) ccda.getPatient().getResource()).addCommunication().setPreferred(true);
		((Patient) ccda.getPatient().getResource()).getCommunication().get(1)
				.setLanguage(new CodeableConceptDt("urn:language", patientEn.getCommunicationPreference()));
		((Patient) ccda.getPatient().getResource()).addCareProvider().setDisplay(patientEn.getProvider());
		
		//resourceFiller(ccda, patientEn);
		//ccda.getPatient();
		//ccda.setPatient(new ResourceReferenceDt(patient));
		return ccda;

	}
	
	private void resourceFiller(CCDA ccda,PatientEn patientEn){
		
		Patient patient = (Patient) ccda.getPatient().getResource();
		patient.setId(patientEn.getCid().toString());

		// NarrativeDt ndt = new NarrativeDt(new XhtmlDt("Patient data"),
		// NarrativeStatusEnum.EMPTY);
		// patient.setText(ndt);
		//
		// List<IdentifierDt> lsit = new LinkedList<IdentifierDt>();
		// lsit.add(new IdentifierDt("system", "12345"));
		//
		// patient.setIdentifier(lsit).getIdentifier().get(0).setUse(IdentifierUseEnum.USUAL).setType(IdentifierTypeCodesEnum.EN);

		patient.setActive(patientEn.isActive());
		patient.addName().addFamily(patientEn.getFname()).addGiven(patientEn.getLname());
		// .setUse(NameUseEnum.OFFICIAL);

		patient.setGender((patientEn.getSex() != null && !patientEn.getSex().isEmpty())
				? AdministrativeGenderEnum.valueOf(patientEn.getSex()) : AdministrativeGenderEnum.UNKNOWN);
		// patient.setPhoto(theValue);

		patient.addContact().addTelecom().setValue(patientEn.getHphone());
		patient.getContact().get(0).addRelationship().setText("Father");

		patient.addTelecom();
		Date birthDate = new Date();
		try {
			birthDate = DateFormat.getDateInstance().parse(patientEn.getBirthday());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		patient.setBirthDateWithDayPrecision(birthDate);
		// patient.setDeceased();
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
		//patient.getManagingOrganization().getResource()
		// patient.setLink(theValue)

		//return patient;
	}

	@Delete
	public MethodOutcome deletePatient(@IdParam IdDt theId) {

		patientServiceImpl.deletePatientById(theId.getIdPartAsLong().intValue());
		return new MethodOutcome();

	}



	private PatientEn resourceToEntityConverter(Patient thePatient) {

		PatientEn patientEn = new PatientEn();

		if (thePatient.getId() != null && !thePatient.getId().isEmpty()) {
			patientEn.setCid(thePatient.getId().getIdPartAsLong().intValue());
		}

		if (thePatient.getAddress() != null && !thePatient.getAddress().isEmpty()) {
			patientEn.setCity1(thePatient.getAddress().get(0).getCity());
			patientEn.setAddress11(thePatient.getAddress().get(0).getLine().get(0).getValue());
		}

		if (thePatient.getContact() != null && !thePatient.getContact().isEmpty()) {
			patientEn.setHphone(thePatient.getContact().get(0).getTelecom().get(0).getValue());
		}

		if (thePatient.getCareProvider() != null && !thePatient.getCareProvider().isEmpty())
			patientEn.setProvider(thePatient.getCareProvider().get(0).getDisplay().getValue());
		if (thePatient.getName() != null && !thePatient.getName().isEmpty()) {
			patientEn.setFname(thePatient.getName().get(0).getGivenAsSingleString());
			patientEn.setLname(thePatient.getName().get(0).getFamilyAsSingleString());
		}

		if (thePatient.getBirthDateElement() != null)
			patientEn.setBirthday(thePatient.getBirthDateElement().getValueAsString());

		if (thePatient.getMaritalStatus() != null) {
			patientEn.setMaritalStatusCode(
					MaritalStatusCodesEnum.valueOf(thePatient.getMaritalStatus().getText()).toString());
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
		// patientEn.setLname("");

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

		Patient patient = new Patient();
		patient.setId(patientEn.getCid().toString());

		// NarrativeDt ndt = new NarrativeDt(new XhtmlDt("Patient data"),
		// NarrativeStatusEnum.EMPTY);
		// patient.setText(ndt);
		//
		// List<IdentifierDt> lsit = new LinkedList<IdentifierDt>();
		// lsit.add(new IdentifierDt("system", "12345"));
		//
		// patient.setIdentifier(lsit).getIdentifier().get(0).setUse(IdentifierUseEnum.USUAL).setType(IdentifierTypeCodesEnum.EN);

		patient.setActive(patientEn.isActive());
		patient.addName().addFamily(patientEn.getFname()).addGiven(patientEn.getLname());
		// .setUse(NameUseEnum.OFFICIAL);

		patient.setGender((patientEn.getSex() != null && !patientEn.getSex().isEmpty())
				? AdministrativeGenderEnum.valueOf(patientEn.getSex()) : AdministrativeGenderEnum.UNKNOWN);
		// patient.setPhoto(theValue);

		patient.addContact().addTelecom().setValue(patientEn.getHphone());
		patient.getContact().get(0).addRelationship().setText("Father");

		patient.addTelecom();
		Date birthDate = new Date();
		try {
			birthDate = DateFormat.getDateInstance().parse(patientEn.getBirthday());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		patient.setBirthDateWithDayPrecision(birthDate);
		// patient.setDeceased();
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
		//patient.getManagingOrganization().getResource()
		// patient.setLink(theValue)

		return patient;
	}
}
