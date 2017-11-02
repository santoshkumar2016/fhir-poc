package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Composition;

/**
 * 
 * Composition service interface
 * 
 * @author santosh
 *
 */
public interface CompositionService {
  
	Composition findCompositionById(Long id);
	
	List<Composition> findCompositionByParamMap(Map<String, String> paramMap);
	
}
