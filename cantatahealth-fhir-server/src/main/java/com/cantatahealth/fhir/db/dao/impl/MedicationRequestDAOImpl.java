/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.MedicationRequestDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class MedicationRequestDAOImpl implements MedicationRequestDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.MedicationAdministrationDAO#findMedicationById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findMedicationRequestById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.MedicationAdministrationDAO#findPMedicationByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findMedicationRequestByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
