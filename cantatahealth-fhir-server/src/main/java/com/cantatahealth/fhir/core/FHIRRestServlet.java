
package com.cantatahealth.fhir.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;

import com.cantatahealth.fhir.provider.AllergyIntoleranceResourceProvider;
import com.cantatahealth.fhir.provider.CCDAResourceProvider;
import com.cantatahealth.fhir.provider.CareTeamResourceProvider;
import com.cantatahealth.fhir.provider.ConditionResourceProvider;
import com.cantatahealth.fhir.provider.DeviceResourceProvider;
import com.cantatahealth.fhir.provider.DiagnosticReportResourceProvider;
import com.cantatahealth.fhir.provider.EncounterResourceProvider;
import com.cantatahealth.fhir.provider.GoalResourceProvider;
import com.cantatahealth.fhir.provider.ImmunizationResourceProvider;
import com.cantatahealth.fhir.provider.LocationResourceProvider;
import com.cantatahealth.fhir.provider.MedicationAdministrationResourceProvider;
import com.cantatahealth.fhir.provider.MedicationRequestResourceProvider;
import com.cantatahealth.fhir.provider.MedicationStatementResourceProvider;
import com.cantatahealth.fhir.provider.ObservationResourceProvider;
import com.cantatahealth.fhir.provider.PatientResourceProvider;
import com.cantatahealth.fhir.provider.ProcedureResourceProvider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.VersionUtil;

public class FHIRRestServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;

	private WebApplicationContext myAppCtx;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();

		FhirVersionEnum fhirVersion = FhirVersionEnum.DSTU3;
		setFhirContext(new FhirContext(fhirVersion));

		// Get the spring context from the web container (it's declared in
		// web.xml)
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();

		List<IResourceProvider> providers = new ArrayList<IResourceProvider>();
		
		// add patient resource provider
		PatientResourceProvider patientResourceProvider = myAppCtx.getBean("patientResourceProvider",
				PatientResourceProvider.class);
		providers.add(patientResourceProvider);
		
		// add allergyIntolerance resource provider
		AllergyIntoleranceResourceProvider allergyIntoleranceResourceProvider = myAppCtx.getBean("allergyIntoleranceResourceProvider",
				AllergyIntoleranceResourceProvider.class);
		providers.add(allergyIntoleranceResourceProvider);
		
		// add medicationadministration resource provider
		MedicationAdministrationResourceProvider medicationAdministrationResourceProvider = myAppCtx.getBean("medicationAdministrationResourceProvider",
				MedicationAdministrationResourceProvider.class);
		providers.add(medicationAdministrationResourceProvider);
				
		// add careteam resource provider
		CareTeamResourceProvider careTeamResourceProvider = myAppCtx.getBean("careTeamResourceProvider",
				CareTeamResourceProvider.class);
		providers.add(careTeamResourceProvider);
		
		// add condition resource provider
		ConditionResourceProvider conditionResourceProvider = myAppCtx.getBean("conditionResourceProvider",
				ConditionResourceProvider.class);
		providers.add(conditionResourceProvider);
		
		// add device resource provider
		DeviceResourceProvider deviceResourceProvider = myAppCtx.getBean("deviceResourceProvider",
				DeviceResourceProvider.class);
		providers.add(deviceResourceProvider);
		
		// add diagnosticreport resource provider
		DiagnosticReportResourceProvider diagnosticReportResourceProvider = myAppCtx.getBean("diagnosticReportResourceProvider",
				DiagnosticReportResourceProvider.class);
		providers.add(diagnosticReportResourceProvider);
		
		// add goal resource provider
		GoalResourceProvider goalResourceProvider = myAppCtx.getBean("goalResourceProvider",
				GoalResourceProvider.class);
		providers.add(goalResourceProvider);
		
		// add immunization resource provider
		ImmunizationResourceProvider immunizationResourceProvider = myAppCtx.getBean("immunizationResourceProvider",
				ImmunizationResourceProvider.class);
		providers.add(immunizationResourceProvider);
		
		// add location resource provider
		LocationResourceProvider locationResourceProvider = myAppCtx.getBean("locationResourceProvider",
				LocationResourceProvider.class);
		providers.add(locationResourceProvider);
		
		// add medicationrequest resource provider
		MedicationRequestResourceProvider medicationRequestResourceProvider = myAppCtx.getBean("medicationRequestResourceProvider",
				MedicationRequestResourceProvider.class);
		providers.add(medicationRequestResourceProvider);
		
		// add medicationstatement resource provider
		MedicationStatementResourceProvider medicationStatementResourceProvider = myAppCtx.getBean("medicationStatementResourceProvider",
				MedicationStatementResourceProvider.class);
		providers.add(medicationStatementResourceProvider);
		
		// add observation resource provider
		ObservationResourceProvider observationResourceProvider = myAppCtx.getBean("observationResourceProvider",
				ObservationResourceProvider.class);
		providers.add(observationResourceProvider);
		
		// add procedure resource provider
		ProcedureResourceProvider procedureResourceProvider = myAppCtx.getBean("procedureResourceProvider",
				ProcedureResourceProvider.class);
		providers.add(procedureResourceProvider);
		

		// add Composition resource provider
//		CompositionResourceProvider compositionResourceProvider = myAppCtx.getBean("compositionResourceProvider",
//				CompositionResourceProvider.class);
//		providers.add(compositionResourceProvider);
		
		// add Composition resource provider
		CCDAResourceProvider ccdaResourceProvider = myAppCtx.getBean("ccdaResourceProvider",
				CCDAResourceProvider.class);
		providers.add(ccdaResourceProvider);

		// add encounter resource provider
		EncounterResourceProvider encounterResourceProvider = myAppCtx.getBean("encounterResourceProvider",
				EncounterResourceProvider.class);
				providers.add(encounterResourceProvider);
		

		setResourceProviders(providers);

		/*
		 * Enable ETag Support (this is already the default)
		 */
		setETagSupport(ETagSupportEnum.ENABLED);

		/*
		 * This server tries to dynamically generate narratives
		 */
		FhirContext ctx = getFhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		/*
		 * Default to JSON and pretty printing
		 */
		setDefaultPrettyPrint(true);
		setDefaultResponseEncoding(EncodingEnum.JSON);

		/*
		 * Load interceptors for the server from Spring (these are defined in
		 * FhirServerConfig.java)
		 */
		Collection<IServerInterceptor> interceptorBeans = myAppCtx.getBeansOfType(IServerInterceptor.class).values();
		for (IServerInterceptor interceptor : interceptorBeans) {
			this.registerInterceptor(interceptor);
		}
		
		/*
		 * Enable CORS
		 */
//		CorsConfiguration config = new CorsConfiguration();
//		CorsInterceptor corsInterceptor = new CorsInterceptor(config);
//		config.addAllowedHeader("Accept");
//		config.addAllowedHeader("Content-Type");
//		config.addAllowedOrigin("*");
//		config.addExposedHeader("Location");
//		config.addExposedHeader("Content-Location");
//		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//		registerInterceptor(corsInterceptor);

	}
	
	@Override
	public void addHeadersToResponse(HttpServletResponse theHttpResponse) {
		StringBuilder b = new StringBuilder();
		b.append("CANTATA HELATH ");
		b.append(VersionUtil.getVersion());
		b.append(" REST Server (FHIR Server; FHIR 1.0");
		//b.append(myFhirContext.getVersion().getVersion().getFhirVersionString());
		//b.append('/');
		//b.append(myFhirContext.getVersion().getVersion().name());
		b.append(")");
		theHttpResponse.addHeader("X-Powered-By", b.toString());
	}

}
