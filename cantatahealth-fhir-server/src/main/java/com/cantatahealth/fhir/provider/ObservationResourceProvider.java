package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.service.ObservationService;

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
 * Provider for Observation resource
 * 
 * @author santosh
 *
 */
@Component
public class ObservationResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(ObservationResourceProvider.class);

	@Autowired
	private ObservationService ObservationService;

	/**
	 * Constructor
	 */
	public ObservationResourceProvider() {
		logger.info("************************* ObservationResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Observation by id
	 * 
	 * @param theId
	 *            of Observation to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Observation readObservation(@IdParam IdType theId) {

		logger.debug("Reading Observation resource with Id:{}", theId.getIdPartAsLong());

		Observation Observation = ObservationService.findObservationById(theId.getIdPartAsLong());

		if (Observation == null || Observation.isEmpty()) {
			throw new ResourceNotFoundException("Observation Resource not found : " + theId.getValueAsString());
		}
		
		return Observation;

	}
	
	@Search()
	public List<Observation> findObservationsBy(@OptionalParam(name = Observation.SP_PATIENT) StringDt patient,
			@OptionalParam(name = Observation.SP_CATEGORY) StringDt category,
			@OptionalParam(name = Observation.SP_DATE) DateRangeParam period){
		
		List<Observation> ObservationList = new ArrayList<Observation>();
		Map<String, String> paramMap = new ConcurrentHashMap<String,String>();
		
		if(period != null) { 
			 if(period.getLowerBound() != null) {
				 logger.debug("Search Parameter Patient:{}", period.getLowerBound().getValueAsString());
				 paramMap.put(Observation.SP_DATE+"_"+period.getLowerBound().getPrefix().name(), period.getLowerBound().getValueAsString());
			 }
			 if(period.getUpperBound() != null) {
				 logger.debug("Search Parameter Patient:{}", period.getUpperBound().getValueAsString());
				 paramMap.put(Observation.SP_DATE+"_"+period.getUpperBound().getPrefix().name(), period.getUpperBound().getValueAsString());
			 }
		 }
		
		if (patient != null) {
			logger.debug("Search Parameter Patient:{}", patient.getValueAsString());
			paramMap.put(Observation.SP_PATIENT, patient.getValue());
		}
		
		if (category != null) {
			logger.debug("Search Parameter Category:{}", category.getValueAsString());
			paramMap.put(Observation.SP_CATEGORY, category.getValue());
		}
		
		ObservationList = ObservationService.findObservationByParamMap(paramMap);
		
		return ObservationList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Observation> getResourceType() {
		return Observation.class;
	}

}
