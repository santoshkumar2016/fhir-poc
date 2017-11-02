package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.MedicationAdministrationDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationMapDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.service.MedicationAdministrationService;
import com.cantatahealth.fhir.service.util.MedicationAdministrationResourceMapper;

/**
 * 
 * Implementation class for MedicationAdministration services
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class MedicationAdministrationServiceImpl implements MedicationAdministrationService {

	@Autowired
	MedicationAdministrationDAO medicationAdministrationDAO;

	@Autowired
	MedicationAdministrationResourceMapper medicationMapper;
	
	/**
	 * 
	 */
	@Override
	public MedicationAdministration findMedicationAdministrationById(Long id) {

		MedicationAdministration medication = new MedicationAdministration();

		MedicationAdministrationDbEntity medicationDbEntity = medicationAdministrationDAO.findMedicationAdministrationById(id);

		if (medicationDbEntity == null)
			return medication;
		/**Mapping base components of AllergyIntolerance*/
		else{	
			medication = medicationMapper.getMedicationAdministrationBaseComponents(medication,medicationDbEntity);
		}	
		
		Set<MedicationAdministrationMapDbEntity> maMapDbMapEntset = medicationDbEntity.getMedicationAdministrationMapDbEntities();		
		if(!maMapDbMapEntset.isEmpty())
		{
		Iterator<MedicationAdministrationMapDbEntity> maMapDbSetItr = maMapDbMapEntset.iterator();
		while (maMapDbSetItr.hasNext()) {
			MedicationAdministrationMapDbEntity medicationAdministrationMapDbEntity = maMapDbSetItr.next();
			medication = medicationMapper.dbModelToResource(medicationAdministrationMapDbEntity, medication);
			}
		}
		
		
		return medication;

	}

	public List<MedicationAdministration> findMedicationAdministrationByParamMap(Map<String, String> paramMap) {

		List<MedicationAdministrationDbEntity> mADbEntityList = medicationAdministrationDAO.findMedicationAdministrationByParamMap(paramMap);
		List<MedicationAdministration> medicationList = null;
		
		if(!mADbEntityList.isEmpty()) {
		Iterator<MedicationAdministrationDbEntity> maMapDbSetItr = mADbEntityList.iterator();
		while(maMapDbSetItr.hasNext())
			if(medicationList == null)
				medicationList= medicationMapper.dbModelToResource(maMapDbSetItr.next().getMedicationAdministrationMapDbEntities());
			else
				medicationList.addAll(medicationMapper.dbModelToResource(maMapDbSetItr.next().getMedicationAdministrationMapDbEntities()));
		}
		return medicationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findMedicationAdministrationById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findMedicationAdministrationByParamMap(paramMap);
	}
}
