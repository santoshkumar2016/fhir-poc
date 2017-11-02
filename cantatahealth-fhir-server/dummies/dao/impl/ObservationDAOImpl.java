/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.ObservationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class ObservationDAOImpl implements ObservationDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ObservationDAO#findObservationById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findObservationById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ObservationDAO#findPObservationByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findObservationByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
