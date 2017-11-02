package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.model.CCDAResource;

/**
 * 
 * CCDAResource service interface
 * 
 * @author santosh
 *
 */
public interface CCDAResourceService {
  
	CCDAResource findCCDAResourceById(Long id);
	
	List<CCDAResource> findCCDAResourceByParamMap(Map<String, String> paramMap);
	
}
