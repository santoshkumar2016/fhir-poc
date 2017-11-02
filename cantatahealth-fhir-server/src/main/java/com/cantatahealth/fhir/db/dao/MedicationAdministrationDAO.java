package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Medication resource
 * 
 * @author santosh
 *
 */
public interface MedicationAdministrationDAO {

	/**
	 * fetch MedicationAdministration resource by id
	 *
	 * @param id
	 * @return
	 */
	MedicationAdministrationDbEntity findMedicationAdministrationById(Long id);

	/**
	 * search medication resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<MedicationAdministrationDbEntity> findMedicationAdministrationByParamMap(Map<String, String> paramMap);
	
}
