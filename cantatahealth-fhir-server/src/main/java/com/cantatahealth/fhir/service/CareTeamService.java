package com.cantatahealth.fhir.service;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Patient;
/**
 * 
 * CareTeam service interface
 * 
 * @author santosh
 *
 */
public interface CareTeamService extends FHIRService{
  
	CareTeam findCareTeamById(Long id);
	
	List<CareTeam> findCareTeamByParamMap(Map<String, String> paramMap);
	
}
