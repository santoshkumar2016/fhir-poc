package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.ConditionDbEntity;
import com.cantatahealth.fhir.db.model.ConditionMapDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps Condition db data to Condition resource
 * 
 * @author santosh
 *
 */
@Component
public class ConditionResourceMapper {

	Logger logger = LoggerFactory.getLogger(ConditionResourceMapper.class);
	
	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	/**
	 * 
	 * Convert Condition db entity to Condition resource
	 * 
	 * @param patientMapDbEntity
	 * @param condition
	 * @return
	 */
	public Condition dbModelToResource(ConditionDbEntity conditionDbEntity, Condition condition) {

		if(conditionDbEntity == null){
			return condition;
		}
		
		condition.setId(String.valueOf(conditionDbEntity.getId()));
		
		if(conditionDbEntity.getPatientDbEntity() != null){
			condition.setSubject(new Reference(ResourcesLiteralsUtil.Patient_Ref
					+ String.valueOf(conditionDbEntity.getPatientDbEntity().getId())));
		}
	
		if (conditionDbEntity.getAbatementdatetime() != null) {
			DateTimeType dateType = new DateTimeType(conditionDbEntity.getAbatementdatetime());
			condition.setAbatement(dateType);
		}
		if (conditionDbEntity.getCodingDbEntityByClinicalstatus() != null) {
			try {
				if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByClinicalstatus().getCode()))
					condition.setClinicalStatus(ConditionClinicalStatus
							.fromCode(conditionDbEntity.getCodingDbEntityByClinicalstatus().getCode().toLowerCase()));
			} catch (FHIRException e) {

				e.printStackTrace();
			}
		}

		if (conditionDbEntity.getCodingDbEntityByCategory() != null) {
			CodeableConcept codCon = new CodeableConcept();
			Coding coding = new Coding();
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByCategory().getCode()))
				coding.setCode(conditionDbEntity.getCodingDbEntityByCategory().getCode());
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByCategory().getDisplay()))
				coding.setDisplay(conditionDbEntity.getCodingDbEntityByCategory().getDisplay());
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByCategory().getSystem()))
				coding.setSystem(conditionDbEntity.getCodingDbEntityByCategory().getSystem());
			codCon.getCoding().add(coding);
			condition.getCategory().add(codCon);
		}

		if (conditionDbEntity.getCodingDbEntityByCode() != null) {
			CodeableConcept codCon = new CodeableConcept();
			Coding coding = new Coding();
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByCode().getCode()))
				coding.setCode(conditionDbEntity.getCodingDbEntityByCode().getCode());
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByCode().getDisplay()))
				coding.setDisplay(conditionDbEntity.getCodingDbEntityByCode().getDisplay());
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByCode().getSystem()))
				coding.setSystem(conditionDbEntity.getCodingDbEntityByCode().getSystem());
			codCon.getCoding().add(coding);
			condition.setCode(codCon);

		}

		if (conditionDbEntity.getCodingDbEntityBySeverity() != null) {
			CodeableConcept codCon = new CodeableConcept();
			Coding coding = new Coding();
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityBySeverity().getCode()))
				coding.setCode(conditionDbEntity.getCodingDbEntityBySeverity().getCode());
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityBySeverity().getDisplay()))
				coding.setDisplay(conditionDbEntity.getCodingDbEntityBySeverity().getDisplay());
			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityBySeverity().getSystem()))
				coding.setSystem(conditionDbEntity.getCodingDbEntityBySeverity().getSystem());
			codCon.getCoding().add(coding);
			condition.setSeverity(codCon);

		}
		
		if (conditionDbEntity.getOnsetdatetime() != null) {
			DateTimeType onsetDate = new DateTimeType(conditionDbEntity.getOnsetdatetime());
			condition.setOnset(onsetDate);
		}

		if (conditionDbEntity.getCodingDbEntityByVerificationstatus() != null) {

			if (AppUtil.isNotEmpty(conditionDbEntity.getCodingDbEntityByVerificationstatus().getCode()))
				try {
					condition.setVerificationStatus(ConditionVerificationStatus.fromCode(
							conditionDbEntity.getCodingDbEntityByVerificationstatus().getCode().toLowerCase()));
				} catch (FHIRException e) {

					e.printStackTrace();
				}

		}

		if (conditionDbEntity.getEncounterDbEntity() != null)
			condition.setContext(new Reference(ResourcesLiteralsUtil.Encounter_Ref
					+ String.valueOf(conditionDbEntity.getEncounterDbEntity().getId())));

		if (conditionDbEntity.getPractitionerDbEntity() != null) {
			Practitioner p = practitionerResourceMapper.toPractitionerResource(conditionDbEntity.getPractitionerDbEntity().getId());
			condition.addContained(p);
			condition.setAsserter(new Reference(ResourcesLiteralsUtil.Identifier_Hash+ResourcesLiteralsUtil.Practitioner_Ref
					+ String.valueOf(conditionDbEntity.getPractitionerDbEntity().getId())));
		}

		return condition;
	}

	/**
	 * 
	 * Convert ConditionMap db entity to Condition resource
	 * 
	 * @param patientMapDbEntity
	 * @param condition
	 * @return
	 */
	// public Condition dbModelToResource(ConditionMapDbEntity
	// conditionMapDbEntity, Condition condition) {
	//
	//
	// if(conditionMapDbEntity == null ||
	// conditionMapDbEntity.getConditionDbEntity() == null){
	// return condition;
	// }
	//
	// ConditionDbEntity conditionDbEntity =
	// conditionMapDbEntity.getConditionDbEntity();
	//
	// condition = dbModelToResource(conditionDbEntity, condition);
	//
	// if(conditionMapDbEntity.getPractitionerDbEntity() != null){
	// condition.setContext(new Reference(ResourcesLiteralsUtil.Practitioner_Ref
	// +
	// String.valueOf(conditionMapDbEntity.getPractitionerDbEntity().getId())));
	// }
	//
	//
	// return condition;
	// }

	public List<Condition> dbModelToResource(List<ConditionDbEntity> conditionDbEntityList) {

		List<Condition> conditionList = new ArrayList<Condition>();

		for (ConditionDbEntity condDbEnt : conditionDbEntityList)
			conditionList.add(dbModelToResource(condDbEnt, new Condition()));

		return conditionList;
	}

	/**
	 * 
	 * Convert list of ConditionMap db entities to list of Condition resources
	 * 
	 * @param ConditionDbEntityList
	 * @return
	 */
	// public List<Condition> dbModelToResourceMap(List<ConditionMapDbEntity>
	// conditionDbEntityList) {
	//
	// Map<String, Condition> conditionMap = new HashMap<>();
	//
	// Iterator<ConditionMapDbEntity> condMapDbSetItr =
	// conditionDbEntityList.iterator();
	// while (condMapDbSetItr.hasNext()) {
	//
	// ConditionMapDbEntity patMapDbEnt = condMapDbSetItr.next();
	// if
	// (conditionMap.containsKey(String.valueOf(patMapDbEnt.getConditionDbEntity().getId())))
	// {
	//
	// Condition condition = dbModelToResource(patMapDbEnt,
	// conditionMap.get(String.valueOf(patMapDbEnt.getConditionDbEntity().getId())));
	// conditionMap.put(condition.getId(), condition);
	//
	// } else {
	// Condition condition = dbModelToResource(patMapDbEnt, new Condition());
	// conditionMap.put(condition.getId(), condition);
	//
	// }
	// }
	//
	// List<Condition> conditionList = new
	// ArrayList<Condition>(conditionMap.values());
	//
	// return conditionList;
	//
	//
	//
	// }

}
