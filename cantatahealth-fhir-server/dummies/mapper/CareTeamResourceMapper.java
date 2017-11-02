package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps CareTeam data to CareTeam resource
 * 
 * @author santosh
 *
 */
@Component
public class CareTeamResourceMapper {

	Logger logger = LoggerFactory.getLogger(CareTeamResourceMapper.class);

	/**
	 * 
	 * Convert CareTeam db entity to CareTeam resource
	 * 
	 * @param patientMapDbEntity
	 * @param CareTeam
	 * @return
	 */
	public CareTeam dbModelToResource(DummyDbEntity patientMapDbEntity, CareTeam CareTeam) {

		return CareTeam;
	}

	/**
	 * 
	 * Convert list of CareTeam db entities to list of CareTeam resources
	 * 
	 * @param CareTeamDbEntityList
	 * @return
	 */
	public List<CareTeam> dbModelToResource(List<DummyDbEntity> CareTeamDbEntityList) {

		Map<String, CareTeam> CareTeamMap = new HashMap<>();

		List<CareTeam> CareTeamList = new ArrayList<CareTeam>(CareTeamMap.values());

		return CareTeamList;
	}

}
