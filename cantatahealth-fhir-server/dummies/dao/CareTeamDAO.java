package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for CareTeam resource
 * 
 * @author santosh
 *
 */
public interface CareTeamDAO {

	/**
	 * fetch CareTeam resource by id
	 *
	 * @param id
	 * @return
	 */
	DummyDbEntity findCareTeamById(Long id);

	/**
	 * search CareTeam resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DummyDbEntity> findCareTeamByParamMap(Map<String, String> paramMap);
	
}
