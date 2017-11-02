package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.MedicationStatementService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for MedicationStatement resource
 * 
 * @author santosh
 *
 */
public class MedicationStatementResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(MedicationStatementResourceProvider.class);

	@Autowired
	private MedicationStatementService MedicationStatementService;

	/**
	 * Constructor
	 */
	public MedicationStatementResourceProvider() {
		logger.info("************************* MedicationStatementResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read MedicationStatement by id
	 * 
	 * @param theId
	 *            of MedicationStatement to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public MedicationStatement readMedicationStatement(@IdParam IdType theId) {

		logger.debug("Reading MedicationStatement resource with Id:{}", theId.getIdPartAsLong());

		MedicationStatement MedicationStatement = MedicationStatementService.findMedicationStatementById(theId.getIdPartAsLong());

		if (MedicationStatement == null || MedicationStatement.isEmpty()) {
			throw new ResourceNotFoundException("MedicationStatement Resource not found : " + theId.getValueAsString());
		}
		
		return MedicationStatement;

	}
	
	@Search()
	public List<MedicationStatement> findMedicationStatementsBy(@OptionalParam(name = MedicationStatement.SP_CODE) StringDt activ){
		
		List<MedicationStatement> MedicationStatementList = new ArrayList<MedicationStatement>();
		
		return MedicationStatementList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<MedicationStatement> getResourceType() {
		return MedicationStatement.class;
	}

}
