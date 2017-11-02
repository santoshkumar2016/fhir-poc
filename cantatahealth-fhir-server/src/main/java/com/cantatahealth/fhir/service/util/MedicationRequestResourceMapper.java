package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps MedicationRequest data to MedicationRequest resource
 * 
 * @author santosh
 *
 */
@Component
public class MedicationRequestResourceMapper {

	Logger logger = LoggerFactory.getLogger(MedicationRequestResourceMapper.class);

	/**
	 * 
	 * Convert MedicationRequest db entity to MedicationRequest resource
	 * 
	 * @param patientMapDbEntity
	 * @param MedicationRequest
	 * @return
	 */
	public MedicationRequest dbModelToResource(DummyDbEntity patientMapDbEntity, MedicationRequest MedicationRequest) {

		return MedicationRequest;
	}

	/**
	 * 
	 * Convert list of MedicationRequest db entities to list of MedicationRequest resources
	 * 
	 * @param MedicationRequestDbEntityList
	 * @return
	 */
	public List<MedicationRequest> dbModelToResource(List<DummyDbEntity> MedicationRequestDbEntityList) {

		Map<String, MedicationRequest> MedicationRequestMap = new HashMap<>();

		List<MedicationRequest> MedicationRequestList = new ArrayList<MedicationRequest>(MedicationRequestMap.values());

		return MedicationRequestList;
	}

}
