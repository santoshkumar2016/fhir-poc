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
	 * fetch medication resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findMedicationById(Long id);

	/**
	 * search medication resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findMedicationByParamMap(Map<String, String> paramMap);
	
}
