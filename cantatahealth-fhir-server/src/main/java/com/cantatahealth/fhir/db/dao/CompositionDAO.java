package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Composition resource
 * 
 * @author santosh
 *
 */
public interface CompositionDAO {

	/**
	 * fetch Composition resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findCompositionById(Long id);

	/**
	 * search Composition resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findCompositionByParamMap(Map<String, String> paramMap);
	
}
