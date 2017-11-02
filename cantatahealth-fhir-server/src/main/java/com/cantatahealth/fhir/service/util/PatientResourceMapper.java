package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystemEnumFactory;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.dstu3.model.Patient.PatientLinkComponent;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.RelatedPerson;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.ContactPointDbEntity;
import com.cantatahealth.fhir.db.model.ExtensionDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.IdentifierDbEntity;
import com.cantatahealth.fhir.db.model.OrganizationDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.util.DbQueries;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;

import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Patient service util
 * 
 * @author santosh
 *
 */
@Component
public class PatientResourceMapper {

	Logger logger = LoggerFactory.getLogger(PatientResourceMapper.class);
	
	@Autowired
	FhirDbUtil hibutil; 
	
	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;

	@Autowired
	OrganizationResourceMapper organizationResourceMapper;

	@Autowired
	RelatedPersonResourceMapper relatedPersonResourceMapper;

	/**
	 * public static Map<String, String> patientFieldToColumnMap = new
	 * HashMap<String, String>();
	 * 
	 * static { patientFieldToColumnMap.put(Patient.SP_ACTIVE, "ACTIVE");
	 * patientFieldToColumnMap.put(Patient.SP_FAMILY, "FNAME");
	 * patientFieldToColumnMap.put(Patient.SP_ADDRESS_CITY, "CITY1");
	 * 
	 * }
	 * 
	 * public Map<String, String> getColumnMap(Map<String, String> paramMap) {
	 * 
	 * Iterator<Entry<String, String>> iterator =
	 * paramMap.entrySet().iterator();
	 * 
	 * while (iterator.hasNext()) {
	 * 
	 * Entry<String, String> paramValue = iterator.next(); String key =
	 * paramValue.getKey(); String value = paramValue.getValue(); switch (key) {
	 * case Patient.SP_ACTIVE: logger.debug("Search parameter active:{}",
	 * paramValue); iterator.remove();
	 * paramMap.put(patientFieldToColumnMap.get(Patient.SP_ACTIVE), value);
	 * break;
	 * 
	 * case Patient.SP_FAMILY: logger.debug("Search Patient family:{}",
	 * paramValue); iterator.remove();
	 * paramMap.put(patientFieldToColumnMap.get(Patient.SP_FAMILY), value);
	 * break;
	 * 
	 * case Patient.SP_ADDRESS_CITY: logger.debug(
	 * "Search Patient addressCity:{}", paramValue); iterator.remove();
	 * paramMap.put(patientFieldToColumnMap.get(Patient.SP_ADDRESS_CITY),
	 * value); break;
	 * 
	 * } }
	 * 
	 * return paramMap; }
	 * 
	 **/

	/**
	 * 
	 * @param idfDbEntity
	 * @return This method converts DBentity to Identifier Model class and
	 *         returns the instance
	 */
	public Identifier toIdentifierResource(IdentifierDbEntity idfDbEntity) {

		Identifier idf = new Identifier();

		if (idfDbEntity != null) {

			idf.setId(String.valueOf(idfDbEntity.getId()));
			if (AppUtil.isNotEmpty(idfDbEntity.getSystem())) {
				idf.setSystem(idfDbEntity.getSystem());
			}

			if (AppUtil.isNotEmpty(idfDbEntity.getValue())) {
				idf.setValue(idfDbEntity.getValue());
			}

			if (AppUtil.isNotEmpty(idfDbEntity.getUseAlias())) {
				try {
					idf.setUse(IdentifierUse.valueOf(idfDbEntity.getUseAlias()));
				} catch (Exception e) {
					logger.error("Invalid value : " + idfDbEntity.getUseAlias() + " for field use.");
				}

			}
		}
		return idf;

	}

	/**
	 * 
	 * @param adrDbEntity
	 * @return This method converts the DBEntity to Address model class and
	 *         returns the model instance
	 */
	public Address toAddressResource(AddressDbEntity adrDbEntity) {
		Address addr = new Address();

		if (adrDbEntity == null) {
			return addr;
		}
		if (AppUtil.isNotEmpty(adrDbEntity.getCity()))
			addr.setCity(adrDbEntity.getCity().toString());
		if (AppUtil.isNotEmpty(adrDbEntity.getCountry()))
			addr.setCountry(adrDbEntity.getCountry().toString());

		if (AppUtil.isNotEmpty(adrDbEntity.getLine())) {
			StringType type = new StringType();
			type.setValue(adrDbEntity.getLine().toString());
			List<StringType> list = addr.getLine();
			list.add(type);
			addr.setLine(list);
		}

		if (adrDbEntity.getCodingDbEntity() != null)
			try {
				if (AppUtil.isNotEmpty(adrDbEntity.getCodingDbEntity().getCode()))
					addr.setUse(AddressUse.fromCode(adrDbEntity.getCodingDbEntity().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (AppUtil.isNotEmpty(adrDbEntity.getState())) {
			addr.setState(adrDbEntity.getState().toString());
		}

		if (AppUtil.isNotEmpty(adrDbEntity.getPostalcode())) {
			addr.setPostalCode(adrDbEntity.getPostalcode().toString());
		}

		return addr;
	}

	/**
	 * 
	 * @param humanNameDbEntity
	 * @return This method converts dbEntity to HumanName model class and
	 *         returns the model object.
	 */
	public HumanName toHumanNameResource(HumanNameDbEntity humanNameDbEntity) {

		HumanName name = new HumanName();

		if (humanNameDbEntity == null) {
			return name;
		}

		if (AppUtil.isNotEmpty(humanNameDbEntity.getFamily())) {
			name.setFamily(humanNameDbEntity.getFamily().toString());
		}

		List<StringType> strTypList = new ArrayList<StringType>();
		if (AppUtil.isNotEmpty(humanNameDbEntity.getGiven())) {
			name.addGiven(humanNameDbEntity.getGiven());
		}

		strTypList.clear();
		if (AppUtil.isNotEmpty(humanNameDbEntity.getPrefix())) {

			name.addPrefix(humanNameDbEntity.getPrefix());
		}

		strTypList.clear();
		if (AppUtil.isNotEmpty(humanNameDbEntity.getSuffix())) {

			name.addSuffix(humanNameDbEntity.getSuffix());
		}

		return name;
	}

	/**
	 * 
	 * @param patientMapDbEntity
	 * @param patient
	 * @return This method reurns patient with all it's attributes mapped.
	 */
	public Patient dbModelToResource(PatientMapDbEntity patientMapDbEntity, Patient patient, Extension... ext) {

		PatientDbEntity patientDbEntity = patientMapDbEntity.getPatientDbEntity();
		if (patientDbEntity != null) {
			/** Setting Id */
			patient.setId(String.valueOf(patientDbEntity.getId()));

			// birthDate
			if (patientDbEntity.getBirthdate() != null) {
				patient.setBirthDate(patientDbEntity.getBirthdate());
			}

			/** Gender */
			try {
				if (AppUtil.isNotEmpty(patientDbEntity.getCodingDbEntityByGender().getCode()))
					patient.setGender(AdministrativeGender
							.fromCode(patientDbEntity.getCodingDbEntityByGender().getCode().toLowerCase()));
			} catch (FHIRException e) {

				e.printStackTrace();
			}

			/** Marital Status */
			if (patientDbEntity.getCodingDbEntityByMaritalstatus() != null) {
				if (AppUtil.isNotEmpty(patientDbEntity.getCodingDbEntityByMaritalstatus().getCode())) {
					CodeableConcept codCon = new CodeableConcept();
					Coding coding = new Coding();
					coding.setDisplay(patientDbEntity.getCodingDbEntityByMaritalstatus().getDisplay());
					coding.setCode(patientDbEntity.getCodingDbEntityByMaritalstatus().getCode());
					codCon.getCoding().add(coding);
					patient.setMaritalStatus(codCon);
				}
			}

		}

		// identifier
		IdentifierDbEntity idfDbEntity = patientMapDbEntity.getIdentifierDbEntity();
		Identifier idf = toIdentifierResource(idfDbEntity);
		if (!idf.isEmpty() && !patient.getIdentifier().contains(idf)) {
			patient.getIdentifier().add(idf);
		}
		// Modified for Extension
		// Race
		if (patientMapDbEntity.getCodingDbEntityByRace() != null) {
			ExtensionDbEntity extdb = hibutil.executeSQLQuery(DbQueries.extensionQuery,"nameValue","race", ExtensionDbEntity.class);
			ext[0].setUrl(extdb.getUrl());
			ext[0].addExtension(extdb.getUrl(),
					new Coding(patientMapDbEntity.getCodingDbEntityByRace().getSystem(),
							patientMapDbEntity.getCodingDbEntityByRace().getCode(),
							patientMapDbEntity.getCodingDbEntityByRace().getDisplay()));
		}
		// Modified for Extension
		// Ethnicity
		if (patientMapDbEntity.getCodingDbEntityByEthnicity() != null) {
			ExtensionDbEntity extdb = hibutil.executeSQLQuery(DbQueries.extensionQuery,"nameValue","ethnicity", ExtensionDbEntity.class);
			ext[1].setUrl(extdb.getUrl());
			ext[1].addExtension(new Extension().setUrl(extdb.getUrl())
					.setValue(new Coding(patientMapDbEntity.getCodingDbEntityByEthnicity().getSystem(),
							patientMapDbEntity.getCodingDbEntityByEthnicity().getCode(),
							patientMapDbEntity.getCodingDbEntityByEthnicity().getDisplay())));
		}

		// address
		AddressDbEntity adrDbEntity = patientMapDbEntity.getAddressDbEntity();
		Address addr = toAddressResource(adrDbEntity);
		if (!addr.isEmpty() && !patient.getAddress().contains(addr)) {
			patient.getAddress().add(addr);
		}

		// human name
		HumanNameDbEntity entity = patientMapDbEntity.getHumanNameDbEntity();
		HumanName name = toHumanNameResource(entity);
		if (!name.isEmpty() && !patient.getName().contains(name)) {
			patient.getName().add(name);
		}

		/** Organization */
		OrganizationDbEntity orgDbEntity = patientMapDbEntity.getOrganizationDbEntity();
		if (orgDbEntity != null) {
			Organization o = organizationResourceMapper.toOrganizationResource(orgDbEntity.getId());
			patient.getManagingOrganization()
					.setReference(ResourcesLiteralsUtil.Identifier_Hash + 
							ResourcesLiteralsUtil.Organization_Ref + orgDbEntity.getId()).setResource(o);
		}
		/** General Practitioner */
		if (patientMapDbEntity.getPractitionerDbEntity() != null) {
			
			boolean duplicatePractitioner = false;
			for(Reference re : patient.getGeneralPractitioner()) {
				if((String.valueOf(re.getResource().getIdElement())).equals(ResourcesLiteralsUtil.Identifier_Hash
						+ patientMapDbEntity.getPractitionerDbEntity().getId())) {	
					duplicatePractitioner = true;
					break;
				}
			}
			
			if(!duplicatePractitioner){
				Practitioner p = practitionerResourceMapper
						.toPractitionerResource(patientMapDbEntity.getPractitionerDbEntity().getId());
				p.setId(ResourcesLiteralsUtil.Identifier_Hash
						+ patientMapDbEntity.getPractitionerDbEntity().getId());
				
				patient.addGeneralPractitioner().setReference(ResourcesLiteralsUtil.Identifier_Hash
						+ patientMapDbEntity.getPractitionerDbEntity().getId()).setResource(p);
			}
			
			
		}

		// Link\Related Person
		if (patientMapDbEntity.getRelatedPersonDbEntity() != null) {
			RelatedPerson rp = relatedPersonResourceMapper
					.toRelatedPersonResource(patientMapDbEntity.getRelatedPersonDbEntity().getId());
			Reference ref = new Reference(ResourcesLiteralsUtil.Identifier_Hash+ ResourcesLiteralsUtil.Related_Ref
					+ patientMapDbEntity.getRelatedPersonDbEntity().getId());
			ref.setResource(rp);
			patient.addLink(new PatientLinkComponent().setOther(ref));
		}

		/** Communication */
		if (patientMapDbEntity.getCommunicationDbEntity() != null) {

			CodeableConcept codCon = new CodeableConcept();
			Coding coding = new Coding();
			coding.setDisplay(patientMapDbEntity.getCommunicationDbEntity().getLanguage());
			codCon.getCoding().add(coding);
			PatientCommunicationComponent comp = new PatientCommunicationComponent(codCon);
			comp.setPreferred(patientMapDbEntity.getCommunicationDbEntity().getPreferred());
			patient.getCommunication().add(comp);
			// patient.addCommunication().setPreferred(patientMapDbEntity.getCommunicationDbEntity().getPreferred());

		}
		/** Contact Point */
		ContactPointDbEntity conctDbEntity = patientMapDbEntity.getContactPointDbEntity();
		if (conctDbEntity != null) {

			conctDbEntity.getRank();
			conctDbEntity.getActive();

			ContactPoint cp = new ContactPoint();

			/** Contact System */
			if (AppUtil.isNotEmpty(conctDbEntity.getSystem())) {
				ContactPointSystemEnumFactory fac = new ContactPointSystemEnumFactory();
				ContactPointSystem sys;
				try {
					sys = fac.fromCode(conctDbEntity.getSystem().toLowerCase());
					cp.setSystem(sys);
				} catch (IllegalArgumentException e) {
					logger.error("Unknown system value '" + conctDbEntity.getSystem() + "'", e);
				}
			}
			if (AppUtil.isNotEmpty(conctDbEntity.getValue())) {
				cp.setValue(conctDbEntity.getValue());
			}

			/** Contact Point use */
			if (AppUtil.isNotEmpty(conctDbEntity.getUseAlias())) {
				try {
					cp.setUse(ContactPointUse.fromCode(conctDbEntity.getUseAlias().toLowerCase()));
				} catch (Exception e) {
					logger.error("Unknown usealias value '" + conctDbEntity.getUseAlias() + "'", e);
				}
			}

			/** Contact Rank */
			if (conctDbEntity.getRank() != null) {
				cp.setRank(conctDbEntity.getRank());
			}

			patient.getTelecom().add(cp);

		}

		return patient;
	}

	/**
	 * 
	 * @param patMapDbEntityList
	 * @return This method returns the list of patients when searched by
	 *         parameters.
	 */
	// Modified for Extension
	public List<Patient> dbModelToResource(List<PatientMapDbEntity> patMapDbEntityList) {

		Map<String, Patient> patientMap = new HashMap<>();
		Extension race = new Extension();
		Extension ethnicity = new Extension();
		Patient patient = null;
		Iterator<PatientMapDbEntity> patMapDbSetItr = patMapDbEntityList.iterator();
		while (patMapDbSetItr.hasNext()) {

			PatientMapDbEntity patMapDbEnt = patMapDbSetItr.next();
			if (patientMap.containsKey(String.valueOf(patMapDbEnt.getPatientDbEntity().getId()))) {

				patient = dbModelToResource(patMapDbEnt,
						patientMap.get(String.valueOf(patMapDbEnt.getPatientDbEntity().getId())), race, ethnicity);
				patientMap.put(patient.getId(), patient);

			} else {
				patient = dbModelToResource(patMapDbEnt, new Patient(), race, ethnicity);
				patientMap.put(patient.getId(), patient);

			}
		}
		if (patient != null) {
			patient.addExtension(race);
			patient.addExtension(ethnicity);
		}
		List<Patient> patientList = new ArrayList<Patient>(patientMap.values());

		return patientList;
	}

}
