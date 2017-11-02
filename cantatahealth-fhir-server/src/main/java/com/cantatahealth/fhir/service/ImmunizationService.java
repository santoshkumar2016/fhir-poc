package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Immunization;
/**
 * 
 * Immunization service interface
 * 
 * @author santosh
 *
 */
public interface ImmunizationService extends FHIRService{
  
	Immunization findImmunizationById(Long id);
	
	List<Immunization> findImmunizationByParamMap(Map<String, String> paramMap);
	
}
