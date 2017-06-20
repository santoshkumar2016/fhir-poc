package com.nalashaa.fhir.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nalashaa.fhir.dao.PatientDAO;
import com.nalashaa.fhir.dao.impl.PatientDAOImpl;
import com.nalashaa.fhir.db.model.PatientEn;
import com.nalashaa.fhir.service.PatientService;


@Service
@Transactional
public class PatientServiceImpl implements PatientService{
 
	
//	@Qualifier("patientDAOImpl")
	//@Autowired
    private PatientDAO patientDAOImpl = new PatientDAOImpl();
     
    public Integer savePatient(PatientEn patient) {
        return patientDAOImpl.savePatient(patient);
    }
 
    public List<PatientEn> findAllPatients() {
        return patientDAOImpl.findAllPatients();
    }
 
    public void deletePatientById(Integer id) {
        patientDAOImpl.deletePatientById(id);
    }
 
    public PatientEn findPatientById(Integer id) {
        return patientDAOImpl.findPatientById(id);
    }
 
    public void updatePatient(PatientEn patient){
        patientDAOImpl.updatePatient(patient);
    }


	public List<PatientEn> findPatientByName(String fname) {
		return patientDAOImpl.findPatientByName(fname);
	}

	@Override
	public List<PatientEn> findPatientByAddressCity(String addressCity) {
		return patientDAOImpl.findPatientByAddressCity(addressCity);
	}
	
	public List<PatientEn> findPatientByParamMap(HashMap<String, String> paramMap){
		return patientDAOImpl.findPatientByParamMap(paramMap);
	}
}
