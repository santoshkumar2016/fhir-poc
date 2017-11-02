package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunnization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.ImmunnizationService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Immunnization resource
 * 
 * @author santosh
 *
 */
public class ImmunnizationResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(ImmunnizationResourceProvider.class);

	@Autowired
	private ImmunnizationService ImmunnizationService;

	/**
	 * Constructor
	 */
	public ImmunnizationResourceProvider() {
		logger.info("************************* ImmunnizationResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Immunnization by id
	 * 
	 * @param theId
	 *            of Immunnization to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Immunnization readImmunnization(@IdParam IdType theId) {

		logger.debug("Reading Immunnization resource with Id:{}", theId.getIdPartAsLong());

		Immunnization Immunnization = ImmunnizationService.findImmunnizationById(theId.getIdPartAsLong());

		if (Immunnization == null || Immunnization.isEmpty()) {
			throw new ResourceNotFoundException("Immunnization Resource not found : " + theId.getValueAsString());
		}
		
		return Immunnization;

	}
	
	@Search()
	public List<Immunnization> findImmunnizationsBy(@OptionalParam(name = Immunnization.SP_CODE) StringDt activ){
		
		List<Immunnization> ImmunnizationList = new ArrayList<Immunnization>();
		
		return ImmunnizationList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Immunnization> getResourceType() {
		return Immunnization.class;
	}

}
