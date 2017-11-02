package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Procedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.EncounterDAO;
import com.cantatahealth.fhir.db.dao.ProcedureDAO;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.EncounterDbEntity;
import com.cantatahealth.fhir.db.model.EncounterMapDbEntity;
import com.cantatahealth.fhir.db.model.GoalDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureMapDbEntity;
import com.cantatahealth.fhir.service.EncounterService;
import com.cantatahealth.fhir.service.ProcedureService;
import com.cantatahealth.fhir.service.util.EncounterResourceMapper;
import com.cantatahealth.fhir.service.util.ProcedureResourceMapper;

/**
 * 
 * Implementation class for Procedure service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class EncounterServiceImpl implements EncounterService {

	@Autowired
	EncounterDAO encounterDAO;

	@Autowired
	EncounterResourceMapper encounterMapper;

	@Override
	public Encounter findEncounterById(Long id) {

		Encounter encounter = new Encounter();

		EncounterDbEntity encounterDbEntity = encounterDAO.findEncounterById(id);

		if (encounterDbEntity == null)
			return encounter;
		else{
			encounter = encounterMapper.getEncounterBaseComponents(encounter,encounterDbEntity);
		}
		
		Set<EncounterMapDbEntity> enMapDbMapEntset = encounterDbEntity.getEncounterMapDbEntities();
		Iterator<EncounterMapDbEntity> enMapDbSetItr = enMapDbMapEntset.iterator();
		while (enMapDbSetItr.hasNext()) {
			EncounterMapDbEntity encounterMapDbEntity = enMapDbSetItr.next();

			encounter = encounterMapper.dbModelToResource(encounterMapDbEntity, encounter);
		}
//		Procedure = ProcedureMapper.dbModelToResource(procedureDbEntity, Procedure);
		return encounter;

	}

	public List<Encounter> findEncounterByParamMap(Map<String, String> paramMap) {

		List<EncounterDbEntity> enMapDbEntityList = encounterDAO.findEncounterByParamMap(paramMap);
		List<Encounter> encounterList = null;
		
		if(!enMapDbEntityList.isEmpty()) {
		Iterator<EncounterDbEntity> enMapDbSetItr = enMapDbEntityList.iterator();
		while(enMapDbSetItr.hasNext())
			if(encounterList == null)
				encounterList= encounterMapper.dbModelToResource(enMapDbSetItr.next().getEncounterMapDbEntities());
			else
				encounterList.addAll(encounterMapper.dbModelToResource(enMapDbSetItr.next().getEncounterMapDbEntities()));
		}
		
//		List<Procedure> ProcedureList = ProcedureMapper.dbModelToResource(prMapDbEntityList);

		return encounterList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findEncounterById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findEncounterByParamMap(paramMap);
	}
}
