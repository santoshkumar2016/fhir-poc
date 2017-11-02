/**
 * 
 */
package com.cantatahealth.fhir.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

/**
 * @author santosh
 *
 *
 */

@Component
public class FhirInterceptor extends InterceptorAdapter {
	
	Logger logger = LoggerFactory.getLogger(FhirInterceptor.class);


	@Override
	public boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException,
			HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
					throws ServletException, IOException {
		
		//logger.error("" + theException.getStackTrace()[0].);
		// TODO Auto-generated method stub
		return super.handleException(theRequestDetails, theException, theServletRequest, theServletResponse);
	}
}
