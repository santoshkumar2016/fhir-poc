package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Procedure;

/**
 * 
 * Encounter service interface
 * 
 * @author santosh
 *
 */
public interface EncounterService extends FHIRService{
  
	Encounter findEncounterById(Long id);
	
	List<Encounter> findEncounterByParamMap(Map<String, String> paramMap);
	
}
