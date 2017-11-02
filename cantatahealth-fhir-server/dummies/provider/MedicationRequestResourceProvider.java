package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.MedicationRequestService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for MedicationRequest resource
 * 
 * @author santosh
 *
 */
public class MedicationRequestResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(MedicationRequestResourceProvider.class);

	@Autowired
	private MedicationRequestService MedicationRequestService;

	/**
	 * Constructor
	 */
	public MedicationRequestResourceProvider() {
		logger.info("************************* MedicationRequestResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read MedicationRequest by id
	 * 
	 * @param theId
	 *            of MedicationRequest to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public MedicationRequest readMedicationRequest(@IdParam IdType theId) {

		logger.debug("Reading MedicationRequest resource with Id:{}", theId.getIdPartAsLong());

		MedicationRequest MedicationRequest = MedicationRequestService.findMedicationRequestById(theId.getIdPartAsLong());

		if (MedicationRequest == null || MedicationRequest.isEmpty()) {
			throw new ResourceNotFoundException("MedicationRequest Resource not found : " + theId.getValueAsString());
		}
		
		return MedicationRequest;

	}
	
	@Search()
	public List<MedicationRequest> findMedicationRequestsBy(@OptionalParam(name = MedicationRequest.SP_CODE) StringDt activ){
		
		List<MedicationRequest> MedicationRequestList = new ArrayList<MedicationRequest>();
		
		return MedicationRequestList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<MedicationRequest> getResourceType() {
		return MedicationRequest.class;
	}

}
