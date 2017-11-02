package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Condition data to Condition resource
 * 
 * @author santosh
 *
 */
@Component
public class ConditionResourceMapper {

	Logger logger = LoggerFactory.getLogger(ConditionResourceMapper.class);

	/**
	 * 
	 * Convert Condition db entity to Condition resource
	 * 
	 * @param patientMapDbEntity
	 * @param Condition
	 * @return
	 */
	public Condition dbModelToResource(DummyDbEntity patientMapDbEntity, Condition Condition) {

		return Condition;
	}

	/**
	 * 
	 * Convert list of Condition db entities to list of Condition resources
	 * 
	 * @param ConditionDbEntityList
	 * @return
	 */
	public List<Condition> dbModelToResource(List<DummyDbEntity> ConditionDbEntityList) {

		Map<String, Condition> ConditionMap = new HashMap<>();

		List<Condition> ConditionList = new ArrayList<Condition>(ConditionMap.values());

		return ConditionList;
	}

}
