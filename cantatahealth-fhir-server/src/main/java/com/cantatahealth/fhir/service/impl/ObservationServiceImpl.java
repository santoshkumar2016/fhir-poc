package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.ObservationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationMapDbEntity;
import com.cantatahealth.fhir.db.model.ObservationDbEntity;
import com.cantatahealth.fhir.db.model.ObservationMapDbEntity;
import com.cantatahealth.fhir.service.ObservationService;
import com.cantatahealth.fhir.service.util.ObservationResourceMapper;

/**
 * 
 * Implementation class for Observation service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class ObservationServiceImpl implements ObservationService {

	@Autowired
	ObservationDAO ObservationDAO;

	@Autowired
	ObservationResourceMapper ObservationMapper;

	@Override
	public Observation findObservationById(Long id) {

		Observation observation = new Observation();

		ObservationDbEntity ObservationDbEntity = ObservationDAO.findObservationById(id);

		if (ObservationDbEntity == null)
			return observation;
		else{	
			observation = ObservationMapper.getObservationBaseComponents(observation,ObservationDbEntity);
		}	
		
		Set<ObservationMapDbEntity> obMapDbMapEntset = ObservationDbEntity.getObservationMapDbEntities();		
		if(!obMapDbMapEntset.isEmpty())
		{
		Iterator<ObservationMapDbEntity> obMapDbSetItr = obMapDbMapEntset.iterator();
		while (obMapDbSetItr.hasNext()) {
			ObservationMapDbEntity observationMapDbEntity = obMapDbSetItr.next();
			observation = ObservationMapper.dbModelToResource(observationMapDbEntity, observation);
			}
		}
		return observation;

	}

	public List<Observation> findObservationByParamMap(Map<String, String> paramMap) {

		List<ObservationDbEntity> obsMapDbEntityList = ObservationDAO.findObservationByParamMap(paramMap);
		List<Observation> observationList = null;
		
		if(!obsMapDbEntityList.isEmpty()) {
			Iterator<ObservationDbEntity> obsMapDbSetItr = obsMapDbEntityList.iterator();
			while(obsMapDbSetItr.hasNext()) {
				ObservationDbEntity obd = obsMapDbSetItr.next();
				if(observationList == null) {
					observationList= ObservationMapper.dbModelToResource(obd.getObservationMapDbEntities());
				continue;
				}
		
				if(obd.getObservationMapDbEntities().isEmpty()){
					Observation obs=ObservationMapper.getObservationBaseComponents(new Observation(), obd);
					observationList.add(obs);
				}
				else
					observationList.addAll(ObservationMapper.dbModelToResource(obd.getObservationMapDbEntities()));
			}
		}
//		List<Observation> ObservationList = ObservationMapper.dbModelToResource(obsMapDbEntityList);

		return observationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findObservationById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findObservationByParamMap(paramMap);
	}
}
