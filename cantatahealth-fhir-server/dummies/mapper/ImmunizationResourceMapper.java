package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Immunization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Immunization data to Immunization resource
 * 
 * @author santosh
 *
 */
@Component
public class ImmunizationResourceMapper {

	Logger logger = LoggerFactory.getLogger(ImmunizationResourceMapper.class);

	/**
	 * 
	 * Convert Immunization db entity to Immunization resource
	 * 
	 * @param patientMapDbEntity
	 * @param Immunization
	 * @return
	 */
	public Immunization dbModelToResource(DummyDbEntity patientMapDbEntity, Immunization Immunization) {

		return Immunization;
	}

	/**
	 * 
	 * Convert list of Immunization db entities to list of Immunization resources
	 * 
	 * @param ImmunizationDbEntityList
	 * @return
	 */
	public List<Immunization> dbModelToResource(List<DummyDbEntity> ImmunizationDbEntityList) {

		Map<String, Immunization> ImmunizationMap = new HashMap<>();

		List<Immunization> ImmunizationList = new ArrayList<Immunization>(ImmunizationMap.values());

		return ImmunizationList;
	}

}
