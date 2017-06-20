package com.nalashaa.fhir.server.acl.service;

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.dstu2.resource.Immunization;

public interface ImmunizationtService {

	Integer saveImmunization(Immunization theImmunization);

	List<Immunization> findImmunizationByParamMap(Map<String, String> paramMap);

	Immunization findImmunizationById(int intValue);

	void deleteImmunizationById(int intValue);

	void updateImmunization(Immunization theImmunization);

}
