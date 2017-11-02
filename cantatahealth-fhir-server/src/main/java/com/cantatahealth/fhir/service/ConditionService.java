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
public interface ConditionService extends FHIRService{
  
	/**
	 * Find condition resource by id
	 * 
	 * @param id
	 * @return
	 */
	Condition findConditionById(Long id);
	
	/**
	 * 
	 * Search condition resource by param
	 * 
	 * @param paramMap
	 * @return
	 */
	List<Condition> findConditionByParamMap(Map<String, String> paramMap);
	
}
