package com.cantatahealth.fhir.provider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.service.AllergyIntoleranceService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * This is a resource provider for resource type AllergyIntolerance
 * 
 * @author somadutta
 *
 */
@Component
public class AllergyIntoleranceResourceProvider implements IResourceProvider {

		

		Logger logger = LoggerFactory.getLogger(AllergyIntoleranceResourceProvider.class);

		@Autowired
		private AllergyIntoleranceService allergyIntoleranceService;

		/**
		 * Constructor
		 */
		public AllergyIntoleranceResourceProvider() {
			logger.info("************************* AllergyIntoleranceResourceProvider Instantiated *************************");
		}

		/**
		 * This method reads a AllergyIntolerance resource.
		 * 
		 * <p>
		 * Read operations take a single parameter annotated with the
		 * {@link IdParam} paramater, and return a single resource instance.
		 * </p>
		 * 
		 * @param theId
		 *            the Id of AllergyIntolerance resource to read
		 * 
		 * @return Returns a resource matching this identifier, or null if none
		 *         exists.
		 */
		@Read(version = true)
		public AllergyIntolerance readAllergyIntolerance(@IdParam IdType theId) {

			logger.debug("Reading AllergyIntolerance resource with Id:{}", theId.getIdPartAsLong());

			AllergyIntolerance allergyIntolerance = allergyIntoleranceService.findAllergyIntoleranceById(theId.getIdPartAsLong());

			if (allergyIntolerance == null || allergyIntolerance.isEmpty()) {
				throw new ResourceNotFoundException("Resource not found : " + theId.getValueAsString());
			}

			return allergyIntolerance;

		}
		
		/**
		 * 
		 * @param patient
		 * @param periodStartDate(onset)
		 * @param periodEndDate(onset)
		 * @return This method returns a list of allergies or intolerances. This list may contain
		 *         multiple matching resources, or it may also be empty.
		 */
		@Search()
		public List<AllergyIntolerance> findAllergyIntoleranceBy(@OptionalParam(name = AllergyIntolerance.SP_PATIENT) StringDt patient,
				@OptionalParam(name = AllergyIntolerance.SP_ONSET) DateRangeParam onset) {

			List<AllergyIntolerance> retVal = new LinkedList<AllergyIntolerance>();
			List<AllergyIntoleranceDbEntity> PatientDbEntityListAll = new LinkedList<AllergyIntoleranceDbEntity>();
			
			Map<String, String> paramMap = new ConcurrentHashMap<String,String>();
			 if(onset != null) { 
				 if(onset.getLowerBound() != null) {
					 logger.debug("Search Parameter Patient:{}", onset.getLowerBound().getValueAsString());
					 paramMap.put(AllergyIntolerance.SP_ONSET+"_"+onset.getLowerBound().getPrefix().name(),
							 onset.getLowerBound().getValueAsString());
				 }
				 if(onset.getUpperBound() != null) {
					 logger.debug("Search Parameter Patient:{}", onset.getUpperBound().getValueAsString());
					 paramMap.put(AllergyIntolerance.SP_ONSET+"_"+onset.getUpperBound().getPrefix().name(),
							 onset.getUpperBound().getValueAsString());
				 }
			 }
			 
			if (patient != null) {
				logger.debug("Search Parameter Patient:{}", patient.getValueAsString());
				paramMap.put(AllergyIntolerance.SP_PATIENT, patient.getValue());
			}					
			
		/*	if (!paramMap.containsKey(AllergyIntolerance.SP_PATIENT) && (paramMap.containsKey(onset.getLowerBound().getPrefix().name())
					|| paramMap.containsKey(onset.getUpperBound().getPrefix().name()))) {
				throw new ResourceNotFoundException("Patient ID is required ");
			}*/
			
			retVal = allergyIntoleranceService.findAllergyIntoleranceByParamMap(paramMap);

			logger.debug("Found {} Patient resource.", PatientDbEntityListAll.size());

			return retVal;
		}

		/**
		 * The getResourceType method indicate what type of resource this provider
		 * supplies.
		 */
		@Override
		public Class<AllergyIntolerance> getResourceType() {
			return AllergyIntolerance.class;
		}

	
}
