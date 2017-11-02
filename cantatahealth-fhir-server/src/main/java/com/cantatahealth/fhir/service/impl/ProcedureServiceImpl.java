package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Procedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.ProcedureDAO;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.GoalDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureMapDbEntity;
import com.cantatahealth.fhir.service.ProcedureService;
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
public class ProcedureServiceImpl implements ProcedureService {

	@Autowired
	ProcedureDAO ProcedureDAO;

	@Autowired
	ProcedureResourceMapper ProcedureMapper;

	@Override
	public Procedure findProcedureById(Long id) {

		Procedure procedure = new Procedure();

		ProcedureDbEntity procedureDbEntity = ProcedureDAO.findProcedureById(id);

		if (procedureDbEntity == null)
			return procedure;
		else{
			procedure = ProcedureMapper.getProcedureBaseComponents(procedure,procedureDbEntity);
		}
		
		Set<ProcedureMapDbEntity> prMapDbMapEntset = procedureDbEntity.getProcedureMapDbEntities();
		Iterator<ProcedureMapDbEntity> prMapDbSetItr = prMapDbMapEntset.iterator();
		while (prMapDbSetItr.hasNext()) {
			ProcedureMapDbEntity procedureMapDbEntity = prMapDbSetItr.next();

			procedure = ProcedureMapper.dbModelToResource(procedureMapDbEntity, procedure);
		}
//		Procedure = ProcedureMapper.dbModelToResource(procedureDbEntity, Procedure);
		return procedure;

	}

	public List<Procedure> findProcedureByParamMap(Map<String, String> paramMap) {

		List<ProcedureDbEntity> prMapDbEntityList = ProcedureDAO.findProcedureByParamMap(paramMap);
		List<Procedure> procedureList = null;
		
		if(!prMapDbEntityList.isEmpty()) {
		Iterator<ProcedureDbEntity> prMapDbSetItr = prMapDbEntityList.iterator();
		while(prMapDbSetItr.hasNext())
			if(procedureList == null)
				procedureList= ProcedureMapper.dbModelToResource(prMapDbSetItr.next().getProcedureMapDbEntities());
			else
				procedureList.addAll(ProcedureMapper.dbModelToResource(prMapDbSetItr.next().getProcedureMapDbEntities()));
		}
		
//		List<Procedure> ProcedureList = ProcedureMapper.dbModelToResource(prMapDbEntityList);

		return procedureList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findProcedureById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findProcedureByParamMap(paramMap);
	}

}
