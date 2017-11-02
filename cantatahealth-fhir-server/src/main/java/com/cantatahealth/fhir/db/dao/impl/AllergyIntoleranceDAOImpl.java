/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Patient;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.AllergyIntoleranceDAO;
import com.cantatahealth.fhir.db.dao.PatientDAO;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;
import com.cantatahealth.fhir.db.model.PeriodDbEntity;
import com.cantatahealth.fhir.db.model.ResourceDbEntity;
import com.cantatahealth.fhir.db.util.DbQueries;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * DAO implementation class for AllergyIntolerance model
 * @author somadutta
 *
 */
@Repository
public class AllergyIntoleranceDAOImpl implements AllergyIntoleranceDAO {

	@Autowired
	FhirDbUtil hibUtil;
	
	Logger logger = Logger.getLogger(AllergyIntoleranceDAOImpl.class);

	@Override
	public AllergyIntoleranceDbEntity findAllergyIntoleranceById(Long id) {
		
		return hibUtil.fetchById(AllergyIntoleranceDbEntity.class, id);
	}
	
	@Override
	public ResourceDbEntity getAllergyIntoleranceResourceEntityByName(String resourceName) {
		
		ResourceDbEntity resDbEntity = hibUtil.executeSQLQuery(ResourcesLiteralsUtil.AllergyIntolerance_Query, ResourceDbEntity.class);
		return resDbEntity;
	}
	
	/**
	 * This method list of  AllergyIntoleranceDbEntities based on search parameters provided in URL.
	 */
	 @Override
	 public List<AllergyIntoleranceMapDbEntity> findAllergyIntoleranceByParamMap(Map<String, String>
	 paramMap) {
		 EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		 List<AllergyIntoleranceMapDbEntity> results = new ArrayList<AllergyIntoleranceMapDbEntity>();
			CriteriaQuery<AllergyIntoleranceMapDbEntity> aIMapQuery = builder.createQuery(AllergyIntoleranceMapDbEntity.class);
			Root<AllergyIntoleranceMapDbEntity> aImapRoot = aIMapQuery.from(AllergyIntoleranceMapDbEntity.class);

			Join<AllergyIntoleranceMapDbEntity, AllergyIntoleranceDbEntity> pmapAllergyIntoleranceJoin = aImapRoot.join("allergyIntoleranceDbEntity");
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (AllergyIntolerance.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<AllergyIntoleranceDbEntity, PatientDbEntity> pmapNameJoin = pmapAllergyIntoleranceJoin.join("patientDbEntity");
					predicateList.add(builder.equal(pmapNameJoin.get("id"), paramValue));
				}
				if(paramKey.contains(AllergyIntolerance.SP_ONSET)) {
					Join<AllergyIntoleranceDbEntity, PeriodDbEntity> aImapPeriodJoin = pmapAllergyIntoleranceJoin.join("periodDbEntity");
					Date paramDate = null;
					try {
						paramDate=AppUtil.formatToDateString(paramValue);		
					}
					catch (ParseException e)
					{
						e.printStackTrace();
						logger.error("Invalid date : " + paramValue);
						logger.error("Returning empty AllergyIntolerance list!!!");
						return results;
					}
					paramKey = paramKey.substring(paramKey.indexOf("_") + 1, paramKey.length());
					Predicate p = hibUtil.getPredicate(paramKey, paramDate, "start", "endAlias", builder, aImapPeriodJoin);
					if(p != null)
						predicateList.add(p);
					}	
				}
			
			aIMapQuery.select(aImapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
			
			results = hibUtil.findByParamMap(aIMapQuery);

			return  results;
		}
	 }
	

