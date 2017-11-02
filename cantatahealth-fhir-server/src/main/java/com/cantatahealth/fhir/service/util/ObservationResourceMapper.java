package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Quantity.QuantityComparator;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.ObservationDbEntity;
import com.cantatahealth.fhir.db.model.ObservationMapDbEntity;
import com.cantatahealth.fhir.db.model.QuantityDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps Observation data to Observation resource
 * 
 * @author santosh
 *
 */
@Component
public class ObservationResourceMapper {

	Logger logger = LoggerFactory.getLogger(ObservationResourceMapper.class);

	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	
	public Observation getObservationBaseComponents(Observation observation,ObservationDbEntity observationDbEntity) {
		
		//Setting Id
		observation.setId(String.valueOf(observationDbEntity.getId()));
		
		// Status
		if(observationDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(observationDbEntity.getCodingDbEntityByStatus().getCode()))
			try {
				observation.setStatus(ObservationStatus.fromCode(observationDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				logger.error("Unknown Value for Observation Status" + e);
				e.printStackTrace();
			}
		
		// Category
		if(observationDbEntity.getCodingDbEntityByCategory() != null) {
			observation.addCategory(new CodeableConcept().addCoding(new Coding(observationDbEntity.getCodingDbEntityByCategory().getSystem(),
					observationDbEntity.getCodingDbEntityByCategory().getCode(),
					observationDbEntity.getCodingDbEntityByCategory().getDisplay())));
		}
		
		// Code
		if(observationDbEntity.getCodingDbEntityByCode() != null) {
			observation.setCode(new CodeableConcept().addCoding(new Coding(observationDbEntity.getCodingDbEntityByCode().getSystem(),
					observationDbEntity.getCodingDbEntityByCode().getCode(),
					observationDbEntity.getCodingDbEntityByCode().getDisplay())));
		}
		
		// Subject/Patient
		if(observationDbEntity.getPatientDbEntity() != null)
			observation.setSubject(new Reference(ResourcesLiteralsUtil.Patient_Ref + observationDbEntity.getPatientDbEntity().getId()));
		
		// Context
		if(observationDbEntity.getEncounterDbEntity() != null)
			observation.setContext(new Reference(ResourcesLiteralsUtil.Encounter_Ref + observationDbEntity.getEncounterDbEntity().getId()));
		
		// Effective Period
		if(observationDbEntity.getPeriodDbEntity() != null) {
			observation.setEffective(new Period().setStart(observationDbEntity.getPeriodDbEntity().getStart()).
					setEnd(observationDbEntity.getPeriodDbEntity().getEndAlias()));
		}
		
		// ValueQuantity
		if(AppUtil.isNotEmpty(observationDbEntity.getValueString()) && observationDbEntity.getQuantityDbEntity() != null)
			observation.setValue(new StringType(observationDbEntity.getValueString()));
		else if(observationDbEntity.getQuantityDbEntity() != null) {
			Quantity q = toQuantityResource(observationDbEntity.getQuantityDbEntity());
			observation.setValue(q);
		}
		else if(AppUtil.isNotEmpty(observationDbEntity.getValueString())){
			observation.setValue(new StringType(observationDbEntity.getValueString()));
		}
		
		// Interpretation
		if(observationDbEntity.getCodingDbEntityByInterpretation() != null) {
			observation.setInterpretation(new CodeableConcept().addCoding(new Coding(observationDbEntity.getCodingDbEntityByInterpretation().getSystem(),
					observationDbEntity.getCodingDbEntityByInterpretation().getCode(),
					observationDbEntity.getCodingDbEntityByInterpretation().getDisplay())));
		}
		
		// BodySite
		if(observationDbEntity.getCodingDbEntityByBodysite() != null) {
			observation.setBodySite(new CodeableConcept().addCoding(new Coding(observationDbEntity.getCodingDbEntityByBodysite().getSystem(),
					observationDbEntity.getCodingDbEntityByBodysite().getCode(),
					observationDbEntity.getCodingDbEntityByBodysite().getDisplay())));
		}
		
		return observation;
	}
	
	/**
	 * 
	 * @param quantityDbEntity
	 * @return
	 */
	private Quantity toQuantityResource(QuantityDbEntity quantityDbEntity)
	{
		Quantity quantity = new SimpleQuantity();;
		quantity.setValue(quantityDbEntity.getValue());
			
			/** To be modified*/
			if(quantityDbEntity.getCodingDbEntity() != null)
				if(AppUtil.isNotEmpty(quantityDbEntity.getCodingDbEntity().getCode()))
					try {
						quantity.setComparator(QuantityComparator.fromCode(quantityDbEntity.getCodingDbEntity().getCode().toLowerCase()));
					} catch (FHIRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("unknown value :", e);
					}
			quantity.setUnit(quantityDbEntity.getUnit());
			quantity.setSystem(quantityDbEntity.getSystem());
			
		return quantity;
	}
	/**
	 * 
	 * Convert Observation db entity to Observation resource
	 * 
	 * @param patientMapDbEntity
	 * @param Observation
	 * @return
	 */
	public Observation dbModelToResource(ObservationMapDbEntity observationMapDbEntity, Observation observation) {
		
		 //Reference Range 
		if(observationMapDbEntity.getRangeDbEntity() != null){
			ObservationReferenceRangeComponent orc = new ObservationReferenceRangeComponent();
			Quantity q = new Quantity();
				if(observationMapDbEntity.getRangeDbEntity().getQuantityDbEntityByLow() != null)
			observation.addReferenceRange(orc.setLow((SimpleQuantity)toQuantityResource(observationMapDbEntity.getRangeDbEntity().getQuantityDbEntityByLow())).
					setHigh((SimpleQuantity)toQuantityResource(observationMapDbEntity.getRangeDbEntity().getQuantityDbEntityByHigh())));
		}
		
		// Practitioner
		if(observationMapDbEntity.getPractitionerDbEntity() != null) {
			Practitioner p = practitionerResourceMapper.toPractitionerResource(observationMapDbEntity.getPractitionerDbEntity().getId());
			observation.addContained(p);
			observation.addPerformer(new Reference(ResourcesLiteralsUtil.Identifier_Hash + 
					ResourcesLiteralsUtil.Practitioner_Ref + String.valueOf(observationMapDbEntity.getPractitionerDbEntity().getId())));
		}
		
		return observation;
	}

	/**
	 * 
	 * Convert list of Observation db entities to list of Observation resources
	 * 
	 * @param ObservationDbEntityList
	 * @return
	 */
	public List<Observation> dbModelToResource(Set<ObservationMapDbEntity> ObservationDbEntityList) {

		Map<String, Observation> ObservationMap = new HashMap<>();
		Iterator<ObservationMapDbEntity> obsAMapDbSetItr = ObservationDbEntityList.iterator();
		while (obsAMapDbSetItr.hasNext()) {

			ObservationMapDbEntity obsIMapDbEnt = obsAMapDbSetItr.next();
			if(obsIMapDbEnt.getObservationDbEntity() != null)
				if (ObservationMap.containsKey(String.valueOf(obsIMapDbEnt.getObservationDbEntity().getId()))) {

				Observation observation = dbModelToResource(obsIMapDbEnt, ObservationMap.get(String.valueOf(obsIMapDbEnt.getObservationDbEntity().getId())));
				observation=getObservationBaseComponents(observation, obsIMapDbEnt.getObservationDbEntity());
				ObservationMap.put(observation.getId(),observation );
				
				} else {
				Observation observation = dbModelToResource(obsIMapDbEnt, new Observation());
				observation=getObservationBaseComponents(observation, obsIMapDbEnt.getObservationDbEntity());
				ObservationMap.put(observation.getId(),observation );
				
				}
			}
		
		List<Observation> ObservationList = new ArrayList<Observation>(ObservationMap.values());

		return ObservationList;
	}

}
