package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.GoalDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.GoalDbEntity;
import com.cantatahealth.fhir.db.model.GoalMapDbEntity;
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

		Goal goal = new Goal();

		GoalDbEntity GoalDbEntity = GoalDAO.findGoalById(id);

		if (GoalDbEntity == null)
			return goal;		
		else{
			goal = GoalMapper.getGoalBaseComponents(goal,GoalDbEntity);
		}
		
		Set<GoalMapDbEntity> gMapDbMapEntset = GoalDbEntity.getGoalMapDbEntities();
		Iterator<GoalMapDbEntity> gMapDbSetItr = gMapDbMapEntset.iterator();
		while (gMapDbSetItr.hasNext()) {
			GoalMapDbEntity goalMapDbEntity = gMapDbSetItr.next();

			goal = GoalMapper.dbModelToResource(goalMapDbEntity, goal);
		}

		return goal;

	}

	public List<Goal> findGoalByParamMap(Map<String, String> paramMap) {

		List<GoalDbEntity> goalMapDbEntityList = GoalDAO.findGoalByParamMap(paramMap);

		/*List<CareTeam> CareTeamList = CareTeamMapper.dbModelToResource(patMapDbEntityList);*/

		List<Goal> goalList = null;
		
		if(!goalMapDbEntityList.isEmpty()) {
		Iterator<GoalDbEntity> ctMapDbSetItr = goalMapDbEntityList.iterator();
		while(ctMapDbSetItr.hasNext())
			if(goalList == null)
				goalList= GoalMapper.dbModelToResource(ctMapDbSetItr.next().getGoalMapDbEntities());
			else
				goalList.addAll(GoalMapper.dbModelToResource(ctMapDbSetItr.next().getGoalMapDbEntities()));
		}
		return goalList;
		
		/*List<Goal> GoalList = GoalMapper.dbModelToResource(patMapDbEntityList);*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findGoalById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findGoalByParamMap(paramMap);
	}
}
