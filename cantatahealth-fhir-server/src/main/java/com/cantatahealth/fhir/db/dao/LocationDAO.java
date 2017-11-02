package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.LocationDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Location resource
 * 
 * @author santosh
 *
 */
public interface LocationDAO {

	/**
	 * fetch Location resource by id
	 *
	 * @param id
	 * @return
	 */
	LocationDbEntity findLocationById(Long id);

	/**
	 * search Location resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<LocationDbEntity> findLocationByParamMap(Map<String, String> paramMap);
	
}
