package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.CareTeam.CareTeamParticipantComponent;
import org.hl7.fhir.dstu3.model.CareTeam.CareTeamStatus;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationMapDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps CareTeam data to CareTeam resource
 * 
 * @author santosh
 *
 */
@Component
public class CareTeamResourceMapper {

	Logger logger = LoggerFactory.getLogger(CareTeamResourceMapper.class);

	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	/**
	 * 
	 * Convert CareTeam db entity to CareTeam resource
	 * 
	 * @param careTeamDbEntity
	 * @param CareTeam
	 * @return
	 */
	
	public CareTeam getCareTeamBaseComponents(CareTeam careTeam, CareTeamDbEntity careTeamDbEntity){
		
		
		/** Setting Id */
		if(AppUtil.isNotEmpty(String.valueOf(careTeamDbEntity.getId())))
			{
				careTeam.setId(String.valueOf(careTeamDbEntity.getId()));
			}
		
		// Category
		if(careTeamDbEntity.getCodingDbEntityByCategory() != null) {
			careTeam.addCategory(new CodeableConcept().addCoding(new Coding(careTeamDbEntity.getCodingDbEntityByCategory().getSystem(),
					careTeamDbEntity.getCodingDbEntityByCategory().getCode(),
					careTeamDbEntity.getCodingDbEntityByCategory().getDisplay())));
		}
		
		/** Team Name*/
		if(AppUtil.isNotEmpty(careTeamDbEntity.getName()))
		{
			careTeam.setName(careTeamDbEntity.getName());
		}
		
		/** Status */
		if(careTeamDbEntity.getCodingDbEntityByStatus() != null)
		{	if(AppUtil.isNotEmpty(careTeamDbEntity.getCodingDbEntityByStatus().getCode()))
			try {
				careTeam.setStatus(CareTeamStatus.fromCode(careTeamDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("unknown value :", e);
			}
		}
		
		/** Period */
		
		if(careTeamDbEntity.getPeriodDbEntity() != null)
			careTeam.setPeriod(new Period().setStart(careTeamDbEntity.getPeriodDbEntity().getStart()).setEnd(careTeamDbEntity.getPeriodDbEntity().getEndAlias()));
		
		/** Note */
		if(AppUtil.isNotEmpty(careTeamDbEntity.getNote()))
			{
			careTeam.addNote(new Annotation(new StringType(careTeamDbEntity.getNote())));
			}
		
		/** Subject/Patient */
		if(careTeamDbEntity.getPatientDbEntity() != null)
			careTeam.setSubject(new Reference(ResourcesLiteralsUtil.Patient_Ref + String.valueOf(careTeamDbEntity.getPatientDbEntity().getId())));
		
		/** Context/Encounter */
		if(careTeamDbEntity.getEncounterDbEntity() != null)
			careTeam.setContext(new Reference(ResourcesLiteralsUtil.Encounter_Ref + String.valueOf(careTeamDbEntity.getEncounterDbEntity().getId())));
		
		return careTeam;
	}
		
	public CareTeam dbModelToResource(CareTeamMapDbEntity careTeamMapDbEntity, CareTeam careTeam) {
		
		/** Participant */
		if(careTeamMapDbEntity.getPractitionerDbEntity() != null)
		{
			Practitioner p = practitionerResourceMapper.toPractitionerResource(careTeamMapDbEntity.getPractitionerDbEntity().getId());
			careTeam.addContained(p);
			careTeam.addParticipant(new CareTeamParticipantComponent()
					.setMember(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Practitioner_Ref+
							String.valueOf((careTeamMapDbEntity.getPractitionerDbEntity().getId())))));
		}
		
		return careTeam;
	}

	/**
	 * 
	 * Convert list of CareTeam db entities to list of CareTeam resources
	 * 
	 * @param patMapDbEntityList
	 * @return
	 */
	public List<CareTeam> dbModelToResource(Set<CareTeamMapDbEntity> ctMapDbEntityList) {

		Map<String, CareTeam> careTeamMap = new HashMap<>();	
		Iterator<CareTeamMapDbEntity> amAMapDbSetItr = ctMapDbEntityList.iterator();
		while (amAMapDbSetItr.hasNext()) {

			CareTeamMapDbEntity ctIMapDbEnt = amAMapDbSetItr.next();
			if(ctIMapDbEnt.getCareTeamDbEntity() != null)
			if (careTeamMap.containsKey(String.valueOf(ctIMapDbEnt.getCareTeamDbEntity().getId()))) {

				CareTeam careTeam = dbModelToResource(ctIMapDbEnt, careTeamMap.get(String.valueOf(ctIMapDbEnt.getCareTeamDbEntity().getId())));
				careTeam=getCareTeamBaseComponents(careTeam, ctIMapDbEnt.getCareTeamDbEntity());
					careTeamMap.put(careTeam.getId(),careTeam );
				
			} else {
				CareTeam careTeam = dbModelToResource(ctIMapDbEnt, new CareTeam());
				careTeam=getCareTeamBaseComponents(careTeam, ctIMapDbEnt.getCareTeamDbEntity());
				careTeamMap.put(careTeam.getId(),careTeam );
				
			}
		}
		List<CareTeam> careTeamList = new ArrayList<CareTeam>(careTeamMap.values());
		
		return careTeamList;
	}

}
