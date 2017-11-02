package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.MedicationStatement;

/**
 * 
 * MedicationStatementStatement service interface
 * 
 * @author santosh
 *
 */
public interface MedicationStatementService {
  
	MedicationStatement findMedicationStatementById(Long id);
	
	List<MedicationStatement> findMedicationStatementByParamMap(Map<String, String> paramMap);
	
}
