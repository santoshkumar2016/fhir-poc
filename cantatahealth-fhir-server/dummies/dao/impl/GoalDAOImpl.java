/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.GoalDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class GoalDAOImpl implements GoalDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.GoalDAO#findGoalById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findGoalById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.GoalDAO#findPGoalByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findGoalByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
