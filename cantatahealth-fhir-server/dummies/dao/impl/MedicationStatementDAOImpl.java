/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.MedicationStatementDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class MedicationStatementDAOImpl implements MedicationStatementDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.MedicationDAO#findMedicationById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findMedicationStatementById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.MedicationDAO#findPMedicationByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findMedicationStatementByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
