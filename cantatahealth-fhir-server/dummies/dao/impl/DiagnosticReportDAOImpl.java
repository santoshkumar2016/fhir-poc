/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.DiagnosticReportDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class DiagnosticReportDAOImpl implements DiagnosticReportDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.DiagnosticReportDAO#findDiagnosticReportById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findDiagnosticReportById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.DiagnosticReportDAO#findPDiagnosticReportByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findDiagnosticReportByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
