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
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Goal;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.GoalDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.GoalDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PeriodDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * @author santosh
 *
 */
@Repository
public class GoalDAOImpl implements GoalDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.GoalDAO#findGoalById(java.lang.Long)
	 */
	
	Logger logger = Logger.getLogger(GoalDAOImpl.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	
	@Override
	public GoalDbEntity findGoalById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(GoalDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.GoalDAO#findPGoalByParamMap(java.util.Map)
	 */
	@Override
	public List<GoalDbEntity> findGoalByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		 EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		 List<GoalDbEntity> results = new ArrayList<GoalDbEntity>();

			CriteriaQuery<GoalDbEntity> gMapQuery = builder.createQuery(GoalDbEntity.class);
			Root<GoalDbEntity> gmapRoot = gMapQuery.from(GoalDbEntity.class);			
			
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);
				

				if (Goal.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<GoalDbEntity, PatientDbEntity> pmapNameJoin = gmapRoot.join("patientDbEntity");
					Predicate p = builder.equal(pmapNameJoin.get("id"), paramValue);
					predicateList.add(p);
				}

				Date paramDate = null;
				if (paramKey.contains(Goal.SP_START_DATE)) {
					try {
						paramDate = AppUtil.formatToDateString(paramValue);
					} catch (ParseException e) {
						logger.error("Invalid date : " + paramValue);
						logger.error("Returning empty condition list!!!");
						return results;
					}
				}
				String rangePrefix = paramKey.substring(paramKey.indexOf("_") + 1, paramKey.length());
				Predicate p = hibUtil.getPredicate(rangePrefix, paramDate, "startdate", builder, gmapRoot);
				if (p != null)
					predicateList.add(p);
			
			}
			
			gMapQuery.select(gmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));

			results = hibUtil.findByParamMap(gMapQuery);

			return  results;

			
	}
}
