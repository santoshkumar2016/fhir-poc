/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.ProcedureDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class ProcedureDAOImpl implements ProcedureDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ProcedureDAO#findProcedureById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findProcedureById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ProcedureDAO#findPProcedureByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findProcedureByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
