package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * Service class for patient resource
 * 
 * @author santosh
 *
 */
public interface PatientService extends FHIRService{
  
	Patient findPatientById(Long id);
	
	List<Patient> findPatientByParamMap(Map<String, String> paramMap);
	
}
