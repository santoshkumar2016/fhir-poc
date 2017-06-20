package com.nalashaa.fhir.server.acl.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.cors.CorsConfiguration;

import com.nalashaa.fhir.server.acl.provider.ImmunizationResourceProvider;
import com.nalashaa.fhir.server.acl.provider.PatientResourceProvider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

/**
 * This servlet is the actual FHIR server.
 */
public class FHIRRestfulServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;

	private WebApplicationContext myAppCtx;

	Logger logger = LoggerFactory.getLogger(FHIRRestfulServlet.class);

	/**
	 * Constructor
	 */
	public FHIRRestfulServlet() {
		super(FhirContext.forDstu2()); // Support DSTU2
	}

	@Override
	public void addHeadersToResponse(HttpServletResponse theHttpResponse) {
		StringBuilder b = new StringBuilder();
		// b.append("NALASHAA FHIR ");
		// b.append(VersionUtil.getVersion());
		b.append(" REST Server ");
		// b.append(myFhirContext.getVersion().getVersion().getFhirVersionString());
		// b.append('/');
		// b.append(myFhirContext.getVersion().getVersion().name());
		// b.append(")");
		theHttpResponse.addHeader("X-Powered-By", b.toString());
	}

	@Override
	public BaseMethodBinding<?> determineResourceMethod(RequestDetails requestDetails, String requestPath) {

		logger.info("***********************************Determining resource method******************************");
		// RequestDetails reqDetails = new ServletRequestDetails();
		// requestDetails;
		// create a new servlet request details to include custom processing
		if (requestDetails.getCompleteUrl().contains("?"))
			requestDetails.setCompleteUrl(
					requestDetails.getCompleteUrl().substring(0, requestDetails.getCompleteUrl().indexOf("?")));
		Map<String, String[]> params = new HashMap<String, String[]>();
		requestDetails.getParameters().remove("access_token");

		return super.determineResourceMethod(requestDetails, requestPath);
	}

	/**
	 * This method is called automatically when the servlet is initializing.
	 */
	@Override
	public void initialize() {

		// Get the spring context from the web container (it's declared in
		// web.xml)
		// myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		// SessionFactory sessionFactory = myAppCtx.getBean("sessionFactory",
		// SessionFactory.class);
		// AbstractDao.setSession(sessionFactory);

		logger.info("****************************** Initializing FHIRRestfulServlet ******************************");
		ServletContext servletContext = this.getServletContext();
		myAppCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		PatientResourceProvider patientResourceProvider = myAppCtx.getBean("patientResourceProvider", PatientResourceProvider.class);
		ImmunizationResourceProvider immunizationResourceProvider = myAppCtx.getBean("immunizationResourceProvider", ImmunizationResourceProvider.class);


		/*
		 * Two resource providers are defined. Each one handles a specific type
		 * of resource.
		 */
		List<IResourceProvider> providers = new ArrayList<IResourceProvider>();
		providers.add(patientResourceProvider);
		providers.add(immunizationResourceProvider);
//		providers.add(new OrganizationResourceProvider());
//		
//		providers.add(new ConformanceResourceProvider());
//		providers.add(new CCDAResourceProvider());
		setResourceProviders(providers);

		/*
		 * Use a narrative generator. This is a completely optional step, but
		 * can be useful as it causes HAPI to generate narratives for resources
		 * which don't otherwise have one.
		 */
		// INarrativeGenerator narrativeGen = new
		// DefaultThymeleafNarrativeGenerator();
		// getFhirContext().setNarrativeGenerator(narrativeGen);

		setDefaultResponseEncoding(EncodingEnum.JSON);

		/*
		 * Enable CORS
		 */
		CorsConfiguration config = new CorsConfiguration();
		CorsInterceptor corsInterceptor = new CorsInterceptor(config);
		config.addAllowedHeader("Accept");
		config.addAllowedHeader("Content-Type");
		config.addAllowedOrigin("*");
		config.addExposedHeader("Location");
		config.addExposedHeader("Content-Location");
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		registerInterceptor(corsInterceptor);

		/*
		 * This server interceptor causes the server to return nicely formatter
		 * and coloured responses instead of plain JSON/XML if the request is
		 * coming from a browser window. It is optional, but can be nice for
		 * testing.
		 */
		registerInterceptor(new ResponseHighlighterInterceptor());

		/*
		 * OAuth interceptor
		 */

		//registerInterceptor(new FHIRServerOAuth2Interceptor());

		/*
		 * Tells the server to return pretty-printed responses by default
		 */
		setDefaultPrettyPrint(true);

		logger.info(
				"****************************** FHIRRestfulServlet initialization complete ******************************");

	}

}
