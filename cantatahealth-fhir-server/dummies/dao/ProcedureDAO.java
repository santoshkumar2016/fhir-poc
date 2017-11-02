package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Procedure resource
 * 
 * @author santosh
 *
 */
public interface ProcedureDAO {

	/**
	 * fetch Procedure resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findProcedureById(Long id);

	/**
	 * search Procedure resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findProcedureByParamMap(Map<String, String> paramMap);
	
}
