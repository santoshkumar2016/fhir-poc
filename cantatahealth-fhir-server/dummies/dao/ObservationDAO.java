package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Observation resource
 * 
 * @author santosh
 *
 */
public interface ObservationDAO {

	/**
	 * fetch Observation resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findObservationById(Long id);

	/**
	 * search Observation resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findObservationByParamMap(Map<String, String> paramMap);
	
}
