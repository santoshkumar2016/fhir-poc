package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.MedicationStatementDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.MedicationStatementService;
import com.cantatahealth.fhir.service.util.MedicationStatementResourceMapper;

/**
 * 
 * Implementation class for MedicationStatement service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class MedicationStatementServiceImpl implements MedicationStatementService {

	@Autowired
	MedicationStatementDAO MedicationStatementDAO;

	@Autowired
	MedicationStatementResourceMapper MedicationStatementMapper;

	@Override
	public MedicationStatement findMedicationStatementById(Long id) {

		MedicationStatement MedicationStatement = new MedicationStatement();

		DummyDbEntity MedicationStatementDbEntity = MedicationStatementDAO.findMedicationStatementById(id);

		if (MedicationStatementDbEntity == null)
			return MedicationStatement;

		MedicationStatement = MedicationStatementMapper.dbModelToResource(MedicationStatementDbEntity, MedicationStatement);

		return MedicationStatement;

	}

	public List<MedicationStatement> findMedicationStatementByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = MedicationStatementDAO.findMedicationStatementByParamMap(paramMap);

		List<MedicationStatement> MedicationStatementList = MedicationStatementMapper.dbModelToResource(patMapDbEntityList);

		return MedicationStatementList;
	}

}
