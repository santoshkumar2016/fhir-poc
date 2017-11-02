package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Goal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Goal data to Goal resource
 * 
 * @author santosh
 *
 */
@Component
public class GoalResourceMapper {

	Logger logger = LoggerFactory.getLogger(GoalResourceMapper.class);

	/**
	 * 
	 * Convert Goal db entity to Goal resource
	 * 
	 * @param patientMapDbEntity
	 * @param Goal
	 * @return
	 */
	public Goal dbModelToResource(DummyDbEntity patientMapDbEntity, Goal Goal) {

		return Goal;
	}

	/**
	 * 
	 * Convert list of Goal db entities to list of Goal resources
	 * 
	 * @param GoalDbEntityList
	 * @return
	 */
	public List<Goal> dbModelToResource(List<DummyDbEntity> GoalDbEntityList) {

		Map<String, Goal> GoalMap = new HashMap<>();

		List<Goal> GoalList = new ArrayList<Goal>(GoalMap.values());

		return GoalList;
	}

}
