package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * Device service interface
 * 
 * @author santosh
 *
 */
public interface DeviceService extends FHIRService{
  
	Device findDeviceById(Long id);
	
	List<Device> findDeviceByParamMap(Map<String, String> paramMap);
	
}
