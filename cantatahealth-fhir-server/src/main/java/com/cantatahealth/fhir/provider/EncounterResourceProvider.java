package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.db.model.EncounterDbEntity;
import com.cantatahealth.fhir.service.EncounterService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Encounter resource
 * 
 * @author santosh
 *
 */
public class EncounterResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(EncounterResourceProvider.class);

	@Autowired
	private EncounterService encounterService;

	/**
	 * Constructor
	 */
	public EncounterResourceProvider() {
		logger.info("************************* EncounterResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Procedure by id
	 * 
	 * @param theId
	 *            of Procedure to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Encounter readEncounter(@IdParam IdType theId) {

		logger.debug("Reading Procedure resource with Id:{}", theId.getIdPartAsLong());

		Encounter encounter = encounterService.findEncounterById(theId.getIdPartAsLong());

		if (encounter == null || encounter.isEmpty()) {
			throw new ResourceNotFoundException("Procedure Resource not found : " + theId.getValueAsString());
		}
		
		return encounter;

	}
	
	@Search()
	public List<Encounter> findEncountersBy(@OptionalParam(name = Encounter.SP_PATIENT) StringDt patient,
			@OptionalParam(name = Encounter.SP_DATE) DateRangeParam date){
		
		List<Encounter> encounterList = new ArrayList<Encounter>();
		List<EncounterDbEntity> PatientDbEntityListAll = new LinkedList<EncounterDbEntity>();
		Map<String, String> paramMap = new ConcurrentHashMap<String,String>();
		
		if (date != null) {
			if (date.getLowerBound() != null)
				paramMap.put(Encounter.SP_DATE + "_" + date.getLowerBound().getPrefix().name(),
						date.getLowerBound().getValueAsString());
			if (date.getUpperBound() != null)
				paramMap.put(Encounter.SP_DATE + "_" + date.getUpperBound().getPrefix().name(),
						date.getUpperBound().getValueAsString());
		}
		
		if (patient != null) {
			logger.debug("Search Parameter Patient:{}", patient.getValueAsString());
			paramMap.put(Encounter.SP_PATIENT, patient.getValue());
		}
		
		encounterList = encounterService.findEncounterByParamMap(paramMap);

		logger.debug("Found {} Patient resource.", PatientDbEntityListAll.size());
		return encounterList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Encounter> getResourceType() {
		return Encounter.class;
	}

}
