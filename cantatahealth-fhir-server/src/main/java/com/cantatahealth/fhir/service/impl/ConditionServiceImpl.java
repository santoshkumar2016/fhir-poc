package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.ConditionDAO;
import com.cantatahealth.fhir.db.model.ConditionDbEntity;
import com.cantatahealth.fhir.db.model.ConditionMapDbEntity;
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
	ConditionDAO conditionDAO;

	@Autowired
	ConditionResourceMapper conditionMapper;

	@Override
	public Condition findConditionById(Long id) {

		Condition condition = new Condition();

		ConditionDbEntity conditionDbEntity = conditionDAO.findConditionById(id);

		if (conditionDbEntity == null)
			return condition;

		condition = conditionMapper.dbModelToResource(conditionDbEntity, condition);

		return condition;

	}

	public List<Condition> findConditionByParamMap(Map<String, String> paramMap) {

		List<ConditionDbEntity> conditionMapDbEntityList = conditionDAO.findConditionByParamMap(paramMap);

		List<Condition> ConditionList = conditionMapper.dbModelToResource(conditionMapDbEntityList);

		return ConditionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findConditionById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findConditionByParamMap(paramMap);
	}
}
