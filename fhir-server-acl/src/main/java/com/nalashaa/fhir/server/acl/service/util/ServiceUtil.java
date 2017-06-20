package com.nalashaa.fhir.server.acl.service.util;

import java.util.HashMap;
import java.util.Map;

public class ServiceUtil {
	
	
	public static Map<String, String> entityTableMap = new HashMap<String, String>();

	static {
		entityTableMap.put("PatientEn","contacts");
		entityTableMap.put("ImmunizationEn", "immunization");	
		
	}

}
