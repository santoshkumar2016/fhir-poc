package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Goal.GoalStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cantatahealth.fhir.db.model.GoalMapDbEntity;
import com.cantatahealth.fhir.db.model.GoalDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps Goal data to Goal resource
 * 
 * @author santosh
 *
 */
@Component
public class GoalResourceMapper {

	Logger logger = LoggerFactory.getLogger(GoalResourceMapper.class);
	
	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	/**
	 * 
	 * @param goal
	 * @param goalDbEntity
	 * @return
	 */
	public Goal getGoalBaseComponents(Goal goal, GoalDbEntity goalDbEntity){
		
		
		/** Setting Id */
		if(AppUtil.isNotEmpty(String.valueOf(goalDbEntity.getId())))
			{
				goal.setId(String.valueOf(goalDbEntity.getId()));	
			}
		
		/** Status */
		if(goalDbEntity.getCodingDbEntityByStatus() != null)
		{
			if(AppUtil.isNotEmpty(goalDbEntity.getCodingDbEntityByStatus().getCode())) {
			try {
				goal.setStatus(GoalStatus.fromCode(goalDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			}
		}
		
		/** Priority */
		
		if(goalDbEntity.getCodingDbEntityByPriority() != null)
		{
			goal.setPriority(new CodeableConcept().addCoding(
					new Coding(goalDbEntity.getCodingDbEntityByPriority().getSystem(),
					goalDbEntity.getCodingDbEntityByPriority().getCode(),
					goalDbEntity.getCodingDbEntityByPriority().getDisplay())));
		}
		
		/** Description */
		
		if(goalDbEntity.getCodingDbEntityByDescription() != null)
		{
			goal.setDescription(new CodeableConcept().addCoding(
					new Coding(goalDbEntity.getCodingDbEntityByDescription().getSystem()
					,goalDbEntity.getCodingDbEntityByDescription().getCode()
					,goalDbEntity.getCodingDbEntityByDescription().getDisplay())));
		}
		
		/** Subject/Patient */
		
		if(goalDbEntity.getPatientDbEntity() != null)
			goal.setSubject(new Reference(ResourcesLiteralsUtil.Patient_Ref + String.valueOf(goalDbEntity.getPatientDbEntity().getId())));
		
		/** startDate */
		if(goalDbEntity.getStartdate() != null)
			goal.setStart(new DateType(goalDbEntity.getStartdate()));
		
		/** expressedBy/Practitioner */
		if(goalDbEntity.getPractitionerDbEntity() != null)
		{
			Practitioner p = practitionerResourceMapper.toPractitionerResource(goalDbEntity.getPractitionerDbEntity().getId());
			goal.addContained(p);
			goal.setExpressedBy(new Reference(ResourcesLiteralsUtil.Identifier_Hash +ResourcesLiteralsUtil.Practitioner_Ref+String.valueOf(goalDbEntity.getPractitionerDbEntity().getId())));
		}		
		
		return goal;
	}
	
	/**
	 * 
	 * Convert Goal db entity to Goal resource
	 * 
	 * @param patientMapDbEntity
	 * @param Goal
	 * @return
	 */
	public Goal dbModelToResource(GoalMapDbEntity goalMapDbEntity, Goal goal) {
		
		/** Addresses/Condition */
		if(goalMapDbEntity.getConditionDbEntity() != null)
			goal.addAddresses(new Reference(ResourcesLiteralsUtil.Condition_Ref+String.valueOf(goalMapDbEntity.getConditionDbEntity().getId())));
		
		/** outComeRef/Observation */
		if(goalMapDbEntity.getObservationDbEntity() != null)
			goal.addOutcomeReference(new Reference(ResourcesLiteralsUtil.Observation_Ref + String.valueOf(goalMapDbEntity.getObservationDbEntity().getId())));
			return goal;
	}

	/**
	 * 
	 * Convert list of Goal db entities to list of Goal resources
	 * 
	 * @param GoalDbEntityList
	 * @return
	 */
	public List<Goal> dbModelToResource(Set<GoalMapDbEntity> GoalDbEntityList) {

		Map<String, Goal> goalMap = new HashMap<>();	
		Iterator<GoalMapDbEntity> gMapDbSetItr = GoalDbEntityList.iterator();
		while (gMapDbSetItr.hasNext()) {

			GoalMapDbEntity gIMapDbEnt = gMapDbSetItr.next();
			if(gIMapDbEnt.getGoalDbEntity() != null)
			if (goalMap.containsKey(String.valueOf(gIMapDbEnt.getGoalDbEntity().getId()))) {

				Goal goal = dbModelToResource(gIMapDbEnt, goalMap.get(String.valueOf(gIMapDbEnt.getGoalDbEntity().getId())));
				goal=getGoalBaseComponents(goal, gIMapDbEnt.getGoalDbEntity());
				goalMap.put(goal.getId(),goal );
				
			} else {
				Goal goal = dbModelToResource(gIMapDbEnt, new Goal());
				goal=getGoalBaseComponents(goal, gIMapDbEnt.getGoalDbEntity());
				goalMap.put(goal.getId(),goal );
				
			}
		}
		List<Goal> goalList = new ArrayList<Goal>(goalMap.values());
		
		return goalList;
	}

}
