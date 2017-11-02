package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Medication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.MedicationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.MedicationService;
import com.cantatahealth.fhir.service.util.MedicationResourceMapper;

/**
 * 
 * Implementation class for medication service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

	@Autowired
	MedicationDAO medicationDAO;

	@Autowired
	MedicationResourceMapper medicationMapper;

	@Override
	public Medication findMedicationById(Long id) {

		Medication medication = new Medication();

		DummyDbEntity medicationDbEntity = medicationDAO.findMedicationById(id);

		if (medicationDbEntity == null)
			return medication;

		medication = medicationMapper.dbModelToResource(medicationDbEntity, medication);

		return medication;

	}

	public List<Medication> findMedicationByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = medicationDAO.findMedicationByParamMap(paramMap);

		List<Medication> medicationList = medicationMapper.dbModelToResource(patMapDbEntityList);

		return medicationList;
	}

}
