package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.LocationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.LocationService;
import com.cantatahealth.fhir.service.util.LocationResourceMapper;

/**
 * 
 * Implementation class for Location service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

	@Autowired
	LocationDAO LocationDAO;

	@Autowired
	LocationResourceMapper LocationMapper;

	@Override
	public Location findLocationById(Long id) {

		Location Location = new Location();

		DummyDbEntity LocationDbEntity = LocationDAO.findLocationById(id);

		if (LocationDbEntity == null)
			return Location;

		Location = LocationMapper.dbModelToResource(LocationDbEntity, Location);

		return Location;

	}

	public List<Location> findLocationByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = LocationDAO.findLocationByParamMap(paramMap);

		List<Location> LocationList = LocationMapper.dbModelToResource(patMapDbEntityList);

		return LocationList;
	}

}
