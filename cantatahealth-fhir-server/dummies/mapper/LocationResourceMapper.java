package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Location data to Location resource
 * 
 * @author santosh
 *
 */
@Component
public class LocationResourceMapper {

	Logger logger = LoggerFactory.getLogger(LocationResourceMapper.class);

	/**
	 * 
	 * Convert Location db entity to Location resource
	 * 
	 * @param patientMapDbEntity
	 * @param Location
	 * @return
	 */
	public Location dbModelToResource(DummyDbEntity patientMapDbEntity, Location Location) {

		return Location;
	}

	/**
	 * 
	 * Convert list of Location db entities to list of Location resources
	 * 
	 * @param LocationDbEntityList
	 * @return
	 */
	public List<Location> dbModelToResource(List<DummyDbEntity> LocationDbEntityList) {

		Map<String, Location> LocationMap = new HashMap<>();

		List<Location> LocationList = new ArrayList<Location>(LocationMap.values());

		return LocationList;
	}

}
