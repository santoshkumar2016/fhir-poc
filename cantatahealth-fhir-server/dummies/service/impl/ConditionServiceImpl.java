package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.ConditionDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.ConditionService;
import com.cantatahealth.fhir.service.util.ConditionResourceMapper;

/**
 * 
 * Implementation class for Condition service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class ConditionServiceImpl implements ConditionService {

	@Autowired
	ConditionDAO ConditionDAO;

	@Autowired
	ConditionResourceMapper ConditionMapper;

	@Override
	public Condition findConditionById(Long id) {

		Condition Condition = new Condition();

		DummyDbEntity ConditionDbEntity = ConditionDAO.findConditionById(id);

		if (ConditionDbEntity == null)
			return Condition;

		Condition = ConditionMapper.dbModelToResource(ConditionDbEntity, Condition);

		return Condition;

	}

	public List<Condition> findConditionByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = ConditionDAO.findConditionByParamMap(paramMap);

		List<Condition> ConditionList = ConditionMapper.dbModelToResource(patMapDbEntityList);

		return ConditionList;
	}

}
