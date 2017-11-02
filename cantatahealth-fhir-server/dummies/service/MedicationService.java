package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Medication;

/**
 * 
 * Medication service interface
 * 
 * @author santosh
 *
 */
public interface MedicationService {
  
	Medication findMedicationById(Long id);
	
	List<Medication> findMedicationByParamMap(Map<String, String> paramMap);
	
}
