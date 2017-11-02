package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Immunnization;
import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * Immunnization service interface
 * 
 * @author santosh
 *
 */
public interface ImmunnizationService {
  
	Immunnization findImmunnizationById(Long id);
	
	List<Immunnization> findImmunnizationByParamMap(Map<String, String> paramMap);
	
}
