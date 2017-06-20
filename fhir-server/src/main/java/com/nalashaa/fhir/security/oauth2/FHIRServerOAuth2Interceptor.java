package com.nalashaa.fhir.security.oauth2;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

public class FHIRServerOAuth2Interceptor extends InterceptorAdapter {

	public FHIRServerOAuth2Interceptor() {

		System.out.println("FHIRServerOAuth2Interceptor creatd!!!!!!!!!!!!!!");
	}

	@Override
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest,
			HttpServletResponse theResponse) throws AuthenticationException {
		// if (theRequestDetails.getOtherOperationType() ==
		// OtherOperationTypeEnum.METADATA) {
		// return true;
		// }
		System.out.println("incomingRequestPostProcessed : incomingRequestPostProcessed");
		authenticate(theRequest, theResponse);

		return true;
	}

	public void authenticate(HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {

		System.out.println("authenticate : authenticate");

		String token = theRequest.getHeader(Constants.HEADER_AUTHORIZATION);
//		if (token == null) {
//			throw new AuthenticationException("Not authorized (no authorization header found in request)");
//		}
//		if (!token.startsWith(Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER)) {
//			throw new AuthenticationException("Not authorized (authorization header does not contain a bearer token)");
//		}
//
//		token = token.substring(Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER.length());
//		
//		if(!"12345".equals(token)){
//			throw new AuthenticationException("Not authorized (authorization header contain invalid bearer token)");
//		}
		System.out.println(Constants.HEADER_AUTHORIZATION + "::::"+token);
		
//		try {
//			theResponse.sendRedirect("https://www.google.co.in/#q=servletrediret");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
