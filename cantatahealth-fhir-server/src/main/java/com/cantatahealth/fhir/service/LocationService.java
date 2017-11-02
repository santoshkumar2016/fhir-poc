package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Location;

/**
 * 
 * Location service interface
 * 
 * @author santosh
 *
 */
public interface LocationService extends FHIRService{
  
	Location findLocationById(Long id);
	
	List<Location> findLocationByParamMap(Map<String, String> paramMap);
	
}
