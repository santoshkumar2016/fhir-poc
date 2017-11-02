package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Medication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps medication data to medication resource
 * 
 * @author santosh
 *
 */
@Component
public class MedicationResourceMapper {

	Logger logger = LoggerFactory.getLogger(MedicationResourceMapper.class);

	/**
	 * 
	 * Convert medication db entity to medication resource
	 * 
	 * @param patientMapDbEntity
	 * @param medication
	 * @return
	 */
	public Medication dbModelToResource(DummyDbEntity patientMapDbEntity, Medication medication) {

		return medication;
	}

	/**
	 * 
	 * Convert list of medication db entities to list of medication resources
	 * 
	 * @param medicationDbEntityList
	 * @return
	 */
	public List<Medication> dbModelToResource(List<DummyDbEntity> medicationDbEntityList) {

		Map<String, Medication> medicationMap = new HashMap<>();

		List<Medication> medicationList = new ArrayList<Medication>(medicationMap.values());

		return medicationList;
	}

}
