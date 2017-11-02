package com.cantatahealth.fhir.service.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Medication.MedicationStatus;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationAdministration.MedicationAdministrationDosageComponent;
import org.hl7.fhir.dstu3.model.MedicationAdministration.MedicationAdministrationPerformerComponent;
import org.hl7.fhir.dstu3.model.MedicationAdministration.MedicationAdministrationStatus;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Quantity.QuantityComparator;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.DosageDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationMapDbEntity;
import com.cantatahealth.fhir.db.model.MedicationDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.QuantityDbEntity;
import com.cantatahealth.fhir.db.model.ReactionnDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps medication data to MedicationAdministration resource
 * 
 * @author santosh
 *
 */
@Component
public class MedicationAdministrationResourceMapper {

	Logger logger = LoggerFactory.getLogger(MedicationAdministrationResourceMapper.class);
	
	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	/**
	 * 
	 * @param medication
	 * @param medicationAdministrationDbEntity
	 * @return This method returns the medication administration with it's base components mapped. 
	 */
	public MedicationAdministration getMedicationAdministrationBaseComponents(MedicationAdministration medication, MedicationAdministrationDbEntity medicationAdministrationDbEntity){
		
		/** Setting id */
		medication.setId(String.valueOf(medicationAdministrationDbEntity.getId()));
			
		
		/** Patient */		 
		if(medicationAdministrationDbEntity.getPatientDbEntity() != null)
			medication.setSubject(new Reference(ResourcesLiteralsUtil.Patient_Ref + String.valueOf(medicationAdministrationDbEntity.getPatientDbEntity().getId())));
		
		/** Status*/
		if(medicationAdministrationDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(medicationAdministrationDbEntity.getCodingDbEntityByStatus().getCode()))
			try {			
					medication.setStatus(MedicationAdministrationStatus.fromCode(medicationAdministrationDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
				}
			catch(FHIRException e)
				{
					e.printStackTrace();
					logger.error("unknown value :", e);
				}
		
		/** Category */
		if(medicationAdministrationDbEntity.getCodingDbEntityByCategory() != null)
		{	
			medication.setCategory(new CodeableConcept()
					.addCoding(new Coding(medicationAdministrationDbEntity.getCodingDbEntityByCategory().getSystem()
							,medicationAdministrationDbEntity.getCodingDbEntityByCategory().getCode()
							,medicationAdministrationDbEntity.getCodingDbEntityByCategory().getDisplay())));
		}
		
		/** Effective Period */
		if(medicationAdministrationDbEntity.getPeriodDbEntity() != null)
		{
			medication.setEffective(new Period().setStart(medicationAdministrationDbEntity.getPeriodDbEntity().getStart())
					.setEnd(medicationAdministrationDbEntity.getPeriodDbEntity().getEndAlias()));
		}
		
		/**Not given*/
		if(medicationAdministrationDbEntity.getNotgiven() != null)
			medication.setNotGiven(medicationAdministrationDbEntity.getNotgiven());
		
		/**Reason Not Given*/
		if(medicationAdministrationDbEntity.getCodingDbEntityByReasonnotgiven() != null)
		{	
			medication.addReasonNotGiven(new CodeableConcept()
					.addCoding(new Coding(medicationAdministrationDbEntity.getCodingDbEntityByReasonnotgiven().getSystem()
					,medicationAdministrationDbEntity.getCodingDbEntityByReasonnotgiven().getCode()
					,medicationAdministrationDbEntity.getCodingDbEntityByReasonnotgiven().getDisplay())));
		}
		
		/** Context */
		if(medicationAdministrationDbEntity.getEncounterDbEntity() != null)
			medication.setContext(new Reference(ResourcesLiteralsUtil.Encounter_Ref + String.valueOf(medicationAdministrationDbEntity.getEncounterDbEntity().getId())));
		
		/** Reason Code*/
		if(medicationAdministrationDbEntity.getCodingDbEntityByReasoncode() != null)
		{
			medication.addReasonNotGiven(new CodeableConcept()
					.addCoding(new Coding(medicationAdministrationDbEntity.getCodingDbEntityByReasoncode().getSystem()
					,medicationAdministrationDbEntity.getCodingDbEntityByReasoncode().getCode()
					,medicationAdministrationDbEntity.getCodingDbEntityByReasoncode().getDisplay())));
		}
		
		/** Medication */
		if(medicationAdministrationDbEntity.getMedicationDbEntity() != null) {
		Medication m = toMedicationResource(medicationAdministrationDbEntity.getMedicationDbEntity());			
		medication.addContained(m);
		medication.setMedication(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Medication_Ref +String.valueOf(medicationAdministrationDbEntity.getMedicationDbEntity().getId())));
	}
		
		/** Dosage */
		if(medicationAdministrationDbEntity.getDosageDbEntity() != null) {			
			MedicationAdministrationDosageComponent dose = toDosageResource(medicationAdministrationDbEntity.getDosageDbEntity());	
			medication.setDosage(dose);
		}
		
		/** Note */
		if(AppUtil.isNotEmpty(medicationAdministrationDbEntity.getNote()))
			{
				medication.addNote(new Annotation().setText(medicationAdministrationDbEntity.getNote()));
			}
		return medication;
	}

	/**
	 * 
	 * @param medicationDbEntity
	 * @return
	 */
	private Medication toMedicationResource(MedicationDbEntity medicationDbEntity)
	{
		Medication m = new Medication();
		
		if(AppUtil.isNotEmpty(String.valueOf(medicationDbEntity.getId())))
				m.setId("#" +ResourcesLiteralsUtil.Medication_Ref+ String.valueOf(medicationDbEntity.getId()));
			
		if(medicationDbEntity.getCodingDbEntityByCode() != null)
				m.setCode(new CodeableConcept().addCoding(new Coding(medicationDbEntity.getCodingDbEntityByCode().getSystem(),
						medicationDbEntity.getCodingDbEntityByCode().getCode(),
						medicationDbEntity.getCodingDbEntityByCode().getDisplay())));
			
		if(medicationDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(medicationDbEntity.getCodingDbEntityByStatus().getCode()))
				try {
					m.setStatus(MedicationStatus.fromCode(medicationDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
				} catch (FHIRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		if(medicationDbEntity.getIsbrand() != null)
			m.setIsBrand(medicationDbEntity.getIsbrand());
						
		return m;			
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
	 * @param dosageDbEntity
	 * @return
	 */
	private MedicationAdministrationDosageComponent toDosageResource(DosageDbEntity dosageDbEntity)
	{		
		
		MedicationAdministrationDosageComponent dose = new MedicationAdministrationDosageComponent();
		/** Quantity*/
		Quantity quantity = null;
		if(dosageDbEntity.getQuantityDbEntity() != null) {
			quantity = toQuantityResource(dosageDbEntity.getQuantityDbEntity());
			dose.setDose((SimpleQuantity) quantity);
		}
		
		
		/** Route */
		CodeableConcept route = null;
		if(dosageDbEntity.getCodingDbEntity() != null) {
			route=new CodeableConcept().addCoding(new Coding(dosageDbEntity.getCodingDbEntity().getSystem(),
					dosageDbEntity.getCodingDbEntity().getCode(),
					dosageDbEntity.getCodingDbEntity().getDisplay()));
		dose.setRoute(route);
		}
		
		/** Text */
		if(AppUtil.isNotEmpty(dosageDbEntity.getText()))
			dose.setText(dosageDbEntity.getText());

		return dose; 	
	}
	/**
	 * 
	 * Convert medication db entity to medication resource
	 * 
	 * @param patientMapDbEntity
	 * @param medication
	 * @return This method returns medicationAdministration with associated attributes.
	 */
	public MedicationAdministration dbModelToResource(MedicationAdministrationMapDbEntity medicationAdministrationMapDbEntity, MedicationAdministration medication) {
		
		/** Performer */
		if(medicationAdministrationMapDbEntity.getPractitionerDbEntity() != null) {
			Practitioner p = practitionerResourceMapper.toPractitionerResource((medicationAdministrationMapDbEntity.getPractitionerDbEntity().getId()));
			medication.addContained(p);
			MedicationAdministrationPerformerComponent mafc = new MedicationAdministrationPerformerComponent(
					new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Practitioner_Ref +
							String.valueOf(medicationAdministrationMapDbEntity.getPractitionerDbEntity().getId())));
			medication.addPerformer(mafc);
		}
					
		return medication;
	}

	/**
	 * 
	 * Convert list of medication db entities to list of medication resources
	 * 
	 * @param medicationDbEntityList
	 * @return This method returns the list of medicationAdministration with associated attributes when searched by parameters
	 */
	public List<MedicationAdministration> dbModelToResource(Set<MedicationAdministrationMapDbEntity> medicationDbEntitySet) {

		Map<String, MedicationAdministration> medicationMap = new HashMap<>();	
		Iterator<MedicationAdministrationMapDbEntity> amAMapDbSetItr = medicationDbEntitySet.iterator();
		while (amAMapDbSetItr.hasNext()) {

			MedicationAdministrationMapDbEntity mAIMapDbEnt = amAMapDbSetItr.next();
			if(mAIMapDbEnt.getMedicationAdministrationDbEntity() != null)
			if (medicationMap.containsKey(String.valueOf(mAIMapDbEnt.getMedicationAdministrationDbEntity().getId()))) {

				MedicationAdministration medicationAdministration = dbModelToResource(mAIMapDbEnt, medicationMap.get(String.valueOf(mAIMapDbEnt.getMedicationAdministrationDbEntity().getId())));
				medicationAdministration=getMedicationAdministrationBaseComponents(medicationAdministration, mAIMapDbEnt.getMedicationAdministrationDbEntity());
				medicationMap.put(medicationAdministration.getId(),medicationAdministration );
				
			} else {
				MedicationAdministration medicationAdministration = dbModelToResource(mAIMapDbEnt, new MedicationAdministration());
				medicationAdministration=getMedicationAdministrationBaseComponents(medicationAdministration, mAIMapDbEnt.getMedicationAdministrationDbEntity());
				medicationMap.put(medicationAdministration.getId(), medicationAdministration);
				
			}
		}
		List<MedicationAdministration> medicationList = new ArrayList<MedicationAdministration>(medicationMap.values());

		return medicationList;
	}

}
