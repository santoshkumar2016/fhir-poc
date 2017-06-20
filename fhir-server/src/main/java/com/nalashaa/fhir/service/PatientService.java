package com.nalashaa.fhir.service;

import java.util.HashMap;
import java.util.List;

import com.nalashaa.fhir.db.model.PatientEn;

public interface PatientService {
 
    Integer savePatient(PatientEn patient);
    
    List<PatientEn> findAllPatients();
     
    void deletePatientById(Integer id);
     
    PatientEn findPatientById(Integer id);
    
    List<PatientEn> findPatientByName(String fname);
     
    void updatePatient(PatientEn patient);

	List<PatientEn> findPatientByAddressCity(String addressCity);
	
	List<PatientEn> findPatientByParamMap(HashMap<String, String> paramMap);
}
