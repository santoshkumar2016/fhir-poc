package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.DiagnosticReportService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for DiagnosticReport resource
 * 
 * @author santosh
 *
 */
public class DiagnosticReportResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(DiagnosticReportResourceProvider.class);

	@Autowired
	private DiagnosticReportService DiagnosticReportService;

	/**
	 * Constructor
	 */
	public DiagnosticReportResourceProvider() {
		logger.info("************************* DiagnosticReportResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read DiagnosticReport by id
	 * 
	 * @param theId
	 *            of DiagnosticReport to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public DiagnosticReport readDiagnosticReport(@IdParam IdType theId) {

		logger.debug("Reading DiagnosticReport resource with Id:{}", theId.getIdPartAsLong());

		DiagnosticReport DiagnosticReport = DiagnosticReportService.findDiagnosticReportById(theId.getIdPartAsLong());

		if (DiagnosticReport == null || DiagnosticReport.isEmpty()) {
			throw new ResourceNotFoundException("DiagnosticReport Resource not found : " + theId.getValueAsString());
		}
		
		return DiagnosticReport;

	}
	
	@Search()
	public List<DiagnosticReport> findDiagnosticReportsBy(@OptionalParam(name = DiagnosticReport.SP_CODE) StringDt activ){
		
		List<DiagnosticReport> DiagnosticReportList = new ArrayList<DiagnosticReport>();
		
		return DiagnosticReportList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<DiagnosticReport> getResourceType() {
		return DiagnosticReport.class;
	}

}
