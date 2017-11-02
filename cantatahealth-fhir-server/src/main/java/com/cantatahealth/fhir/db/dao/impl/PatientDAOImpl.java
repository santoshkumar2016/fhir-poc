package com.cantatahealth.fhir.db.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.PatientDAO;
import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.CodingDbEntity;
import com.cantatahealth.fhir.db.model.ContactPointDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.IdentifierDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.model.ResourceDbEntity;
import com.cantatahealth.fhir.db.util.DbQueries;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;

@Repository
public class PatientDAOImpl implements PatientDAO {

	@Autowired
	FhirDbUtil hibUtil;

	public List<Object[]> getPatient(String query) {
		return hibUtil.executeSQLQuery(DbQueries.fullPatientQuery + query);
	}
	
	/**
	 * This method list of  PatientDbEntities based on search parameters provided in URL.
	 */
	@Override
	public List<PatientMapDbEntity> findPatientByParamMap(Map<String, String> paramMap) {

		EntityManager myEntityManager = hibUtil.getEntityManager();

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();

		CriteriaQuery<PatientMapDbEntity> patMapQuery = builder.createQuery(PatientMapDbEntity.class);
		Root<PatientMapDbEntity> pmapRoot = patMapQuery.from(PatientMapDbEntity.class);

		Join<PatientMapDbEntity, PatientDbEntity> pmapPatientJoin = pmapRoot.join("patientDbEntity");

		Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
		List<Predicate> predicateList = new ArrayList<Predicate>();

		while (paramMapKeyItr.hasNext()) {
			String paramKey = paramMapKeyItr.next();
			String paramValue = paramMap.get(paramKey);

			if (Patient.SP_FAMILY.equalsIgnoreCase(paramKey)) {
				Join<PatientMapDbEntity, HumanNameDbEntity> pmapNameJoin = pmapRoot.join("humanNameDbEntity");
				Predicate p = builder.equal(pmapNameJoin.get(Patient.SP_FAMILY), paramValue);
				predicateList.add(p);
			}

			if (Patient.SP_GIVEN.equalsIgnoreCase(paramKey)) {
				Join<PatientMapDbEntity, HumanNameDbEntity> patNameJoin1 = pmapRoot.join("humanNameDbEntity");
				Predicate p = builder.equal(patNameJoin1.get(Patient.SP_GIVEN), paramValue);
				predicateList.add(p);
			}

			if (Patient.SP_IDENTIFIER.equalsIgnoreCase(paramKey)) {
				Join<PatientMapDbEntity, IdentifierDbEntity> patIdentifierJoin = pmapRoot.join("identifierDbEntity");
				Predicate p = builder.equal(patIdentifierJoin.get("value"), paramValue);
				predicateList.add(p);
			}

			if (Patient.SP_GENDER.equalsIgnoreCase(paramKey)) {
				Join<PatientDbEntity, CodingDbEntity> patientCodingJoin = pmapPatientJoin.join("codingDbEntityByGender");				
				Predicate p = builder.equal(patientCodingJoin.get("code"), paramValue);
				predicateList.add(p);
			}

//			if (Patient.SP_BIRTHDATE.equalsIgnoreCase(paramKey)) {
//				Predicate p;
//				try {
//					p = builder.equal(pmapPatientJoin.get(Patient.SP_BIRTHDATE),
//							AppUtil.formatToDateString(paramValue));
//					predicateList.add(p);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
			
			if ((Patient.SP_BIRTHDATE+"_START").equalsIgnoreCase(paramKey)) {
				Predicate p;
				try {
					p = builder.greaterThanOrEqualTo(pmapPatientJoin.get(Patient.SP_BIRTHDATE),
							AppUtil.formatToDateString(paramValue));
					predicateList.add(p);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if ((Patient.SP_BIRTHDATE+"_END").equalsIgnoreCase(paramKey)) {
				Predicate p;
				try {
					p = builder.lessThanOrEqualTo(pmapPatientJoin.get(Patient.SP_BIRTHDATE),
							AppUtil.formatToDateString(paramValue));
					predicateList.add(p);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			
			if (Patient.SP_TELECOM.equalsIgnoreCase(paramKey)) {
				Join<PatientMapDbEntity, ContactPointDbEntity> patientContactPointJoin = pmapRoot.join("contactPointDbEntity");				
				Predicate p = builder.equal(patientContactPointJoin.get("value"), paramValue);
				predicateList.add(p);
			}

		}

		Predicate p1 = builder.equal(pmapPatientJoin.get("id"), pmapRoot.get("patientDbEntity"));
		predicateList.add(p1);

		patMapQuery.select(pmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));

		List<PatientMapDbEntity> patDbEntList = hibUtil.findByParamMap(patMapQuery);

		return patDbEntList;
	}

	@Override
	public PatientDbEntity findPatientById(Long id) {

		return hibUtil.fetchById(PatientDbEntity.class, id);
	}

	@Override
	public List<Object[]> getChildResources(String resourceName) {

		return hibUtil.getChildResources(resourceName);
	}

	@Override
	public List<HumanNameDbEntity> getHumanNames(Long resKey, Long id) {
		return hibUtil.getHumanNames(resKey, id);
	}

	@Override
	public List<AddressDbEntity> getAddress(Long resKey, Long id) {
		return hibUtil.getAddress(resKey, id);
	}

	@Override
	public ResourceDbEntity getPatientResourceEntityByName(String resourceName) {

		ResourceDbEntity resDbEntity = hibUtil.executeSQLQuery("Select * from Resource where resourcename = 'Patient'",
				ResourceDbEntity.class);
		return resDbEntity;
	}

	public List<Object[]> getChildResourcesByResourceKey(Long resourceKey) {
		List<Object[]> childResourceArraryList = hibUtil.getChildResourcesByResourceKey(resourceKey);

		return childResourceArraryList;
	}
}
