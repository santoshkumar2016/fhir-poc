package com.nalashaa.fhir.server.acl.dao;

import java.util.List;
import java.util.Map;

import com.nalashaa.fhir.server.acl.db.model.PatientEn;

public interface PatientDAO {

	Integer savePatient(PatientEn patient);

	List<PatientEn> findAllPatients();

	void deletePatientById(Integer id);

	PatientEn findPatientById(int id);

	void updatePatient(PatientEn employee);

	List<PatientEn> findPatientByParamMap(Map<String, String> paramMap);
}