package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Goal resource
 * 
 * @author santosh
 *
 */
public interface GoalDAO {

	/**
	 * fetch Goal resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findGoalById(Long id);

	/**
	 * search Goal resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findGoalByParamMap(Map<String, String> paramMap);
	
}
