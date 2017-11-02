package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.GoalDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.GoalService;
import com.cantatahealth.fhir.service.util.GoalResourceMapper;

/**
 * 
 * Implementation class for Goal service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class GoalServiceImpl implements GoalService {

	@Autowired
	GoalDAO GoalDAO;

	@Autowired
	GoalResourceMapper GoalMapper;

	@Override
	public Goal findGoalById(Long id) {

		Goal Goal = new Goal();

		DummyDbEntity GoalDbEntity = GoalDAO.findGoalById(id);

		if (GoalDbEntity == null)
			return Goal;

		Goal = GoalMapper.dbModelToResource(GoalDbEntity, Goal);

		return Goal;

	}

	public List<Goal> findGoalByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = GoalDAO.findGoalByParamMap(paramMap);

		List<Goal> GoalList = GoalMapper.dbModelToResource(patMapDbEntityList);

		return GoalList;
	}

}
