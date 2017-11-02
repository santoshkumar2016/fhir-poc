/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.CareTeamDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class CareTeamDAOImpl implements CareTeamDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.CareTeamDAO#findCareTeamById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findCareTeamById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.CareTeamDAO#findPCareTeamByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findCareTeamByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
