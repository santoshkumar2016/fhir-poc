package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.ProcedureService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Procedure resource
 * 
 * @author santosh
 *
 */
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

		Procedure Procedure = ProcedureService.findProcedureById(theId.getIdPartAsLong());

		if (Procedure == null || Procedure.isEmpty()) {
			throw new ResourceNotFoundException("Procedure Resource not found : " + theId.getValueAsString());
		}
		
		return Procedure;

	}
	
	@Search()
	public List<Procedure> findProceduresBy(@OptionalParam(name = Procedure.SP_CODE) StringDt activ){
		
		List<Procedure> ProcedureList = new ArrayList<Procedure>();
		
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
