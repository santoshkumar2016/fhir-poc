package com.nalashaa.fhir.server.acl.dao;

import java.util.List;
import java.util.Map;

import com.nalashaa.fhir.server.acl.db.model.ImmunizationEn;

import ca.uhn.fhir.model.dstu2.resource.Immunization;

public interface ImmunizationDAO {

	Integer saveImmunization(ImmunizationEn theImmunization);

	List<ImmunizationEn> findImmunizationByParamMap(Map<String, String> paramMap);

	ImmunizationEn findImmunizationById(int intValue);

	void deleteImmunizationById(int intValue);

	void updateImmunization(ImmunizationEn theImmunization);

}
