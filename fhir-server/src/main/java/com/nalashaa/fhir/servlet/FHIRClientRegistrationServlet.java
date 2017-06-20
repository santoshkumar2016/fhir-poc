package com.nalashaa.fhir.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nalashaa.fhir.db.model.OauthClientDetailsEn;
import com.nalashaa.fhir.service.OAuthService;
import com.nalashaa.fhir.service.impl.OAuthServiceImpl;

public class FHIRClientRegistrationServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
        {
    	
    	
    		String redirectUri = req.getParameter("redirect_uri");
    		String scope = req.getParameter("scope");
    		String authorities = req.getParameter("authorities");
    		String grantTypes = req.getParameter("granttypes");
           
    		OauthClientDetailsEn client = new OauthClientDetailsEn();
    		client.setScope(scope);
    		client.setWebServerRedirectUri(redirectUri);
    		client.setAuthorities(authorities);
    		client.setAuthorizedGrantTypes(grantTypes);
    		//client.setAuthorities("ROLE_CLIENT,ROLE_TRUSTED_CLIENT");
    		//client.setAuthorizedGrantTypes("password,authorization_code,refresh_token,implicit");
    		
    		UUID uuid = UUID.randomUUID();
    		String clientId = uuid.toString();
    		
    		client.setClientId(clientId);
    		
    		UUID uuidSecret = UUID.randomUUID();
    		String clientSecret = uuid.toString();
    		client.setClientSecret(clientSecret);
    		
    		OAuthService service = new OAuthServiceImpl();
    		service.saveClient(client);
    		
    		 String json = "{Client Id: "+clientId+",\nClient Secret: "+clientSecret+" }";
    		 resp.setContentType("application/json");
    		 resp.setCharacterEncoding("UTF-8");
    		 resp.getWriter().write(json);
    		
        }
	
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
        {
            doGet(req,resp);
        }

}
