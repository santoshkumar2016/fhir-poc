package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Medication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.MedicationService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Medication resource
 * 
 * @author santosh
 *
 */
public class MedicationResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(MedicationResourceProvider.class);

	@Autowired
	private MedicationService medicationService;

	/**
	 * Constructor
	 */
	public MedicationResourceProvider() {
		logger.info("************************* MedicationResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read medication by id
	 * 
	 * @param theId
	 *            of medication to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Medication readMedication(@IdParam IdType theId) {

		logger.debug("Reading Medication resource with Id:{}", theId.getIdPartAsLong());

		Medication medication = medicationService.findMedicationById(theId.getIdPartAsLong());

		if (medication == null || medication.isEmpty()) {
			throw new ResourceNotFoundException("Medication Resource not found : " + theId.getValueAsString());
		}
		
		return medication;

	}
	
	@Search()
	public List<Medication> findMedicationsBy(@OptionalParam(name = Medication.SP_CODE) StringDt activ){
		
		List<Medication> medicationList = new ArrayList<Medication>();
		
		return medicationList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Medication> getResourceType() {
		return Medication.class;
	}

}
