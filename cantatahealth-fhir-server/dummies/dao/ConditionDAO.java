package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Condition resource
 * 
 * @author santosh
 *
 */
public interface ConditionDAO {

	/**
	 * fetch Condition resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findConditionById(Long id);

	/**
	 * search Condition resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findConditionByParamMap(Map<String, String> paramMap);
	
}
