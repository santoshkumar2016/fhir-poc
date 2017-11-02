package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Observation;

/**
 * 
 * Observation service interface
 * 
 * @author santosh
 *
 */
public interface ObservationService {
  
	Observation findObservationById(Long id);
	
	List<Observation> findObservationByParamMap(Map<String, String> paramMap);
	
}
