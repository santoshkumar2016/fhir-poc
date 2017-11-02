package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Composition.SectionComponent;
import org.hl7.fhir.dstu3.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.CompositionService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Composition resource
 * 
 * @author santosh
 *
 */
public class CompositionResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(CompositionResourceProvider.class);

	@Autowired
	private CompositionService compositionService;

	/**
	 * Constructor
	 */
	public CompositionResourceProvider() {
		logger.info("************************* CompositionResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Composition by id
	 * 
	 * @param theId
	 *            of Composition to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Composition readComposition(@IdParam IdType theId) {

		logger.debug("Reading Composition resource with Id:{}", theId.getIdPartAsLong());

		Composition composition = compositionService.findCompositionById(theId.getIdPartAsLong());
		
		composition.setId("111");
		composition.setDate(new Date());
		
		SectionComponent secComp = new SectionComponent();
		secComp.setId("1");
		secComp.setTitle("ccdaSection");
		composition.addSection(secComp);
		

		if (composition == null || composition.isEmpty()) {
			throw new ResourceNotFoundException("Composition Resource not found : " + theId.getValueAsString());
		}
		
		return composition;

	}
	
	@Search()
	public List<Composition> findCompositionsBy(@OptionalParam(name = Composition.SP_AUTHOR) StringDt activ){
		
		List<Composition> CompositionList = new ArrayList<Composition>();
		
		return CompositionList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Composition> getResourceType() {
		return Composition.class;
	}

}
