/**
 * 
 */
package com.cantatahealth.fhir.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.AllergyIntoleranceDAO;
import com.cantatahealth.fhir.db.dao.PatientDAO;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.model.ResourceDbEntity;
import com.cantatahealth.fhir.service.AllergyIntoleranceService;
import com.cantatahealth.fhir.service.util.AllergyIntoleranceResourceMapper;

/**
 * Service Implementation Class for Allergy 
 * @author somadutta
 *
 */
@Service
@Transactional
public class AllergyIntoleranceServiceImpl implements AllergyIntoleranceService {

	
	@Autowired
	AllergyIntoleranceDAO allergyIntoleranceDAO;

	@Autowired
	AllergyIntoleranceResourceMapper allergyIntoleranceResourceMapper;

	/**
	 * This method searches for Allergy Intolerance by ID.
	 */
	public AllergyIntolerance findAllergyIntoleranceById(Long id){

		AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

		AllergyIntoleranceDbEntity allergyIntoleranceDbEntity = allergyIntoleranceDAO.findAllergyIntoleranceById(id);
		
		if (allergyIntoleranceDbEntity == null)
			return allergyIntolerance;
		/**Mapping base components of AllergyIntolerance*/
		else {
			allergyIntolerance = allergyIntoleranceResourceMapper.getAllergyIntoleranceBaseComponents(allergyIntolerance,allergyIntoleranceDbEntity);
		}
		
		Set<AllergyIntoleranceMapDbEntity> alIMapDbMapEntset = allergyIntoleranceDbEntity.getAllergyIntoleranceMapDbEntities();
		
		if(!alIMapDbMapEntset.isEmpty())
		{
		Iterator<AllergyIntoleranceMapDbEntity> alIMapDbSetItr = alIMapDbMapEntset.iterator();
				
		while (alIMapDbSetItr.hasNext()) {
			AllergyIntoleranceMapDbEntity allergyIntoleranceMapDbEntity = alIMapDbSetItr.next();

			allergyIntolerance = allergyIntoleranceResourceMapper.dbModelToResource(allergyIntoleranceMapDbEntity, allergyIntolerance);

		}
		}
		return allergyIntolerance;

	}
	
	/**
	 *  service implementation method for fetching allergy intolerance by parameters
	 */
	public List<AllergyIntolerance> findAllergyIntoleranceByParamMap(Map<String, String> paramMap) {

		List<AllergyIntoleranceMapDbEntity> alIMapDbEntityList = allergyIntoleranceDAO.findAllergyIntoleranceByParamMap(paramMap);
		List<AllergyIntolerance> allergyIntoleranceList = null;
		if(!alIMapDbEntityList.isEmpty())
			allergyIntoleranceList = allergyIntoleranceResourceMapper.dbModelToResource(alIMapDbEntityList);

		return allergyIntoleranceList;
	}

	@Override
	public <T> T findResourceById(Long id) {
		// TODO Auto-generated method stub
		return (T) findAllergyIntoleranceById(id);
	}

	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List<T>)findAllergyIntoleranceByParamMap(paramMap);
	}
}
