package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.ConditionService;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

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
 * Provider for Condition resource
 * 
 * @author santosh
 *
 */
public class ConditionResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(ConditionResourceProvider.class);

	@Autowired
	private ConditionService conditionService;

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
	public Condition readCondition(@IdParam IdType theId) {

		logger.debug("Reading Condition resource with Id:{}", theId.getIdPartAsLong());

		Condition Condition = conditionService.findConditionById(theId.getIdPartAsLong());

		if (Condition == null || Condition.isEmpty()) {
			throw new ResourceNotFoundException("Condition Resource not found : " + theId.getValueAsString());
		}

		return Condition;

	}

	@Search()
	public List<Condition> findConditionsBy(
			@OptionalParam(name = Condition.SP_ABATEMENT_DATE) DateRangeParam abatementDateRange,
			@OptionalParam(name = Condition.SP_PATIENT) StringDt patient, 
			@OptionalParam(name = Condition.SP_ONSET_DATE) DateRangeParam onset) {

		List<Condition> conditionList = new ArrayList<Condition>();

		Map<String, String> paramMap = new ConcurrentHashMap<String, String>();

		if (abatementDateRange != null) {
			if (abatementDateRange.getLowerBound() != null)
				paramMap.put(Condition.SP_ABATEMENT_DATE + "_" + abatementDateRange.getLowerBound().getPrefix().name(),
						abatementDateRange.getLowerBound().getValueAsString());
			if (abatementDateRange.getUpperBound() != null)
				paramMap.put(Condition.SP_ABATEMENT_DATE + "_" + abatementDateRange.getUpperBound().getPrefix().name(),
						abatementDateRange.getUpperBound().getValueAsString());
		}
		
		if (onset != null) {
			if (onset.getLowerBound() != null)
				paramMap.put(Condition.SP_ONSET_DATE + "_" + onset.getLowerBound().getPrefix().name(),
						onset.getLowerBound().getValueAsString());
			if (onset.getUpperBound() != null)
				paramMap.put(Condition.SP_ONSET_DATE + "_" + onset.getUpperBound().getPrefix().name(),
						onset.getUpperBound().getValueAsString());
		}

		if (patient != null) {
			paramMap.put(Condition.SP_PATIENT, patient.getValue());
		}

		conditionList = conditionService.findConditionByParamMap(paramMap);

		if (conditionList.isEmpty()) {
			throw new ResourceNotFoundException("No Condition resource found for given search criteria.");
		}

		return conditionList;
	}

	/**
	 * 
	 */
	@Override
	public Class<Condition> getResourceType() {
		return Condition.class;
	}

}
