package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps MedicationStatement data to MedicationStatement resource
 * 
 * @author santosh
 *
 */
@Component
public class MedicationStatementResourceMapper {

	Logger logger = LoggerFactory.getLogger(MedicationStatementResourceMapper.class);

	/**
	 * 
	 * Convert MedicationStatement db entity to MedicationStatement resource
	 * 
	 * @param patientMapDbEntity
	 * @param MedicationStatement
	 * @return
	 */
	public MedicationStatement dbModelToResource(DummyDbEntity patientMapDbEntity, MedicationStatement MedicationStatement) {

		return MedicationStatement;
	}

	/**
	 * 
	 * Convert list of MedicationStatement db entities to list of MedicationStatement resources
	 * 
	 * @param MedicationStatementDbEntityList
	 * @return
	 */
	public List<MedicationStatement> dbModelToResource(List<DummyDbEntity> MedicationStatementDbEntityList) {

		Map<String, MedicationStatement> MedicationStatementMap = new HashMap<>();

		List<MedicationStatement> MedicationStatementList = new ArrayList<MedicationStatement>(MedicationStatementMap.values());

		return MedicationStatementList;
	}

}
