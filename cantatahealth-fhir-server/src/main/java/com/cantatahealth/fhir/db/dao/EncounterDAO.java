package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.EncounterDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureDbEntity;

/**
 * DAO class for Procedure resource
 * 
 * @author santosh
 *
 */
public interface EncounterDAO {

	/**
	 * fetch Procedure resource by id
	 *
	 * @param id
	 * @return
	 */
	EncounterDbEntity findEncounterById(Long id);

	/**
	 * search Procedure resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<EncounterDbEntity> findEncounterByParamMap(Map<String, String> paramMap);
	
}
