package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.MedicationRequestDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.MedicationRequestService;
import com.cantatahealth.fhir.service.util.MedicationRequestResourceMapper;

/**
 * 
 * Implementation class for MedicationRequest service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class MedicationRequestServiceImpl implements MedicationRequestService {

	@Autowired
	MedicationRequestDAO MedicationRequestDAO;

	@Autowired
	MedicationRequestResourceMapper MedicationRequestMapper;

	@Override
	public MedicationRequest findMedicationRequestById(Long id) {

		MedicationRequest MedicationRequest = new MedicationRequest();

		DummyDbEntity MedicationRequestDbEntity = MedicationRequestDAO.findMedicationRequestById(id);

		if (MedicationRequestDbEntity == null)
			return MedicationRequest;

		MedicationRequest = MedicationRequestMapper.dbModelToResource(MedicationRequestDbEntity, MedicationRequest);

		return MedicationRequest;

	}

	public List<MedicationRequest> findMedicationRequestByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = MedicationRequestDAO.findMedicationRequestByParamMap(paramMap);

		List<MedicationRequest> MedicationRequestList = MedicationRequestMapper.dbModelToResource(patMapDbEntityList);

		return MedicationRequestList;
	}

}
