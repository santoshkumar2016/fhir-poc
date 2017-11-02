package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * Condition service interface
 * 
 * @author santosh
 *
 */
public interface ConditionService {
  
	Condition findConditionById(Long id);
	
	List<Condition> findConditionByParamMap(Map<String, String> paramMap);
	
}
