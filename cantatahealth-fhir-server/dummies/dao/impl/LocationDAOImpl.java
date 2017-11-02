/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.LocationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class LocationDAOImpl implements LocationDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.LocationDAO#findLocationById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findLocationById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.LocationDAO#findPLocationByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findLocationByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
