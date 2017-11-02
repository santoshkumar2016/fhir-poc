package com.cantatahealth.fhir.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Patient.ContactComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.PatientDAO;
import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.IdentifierDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.model.ResourceDbEntity;
import com.cantatahealth.fhir.service.PatientService;
import com.cantatahealth.fhir.service.util.PatientResourceMapper;
import com.cantatahealth.fhir.util.AppUtil;

/**
 * 
 * Implementation class for patient services
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class PatientServiceImpl implements PatientService {

	@Autowired
	PatientDAO patientDAO;

	@Autowired
	PatientResourceMapper patientServiceUtil;

	public List<ResourceDbEntity> getChildResources() {
		return null;

	}

	public List<Patient> findPatientByParamMap(Map<String, String> paramMap) {

		List<PatientMapDbEntity> patMapDbEntityList = patientDAO.findPatientByParamMap(paramMap);

		List<Patient> patientList = patientServiceUtil.dbModelToResource(patMapDbEntityList);

		return patientList;
	}
	/**
	 * 
	 */
	@Override
	public Patient findPatientById(Long id) {

		Patient patient = new Patient();

		PatientDbEntity patientDbEntity = patientDAO.findPatientById(id);

		if (patientDbEntity == null)
			return patient;

		Set<PatientMapDbEntity> patMapDbMapEntset = patientDbEntity.getPatientMapDbEntities();

		List<PatientMapDbEntity> patList = new ArrayList<>(patMapDbMapEntset);
		Extension race = new Extension();
		Extension ethnicity = new Extension();
		Iterator<PatientMapDbEntity> patMapDbSetItr = patMapDbMapEntset.iterator();
		while (patMapDbSetItr.hasNext()) {
			PatientMapDbEntity patientMapDbEntity = patMapDbSetItr.next();

			patient = patientServiceUtil.dbModelToResource(patientMapDbEntity, patient,race,ethnicity);

		}
		if(patient != null){
			patient.addExtension(race);
			patient.addExtension(ethnicity);
		}
		return patient;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findPatientById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findPatientByParamMap(paramMap);
	}
}
