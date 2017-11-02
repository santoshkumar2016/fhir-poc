/**
 * 
 */
package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceClinicalStatus;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceType;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.CodingDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.ReactionnDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Utility class for AllergyIntolerance Model
 * @author somadutta
 *
 */
@Component
public class AllergyIntoleranceResourceMapper {
	
	Logger logger = LoggerFactory.getLogger(AllergyIntoleranceResourceMapper.class);
	
	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	/**
	 * 
	 * @param allergyIntolerance
	 * @param allergyIntoleranceDbEntity
	 * @return This method returns AllergyIntolerance with all the base components
	 */
	public AllergyIntolerance getAllergyIntoleranceBaseComponents(AllergyIntolerance allergyIntolerance, AllergyIntoleranceDbEntity allergyIntoleranceDbEntity){

		
		/** Setting id */
		allergyIntolerance.setId(String.valueOf(allergyIntoleranceDbEntity.getId()));
			
		
		/** Verification Status */
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

		/** Allergy Type */
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

		/**Allergy Category */
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
		/** Allergy Criticality */
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
		/**Clinical Status */
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
		
		/** Allergy Code */
		if(allergyIntoleranceDbEntity.getCodingDbEntityByCode() != null) {
		allergyIntolerance.setCode(new CodeableConcept().addCoding(new Coding(allergyIntoleranceDbEntity.getCodingDbEntityByCode().getSystem(),
				allergyIntoleranceDbEntity.getCodingDbEntityByCode().getCode(),
				allergyIntoleranceDbEntity.getCodingDbEntityByCode().getDisplay())));
		}
		
		/** Patient */
		PatientDbEntity patientDbEntity = allergyIntoleranceDbEntity.getPatientDbEntity();
		if(patientDbEntity!= null)
			allergyIntolerance.setPatient(new Reference(ResourcesLiteralsUtil.Patient_Ref + String.valueOf(patientDbEntity.getId())));
		

		/** Allergy Onset Period */
		if(allergyIntoleranceDbEntity.getPeriodDbEntity() != null)
		allergyIntolerance.setOnset(new Period().setStart(allergyIntoleranceDbEntity.getPeriodDbEntity().getStart())
				.setEnd(allergyIntoleranceDbEntity.getPeriodDbEntity().getEndAlias()));
		
		/** Allergy Note */
		Annotation t = new Annotation(new StringType(allergyIntoleranceDbEntity.getNote()));
		allergyIntolerance.addNote(t);
		
		return allergyIntolerance;
}

	/**
	 * 
	 * @param allergyIntoleranceMapDbEntity
	 * @param allergyIntolerance
	 * @return This method returns AllergyIntolerance with all associated attributes mapped.
	 */
	public AllergyIntolerance dbModelToResource(AllergyIntoleranceMapDbEntity allergyIntoleranceMapDbEntity, AllergyIntolerance allergyIntolerance){

						
		/** Recorder */
		if(allergyIntoleranceMapDbEntity.getPractitionerDbEntity() != null)
		{
			Practitioner p = practitionerResourceMapper.toPractitionerResource(allergyIntoleranceMapDbEntity.getPractitionerDbEntity().getId());
			allergyIntolerance.addContained(p);
			allergyIntolerance.setRecorder(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Practitioner_Ref+String.valueOf(allergyIntoleranceMapDbEntity.getPractitionerDbEntity().getId())));
		}
		
		/** Asserter */
		if(allergyIntoleranceMapDbEntity.getPractitionerDbEntity() != null)
		{
			Practitioner p = practitionerResourceMapper.toPractitionerResource(allergyIntoleranceMapDbEntity.getPractitionerDbEntity().getId());
			allergyIntolerance.addContained(p);
			allergyIntolerance.setAsserter(new Reference(ResourcesLiteralsUtil.Identifier_Hash +ResourcesLiteralsUtil.Practitioner_Ref+ String.valueOf(allergyIntoleranceMapDbEntity.getPractitionerDbEntity().getId())));
		}

		
		/** Reaction For Allergy */
		ReactionnDbEntity reacDbEntity = allergyIntoleranceMapDbEntity.getReactionnDbEntity();
		if(reacDbEntity!=null)
		return getReactionComponent(allergyIntolerance,reacDbEntity);
		
		return allergyIntolerance;
	}
	
	/**
	 * 
	 * @param alIMapDbEntityList
	 * @return This method returns list of AllergyIntolerance with it's corresponding attributes mapped,
	 *  when searched with parameters.
	 */
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

	
	/**
	 * 
	 * @param allergyIntolerance
	 * @param reacDbEntity
	 * @return This method returns Allergy Intolerance with Reaction component
	 */
	private AllergyIntolerance getReactionComponent(AllergyIntolerance allergyIntolerance, ReactionnDbEntity reacDbEntity)
	{
		AllergyIntoleranceReactionComponent comp = new AllergyIntoleranceReactionComponent();
		
		/**Reaction Manifestation*/
		if(reacDbEntity.getCodingDbEntityByManifestation() != null){
		comp.addManifestation(new CodeableConcept().addCoding(new Coding(reacDbEntity.getCodingDbEntityByManifestation().getSystem(),
				reacDbEntity.getCodingDbEntityByManifestation().getCode(),
				reacDbEntity.getCodingDbEntityByManifestation().getDisplay())));
		}
		
		/** Reaction Onset */
		comp.setOnset(reacDbEntity.getOnset());
		
		/** Reaction Severity */
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

		/** Reaction Note*/
		Annotation an = new Annotation(new StringType(reacDbEntity.getNote()));
		comp.addNote(an);

		allergyIntolerance.addReaction(comp);
		
		return allergyIntolerance;
	}

}
