package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.CareTeamService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
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
	private CareTeamService CareTeamService;

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

		CareTeam CareTeam = CareTeamService.findCareTeamById(theId.getIdPartAsLong());

		if (CareTeam == null || CareTeam.isEmpty()) {
			throw new ResourceNotFoundException("CareTeam Resource not found : " + theId.getValueAsString());
		}
		
		return CareTeam;

	}
	
	@Search()
	public List<CareTeam> findCareTeamsBy(@OptionalParam(name = CareTeam.SP_CODE) StringDt activ){
		
		List<CareTeam> CareTeamList = new ArrayList<CareTeam>();
		
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
