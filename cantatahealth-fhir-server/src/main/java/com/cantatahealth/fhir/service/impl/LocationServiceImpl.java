package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.LocationDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.GoalMapDbEntity;
import com.cantatahealth.fhir.db.model.LocationDbEntity;
import com.cantatahealth.fhir.db.model.LocationMapDbEntity;
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

		Location location = new Location();

		LocationDbEntity locationDbEntity = LocationDAO.findLocationById(id);

		if (locationDbEntity == null)
			return location;
		else{
			location = LocationMapper.getLocationBaseComponents(location,locationDbEntity);
		}
		
		Set<LocationMapDbEntity> locationMapDbEntset = locationDbEntity.getLocationMapDbEntities();
		Iterator<LocationMapDbEntity> locationMapDbSetItr = locationMapDbEntset.iterator();
		while (locationMapDbSetItr.hasNext()) {
			LocationMapDbEntity locationMapDbEntity = locationMapDbSetItr.next();

			location = LocationMapper.dbModelToResource(locationMapDbEntity,location);
		}


		/*Location = LocationMapper.dbModelToResource(locationDbEntity, Location);*/

		return location;

	}

	public List<Location> findLocationByParamMap(Map<String, String> paramMap) {

		List<LocationDbEntity> locMapDbEntityList = LocationDAO.findLocationByParamMap(paramMap);
		
		List<Location> locationList = null;
		
		if(!locMapDbEntityList.isEmpty()) {
		Iterator<LocationDbEntity> ctMapDbSetItr = locMapDbEntityList.iterator();
		while(ctMapDbSetItr.hasNext())
			if(locationList == null)
				locationList= LocationMapper.dbModelToResource(ctMapDbSetItr.next().getLocationMapDbEntities());
			else
				locationList.addAll(LocationMapper.dbModelToResource(ctMapDbSetItr.next().getLocationMapDbEntities()));
		}

		return locationList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findLocationById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findLocationByParamMap(paramMap);
	}

}
