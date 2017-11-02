package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.model.ResourceDbEntity;

public interface PatientDAO {

	PatientDbEntity findPatientById(Long id);

	List<PatientMapDbEntity> findPatientByParamMap(Map<String, String> paramMap);
	
	ResourceDbEntity getPatientResourceEntityByName(String resourceName);
	
	List<Object[]> getChildResourcesByResourceKey(Long resourceKey);

	List<Object[]> getChildResources(String sqlQuery);
	
	List<Object[]> getPatient(String query);
	
	public List<HumanNameDbEntity> getHumanNames(Long resKey, Long id);
	
	public List<AddressDbEntity> getAddress(Long resKey, Long id);
}