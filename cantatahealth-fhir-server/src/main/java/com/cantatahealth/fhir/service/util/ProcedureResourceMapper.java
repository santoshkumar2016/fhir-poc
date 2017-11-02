package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent;
import org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
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
public class ProcedureResourceMapper {

	Logger logger = LoggerFactory.getLogger(ProcedureResourceMapper.class);

	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	
	public Procedure getProcedureBaseComponents( Procedure procedure,ProcedureDbEntity procedureDbEntity) {
		
		//Setting Id
		procedure.setId(String.valueOf(procedureDbEntity.getId()));
		
		//Subject/Patient
		if(procedureDbEntity.getPatientDbEntity() != null)
			procedure.setSubject(
					new Reference(ResourcesLiteralsUtil.Patient_Ref + 
							String.valueOf(procedureDbEntity.getPatientDbEntity().getId())));
		
		//Status
		if(procedureDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(procedureDbEntity.getCodingDbEntityByStatus().getCode()))
				try {
					procedure.setStatus(ProcedureStatus.fromCode(procedureDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
				} catch (FHIRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		//Context
		if(procedureDbEntity.getEncounterDbEntity() != null)
			procedure.setContext(new Reference(ResourcesLiteralsUtil.Encounter_Ref + String.valueOf(procedureDbEntity.getEncounterDbEntity().getId())));
		
		// Not Done
		if(procedureDbEntity.getNotdone() != null)
			procedure.setNotDone(procedureDbEntity.getNotdone());
		
		// Not Done Reason
		if(procedureDbEntity.getCodingDbEntityByNotdonereason() != null)
			procedure.setNotDoneReason(new CodeableConcept().addCoding(new Coding(procedureDbEntity.getCodingDbEntityByNotdonereason().getSystem(),
					procedureDbEntity.getCodingDbEntityByNotdonereason().getCode(),
					procedureDbEntity.getCodingDbEntityByNotdonereason().getDisplay())));
		
		//Category
		if(procedureDbEntity.getCodingDbEntityByCategory() != null)
			procedure.setCategory(new CodeableConcept().addCoding(new Coding(procedureDbEntity.getCodingDbEntityByCategory().getSystem(),
					procedureDbEntity.getCodingDbEntityByCategory().getCode(),
					procedureDbEntity.getCodingDbEntityByCategory().getDisplay())));
		
		//Code
		if(procedureDbEntity.getCodingDbEntityByCode() != null)
			procedure.setCode(new CodeableConcept().addCoding(new Coding(procedureDbEntity.getCodingDbEntityByCode().getSystem(),
					procedureDbEntity.getCodingDbEntityByCode().getCode(),
					procedureDbEntity.getCodingDbEntityByCode().getDisplay())));
		
		//Performed Period
		if(procedureDbEntity.getPeriodDbEntity() != null)
			procedure.setPerformed(new Period().setStart(procedureDbEntity.getPeriodDbEntity().getStart()).
					setEnd(procedureDbEntity.getPeriodDbEntity().getEndAlias()));
		
		//Reason Code
		if(procedureDbEntity.getCodingDbEntityByReasoncode() != null)
			procedure.addReasonCode(new CodeableConcept().addCoding(new Coding(procedureDbEntity.getCodingDbEntityByReasoncode().getSystem(),
					procedureDbEntity.getCodingDbEntityByReasoncode().getCode(),
					procedureDbEntity.getCodingDbEntityByReasoncode().getDisplay())));

		//Location
		if(procedureDbEntity.getLocationDbEntity() != null)
			procedure.setLocation(new Reference(
				ResourcesLiteralsUtil.Location_Ref + String.valueOf(procedureDbEntity.getLocationDbEntity().getId())));
		
		return procedure;
	}
	/**
	 * 
	 * Convert Procedure db entity to Procedure resource
	 * 
	 * @param patientMapDbEntity
	 * @param Procedure
	 * @return
	 */
	public Procedure dbModelToResource(ProcedureMapDbEntity procedureMapDbEntity, Procedure procedure) {
				
		//Condition
		if(procedureMapDbEntity.getConditionDbEntity() != null)
		{
			procedure.addReasonReference(new Reference(
					ResourcesLiteralsUtil.Condition_Ref + String.valueOf(procedureMapDbEntity.getConditionDbEntity().getId())));
		}
		//Observation
		if(procedureMapDbEntity.getObservationDbEntity() != null) {
			procedure.addReasonReference(new Reference(
					ResourcesLiteralsUtil.Observation_Ref + String.valueOf(procedureMapDbEntity.getObservationDbEntity().getId())));
		}
		
		//Actor\Practitioner
		if(procedureMapDbEntity.getPractitionerDbEntity() != null) {
			Practitioner p = practitionerResourceMapper.toPractitionerResource((procedureMapDbEntity.getPractitionerDbEntity().getId()));
			procedure.addContained(p);
			procedure.addPerformer(new ProcedurePerformerComponent().setActor(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Practitioner_Ref + String.valueOf(procedureMapDbEntity.getPractitionerDbEntity().getId()))));
		}
		return procedure;
	}

	/**
	 * 
	 * Convert list of Procedure db entities to list of Procedure resources
	 * 
	 * @param ProcedureDbEntityList
	 * @return
	 */
	public List<Procedure> dbModelToResource(Set<ProcedureMapDbEntity> procedureDbEntityList) {

		Map<String, Procedure> ProcedureMap = new HashMap<>();
		Iterator<ProcedureMapDbEntity> prMapDbSetItr = procedureDbEntityList.iterator();
		while (prMapDbSetItr.hasNext()) {

			ProcedureMapDbEntity prMapDbEnt = prMapDbSetItr.next();
			if(prMapDbEnt.getProcedureDbEntity() != null)
			if (ProcedureMap.containsKey(String.valueOf(prMapDbEnt.getProcedureDbEntity().getId()))) {

				Procedure procedure = dbModelToResource(prMapDbEnt, ProcedureMap.get(String.valueOf(prMapDbEnt.getProcedureDbEntity().getId())));
				procedure=getProcedureBaseComponents(procedure, prMapDbEnt.getProcedureDbEntity());
				ProcedureMap.put(procedure.getId(),procedure );
				
			} else {
				Procedure procedure = dbModelToResource(prMapDbEnt, new Procedure());
				procedure=getProcedureBaseComponents(procedure, prMapDbEnt.getProcedureDbEntity());
				ProcedureMap.put(procedure.getId(),procedure );
				
			}
		}
		List<Procedure> ProcedureList = new ArrayList<Procedure>(ProcedureMap.values());

		return ProcedureList;
	}

}
