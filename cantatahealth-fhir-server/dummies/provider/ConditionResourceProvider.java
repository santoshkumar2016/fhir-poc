package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.ConditionService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Condition resource
 * 
 * @author santosh
 *
 */
public class ConditionResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(ConditionResourceProvider.class);

	@Autowired
	private ConditionService ConditionService;

	/**
	 * Constructor
	 */
	public ConditionResourceProvider() {
		logger.info("************************* ConditionResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Condition by id
	 * 
	 * @param theId
	 *            of Condition to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Condition readCondition(@IdParam IdType theId) {

		logger.debug("Reading Condition resource with Id:{}", theId.getIdPartAsLong());

		Condition Condition = ConditionService.findConditionById(theId.getIdPartAsLong());

		if (Condition == null || Condition.isEmpty()) {
			throw new ResourceNotFoundException("Condition Resource not found : " + theId.getValueAsString());
		}
		
		return Condition;

	}
	
	@Search()
	public List<Condition> findConditionsBy(@OptionalParam(name = Condition.SP_CODE) StringDt activ){
		
		List<Condition> ConditionList = new ArrayList<Condition>();
		
		return ConditionList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Condition> getResourceType() {
		return Condition.class;
	}

}
