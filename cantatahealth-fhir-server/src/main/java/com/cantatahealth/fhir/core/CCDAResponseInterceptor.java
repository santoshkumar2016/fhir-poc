package com.cantatahealth.fhir.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringResult;

import com.cantatahealth.fhir.cccda.service.CCDAService;
import com.cantatahealth.fhir.service.PatientService;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.DateClientParam.IDateCriterion;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

/**
 * 
 * Interceptor to handle request for CCDA resources
 * 
 * CCDA is still not supported in FHIR and there is no CCDA resource structure
 * in fhir This interceptor will intercept rquest for ccda resource and create a
 * custom ccda xml strucure and write to client stream
 * 
 * @author santosh
 *
 */
@Component
public class CCDAResponseInterceptor extends InterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(CCDAResponseInterceptor.class);

	@Autowired
	PatientService patientService;

	@Autowired
	CCDAService ccdaService;

	IGenericClient client = null;

	FhirContext ctx = null;

	// FHIR server URL,
	// TODO read from prop file
	// String serverBase = "http://localhost:8080/cantatahealth-fhir/baseDstu3";

	/**
	 * 
	 * Initialize the FHIR context
	 * 
	 * FhirContext is a not so lighweight and so should not be created a for
	 * each request create a single instance at startup of application or on
	 * first ccda request
	 * 
	 */
	private void initializeContext() {
		ctx = FhirContext.forDstu3();
		//
		// // set sockettimeout for pretty long duration as it may cause issue
		// while accessing huge number of records
		// ctx.getRestfulClientFactory().setSocketTimeout(600000);//
		// setServerValidationMode(ServerValidationModeEnum.NEVER);
		//
		// // Disable server validation (don't pull the server's metadata first)
		// //this is required as in case of huge metadata this may cause server
		// failure
		// ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		// client = ctx.newRestfulGenericClient(serverBase);
	}

	/**
	 * Checks if this request is for ccda resource
	 */
	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject,
			HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
					throws AuthenticationException {

		String resouceName = theRequestDetails.getResourceName();

		if (!"CCDA".equals(resouceName)) {
			return true;
		}

		System.out.println("CCDA processing start: " + new Date());
		if (ctx == null)
			initializeContext();

		// System.out.println("theServletRequest.getRequestURL()" +
		// theServletRequest.getRequestURL());
		// if (serverBase == null) {
		// serverBase = theServletRequest.getRequestURL().substring(0,
		// theServletRequest.getRequestURL().lastIndexOf("/"));
		// System.out.println("serverBase:" + serverBase);
		// }

		// process ccda
		streamCCDAResponse(theRequestDetails, theServletResponse, theResponseObject, theServletRequest, 200);

		return false;
	}

	/**
	 * 
	 * 
	 * @param theRequestDetails
	 * @param theServletResponse
	 * @param resource
	 * @param theServletRequest
	 * @param theStatusCode
	 */
	private void streamCCDAResponse(RequestDetails theRequestDetails, HttpServletResponse theServletResponse,
			IBaseResource resource, ServletRequest theServletRequest, int theStatusCode) {

		if (theRequestDetails.getServer() instanceof RestfulServer) {
			RestfulServer rs = (RestfulServer) theRequestDetails.getServer();
			rs.addHeadersToResponse(theServletResponse);
		}

		IParser parser;
		Map<String, String[]> parameters = theRequestDetails.getParameters();

		String patientId = parameters.get("patient")[0];

		/** patient is mandatory parameter for CCDA request **/
		if (!AppUtil.isNotEmpty(patientId)) {
			try {
				theServletResponse.getWriter().write(
						"<Error>Mandatory parameter patient is missing.\nExample of valid requests:\nCCDA?patient=1\nCCDA?patient=1&fromdate=>2013-01-14&todate=<2017-01-14 </Error>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		/** Check if from-date and to-date parameters are present in request **/
		String fromDate = null;
		String toDate = null;

		String fromDatePrefix = "";
		String toDatePrefix = "";

		if (parameters.get("from-date") != null) {
			try {
				fromDate = parameters.get("from-date")[0];
				fromDatePrefix = fromDate.substring(0, fromDate.length() - 10);
				fromDate = fromDate.substring(fromDate.length() - 10, fromDate.length());

				if (ParamPrefixEnum.GREATERTHAN.getDstu1Value().equals(fromDatePrefix)
						|| ParamPrefixEnum.GREATERTHAN_OR_EQUALS.getDstu1Value().equals(fromDatePrefix)) {

				} else {
					theServletResponse.getWriter().write("<Error>Invalid from-date prefix</Error>");
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (parameters.get("to-date") != null) {
			try {
				toDate = parameters.get("to-date")[0];
				toDatePrefix = toDate.substring(0, toDate.length() - 10);
				toDate = toDate.substring(toDate.length() - 10, toDate.length());

				if (ParamPrefixEnum.LESSTHAN.getDstu1Value().equals(toDatePrefix)
						|| ParamPrefixEnum.LESSTHAN_OR_EQUALS.getDstu1Value().equals(toDatePrefix)) {

				} else {
					theServletResponse.getWriter().write("<Error>Invalid to-date prefix</Error>");
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (fromDate != null && toDate != null
					&& AppUtil.formatToDateString(fromDate).after(AppUtil.formatToDateString(toDate))) {
				theServletResponse.getWriter().write("<Error>from-date can not be greater than to-date</Error>");
				return;
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/**
		 * Check if from-date and to-date parameters are present in request END
		 **/

		// if (parameters.containsKey(Constants.PARAM_FORMAT)) {
		// theServletResponse.setHeader("Content-Type", "application/xml");
		// parser =
		// RestfulServerUtils.getNewParser(theRequestDetails.getServer().getFhirContext(),
		// theRequestDetails);
		// } else {
		// // theServletResponse.setHeader("Content-Type", "application/json");
		// EncodingEnum defaultResponseEncoding =
		// theRequestDetails.getServer().getDefaultResponseEncoding();
		// parser =
		// defaultResponseEncoding.newParser(theRequestDetails.getServer().getFhirContext());
		// RestfulServerUtils.configureResponseParser(theRequestDetails,
		// parser);
		// }

		parser = ctx.newXmlParser().setPrettyPrint(true);

		// This interceptor defaults to pretty printing unless the user
		// has specifically requested us not to
		boolean prettyPrintResponse = true;
		String[] prettyParams = parameters.get(Constants.PARAM_PRETTY);
		if (prettyParams != null && prettyParams.length > 0) {
			if (Constants.PARAM_PRETTY_VALUE_FALSE.equals(prettyParams[0])) {
				prettyPrintResponse = false;
			}
		}
		if (prettyPrintResponse) {
			parser.setPrettyPrint(prettyPrintResponse);
		}

		// client = ctx.newRestfulGenericClient(serverBase);

		/** Patient **/
		// Patient patient =
		// client.read().resource(Patient.class).withId(patientId).execute();

		Patient patient = ccdaService.getResourceById(ResourcesLiteralsUtil.Resource_Patient, Long.valueOf(patientId)); // patientService.findPatientById(Long.valueOf(patientId));

		String patientXml = parser.encodeResourceToString(patient);

		Map<String, String> paramMap = new ConcurrentHashMap<String, String>();

		/** AllergyIntolerance **/
		paramMap.put(AllergyIntolerance.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(AllergyIntolerance.SP_ONSET + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(),
					fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(AllergyIntolerance.SP_ONSET + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(),
					toDate);
		String allerygiesXml = resourceToXmlString(
				getResourceList(ResourcesLiteralsUtil.Resource_AllergyIntolerance, paramMap), parser);

		/** MedicationAdministration **/
		paramMap.clear();
		paramMap.put(MedicationAdministration.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(MedicationAdministration.SP_EFFECTIVE_TIME + "_"
					+ ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(), fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(MedicationAdministration.SP_EFFECTIVE_TIME + "_"
					+ ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		String medicationAdministrationsXml = resourceToXmlString(
				getResourceList(ResourcesLiteralsUtil.Resource_MedicationAdministration, paramMap), parser);

		/** Condition **/
		paramMap.clear();
		paramMap.put(Condition.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null) {
			paramMap.put(Condition.SP_ABATEMENT_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(),
					fromDate);
			paramMap.put(Condition.SP_ONSET_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(),
					fromDate);
		}
		if (toDate != null && toDatePrefix != null) {
			paramMap.put(Condition.SP_ABATEMENT_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(),
					toDate);
			paramMap.put(Condition.SP_ONSET_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		}
		String conditionsXml = resourceToXmlString(getResourceList(ResourcesLiteralsUtil.Resource_Condition, paramMap),
				parser);

		/** CareTeam **/
		paramMap.clear();
		paramMap.put(CareTeam.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(CareTeam.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(), fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(CareTeam.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		String careTeamXml = resourceToXmlString(getResourceList(ResourcesLiteralsUtil.Resource_CareTeam, paramMap),
				parser);

		/** Encounter **/
		paramMap.clear();
		paramMap.put(Encounter.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(Encounter.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(), fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(Encounter.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		String encounterXml = resourceToXmlString(getResourceList(ResourcesLiteralsUtil.Resource_Encounter, paramMap),
				parser);

		/** Goal **/
		paramMap.clear();
		paramMap.put(Goal.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(Goal.SP_START_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(), fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(Goal.SP_START_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		String goalXml = resourceToXmlString(getResourceList(ResourcesLiteralsUtil.Resource_Goal, paramMap), parser);

		/** Immunization **/
		paramMap.clear();
		paramMap.put(Immunization.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(Immunization.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(), fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(Immunization.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		String immunizationsXml = resourceToXmlString(
				getResourceList(ResourcesLiteralsUtil.Resource_Immunization, paramMap), parser);

		/** Location has no patient id **/
		// location fetch only if organization id is present in patient
		String locationXml = "";
		if (patient.getManagingOrganization() != null) {
			String orgId = patient.getManagingOrganization().getResource().getIdElement().getValueAsString();
			orgId = orgId.substring(orgId.indexOf("/")+1, orgId.length());
			paramMap.clear();
			paramMap.put(Location.SP_ORGANIZATION, orgId);
			locationXml = resourceToXmlString(getResourceList(ResourcesLiteralsUtil.Resource_Location, paramMap),
					parser);
		}

		/** Device **/
		paramMap.clear();
		paramMap.put(Device.SP_PATIENT, patientId);
		String deviceXml = resourceToXmlString(getResourceList(ResourcesLiteralsUtil.Resource_Device, paramMap),
				parser);

		/** Observation **/
		paramMap.clear();
		paramMap.put(Observation.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(Observation.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(), fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(Observation.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		String observationsXml = resourceToXmlString(
				getResourceList(ResourcesLiteralsUtil.Resource_Observation, paramMap), parser);

		/** Procedure **/
		paramMap.clear();
		paramMap.put(Procedure.SP_PATIENT, patientId);
		if (fromDate != null && fromDatePrefix != null)
			paramMap.put(Procedure.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(fromDatePrefix).name(), fromDate);
		if (toDate != null && toDatePrefix != null)
			paramMap.put(Procedure.SP_DATE + "_" + ParamPrefixEnum.forDstu1Value(toDatePrefix).name(), toDate);
		String procedure = resourceToXmlString(getResourceList(ResourcesLiteralsUtil.Resource_Procedure, paramMap),
				parser);

		StringBuffer finalCCDAInputXml = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		finalCCDAInputXml.append("\n<CCDAASFHIR>");

		finalCCDAInputXml.append("\n" + patientXml);

		if (AppUtil.isNotEmpty(allerygiesXml)) {
			finalCCDAInputXml.append(allerygiesXml);
		}

		if (AppUtil.isNotEmpty(medicationAdministrationsXml)) {
			finalCCDAInputXml.append(medicationAdministrationsXml);
		}

		if (AppUtil.isNotEmpty(locationXml)) {
			finalCCDAInputXml.append(locationXml);
		}

		if (AppUtil.isNotEmpty(deviceXml)) {
			finalCCDAInputXml.append(deviceXml);
		}

		if (AppUtil.isNotEmpty(careTeamXml)) {
			finalCCDAInputXml.append(careTeamXml);
		}

		if (AppUtil.isNotEmpty(encounterXml)) {
			finalCCDAInputXml.append(encounterXml);
		}

		if (AppUtil.isNotEmpty(goalXml)) {
			finalCCDAInputXml.append(goalXml);
		}

		if (AppUtil.isNotEmpty(immunizationsXml)) {
			finalCCDAInputXml.append(immunizationsXml);
		}

		if (AppUtil.isNotEmpty(conditionsXml)) {
			finalCCDAInputXml.append(conditionsXml);
		}

		if (AppUtil.isNotEmpty(procedure)) {
			finalCCDAInputXml.append(procedure);
		}

		if (AppUtil.isNotEmpty(observationsXml)) {
			finalCCDAInputXml.append(observationsXml);
		}

		finalCCDAInputXml.append("\n</CCDAASFHIR>");

		String finaXML = finalCCDAInputXml.toString();

		// System.out.println("finaXML : Beofore removing namespace::" +
		// finaXML);
		try {

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Source xmlDoc1 = new StreamSource(new ByteArrayInputStream(finaXML.getBytes()));

			// removes xml namespaces from input xml
			Source namespaceXslDoc = new StreamSource(
					CCDAResponseInterceptor.class.getClassLoader().getResourceAsStream("RemoveXmlNamespaces.xslt"));
			Transformer namespaceTransformer = tFactory.newTransformer(namespaceXslDoc);
			namespaceTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			StringResult srnameSpace = new StringResult();
			namespaceTransformer.transform(xmlDoc1, srnameSpace);
			finaXML = srnameSpace.toString();
			namespaceTransformer = null;

			// System.out.println("finaXML : After removing namespace::" +
			// finaXML);
			// XML Transformation
			Source xslDoc = new StreamSource(
					CCDAResponseInterceptor.class.getClassLoader().getResourceAsStream("CANTATA_Create_CDA.xslt"));

			xmlDoc1 = new StreamSource(new ByteArrayInputStream(finaXML.getBytes()));

			Transformer ccdaTransformer = tFactory.newTransformer(xslDoc);
			ccdaTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			StringResult sr = new StringResult();
			ccdaTransformer.transform(xmlDoc1, sr);
			ccdaTransformer = null;

			/** write response to client stream **/
			// theServletResponse.getWriter().write(finaXML);
			System.out.println("finaXML: " + finaXML);
			theServletResponse.getWriter().write(sr.toString());

			// System.out.println("CCDA processing END: " + new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private <T> List<T> getResourceList(String resourceName, Map<String, String> paramMap) {
		return ccdaService.getResourceByParamMap(resourceName, paramMap);
	}

	private <T extends IBaseResource> String resourceToXmlString(List<IBaseResource> resourceList, IParser p) {

		String resourceXml = "";

		if (resourceList == null)
			return resourceXml;

		Iterator<IBaseResource> itr = resourceList.iterator();
		while (itr.hasNext()) {
			resourceXml += "\n" + p.encodeResourceToString(itr.next());
		}

		return resourceXml;
	}

	private <T extends IBaseResource> String getResourceXMLAsString(String patientId, Class<T> entityClass, IParser p) {

		/** AllergyIntolerance **/
		Bundle results = client.search().forResource(entityClass).where(AllergyIntolerance.PATIENT.hasId(patientId))
				.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class).execute();
		Iterator<BundleEntryComponent> itr = results.getEntry().iterator();
		String allerygies = "";
		while (itr.hasNext()) {
			BundleEntryComponent comp = itr.next();
			Resource allergyInt = comp.getResource();
			allerygies += p.encodeResourceToString(allergyInt);
		}

		return allerygies;
	}

	private <T extends IBaseResource> String getConditionalResourceXMLAsString(String patientId, Class<T> entityClass,
			IParser p, IDateCriterion fromDateCrit, IDateCriterion toDateCrit) {

		Bundle results = null;
		String resourceXml = "";

		ReferenceClientParam refParam = new ReferenceClientParam("patient");

		// IDateCriterion fromDateCrit = getDateCriterion(fromDate,
		// fromDatePrefix, dateParamName, true);
		// IDateCriterion toDateCrit = getDateCriterion(fromDate,
		// fromDatePrefix, dateParamName, false);

		// if("AllergyIntolerance".equals(entityClass.getCanonicalName()))
		System.out.println("entityClass.getCanonicalName()" + entityClass.getSimpleName());

		if (fromDateCrit != null && toDateCrit != null) {
			results = client.search().forResource(entityClass).where(refParam.hasId(patientId)).and(fromDateCrit)
					.and(toDateCrit).include(AllergyIntolerance.INCLUDE_RECORDER)
					.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class).execute();

		} else if (fromDateCrit != null) {
			results = client.search().forResource(entityClass).where(refParam.hasId(patientId)).and(fromDateCrit)
					.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class).execute();
		} else if (toDateCrit != null) {
			results = client.search().forResource(entityClass).where(refParam.hasId(patientId)).and(toDateCrit)
					.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class).execute();
		} else {
			resourceXml = resourceXml + "\n" + getResourceXMLAsString(patientId, entityClass, p);
		}

		if (results != null) {
			Iterator<BundleEntryComponent> itr = results.getEntry().iterator();
			while (itr.hasNext()) {
				BundleEntryComponent comp = itr.next();
				Resource allergyInt = comp.getResource();
				resourceXml = resourceXml + "\n" + p.encodeResourceToString(allergyInt);
			}
		}

		return resourceXml;
	}

	private IDateCriterion getDateCriterion(String date, String datePrefix, String dateParamName, boolean isFromDate) {

		DateClientParam dateParam = new DateClientParam(dateParamName);
		IDateCriterion fromDateCrit = null;
		IDateCriterion toDateCrit = null;

		if (isFromDate) {
			if (date != null) {
				if (ParamPrefixEnum.GREATERTHAN.getDstu1Value().equals(datePrefix)) {
					fromDateCrit = dateParam.after().day(date);
				}
				if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS.getDstu1Value().equals(datePrefix)) {
					fromDateCrit = dateParam.afterOrEquals().day(date);
				}
			}
		} else {
			if (date != null) {
				if (ParamPrefixEnum.LESSTHAN.getDstu1Value().equals(datePrefix)) {
					toDateCrit = dateParam.before().day(date);
				}
				if (ParamPrefixEnum.LESSTHAN_OR_EQUALS.getDstu1Value().equals(datePrefix)) {
					toDateCrit = dateParam.beforeOrEquals().day(date);
				}
			}
		}
		return fromDateCrit;

	}
}
