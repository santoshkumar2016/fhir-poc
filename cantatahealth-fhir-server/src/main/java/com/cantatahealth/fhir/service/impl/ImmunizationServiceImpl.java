package com.cantatahealth.fhir.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Immunization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.ImmunizationDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.ImmunizationDbEntity;
import com.cantatahealth.fhir.db.model.ImmunizationMapDbEntity;
import com.cantatahealth.fhir.service.ImmunizationService;
import com.cantatahealth.fhir.service.util.ImmunizationResourceMapper;

/**
 * 
 * Implementation class for Immunization service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class ImmunizationServiceImpl implements ImmunizationService {

	@Autowired
	ImmunizationDAO ImmunizationDAO;

	@Autowired
	ImmunizationResourceMapper ImmunizationMapper;

	@Override
	public Immunization findImmunizationById(Long id) {

		Immunization immunization = new Immunization();

		ImmunizationDbEntity immunizationDbEntity = ImmunizationDAO.findImmunizationById(id);

		if (immunizationDbEntity == null)
			return immunization;
		else{
			immunization = ImmunizationMapper.getImmunizationBaseComponents(immunization,immunizationDbEntity);
		}
		
		Set<ImmunizationMapDbEntity> iMapDbMapEntset = immunizationDbEntity.getImmunizationMapDbEntities();
		Iterator<ImmunizationMapDbEntity> iMapDbSetItr = iMapDbMapEntset.iterator();
		while (iMapDbSetItr.hasNext()) {
			ImmunizationMapDbEntity immunizationMapDbEntity = iMapDbSetItr.next();
			immunization = ImmunizationMapper.dbModelToResource(immunizationMapDbEntity, immunization);
		}
		return immunization;

	}

	public List<Immunization> findImmunizationByParamMap(Map<String, String> paramMap) {

		List<ImmunizationDbEntity> immMapDbEntityList = ImmunizationDAO.findImmunizationByParamMap(paramMap);

		/*List<Immunization> ImmunizationList = ImmunizationMapper.dbModelToResource(patMapDbEntityList);*/
		
		List<Immunization> immunizationList = new ArrayList<Immunization>();
		
		if(!immMapDbEntityList.isEmpty()) {
		Iterator<ImmunizationDbEntity> ctMapDbSetItr = immMapDbEntityList.iterator();
		while(ctMapDbSetItr.hasNext())
			if(immunizationList == null)
				immunizationList= ImmunizationMapper.dbModelToResource(ctMapDbSetItr.next().getImmunizationMapDbEntities());
			else
				immunizationList.addAll(ImmunizationMapper.dbModelToResource(ctMapDbSetItr.next().getImmunizationMapDbEntities()));
		}
		
		return immunizationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findImmunizationById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findImmunizationByParamMap(paramMap);
	}
}
