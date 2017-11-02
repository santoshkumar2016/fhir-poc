package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Immunization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.ImmunizationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.ImmunizationService;
import com.cantatahealth.fhir.service.util.ImmunizationResourceMapper;

/**
 * 
 * Implementation class for Immunization service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class ImmunizationServiceImpl implements ImmunizationService {

	@Autowired
	ImmunizationDAO ImmunizationDAO;

	@Autowired
	ImmunizationResourceMapper ImmunizationMapper;

	@Override
	public Immunization findImmunizationById(Long id) {

		Immunization Immunization = new Immunization();

		DummyDbEntity ImmunizationDbEntity = ImmunizationDAO.findImmunizationById(id);

		if (ImmunizationDbEntity == null)
			return Immunization;

		Immunization = ImmunizationMapper.dbModelToResource(ImmunizationDbEntity, Immunization);

		return Immunization;

	}

	public List<Immunization> findImmunizationByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = ImmunizationDAO.findImmunizationByParamMap(paramMap);

		List<Immunization> ImmunizationList = ImmunizationMapper.dbModelToResource(patMapDbEntityList);

		return ImmunizationList;
	}

}
