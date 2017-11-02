package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Observation data to Observation resource
 * 
 * @author santosh
 *
 */
@Component
public class ObservationResourceMapper {

	Logger logger = LoggerFactory.getLogger(ObservationResourceMapper.class);

	/**
	 * 
	 * Convert Observation db entity to Observation resource
	 * 
	 * @param patientMapDbEntity
	 * @param Observation
	 * @return
	 */
	public Observation dbModelToResource(DummyDbEntity patientMapDbEntity, Observation Observation) {

		return Observation;
	}

	/**
	 * 
	 * Convert list of Observation db entities to list of Observation resources
	 * 
	 * @param ObservationDbEntityList
	 * @return
	 */
	public List<Observation> dbModelToResource(List<DummyDbEntity> ObservationDbEntityList) {

		Map<String, Observation> ObservationMap = new HashMap<>();

		List<Observation> ObservationList = new ArrayList<Observation>(ObservationMap.values());

		return ObservationList;
	}

}
