package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.MedicationAdministration;
/**
 * 
 * MedicationAdministration service interface
 * 
 * @author santosh
 *
 */
public interface MedicationAdministrationService extends FHIRService{
  
	MedicationAdministration findMedicationAdministrationById(Long id);
	
	List<MedicationAdministration> findMedicationAdministrationByParamMap(Map<String, String> paramMap);
	
}
