package com.nalashaa.fhir.server.acl.service.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nalashaa.fhir.server.acl.dao.PatientDAO;
import com.nalashaa.fhir.server.acl.db.model.PatientEn;
import com.nalashaa.fhir.server.acl.service.PatientService;
import com.nalashaa.fhir.server.acl.service.util.PatientServiceUtil;

import ca.uhn.fhir.model.dstu2.resource.Patient;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

	Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

	@Autowired
	PatientServiceUtil modelMapper;

	@Autowired
	private PatientDAO patientDAO;

	public Integer savePatient(Patient patient) {
		return patientDAO.savePatient(modelMapper.resourceToEntityConverter(patient));
	}

	public List<PatientEn> findAllPatients() {
		return patientDAO.findAllPatients();
	}

	public void deletePatientById(Integer id) {
		patientDAO.deletePatientById(id);
	}

	public Patient findPatientById(Integer id) {
		return modelMapper.entityToResourceConverter(patientDAO.findPatientById(id));
	}

	public void updatePatient(Patient patient) {
		patientDAO.updatePatient(modelMapper.resourceToEntityConverter(patient));
	}

	public List<Patient> findPatientByParamMap(Map<String, String> paramMap) {

		// Iterator<Entry<String, String>> iterator =
		// paramMap.entrySet().iterator();
		//
		// while (iterator.hasNext()) {
		//
		// Entry<String, String> paramValue = iterator.next();
		// String key = paramValue.getKey();
		// String value = paramValue.getValue();
		// switch (key) {
		// case Patient.SP_ACTIVE:
		// logger.debug("Search parameter active:{}", paramValue);
		// iterator.remove();
		// paramMap.put(PatientServiceUtil.patientFieldToColumnMap.get(Patient.SP_ACTIVE),
		// value);
		// break;
		//
		// case Patient.SP_FAMILY:
		// logger.debug("Search Patient family:{}", paramValue);
		// iterator.remove();
		// paramMap.put(PatientServiceUtil.patientFieldToColumnMap.get(Patient.SP_FAMILY),
		// value);
		// break;
		//
		// case Patient.SP_ADDRESS_CITY:
		// logger.debug("Search Patient addressCity:{}", paramValue);
		// iterator.remove();
		// paramMap.put(PatientServiceUtil.patientFieldToColumnMap.get(Patient.SP_ADDRESS_CITY),
		// value);
		// break;
		//
		// }
		// }

		List<Patient> patientList = new LinkedList<Patient>();

		List<PatientEn> patientEntityList = new LinkedList<PatientEn>();
		patientEntityList = patientDAO.findPatientByParamMap(modelMapper.getColumnMap(paramMap));

		Iterator<PatientEn> itr = patientEntityList.iterator();

		while (itr.hasNext()) {
			patientList.add(modelMapper.entityToResourceConverter(itr.next()));
		}

		return patientList;
	}
}
