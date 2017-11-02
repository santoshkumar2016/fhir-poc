package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.CareTeamDAO;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
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

		CareTeam careTeam = new CareTeam();

		CareTeamDbEntity careTeamDbEntity = CareTeamDAO.findCareTeamById(id);

		if (careTeamDbEntity == null)
			return careTeam;
		/**Mapping base components of careTeam*/
		else{
			careTeam = CareTeamMapper.getCareTeamBaseComponents(careTeam,careTeamDbEntity);
		}
		
		Set<CareTeamMapDbEntity> cTMapDbMapEntset = careTeamDbEntity.getCareTeamMapDbEntities();
		Iterator<CareTeamMapDbEntity> cTMapDbSetItr = cTMapDbMapEntset.iterator();
		while (cTMapDbSetItr.hasNext()) {
			CareTeamMapDbEntity careTeamMapDbEntity = cTMapDbSetItr.next();

			careTeam = CareTeamMapper.dbModelToResource(careTeamMapDbEntity, careTeam);
		}

		return careTeam;
	}

	public List<CareTeam> findCareTeamByParamMap(Map<String, String> paramMap) {

		List<CareTeamDbEntity> ctMapDbEntityList = CareTeamDAO.findCareTeamByParamMap(paramMap);

		/*List<CareTeam> CareTeamList = CareTeamMapper.dbModelToResource(patMapDbEntityList);*/

		List<CareTeam> careTeamList = null;
		
		if(!ctMapDbEntityList.isEmpty()) {
		Iterator<CareTeamDbEntity> ctMapDbSetItr = ctMapDbEntityList.iterator();
		while(ctMapDbSetItr.hasNext())
			if(careTeamList == null)
				careTeamList= CareTeamMapper.dbModelToResource(ctMapDbSetItr.next().getCareTeamMapDbEntities());
			else
				careTeamList.addAll(CareTeamMapper.dbModelToResource(ctMapDbSetItr.next().getCareTeamMapDbEntities()));
		}
		
		return careTeamList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findCareTeamById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findCareTeamByParamMap(paramMap);
	}
}
