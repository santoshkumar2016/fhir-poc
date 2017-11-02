/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.CompositionDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class CompositionDAOImpl implements CompositionDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.CompositionDAO#findCompositionById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findCompositionById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.CompositionDAO#findPCompositionByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findCompositionByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
