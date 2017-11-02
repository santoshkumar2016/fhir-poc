package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.CareTeamDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.CareTeamService;
import com.cantatahealth.fhir.service.util.CareTeamResourceMapper;

/**
 * 
 * Implementation class for CareTeam service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class CareTeamServiceImpl implements CareTeamService {

	@Autowired
	CareTeamDAO CareTeamDAO;

	@Autowired
	CareTeamResourceMapper CareTeamMapper;

	@Override
	public CareTeam findCareTeamById(Long id) {

		CareTeam CareTeam = new CareTeam();

		DummyDbEntity CareTeamDbEntity = CareTeamDAO.findCareTeamById(id);

		if (CareTeamDbEntity == null)
			return CareTeam;

		CareTeam = CareTeamMapper.dbModelToResource(CareTeamDbEntity, CareTeam);

		return CareTeam;

	}

	public List<CareTeam> findCareTeamByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = CareTeamDAO.findCareTeamByParamMap(paramMap);

		List<CareTeam> CareTeamList = CareTeamMapper.dbModelToResource(patMapDbEntityList);

		return CareTeamList;
	}

}
