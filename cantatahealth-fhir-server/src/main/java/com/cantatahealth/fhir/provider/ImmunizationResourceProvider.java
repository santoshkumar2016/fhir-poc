package com.cantatahealth.fhir.provider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.ImmunizationDbEntity;
import com.cantatahealth.fhir.service.ImmunizationService;

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
 * Provider for Immunnization resource
 * 
 * @author santosh
 *
 */
public class ImmunizationResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(ImmunizationResourceProvider.class);

	@Autowired
	private ImmunizationService ImmunizationService;

	/**
	 * Constructor
	 */
	public ImmunizationResourceProvider() {
		logger.info("************************* ImmunizationResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Immunnization by id
	 * 
	 * @param theId
	 *            of Immunnization to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Immunization readImmunnization(@IdParam IdType theId) {

		logger.debug("Reading Immunnization resource with Id:{}", theId.getIdPartAsLong());

		Immunization Immunnization = ImmunizationService.findImmunizationById(theId.getIdPartAsLong());

		if (Immunnization == null || Immunnization.isEmpty()) {
			throw new ResourceNotFoundException("Immunnization Resource not found : " + theId.getValueAsString());
		}
		
		return Immunnization;

	}
	
	@Search()
	public List<Immunization> findImmunnizationsBy(@OptionalParam(name = Immunization.SP_PATIENT) StringDt patient,
			@OptionalParam(name = Immunization.SP_DATE) DateRangeParam period){
		
		List<Immunization> ImmunizationList = new ArrayList<Immunization>();
		List<ImmunizationDbEntity> PatientDbEntityListAll = new LinkedList<ImmunizationDbEntity>();
		Map<String, String> paramMap = new ConcurrentHashMap<String,String>();
		
		if (period != null) {
			if (period.getLowerBound() != null)
				paramMap.put(Immunization.SP_DATE + "_" + period.getLowerBound().getPrefix().name(),
						period.getLowerBound().getValueAsString());
			if (period.getUpperBound() != null)
				paramMap.put(Immunization.SP_DATE + "_" + period.getUpperBound().getPrefix().name(),
						period.getUpperBound().getValueAsString());
		}

		if (patient != null) {
			logger.debug("Search Parameter Patient:{}", patient.getValueAsString());
			paramMap.put(Immunization.SP_PATIENT, patient.getValue());
		}
		
		ImmunizationList = ImmunizationService.findImmunizationByParamMap(paramMap);

		logger.debug("Found {} Patient resource.", PatientDbEntityListAll.size());

		
		return ImmunizationList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Immunization> getResourceType() {
		return Immunization.class;
	}

}
