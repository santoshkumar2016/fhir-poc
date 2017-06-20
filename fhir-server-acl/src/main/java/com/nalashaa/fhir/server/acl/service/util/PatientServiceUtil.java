package com.nalashaa.fhir.server.acl.service.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nalashaa.fhir.server.acl.db.model.PatientEn;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.XhtmlDt;

@Component
public class PatientServiceUtil {

	Logger logger = LoggerFactory.getLogger(PatientServiceUtil.class);

	public static Map<String, String> patientFieldToColumnMap = new HashMap<String, String>();

	static {
		patientFieldToColumnMap.put(Patient.SP_ACTIVE, "ACTIVE");
		patientFieldToColumnMap.put(Patient.SP_FAMILY, "FNAME");
		patientFieldToColumnMap.put(Patient.SP_ADDRESS_CITY, "CITY1");
		
	}

	public Map<String, String> getColumnMap(Map<String, String> paramMap) {

		Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();

		while (iterator.hasNext()) {

			Entry<String, String> paramValue = iterator.next();
			String key = paramValue.getKey();
			String value = paramValue.getValue();
			switch (key) {
			case Patient.SP_ACTIVE:
				logger.debug("Search parameter active:{}", paramValue);
				iterator.remove();
				paramMap.put(patientFieldToColumnMap.get(Patient.SP_ACTIVE), value);
				break;

			case Patient.SP_FAMILY:
				logger.debug("Search Patient family:{}", paramValue);
				iterator.remove();
				paramMap.put(patientFieldToColumnMap.get(Patient.SP_FAMILY), value);
				break;

			case Patient.SP_ADDRESS_CITY:
				logger.debug("Search Patient addressCity:{}", paramValue);
				iterator.remove();
				paramMap.put(patientFieldToColumnMap.get(Patient.SP_ADDRESS_CITY), value);
				break;

			}
		}

		return paramMap;
	}

	public PatientEn resourceToEntityConverter(Patient thePatient) {

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

	public Patient entityToResourceConverter(PatientEn patientEn) {

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
