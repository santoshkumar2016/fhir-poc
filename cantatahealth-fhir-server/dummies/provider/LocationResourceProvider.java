package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.LocationService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Location resource
 * 
 * @author santosh
 *
 */
public class LocationResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(LocationResourceProvider.class);

	@Autowired
	private LocationService LocationService;

	/**
	 * Constructor
	 */
	public LocationResourceProvider() {
		logger.info("************************* LocationResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Location by id
	 * 
	 * @param theId
	 *            of Location to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Location readLocation(@IdParam IdType theId) {

		logger.debug("Reading Location resource with Id:{}", theId.getIdPartAsLong());

		Location Location = LocationService.findLocationById(theId.getIdPartAsLong());

		if (Location == null || Location.isEmpty()) {
			throw new ResourceNotFoundException("Location Resource not found : " + theId.getValueAsString());
		}
		
		return Location;

	}
	
	@Search()
	public List<Location> findLocationsBy(@OptionalParam(name = Location.SP_CODE) StringDt activ){
		
		List<Location> LocationList = new ArrayList<Location>();
		
		return LocationList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Location> getResourceType() {
		return Location.class;
	}

}
