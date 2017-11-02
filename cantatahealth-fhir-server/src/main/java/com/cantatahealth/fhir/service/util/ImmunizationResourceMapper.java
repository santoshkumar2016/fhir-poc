package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationPractitionerComponent;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.ImmunizationDbEntity;
import com.cantatahealth.fhir.db.model.ImmunizationMapDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps Immunization data to Immunization resource
 * 
 * @author santosh
 *
 */
@Component
public class ImmunizationResourceMapper {

	Logger logger = LoggerFactory.getLogger(ImmunizationResourceMapper.class);
	
	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	
	@Autowired
	OrganizationResourceMapper organizationResourceMapper;
	
	public Immunization getImmunizationBaseComponents(Immunization immunization, ImmunizationDbEntity immunizationDbEntity)
	{
		
		/** Setting Id*/
		if(AppUtil.isNotEmpty(String.valueOf(immunizationDbEntity.getId())))
			immunization.setId(String.valueOf(immunizationDbEntity.getId()));
		
		/** Patient*/
		if(immunizationDbEntity.getPatientDbEntity() != null)
			immunization.setPatient(new Reference(ResourcesLiteralsUtil.Patient_Ref + String.valueOf(immunizationDbEntity.getPatientDbEntity().getId())));
		
		//Encounter
		if(immunizationDbEntity.getEncounterDbEntity() != null)
			immunization.setEncounter(new Reference(ResourcesLiteralsUtil.Encounter_Ref + String.valueOf(immunizationDbEntity.getEncounterDbEntity().getId())));
	
		//Organization
		if(immunizationDbEntity.getOrganizationDbEntity() != null) {
			Organization org = organizationResourceMapper.toOrganizationResource(immunizationDbEntity.getOrganizationDbEntity().getId());
			immunization.addContained(org);
			immunization.setManufacturer(new Reference(ResourcesLiteralsUtil.Identifier_Hash+ResourcesLiteralsUtil.Organization_Ref + String.valueOf(immunizationDbEntity.getOrganizationDbEntity().getId())));
		}
		
		//Status
		if(immunizationDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(immunizationDbEntity.getCodingDbEntityByStatus().getCode()))
				try {
					immunization.setStatus(ImmunizationStatus.fromCode(immunizationDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
				} 
				catch (FHIRException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		//notGiven - Boolean
		if(immunizationDbEntity.getNotgiven() != null)
			immunization.setNotGiven(immunizationDbEntity.getNotgiven());
		
		//vaccineCode
		if(immunizationDbEntity.getCodingDbEntityByVaccinecode() != null)
			immunization.setVaccineCode(new CodeableConcept().addCoding(new Coding(immunizationDbEntity.getCodingDbEntityByVaccinecode().getSystem(),
					immunizationDbEntity.getCodingDbEntityByVaccinecode().getCode(),
					immunizationDbEntity.getCodingDbEntityByVaccinecode().getDisplay())));
		
		//Date
		if(immunizationDbEntity.getDate() != null)
			immunization.setDate(immunizationDbEntity.getDate());
		
		//lotNumber
		if(AppUtil.isNotEmpty(immunizationDbEntity.getLotnumber()))
			immunization.setLotNumber(immunizationDbEntity.getLotnumber());
		
		//Expiration Date
		if(immunizationDbEntity.getExpirationdate() != null)
			immunization.setExpirationDate(immunizationDbEntity.getExpirationdate());
		
		//Site
		if(immunizationDbEntity.getCodingDbEntityBySite() != null)
			immunization.setSite(new CodeableConcept().addCoding(new Coding(immunizationDbEntity.getCodingDbEntityBySite().getSystem(),
					immunizationDbEntity.getCodingDbEntityBySite().getCode(),
					immunizationDbEntity.getCodingDbEntityBySite().getDisplay())));
		
		//Route
		if(immunizationDbEntity.getCodingDbEntityByRoute() != null)
			immunization.setRoute(new CodeableConcept().addCoding(new Coding(immunizationDbEntity.getCodingDbEntityByRoute().getSystem(),
					immunizationDbEntity.getCodingDbEntityByRoute().getCode(),
					immunizationDbEntity.getCodingDbEntityByRoute().getDisplay())));
		
		//Note
		if(AppUtil.isNotEmpty(immunizationDbEntity.getNote()))
			immunization.addNote(new Annotation(new StringType(immunizationDbEntity.getNote())));
		
			
		return immunization;
	}
	/**
	 * 
	 * Convert Immunization db entity to Immunization resource
	 * 
	 * @param patientMapDbEntity
	 * @param Immunization
	 * @return
	 */
	public Immunization dbModelToResource(ImmunizationMapDbEntity immunizationtMapDbEntity, Immunization immunization) {

		
		//Location
		if(immunizationtMapDbEntity.getLocationDbEntity() != null)
			immunization.setLocation(new Reference(ResourcesLiteralsUtil.Location_Ref + String.valueOf(immunizationtMapDbEntity.getLocationDbEntity().getId())));
		
		//Practitioner
		if(immunizationtMapDbEntity.getPractitionerDbEntity() != null) { 
			
			Practitioner p = practitionerResourceMapper.toPractitionerResource(immunizationtMapDbEntity.getPractitionerDbEntity().getId());
			immunization.addContained(p);
			immunization.addPractitioner(new ImmunizationPractitionerComponent().
					setActor(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Practitioner_Ref+
							String.valueOf(immunizationtMapDbEntity.getPractitionerDbEntity().getId()))));
		}
		
		return immunization;
	}

	/**
	 * 
	 * Convert list of Immunization db entities to list of Immunization resources
	 * 
	 * @param ImmunizationDbEntityList
	 * @return This method returns the list of medicationAdministration with associated attributes when searched by parameters
	 */
	public List<Immunization> dbModelToResource(Set<ImmunizationMapDbEntity> ImmunizationDbEntityList) {

		Map<String, Immunization> ImmunizationMap = new HashMap<>();
		
		Iterator<ImmunizationMapDbEntity> immMapDbSetItr = ImmunizationDbEntityList.iterator();
		while (immMapDbSetItr.hasNext()) {

			ImmunizationMapDbEntity immMapDbEnt = immMapDbSetItr.next();
			if(immMapDbEnt.getImmunizationDbEntity() != null)
			if (ImmunizationMap.containsKey(String.valueOf(immMapDbEnt.getImmunizationDbEntity().getId()))) {

				Immunization immunization = dbModelToResource(immMapDbEnt, ImmunizationMap.get(String.valueOf(immMapDbEnt.getImmunizationDbEntity().getId())));
				immunization=getImmunizationBaseComponents(immunization, immMapDbEnt.getImmunizationDbEntity());
				ImmunizationMap.put(immunization.getId(),immunization );
				
			} else {
				Immunization immunization = dbModelToResource(immMapDbEnt, new Immunization());
				immunization=getImmunizationBaseComponents(immunization, immMapDbEnt.getImmunizationDbEntity());
				ImmunizationMap.put(immunization.getId(),immunization );
				
			}
		}		
		List<Immunization> ImmunizationList = new ArrayList<Immunization>(ImmunizationMap.values());

		return ImmunizationList;
	}

}
