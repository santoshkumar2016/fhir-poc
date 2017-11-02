package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Procedure;

/**
 * 
 * Procedure service interface
 * 
 * @author santosh
 *
 */
public interface ProcedureService extends FHIRService{
  
	Procedure findProcedureById(Long id);
	
	List<Procedure> findProcedureByParamMap(Map<String, String> paramMap);
	
}
