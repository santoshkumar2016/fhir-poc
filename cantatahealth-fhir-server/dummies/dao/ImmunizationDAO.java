package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Immunization resource
 * 
 * @author santosh
 *
 */
public interface ImmunizationDAO {

	/**
	 * fetch Immunization resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findImmunizationById(Long id);

	/**
	 * search Immunization resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findImmunizationByParamMap(Map<String, String> paramMap);
	
}
