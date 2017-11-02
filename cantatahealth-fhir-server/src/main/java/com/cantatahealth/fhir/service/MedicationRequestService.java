package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * MedicationRequest service interface
 * 
 * @author santosh
 *
 */
public interface MedicationRequestService {
  
	MedicationRequest findMedicationRequestById(Long id);
	
	List<MedicationRequest> findMedicationRequestByParamMap(Map<String, String> paramMap);
	
}
