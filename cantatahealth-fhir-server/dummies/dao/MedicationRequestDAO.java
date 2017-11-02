package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Medication resource
 * 
 * @author santosh
 *
 */
public interface MedicationRequestDAO {

	/**
	 * fetch medicationrequest resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findMedicationRequestById(Long id);

	/**
	 * search medicationrequest resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findMedicationRequestByParamMap(Map<String, String> paramMap);
	
}
