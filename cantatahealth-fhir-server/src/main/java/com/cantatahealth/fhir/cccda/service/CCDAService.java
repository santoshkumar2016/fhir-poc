/**
 * 
 */
package com.cantatahealth.fhir.cccda.service;

import java.util.List;
import java.util.Map;

/**
 * @author santosh
 *
 */
public interface CCDAService {
	
	public <T> List<T> getResourceByParamMap(String resourceName, Map<String, String> paramMap);
	
	public <T> T getResourceById(String resourceName, Long id);

}
