package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Encounter.DiagnosisComponent;
import org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.dstu3.model.Encounter.EncounterParticipantComponent;
import org.hl7.fhir.dstu3.model.Encounter.EncounterStatus;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.ReferralRequest;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent;
import org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.EncounterDbEntity;
import com.cantatahealth.fhir.db.model.EncounterMapDbEntity;
import com.cantatahealth.fhir.db.model.GoalMapDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureMapDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps Procedure data to Procedure resource
 * 
 * @author santosh
 *
 */
@Component
public class EncounterResourceMapper {

	Logger logger = LoggerFactory.getLogger(EncounterResourceMapper.class);

	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	
	@Autowired
	OrganizationResourceMapper organizationResourceMapper;
	
	@Autowired
	ReferralResourceMapper referralResourceMapper;
	
	/**
	 * 
	 * @param encounter
	 * @param encounterDbEntity
	 * @return
	 */
	public Encounter getEncounterBaseComponents( Encounter encounter,EncounterDbEntity encounterDbEntity) {
		
		//Setting Id
		encounter.setId(String.valueOf(encounterDbEntity.getId()));
		
		//Subject/Patient
		if(encounterDbEntity.getPatientDbEntity() != null)
			encounter.setSubject(
					new Reference(ResourcesLiteralsUtil.Patient_Ref + 
							String.valueOf(encounterDbEntity.getPatientDbEntity().getId())));
		
		//Status
		if(encounterDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(encounterDbEntity.getCodingDbEntityByStatus().getCode()))
				try {
					encounter.setStatus(EncounterStatus.fromCode(encounterDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
				} catch (FHIRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		//Class
		if(encounterDbEntity.getCodingDbEntityByClass() != null)
			encounter.setClass_(new Coding(encounterDbEntity.getCodingDbEntityByClass().getSystem(),
					encounterDbEntity.getCodingDbEntityByClass().getCode(),
					encounterDbEntity.getCodingDbEntityByClass().getDisplay()));
		
		//Period
		if(encounterDbEntity.getPeriodDbEntity() != null)
			encounter.setPeriod(new Period().
					setStart(encounterDbEntity.getPeriodDbEntity().getStart()).
					setEnd(encounterDbEntity.getPeriodDbEntity().getEndAlias()));
		
		//Service Provider\ Organization
		if(encounterDbEntity.getOrganizationDbEntity() != null) {
			Organization o = organizationResourceMapper.toOrganizationResource(encounterDbEntity.getOrganizationDbEntity().getId());
			encounter.addContained(o);
			encounter.setServiceProvider(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Organization_Ref + 
					String.valueOf(encounterDbEntity.getOrganizationDbEntity().getId())));
		}
			
		return encounter;
	}
	/**
	 * 
	 * Convert Procedure db entity to Procedure resource
	 * 
	 * @param patientMapDbEntity
	 * @param Procedure
	 * @return
	 */
	public Encounter dbModelToResource(EncounterMapDbEntity encounterMapDbEntity, Encounter encounter) {
		
		
		
		//Condition
		if(encounterMapDbEntity.getConditionDbEntity() != null){
			encounter.addDiagnosis(new DiagnosisComponent().setCondition(new Reference(
					ResourcesLiteralsUtil.Condition_Ref + String.valueOf(encounterMapDbEntity.getLocationDbEntity().getId()))));
		}
		
		//Location
		if(encounterMapDbEntity.getLocationDbEntity() != null)
			encounter.addLocation(new EncounterLocationComponent().
					setLocation(new Reference(ResourcesLiteralsUtil.Location_Ref + 
							String.valueOf(encounterMapDbEntity.getLocationDbEntity().getId()))));
		
		// Referral Request - To Be Modified
		if(encounterMapDbEntity.getReferralRequestDbEntity() != null) {
			ReferralRequest r = referralResourceMapper.toReferralRequestResource(encounterMapDbEntity.getReferralRequestDbEntity().getId());
			encounter.addContained(r);
			encounter.addIncomingReferral(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.ReferralRequest_Ref + 
					String.valueOf(encounterMapDbEntity.getReferralRequestDbEntity().getId())));
		}
		
		// Practitioner\ Participant
		if(encounterMapDbEntity.getPractitionerDbEntity() != null) {
			Practitioner p = practitionerResourceMapper.toPractitionerResource(encounterMapDbEntity.getPractitionerDbEntity().getId());
			encounter.addContained(p);
			encounter.addParticipant(new EncounterParticipantComponent().
					setIndividual(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Practitioner_Ref +
					String.valueOf(encounterMapDbEntity.getPractitionerDbEntity().getId()))));
		}
		
		return encounter;
	}

	/**
	 * 
	 * Convert list of Procedure db entities to list of Procedure resources
	 * 
	 * @param ProcedureDbEntityList
	 * @return
	 */
	public List<Encounter> dbModelToResource(Set<EncounterMapDbEntity> encounterDbEntityList) {

		Map<String, Encounter> encounterMap = new HashMap<>();
		Iterator<EncounterMapDbEntity> enMapDbSetItr = encounterDbEntityList.iterator();
		while (enMapDbSetItr.hasNext()) {

			EncounterMapDbEntity enMapDbEnt = enMapDbSetItr.next();
			if(enMapDbEnt.getEncounterDbEntity() != null)
			if (encounterMap.containsKey(String.valueOf(enMapDbEnt.getEncounterDbEntity().getId()))) {

				Encounter encounter = dbModelToResource(enMapDbEnt, encounterMap.get(String.valueOf(enMapDbEnt.getEncounterDbEntity().getId())));
				encounter=getEncounterBaseComponents(encounter, enMapDbEnt.getEncounterDbEntity());
				encounterMap.put(encounter.getId(),encounter );
				
			} else {
				Encounter encounter = dbModelToResource(enMapDbEnt, new Encounter());
				encounter=getEncounterBaseComponents(encounter, enMapDbEnt.getEncounterDbEntity());
				encounterMap.put(encounter.getId(),encounter );
				
			}
		}
		List<Encounter> encounterList = new ArrayList<Encounter>(encounterMap.values());

		return encounterList;
	}

}
