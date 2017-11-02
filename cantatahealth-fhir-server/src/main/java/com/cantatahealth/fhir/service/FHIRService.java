/**
 * 
 */
package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

/**
 * Base service for FHIR resource
 * 
 * @author santosh
 *
 */
public interface FHIRService {
	
	/**
	 * Fetch a fhir resource by id
	 * 
	 * @param id
	 * @return
	 */
	<T> T findResourceById(Long id);
	
	/**
	 * Fetch a list of fhir resources based on a set of search parameters
	 * 
	 * @param paramMap
	 * @return
	 */
	<T> List<T> findResourceByParamMap(Map<String, String> paramMap);

}
