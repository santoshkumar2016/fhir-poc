package com.nalashaa.fhir.dao;

import java.util.HashMap;
import java.util.List;

import com.nalashaa.fhir.db.model.PatientEn;
 
public interface PatientDAO {
 
    Integer savePatient(PatientEn patient);
     
    List<PatientEn> findAllPatients();
     
    void deletePatientById(Integer id);
     
    PatientEn findPatientById(int id);
    
    List<PatientEn> findPatientByName(String fname);
     
    void updatePatient(PatientEn employee);

	List<PatientEn> findPatientByAddressCity(String addressCity);

	List<PatientEn> findPatientByParamMap(HashMap<String, String> paramMap);
}