package com.nalashaa.fhir.config.spring;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

public class FHIRRedirectingFilter extends GenericFilterBean {
	 
    @Override
    public void doFilter(
      ServletRequest request, 
      ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    	if(request.getParameter("access_toke") == null){
    		HttpServletResponse httpRes = (HttpServletResponse) response;
    		httpRes.sendRedirect("https://www.google.co.in/#q=access_toke");
    	}
        chain.doFilter(request, response);
    }
}