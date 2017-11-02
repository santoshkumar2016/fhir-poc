package com.cantatahealth.fhir.provider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.service.CareTeamService;

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
 * Provider for CareTeam resource
 * 
 * @author santosh
 *
 */
public class CareTeamResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(CareTeamResourceProvider.class);

	@Autowired
	private CareTeamService careTeamService;

	/**
	 * Constructor
	 */
	public CareTeamResourceProvider() {
		logger.info("************************* CareTeamResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read CareTeam by id
	 * 
	 * @param theId
	 *            of CareTeam to read
	 * 
	 * @return
	 */
	@Read
	public CareTeam readCareTeam(@IdParam IdType theId) {

		logger.debug("Reading CareTeam resource with Id:{}", theId.getIdPartAsLong());

		CareTeam CareTeam = careTeamService.findCareTeamById(theId.getIdPartAsLong());

		if (CareTeam == null || CareTeam.isEmpty()) {
			throw new ResourceNotFoundException("CareTeam Resource not found : " + theId.getValueAsString());
		}
		
		return CareTeam;

	}
	
	@Search()
	public List<CareTeam> findCareTeamsBy(@OptionalParam(name = CareTeam.SP_PATIENT) StringDt patient,
			@OptionalParam(name = CareTeam.SP_DATE) DateRangeParam period){
		
		List<CareTeam> CareTeamList = new ArrayList<CareTeam>();
		List<CareTeamDbEntity> PatientDbEntityListAll = new LinkedList<CareTeamDbEntity>();
		Map<String, String> paramMap = new ConcurrentHashMap<String,String>();
		
		 if(period != null) { 
			 if(period.getLowerBound() != null) {
				 logger.debug("Search Parameter Patient:{}", period.getLowerBound().getValueAsString());
				 paramMap.put(AllergyIntolerance.SP_DATE+"_"+period.getLowerBound().getPrefix().name(), period.getLowerBound().getValueAsString());
			 }
			 if(period.getUpperBound() != null) {
				 logger.debug("Search Parameter Patient:{}", period.getUpperBound().getValueAsString());
				 paramMap.put(AllergyIntolerance.SP_DATE+"_"+period.getUpperBound().getPrefix().name(), period.getUpperBound().getValueAsString());
			 }
		 }

		if (patient != null) {
			logger.debug("Search Parameter Patient:{}", patient.getValueAsString());
			paramMap.put(CareTeam.SP_PATIENT, patient.getValue());
		}
		
		CareTeamList = careTeamService.findCareTeamByParamMap(paramMap);

		logger.debug("Found {} Patient resource.", PatientDbEntityListAll.size());
		
		return CareTeamList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<CareTeam> getResourceType() {
		return CareTeam.class;
	}

}
