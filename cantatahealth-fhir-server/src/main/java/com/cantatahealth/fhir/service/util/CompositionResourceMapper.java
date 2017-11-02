package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Composition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Composition data to Composition resource
 * 
 * @author santosh
 *
 */
@Component
public class CompositionResourceMapper {

	Logger logger = LoggerFactory.getLogger(CompositionResourceMapper.class);

	/**
	 * 
	 * Convert Composition db entity to Composition resource
	 * 
	 * @param patientMapDbEntity
	 * @param Composition
	 * @return
	 */
	public Composition dbModelToResource(DummyDbEntity patientMapDbEntity, Composition Composition) {

		return Composition;
	}

	/**
	 * 
	 * Convert list of Composition db entities to list of Composition resources
	 * 
	 * @param CompositionDbEntityList
	 * @return
	 */
	public List<Composition> dbModelToResource(List<DummyDbEntity> CompositionDbEntityList) {

		Map<String, Composition> CompositionMap = new HashMap<>();

		List<Composition> CompositionList = new ArrayList<Composition>(CompositionMap.values());

		return CompositionList;
	}

}
