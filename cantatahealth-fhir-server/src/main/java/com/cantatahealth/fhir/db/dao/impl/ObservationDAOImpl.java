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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.ObservationDAO;
import com.cantatahealth.fhir.db.model.CodingDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.EncounterDbEntity;
import com.cantatahealth.fhir.db.model.ObservationDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PeriodDbEntity;
import com.cantatahealth.fhir.db.model.ProcedureDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;

/**
 * @author santosh
 *
 */
@Repository
public class ObservationDAOImpl implements ObservationDAO {
	
	Logger logger = LoggerFactory.getLogger(ObservationDAOImpl.class);

	@Autowired
	FhirDbUtil hibUtil;

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ObservationDAO#findObservationById(java.lang.Long)
	 */
	@Override
	public ObservationDbEntity findObservationById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(ObservationDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ObservationDAO#findPObservationByParamMap(java.util.Map)
	 */
	@Override
	public List<ObservationDbEntity> findObservationByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		 List<ObservationDbEntity> results = new ArrayList<ObservationDbEntity>();
			CriteriaQuery<ObservationDbEntity> obsMapQuery = builder.createQuery(ObservationDbEntity.class);
			Root<ObservationDbEntity> obsmapRoot = obsMapQuery.from(ObservationDbEntity.class);			
			
//			Join<ObservationDbEntity, PeriodDbEntity> prmapPeriodJoin = prmapRoot.join("periodDbEntity");
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (Procedure.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<ObservationDbEntity, PatientDbEntity> pmapNameJoin = obsmapRoot.join("patientDbEntity");
					Predicate p = builder.equal(pmapNameJoin.get("id"), paramValue);
					predicateList.add(p);
				}
				
				if (Observation.SP_CATEGORY.equalsIgnoreCase(paramKey)) {
					Join<ObservationDbEntity, CodingDbEntity> obsmapNameJoin = obsmapRoot.join("codingDbEntityByCategory");
					Predicate p1 = builder.equal(obsmapNameJoin.get("code"), paramValue);
					predicateList.add(p1);
				}
				
				if(paramKey.contains(CareTeam.SP_DATE)) {
					Date paramDate = null;
					Join<ObservationDbEntity, PeriodDbEntity> obsPeriodJoin = obsmapRoot.join("periodDbEntity");
					try {
						paramDate=AppUtil.formatToDateString(paramValue);		
					}
					catch (ParseException e)
					{
						e.printStackTrace();
						logger.error("Invalid date : " + paramValue);
						logger.error("Returning empty CareTeam list!!!");
						return results;
					}
					paramKey = paramKey.substring(paramKey.indexOf("_") + 1, paramKey.length());
					Predicate p = hibUtil.getPredicate(paramKey, paramDate, "start", "endAlias", builder, obsPeriodJoin);
					if(p != null)
						predicateList.add(p);
					}
			}
			
			obsMapQuery.select(obsmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
			results = hibUtil.findByParamMap(obsMapQuery);
			
			System.out.println("results" + results.size());
		return results;
	}

}
