/**
 * 
 */
package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Patient;

import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.model.ResourceDbEntity;

/**
 * @author somadutta
 *
 */
public interface AllergyIntoleranceDAO {
	
	/**
	 * 
	 * @param id
	 * @return fetch AllergyIntolerance resource by id
	 */
	AllergyIntoleranceDbEntity findAllergyIntoleranceById(Long id);
	
	/**
	 * 
	 * @param resourceName
	 * @return fetch AllergyIntolerance resource by name
	 */
	ResourceDbEntity getAllergyIntoleranceResourceEntityByName(String resourceName);
	
	/**
	 * 
	 * @param paramMap
	 * @return fetch AllergyIntolerance resource by param values
	 */
	List<AllergyIntoleranceMapDbEntity> findAllergyIntoleranceByParamMap(Map<String, String> paramMap);
}
