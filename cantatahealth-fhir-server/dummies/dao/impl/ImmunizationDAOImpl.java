/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.ImmunizationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class ImmunizationDAOImpl implements ImmunizationDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ImmunizationDAO#findImmunizationById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findImmunizationById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ImmunizationDAO#findPImmunizationByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findImmunizationByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
