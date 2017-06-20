package com.nalashaa.fhir.server.acl.service.util;

public enum EntityTableEnum {
	
	PATIENT("PatientEn","Contacts"),
	IMMUNIZATION("ImmunizationEn","Immunization");

	
	
	private final String entityName;
	private final String tableName;
	
	/** 
	 * Constructor
	 */
	EntityTableEnum(String entity, String table) {
		entityName = entity;
		tableName = table;
	}

}
