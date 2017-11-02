package com.cantatahealth.fhir.provider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
import com.cantatahealth.fhir.service.MedicationAdministrationService;

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
 * Provider for MedicationAdministration resource
 * 
 * @author santosh
 *
 */
public class MedicationAdministrationResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(MedicationAdministrationResourceProvider.class);

	@Autowired
	private MedicationAdministrationService medicationAdministrationService;

	/**
	 * Constructor
	 */
	public MedicationAdministrationResourceProvider() {
		logger.info("************************* MedicationAdministrationResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read MedicationAdministration by id
	 * 
	 * @param theId
	 *            of MedicationAdministration to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public MedicationAdministration readMedicationAdministration(@IdParam IdType theId) {

		logger.debug("Reading MedicationAdministration resource with Id:{}", theId.getIdPartAsLong());

		MedicationAdministration medication = medicationAdministrationService.findMedicationAdministrationById(theId.getIdPartAsLong());

		if (medication == null || medication.isEmpty()) {
			throw new ResourceNotFoundException("Medication Resource not found : " + theId.getValueAsString());
		}
		
		return medication;

	}
	
	/**
	 * 
	 * @param patient
	 * @param date
	 * @return This method searches medication administration by parameters.
	 */
	@Search()
	public List<MedicationAdministration> findMedicationsBy( @OptionalParam(name = MedicationAdministration.SP_PATIENT) StringDt patient,
			@OptionalParam(name = MedicationAdministration.SP_EFFECTIVE_TIME) DateRangeParam date){
		
		List<MedicationAdministration> medicationList = new ArrayList<MedicationAdministration>();
		
		List<MedicationAdministrationDbEntity> medicationAdministrationDbEntityListAll = new LinkedList<MedicationAdministrationDbEntity>();
		
		Map<String, String> paramMap = new ConcurrentHashMap<String,String>();

		if (patient != null) {
			logger.debug("Search Parameter Patient:{}", patient.getValueAsString());
			paramMap.put(MedicationAdministration.SP_PATIENT, patient.getValue());
		}
		
		if (date != null) {
			if (date.getLowerBound() != null)
				paramMap.put(MedicationAdministration.SP_EFFECTIVE_TIME + "_" + date.getLowerBound().getPrefix().name(),
						date.getLowerBound().getValueAsString());
			if (date.getUpperBound() != null)
				paramMap.put(MedicationAdministration.SP_EFFECTIVE_TIME + "_" + date.getUpperBound().getPrefix().name(),
						date.getUpperBound().getValueAsString());
		}				
		
		medicationList = medicationAdministrationService.findMedicationAdministrationByParamMap(paramMap);

		logger.debug("Found {} Patient resource.", medicationAdministrationDbEntityListAll.size());

		
		return medicationList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<MedicationAdministration> getResourceType() {
		return MedicationAdministration.class;
	}

}
