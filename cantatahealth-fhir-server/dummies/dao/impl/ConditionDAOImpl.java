/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.ConditionDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class ConditionDAOImpl implements ConditionDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ConditionDAO#findConditionById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findConditionById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ConditionDAO#findPConditionByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findConditionByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
