package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * DiagnosticReport service interface
 * 
 * @author santosh
 *
 */
public interface DiagnosticReportService {
  
	DiagnosticReport findDiagnosticReportById(Long id);
	
	List<DiagnosticReport> findDiagnosticReportByParamMap(Map<String, String> paramMap);
	
}
