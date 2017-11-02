package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.ConditionDbEntity;
import com.cantatahealth.fhir.db.model.ConditionMapDbEntity;

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
	ConditionDbEntity findConditionById(Long id);

	/**
	 * search Condition resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<ConditionDbEntity> findConditionByParamMap(Map<String, String> paramMap);
	
}
