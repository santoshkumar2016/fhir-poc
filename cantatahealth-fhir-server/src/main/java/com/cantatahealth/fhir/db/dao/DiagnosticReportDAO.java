package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for DiagnosticReport resource
 * 
 * @author santosh
 *
 */
public interface DiagnosticReportDAO {

	/**
	 * fetch DiagnosticReport resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findDiagnosticReportById(Long id);

	/**
	 * search DiagnosticReport resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findDiagnosticReportByParamMap(Map<String, String> paramMap);
	
}
