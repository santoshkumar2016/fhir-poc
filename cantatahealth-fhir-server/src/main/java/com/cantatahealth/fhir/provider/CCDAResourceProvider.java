package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.model.CCDAResource;
import com.cantatahealth.fhir.service.CCDAResourceService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

/**
 * 
 * Provider for CCDAResource resource
 * 
 * @author santosh
 *
 */
public class CCDAResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(CCDAResourceProvider.class);

	@Autowired
	private CCDAResourceService CCDAResourceService;

	/**
	 * Constructor
	 */
	public CCDAResourceProvider() {
		logger.info("************************* CCDAResourceResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read CCDAResource by id
	 * 
	 * @param theId
	 *            of CCDAResource to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public CCDAResource readCCDAResource(@IdParam IdType theId) {

		logger.debug("Reading CCDAResource resource with Id:{}", theId.getIdPartAsLong());

		//CCDAResource CCDAResource = CCDAResourceService.findCCDAResourceById(theId.getIdPartAsLong());
		
		
		

//		if (CCDAResource == null || CCDAResource.isEmpty()) {
//			throw new ResourceNotFoundException("CCDAResource Resource not found : " + theId.getValueAsString());
//		}
		
		return new CCDAResource();

	}
	
	@Search()
	public List<CCDAResource> findCCDAResourcesBy(@RequiredParam(name = "patient") StringDt patient,
			@OptionalParam(name = "from-date") DateParam fromDate, @OptionalParam(name = "to-date") DateParam toDate){
		
		List<CCDAResource> CCDAResourceList = new ArrayList<CCDAResource>();
		
		//CCDAResource CCDAResource = CCDAResourceService.findCCDAResourceById(Long.valueOf(patient.getValue()));
		
		return CCDAResourceList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<CCDAResource> getResourceType() {
		return CCDAResource.class;
	}

}
