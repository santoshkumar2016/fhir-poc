package com.nalashaa.fhir.server.acl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nalashaa.fhir.server.acl.db.model.ClientDetails;
import com.nalashaa.fhir.server.acl.model.ClientForm;
import com.nalashaa.fhir.server.acl.service.ClientService;

@Controller
@RequestMapping(value = "/client")
public class FHIRServerClientRegistrationController {

	@Autowired
	private ClientService clientService;

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String viewRegistration(Map<String, Object> model) {
		ClientForm client = new ClientForm();
		model.put("clientForm", client);

		List<String> scopeList = new ArrayList<String>();
		scopeList.add("Read");
		scopeList.add("Write");
		// scopeList.add("Immunization");
		model.put("scopeList", scopeList);

		List<String> resourceList = new ArrayList<String>();
		resourceList.add("Patient");
		resourceList.add("Observation");
		resourceList.add("Immunization");
		resourceList.add("Encounter");
		model.put("resourceList", resourceList);

		return "Registration";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegistration(@ModelAttribute("clientForm") ClientForm clientForm, Map<String, Object> model,
			RedirectAttributes redirectAttrib) {

		String redirectURI = clientForm.getWebServerRedirectUri();
		String scope = String.join(",", clientForm.getScopeList());
		String authorities = clientForm.getAuthorities();
		String granttypes = clientForm.getAuthorizedGrantTypes();

		String resourceids = String.join(",", clientForm.getResourceList());
		;
		// String acesstokenvalidity = req.getParameter("acesstokenvalidity");
		// String refreshtokenvalidity =
		// req.getParameter("refreshtokenvalidity");

		ClientDetails client = new ClientDetails();
		client.setScope(scope);
		client.setWebServerRedirectUri(redirectURI);
		client.setAuthorities(authorities);
		client.setAuthorizedGrantTypes(granttypes);
		client.setResourceIds(resourceids);
		
		// values for which reasonable default values need be defined 
		client.setAuthorities("ROLE_CLIENT,ROLE_TRUSTED_CLIENT");
		client.setAuthorizedGrantTypes("password,authorization_code,refresh_token,implicit");
		client.setAccessTokenValidity(300);
		client.setRefreshTokenValidity(500);

		// UUID uuid = UUID.randomUUID();
		String clientId = UUID.randomUUID().toString();

		client.setClientId(clientId);

		// UUID uuidSecret = UUID.randomUUID();
		String clientSecret = UUID.randomUUID().toString();
		client.setClientSecret(clientSecret);

		clientService.saveClient(client);

		clientForm.setClientId(clientId);
		clientForm.setClientSecret(clientSecret);

		model.put("clientForm", clientForm);

		// for testing purpose:
		System.out.println("clientId: " + clientForm.getClientId());
		System.out.println("clientSecret: " + clientForm.getClientSecret());

		redirectAttrib.addFlashAttribute("clientForm", clientForm);

		return "redirect:registrationSuccess";
	}

	// redirected handler
	@RequestMapping(value = "/registrationSuccess", method = RequestMethod.GET)
	public String handleRegistrationDone(@ModelAttribute("clientForm") ClientForm clientForm,
			Map<String, Object> model) {

		model.put("clientForm", clientForm);
		return "RegistrationSuccess";
	}

	/**
	 * Registration support for clients expecting json/xml response
	 * 
	 * @param req
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/registration", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ClientForm> clientRegistration(
			// @RequestParam("redirectURI") String redirectURI,
			// @RequestParam("scope") String scope,
			// @RequestParam("authorities") String authorities,
			// @RequestParam("granttypes") String granttypes,
			HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

		String redirectURI = req.getParameter("redirect_uri");
		String scope = req.getParameter("scope");
		String authorities = req.getParameter("authorities");
		String granttypes = req.getParameter("granttypes");

		String resourceids = req.getParameter("resourceids");
		String acesstokenvalidity = req.getParameter("acesstokenvalidity");
		String refreshtokenvalidity = req.getParameter("refreshtokenvalidity");

		ClientDetails client = new ClientDetails();
		client.setScope(scope);
		client.setWebServerRedirectUri(redirectURI);
		client.setAuthorities(authorities);
		client.setAuthorizedGrantTypes(granttypes);
		client.setResourceIds(resourceids);

		// values for which reasonable default values need be defined 
		client.setAuthorities("ROLE_CLIENT,ROLE_TRUSTED_CLIENT");
		client.setAuthorizedGrantTypes("password,authorization_code,refresh_token,implicit");
		client.setAccessTokenValidity(300);
		client.setRefreshTokenValidity(500);

		// UUID uuid = UUID.randomUUID();
		String clientId = UUID.randomUUID().toString();

		client.setClientId(clientId);

		// UUID uuidSecret = UUID.randomUUID();
		String clientSecret = UUID.randomUUID().toString();
		client.setClientSecret(clientSecret);

		clientService.saveClient(client);

		ClientForm clientCredentials = new ClientForm();
		clientCredentials.setClientId(clientId);
		clientCredentials.setClientSecret(clientSecret);

		// String json = "{Client Id: " + clientId + ",Client Secret: " +
		// clientSecret + " }";
		// response.setContentType("application/json");
		// response.setCharacterEncoding("UTF-8");
		// response.getWriter().write(json);

		// oauthService.saveClient(redirectURI);

		return new ResponseEntity<ClientForm>(clientCredentials, HttpStatus.OK);
	}

}
