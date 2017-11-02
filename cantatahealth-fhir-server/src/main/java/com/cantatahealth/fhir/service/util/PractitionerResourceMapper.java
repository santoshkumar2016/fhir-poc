/**
 * 
 */
package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceClinicalStatus;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceType;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.dao.CareTeamDAO;
import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.CodingDbEntity;
import com.cantatahealth.fhir.db.model.ContactPointDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.LocationMapDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PractitionerDbEntity;
import com.cantatahealth.fhir.db.model.PractitionerMapDbEntity;
import com.cantatahealth.fhir.db.model.ReactionnDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Utility class for AllergyIntolerance Model
 * @author somadutta
 *
 */
@Component
public class PractitionerResourceMapper {
	
	Logger logger = LoggerFactory.getLogger(PractitionerResourceMapper.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	
	public Practitioner toPractitionerResource(Long id)
	{
		Practitioner practitioner = new Practitioner();

		PractitionerDbEntity practitionerDbEntity = hibUtil.fetchById(PractitionerDbEntity.class, id);

		if (practitionerDbEntity == null)
			return practitioner;
		/**Mapping base components of careTeam*/
		else{
			practitioner = getPractitionerBaseComponents(practitioner,practitionerDbEntity);
		}
		
		Set<PractitionerMapDbEntity> pMapDbMapEntset = practitionerDbEntity.getPractitionerMapDbEntities();
		Iterator<PractitionerMapDbEntity> pMapDbSetItr = pMapDbMapEntset.iterator();
		while (pMapDbSetItr.hasNext()) {
			PractitionerMapDbEntity practitionerMapDbEntity = pMapDbSetItr.next();

			practitioner = dbModelToResource(practitionerMapDbEntity, practitioner);
		}

		return practitioner;

	}
	
	public Practitioner getPractitionerBaseComponents(Practitioner practitioner, PractitionerDbEntity practitionerDbEntity)
	{
		
		/** Setting Id */		
		practitioner.setId("#" +ResourcesLiteralsUtil.Practitioner_Ref + String.valueOf(practitionerDbEntity.getId()));
		
		/** Gender */
		if(AppUtil.isNotEmpty(practitionerDbEntity.getGender()))
			try {
				practitioner.setGender(AdministrativeGender.fromCode(practitionerDbEntity.getGender().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return practitioner;
	}
	
	public Practitioner dbModelToResource(PractitionerMapDbEntity practitionerMapDbEntity, Practitioner practitioner)
	{
		/** Contact Point  */
		if(practitionerMapDbEntity.getContactPointDbEntity() != null) {
			ContactPoint cp = toContactPointResource(practitionerMapDbEntity.getContactPointDbEntity());
			practitioner.addTelecom(cp);
		}
		
		/** Address */
		if(practitionerMapDbEntity.getAddressDbEntity() != null)
		{
			Address a = toAddressResource(practitionerMapDbEntity.getAddressDbEntity());
			practitioner.addAddress(a);
		}
		
		/** Human Name */
		if(practitionerMapDbEntity.getHumanNameDbEntity() != null)
		{
			HumanName hn = toHumanNameResource(practitionerMapDbEntity.getHumanNameDbEntity());
			practitioner.addName(hn);
		}
		
		return practitioner;
	}
	
	/**
	 * 
	 * @param adrDbEntity
	 * @return This method converts the DBEntity to Address model class and returns the model instance
	 */
	private Address toAddressResource(AddressDbEntity adrDbEntity) {

		Address addr = new Address();
		
		// City
		if (AppUtil.isNotEmpty(adrDbEntity.getCity()))
			addr.setCity(adrDbEntity.getCity().toString());
		
		// Use
		if(adrDbEntity.getCodingDbEntity() != null)
			try {
				if(AppUtil.isNotEmpty(adrDbEntity.getCodingDbEntity().getCode()))
					addr.setUse(AddressUse.fromCode(adrDbEntity.getCodingDbEntity().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// Country
		if (AppUtil.isNotEmpty(adrDbEntity.getCountry()))
			addr.setCountry(adrDbEntity.getCountry().toString());
		
		// Line/ Type
		if (AppUtil.isNotEmpty(adrDbEntity.getLine())) {
			StringType type = new StringType();
			type.setValue(adrDbEntity.getLine().toString());
			List<StringType> list = addr.getLine();
			list.add(type);
			addr.setLine(list);
		}

		// State
		if (AppUtil.isNotEmpty(adrDbEntity.getState())) {
			addr.setState(adrDbEntity.getState().toString());
		}
		
		// Postal Code
		if (AppUtil.isNotEmpty(adrDbEntity.getPostalcode())) {
			addr.setPostalCode(adrDbEntity.getPostalcode().toString());
		}

		return addr;
	}

	
	
	/**
	 * 
	 * @param contactPointDbEntity
	 * @return
	 */
	private ContactPoint toContactPointResource(ContactPointDbEntity contactPointDbEntity)
	{
	
	ContactPoint cp = new ContactPoint();		
	// System
	if(AppUtil.isNotEmpty(contactPointDbEntity.getSystem()))
		try {
			cp.setSystem(ContactPointSystem.fromCode(contactPointDbEntity.getSystem().toLowerCase()));
		} catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	// Value
	if(AppUtil.isNotEmpty(contactPointDbEntity.getValue()))
		cp.setValue(contactPointDbEntity.getValue());
	
	// Use
	if(AppUtil.isNotEmpty(contactPointDbEntity.getUseAlias()))
		try {
			cp.setUse(ContactPointUse.fromCode(contactPointDbEntity.getUseAlias().toLowerCase()));
		} catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	// Rank
	if(AppUtil.isNotEmpty(String.valueOf(contactPointDbEntity.getRank())))
			cp.setRank(contactPointDbEntity.getRank());
	
	return cp;
}
	
	/**
	 * 
	 * @param humanNameDbEntity
	 * @return This method converts dbEntity to HumanName model class and returns the model object.
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
	 * @param allergyIntolerance
	 * @param allergyIntoleranceDbEntity
	 * @return This method returns AllergyIntolerance with all the base components
	 */
/*	public AllergyIntolerance getAllergyIntoleranceBaseComponents(AllergyIntolerance allergyIntolerance, AllergyIntoleranceDbEntity allergyIntoleranceDbEntity){

		
		*//** Setting id *//*
		allergyIntolerance.setId(String.valueOf(allergyIntoleranceDbEntity.getId()));
			
		
		*//** Verification Status *//*
		if(allergyIntoleranceDbEntity.getCodingDbEntityByVerificationstatus() != null)
			if(AppUtil.isNotEmpty(allergyIntoleranceDbEntity.getCodingDbEntityByVerificationstatus().getCode()))
			try {
					allergyIntolerance.setVerificationStatus(AllergyIntoleranceVerificationStatus.fromCode(allergyIntoleranceDbEntity.getCodingDbEntityByVerificationstatus().getCode().toLowerCase()));
				}
			catch(FHIRException e)
				{
					e.printStackTrace();
					logger.error("unknown value :", e);
				}

		*//** Allergy Type *//*
		if(allergyIntoleranceDbEntity.getCodingDbEntityByType() != null)
			if(AppUtil.isNotEmpty(allergyIntoleranceDbEntity.getCodingDbEntityByType().getCode()))
			try {
					allergyIntolerance.setType(AllergyIntoleranceType.fromCode(allergyIntoleranceDbEntity.getCodingDbEntityByType().getCode().toLowerCase()));					
				}
			catch(FHIRException e)
				{
					e.printStackTrace();
					logger.error("unknown value :", e);
				}

		*//**Allergy Category *//*
		if(allergyIntoleranceDbEntity.getCodingDbEntityByCategory() != null)
			if(AppUtil.isNotEmpty(allergyIntoleranceDbEntity.getCodingDbEntityByCategory().getCode()))	
			try {
					allergyIntolerance.addCategory(AllergyIntoleranceCategory.fromCode(allergyIntoleranceDbEntity.getCodingDbEntityByCategory().getCode().toLowerCase()));
				}
			catch(FHIRException e)
				{
					e.printStackTrace();
					logger.error("unknown value :", e);
				}
		*//** Allergy Criticality *//*
		if(allergyIntoleranceDbEntity.getCodingDbEntityByCriticality() != null)
			if(AppUtil.isNotEmpty(allergyIntoleranceDbEntity.getCodingDbEntityByCriticality().getCode()))
			try
				{
					if(AppUtil.isNotEmpty(allergyIntoleranceDbEntity.getCodingDbEntityByCriticality().getCode()))
						allergyIntolerance.setCriticality(AllergyIntoleranceCriticality.fromCode(allergyIntoleranceDbEntity.getCodingDbEntityByCriticality().getCode().toLowerCase()));
				}
			catch(FHIRException e)
				{
					e.printStackTrace();
					logger.error("unknown value :", e);
				}
		*//**Clinical Status *//*
		if(allergyIntoleranceDbEntity.getCodingDbEntityByClinicalstatus() != null)
			if(AppUtil.isNotEmpty(allergyIntoleranceDbEntity.getCodingDbEntityByClinicalstatus().getCode()))
			try {
					allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.fromCode(allergyIntoleranceDbEntity.getCodingDbEntityByClinicalstatus().getCode().toLowerCase()));
				}
			catch(FHIRException e)
				{
					e.printStackTrace();
					logger.error("unknown value :", e);
				}
		
		*//** Allergy Code *//*
		if(allergyIntoleranceDbEntity.getCodingDbEntityByCode() != null) {
		allergyIntolerance.setCode(new CodeableConcept().addCoding(new Coding(allergyIntoleranceDbEntity.getCodingDbEntityByCode().getSystem(),
				allergyIntoleranceDbEntity.getCodingDbEntityByCode().getCode(),
				allergyIntoleranceDbEntity.getCodingDbEntityByCode().getDisplay())));
		}
		
		*//** Patient *//*
		PatientDbEntity patientDbEntity = allergyIntoleranceDbEntity.getPatientDbEntity();
		if(patientDbEntity!= null)
			allergyIntolerance.setPatient(new Reference(ResourcesLiteralsUtil.Patient_Ref + String.valueOf(patientDbEntity.getId())));
		

		*//** Allergy Onset Period *//*
		if(allergyIntoleranceDbEntity.getPeriodDbEntity() != null)
		allergyIntolerance.setOnset(new Period().setStart(allergyIntoleranceDbEntity.getPeriodDbEntity().getStart())
				.setEnd(allergyIntoleranceDbEntity.getPeriodDbEntity().getEndAlias()));
		
		*//** Allergy Note *//*
		Annotation t = new Annotation(new StringType(allergyIntoleranceDbEntity.getNote()));
		allergyIntolerance.addNote(t);
		
		return allergyIntolerance;
}

	*//**
	 * 
	 * @param allergyIntoleranceMapDbEntity
	 * @param allergyIntolerance
	 * @return This method returns AllergyIntolerance with all associated attributes mapped.
	 *//*
	public AllergyIntolerance dbModelToResource(AllergyIntoleranceMapDbEntity allergyIntoleranceMapDbEntity, AllergyIntolerance allergyIntolerance){

						
		*//** Recorder *//*
		if(allergyIntoleranceMapDbEntity.getPractitionerDbEntity() != null)
			allergyIntolerance.setRecorder(new Reference(ResourcesLiteralsUtil.Practitioner_Ref + String.valueOf(allergyIntoleranceMapDbEntity.getPractitionerDbEntity().getId())));
		
		*//** Asserter *//*
		if(allergyIntoleranceMapDbEntity.getPractitionerDbEntity() != null)
			allergyIntolerance.setAsserter(new Reference(ResourcesLiteralsUtil.Practitioner_Ref + String.valueOf(allergyIntoleranceMapDbEntity.getPractitionerDbEntity().getId())));

		
		*//** Reaction For Allergy *//*
		ReactionnDbEntity reacDbEntity = allergyIntoleranceMapDbEntity.getReactionnDbEntity();
		if(reacDbEntity!=null)
		return getReactionComponent(allergyIntolerance,reacDbEntity);
		
		return allergyIntolerance;
	}
	
	*//**
	 * 
	 * @param alIMapDbEntityList
	 * @return This method returns list of AllergyIntolerance with it's corresponding attributes mapped,
	 *  when searched with parameters.
	 *//*
	public List<AllergyIntolerance> dbModelToResource(List<AllergyIntoleranceMapDbEntity> alIMapDbEntityList) {

		
		Map<String, AllergyIntolerance> allergyIntoleranceMap = new HashMap<>();
		Iterator<AllergyIntoleranceMapDbEntity> alIMapDbSetItr = alIMapDbEntityList.iterator();
		while (alIMapDbSetItr.hasNext()) {

			AllergyIntoleranceMapDbEntity alIMapDbEnt = alIMapDbSetItr.next();
			if(alIMapDbEnt.getAllergyIntoleranceDbEntity() != null)
			if (allergyIntoleranceMap.containsKey(String.valueOf(alIMapDbEnt.getAllergyIntoleranceDbEntity().getId()))) {

				AllergyIntolerance allergyIntolerance = dbModelToResource(alIMapDbEnt, allergyIntoleranceMap.get(String.valueOf(alIMapDbEnt.getAllergyIntoleranceDbEntity().getId())));
				allergyIntolerance= getAllergyIntoleranceBaseComponents(allergyIntolerance, alIMapDbEnt.getAllergyIntoleranceDbEntity());
				allergyIntoleranceMap.put(allergyIntolerance.getId(),allergyIntolerance);
				
			} else {
				
				AllergyIntolerance allergyIntolerance = dbModelToResource(alIMapDbEnt, new AllergyIntolerance());
				allergyIntolerance= getAllergyIntoleranceBaseComponents(allergyIntolerance, alIMapDbEnt.getAllergyIntoleranceDbEntity());
				allergyIntoleranceMap.put(allergyIntolerance.getId(),allergyIntolerance);				
			
			}
		}
		
		List<AllergyIntolerance> allergyIntoleranceList = new ArrayList<AllergyIntolerance>(allergyIntoleranceMap.values());

		return allergyIntoleranceList;
	}

	
	*//**
	 * 
	 * @param allergyIntolerance
	 * @param reacDbEntity
	 * @return This method returns Allergy Intolerance with Reaction component
	 *//*
	private AllergyIntolerance getReactionComponent(AllergyIntolerance allergyIntolerance, ReactionnDbEntity reacDbEntity)
	{
		AllergyIntoleranceReactionComponent comp = new AllergyIntoleranceReactionComponent();
		
		*//**Reaction Manifestation*//*
		if(reacDbEntity.getCodingDbEntityByManifestation() != null){
		comp.addManifestation(new CodeableConcept().addCoding(new Coding(reacDbEntity.getCodingDbEntityByManifestation().getSystem(),
				reacDbEntity.getCodingDbEntityByManifestation().getCode(),
				reacDbEntity.getCodingDbEntityByManifestation().getDisplay())));
		}
		
		*//** Reaction Onset *//*
		comp.setOnset(reacDbEntity.getOnset());
		
		*//** Reaction Severity *//*
		if(reacDbEntity.getCodingDbEntityBySeverity() != null)
		if(AppUtil.isNotEmpty(reacDbEntity.getCodingDbEntityBySeverity().getCode()))
		try {
				comp.setSeverity(AllergyIntoleranceSeverity.fromCode(reacDbEntity.getCodingDbEntityBySeverity().getCode().toLowerCase()));
			}
		catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("unknown value :", e);
			}

		*//** Reaction Note*//*
		Annotation an = new Annotation(new StringType(reacDbEntity.getNote()));
		comp.addNote(an);

		allergyIntolerance.addReaction(comp);
		
		return allergyIntolerance;*/
	

}
