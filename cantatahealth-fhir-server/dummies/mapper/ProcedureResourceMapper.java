package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Procedure data to Procedure resource
 * 
 * @author santosh
 *
 */
@Component
public class ProcedureResourceMapper {

	Logger logger = LoggerFactory.getLogger(ProcedureResourceMapper.class);

	/**
	 * 
	 * Convert Procedure db entity to Procedure resource
	 * 
	 * @param patientMapDbEntity
	 * @param Procedure
	 * @return
	 */
	public Procedure dbModelToResource(DummyDbEntity patientMapDbEntity, Procedure Procedure) {

		return Procedure;
	}

	/**
	 * 
	 * Convert list of Procedure db entities to list of Procedure resources
	 * 
	 * @param ProcedureDbEntityList
	 * @return
	 */
	public List<Procedure> dbModelToResource(List<DummyDbEntity> ProcedureDbEntityList) {

		Map<String, Procedure> ProcedureMap = new HashMap<>();

		List<Procedure> ProcedureList = new ArrayList<Procedure>(ProcedureMap.values());

		return ProcedureList;
	}

}
