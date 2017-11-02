/**
 * 
 */
package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.exceptions.FHIRException;

/**
 * @author somadutta
 *
 */
public interface AllergyIntoleranceService extends FHIRService{

	AllergyIntolerance findAllergyIntoleranceById(Long id);
	
	List<AllergyIntolerance> findAllergyIntoleranceByParamMap(Map<String, String> paramMap);
	
}
