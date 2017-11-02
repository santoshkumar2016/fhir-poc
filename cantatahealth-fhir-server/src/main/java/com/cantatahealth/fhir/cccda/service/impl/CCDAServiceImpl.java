package com.cantatahealth.fhir.cccda.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.cccda.service.CCDAService;
import com.cantatahealth.fhir.service.AllergyIntoleranceService;
import com.cantatahealth.fhir.service.CareTeamService;
import com.cantatahealth.fhir.service.ConditionService;
import com.cantatahealth.fhir.service.DeviceService;
import com.cantatahealth.fhir.service.EncounterService;
import com.cantatahealth.fhir.service.FHIRService;
import com.cantatahealth.fhir.service.GoalService;
import com.cantatahealth.fhir.service.ImmunizationService;
import com.cantatahealth.fhir.service.LocationService;
import com.cantatahealth.fhir.service.MedicationAdministrationService;
import com.cantatahealth.fhir.service.ObservationService;
import com.cantatahealth.fhir.service.PatientService;
import com.cantatahealth.fhir.service.ProcedureService;

@Service
@Transactional
public class CCDAServiceImpl implements CCDAService {

	@Autowired
	private PatientService patientService;
	
	@Autowired
	private AllergyIntoleranceService allergyIntoleranceService;
	

	@Autowired
	private MedicationAdministrationService medicationAdministrationService;
	
	@Autowired
	private ConditionService conditionService;
	
	@Autowired
	private ImmunizationService immunizationService;
	
	@Autowired
	private GoalService goalService;
	
	@Autowired
	private CareTeamService careTeamService;
	
	@Autowired
	private EncounterService encounterService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private ProcedureService procedureService;
	
	@Autowired
	private ObservationService observationService;
	
	@Autowired
	private DeviceService deviceService;
	
	
	@Override
	public <T> List<T> getResourceByParamMap(String resourceName, Map<String, String> paramMap) {
		
		FHIRService service = getServiceByResourceName(resourceName);
		
		return service.findResourceByParamMap(paramMap);
		
	}

	private FHIRService getServiceByResourceName(String resourceName) {
		
		FHIRService service = null;
		switch(resourceName){
	
		case "Patient" : return patientService;
		
		case "AllergyIntolerance" : return allergyIntoleranceService;
		
		case "MedicationAdministration" : return medicationAdministrationService;
		
		case "Condition" : return conditionService;
		
		case "CareTeam" : return careTeamService;
		
		case "Immunization" : return immunizationService;
		
		case "Goal" : return goalService;
		
		case "Encounter" : return encounterService;
		
		case "Location" : return locationService;
		
		case "Procedure" : return procedureService;
		
		case "Observation" : return observationService;
		
		case "Device" : return deviceService;
		
		default : System.out.println("Resource not supported::" + resourceName);
		}
		
		return service;
		
	}

	@Override
	public <T> T getResourceById(String resourceName, Long id) {
		FHIRService service = getServiceByResourceName(resourceName);
		return service.findResourceById(Long.valueOf(id));
	}
	
	
	
	
	
}
