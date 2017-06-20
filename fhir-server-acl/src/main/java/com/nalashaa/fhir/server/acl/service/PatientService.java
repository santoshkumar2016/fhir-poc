package com.nalashaa.fhir.server.acl.service;

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public interface PatientService {

	Integer savePatient(Patient patient);

	void deletePatientById(Integer id);

	Patient findPatientById(Integer id);

	void updatePatient(Patient patient);

	List<Patient> findPatientByParamMap(Map<String, String> paramMap);
}
