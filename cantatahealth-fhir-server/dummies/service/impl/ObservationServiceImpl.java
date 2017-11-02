package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.ObservationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.ObservationService;
import com.cantatahealth.fhir.service.util.ObservationResourceMapper;

/**
 * 
 * Implementation class for Observation service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class ObservationServiceImpl implements ObservationService {

	@Autowired
	ObservationDAO ObservationDAO;

	@Autowired
	ObservationResourceMapper ObservationMapper;

	@Override
	public Observation findObservationById(Long id) {

		Observation Observation = new Observation();

		DummyDbEntity ObservationDbEntity = ObservationDAO.findObservationById(id);

		if (ObservationDbEntity == null)
			return Observation;

		Observation = ObservationMapper.dbModelToResource(ObservationDbEntity, Observation);

		return Observation;

	}

	public List<Observation> findObservationByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = ObservationDAO.findObservationByParamMap(paramMap);

		List<Observation> ObservationList = ObservationMapper.dbModelToResource(patMapDbEntityList);

		return ObservationList;
	}

}
