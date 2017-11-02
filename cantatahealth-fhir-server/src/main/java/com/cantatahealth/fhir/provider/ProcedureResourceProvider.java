package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.GoalDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureDbEntity;
import com.cantatahealth.fhir.service.ProcedureService;

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
 * Provider for Procedure resource
 * 
 * @author santosh
 *
 */
@Component
public class ProcedureResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(ProcedureResourceProvider.class);

	@Autowired
	private ProcedureService ProcedureService;

	/**
	 * Constructor
	 */
	public ProcedureResourceProvider() {
		logger.info("************************* ProcedureResourceProvider Instantiated *************************");
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
	public Procedure readProcedure(@IdParam IdType theId) {

		logger.debug("Reading Procedure resource with Id:{}", theId.getIdPartAsLong());

		Procedure procedure = ProcedureService.findProcedureById(theId.getIdPartAsLong());

		if (procedure == null || procedure.isEmpty()) {
			throw new ResourceNotFoundException("Procedure Resource not found : " + theId.getValueAsString());
		}
		
		return procedure;

	}
	
	@Search()
	public List<Procedure> findProceduresBy(@OptionalParam(name = Procedure.SP_PATIENT) StringDt patient,
			@OptionalParam(name = Procedure.SP_CONTEXT) StringDt context,
			@OptionalParam(name = Procedure.SP_DATE) DateRangeParam date){
		
		List<Procedure> ProcedureList = new ArrayList<Procedure>();
		List<ProcedureDbEntity> PatientDbEntityListAll = new LinkedList<ProcedureDbEntity>();
		Map<String, String> paramMap = new ConcurrentHashMap<String,String>();
		
		if (date != null) {
			if (date.getLowerBound() != null)
				paramMap.put(Procedure.SP_DATE + "_" + date.getLowerBound().getPrefix().name(),
						date.getLowerBound().getValueAsString());
			if (date.getUpperBound() != null)
				paramMap.put(Procedure.SP_DATE + "_" + date.getUpperBound().getPrefix().name(),
						date.getUpperBound().getValueAsString());
		}
		
		if (patient != null) {
			logger.debug("Search Parameter Patient:{}", patient.getValueAsString());
			paramMap.put(Procedure.SP_PATIENT, patient.getValue());
		}
		
		if(context != null)
			paramMap.put(Procedure.SP_CONTEXT,context.getValue());
		
		ProcedureList = ProcedureService.findProcedureByParamMap(paramMap);

		logger.debug("Found {} Patient resource.", PatientDbEntityListAll.size());
		return ProcedureList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Procedure> getResourceType() {
		return Procedure.class;
	}

}
