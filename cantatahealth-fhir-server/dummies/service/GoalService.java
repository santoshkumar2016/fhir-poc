package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * Goal service interface
 * 
 * @author santosh
 *
 */
public interface GoalService {
  
	Goal findGoalById(Long id);
	
	List<Goal> findGoalByParamMap(Map<String, String> paramMap);
	
}
