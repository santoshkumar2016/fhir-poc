package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Goal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.GoalService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Goal resource
 * 
 * @author santosh
 *
 */
public class GoalResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(GoalResourceProvider.class);

	@Autowired
	private GoalService GoalService;

	/**
	 * Constructor
	 */
	public GoalResourceProvider() {
		logger.info("************************* GoalResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Goal by id
	 * 
	 * @param theId
	 *            of Goal to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Goal readGoal(@IdParam IdType theId) {

		logger.debug("Reading Goal resource with Id:{}", theId.getIdPartAsLong());

		Goal Goal = GoalService.findGoalById(theId.getIdPartAsLong());

		if (Goal == null || Goal.isEmpty()) {
			throw new ResourceNotFoundException("Goal Resource not found : " + theId.getValueAsString());
		}
		
		return Goal;

	}
	
	@Search()
	public List<Goal> findGoalsBy(@OptionalParam(name = Goal.SP_CODE) StringDt activ){
		
		List<Goal> GoalList = new ArrayList<Goal>();
		
		return GoalList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Goal> getResourceType() {
		return Goal.class;
	}

}
