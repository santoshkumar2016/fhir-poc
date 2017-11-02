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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.ConditionDAO;
import com.cantatahealth.fhir.db.model.ConditionDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;

/**
 * @author santosh
 *
 */
@Repository
public class ConditionDAOImpl implements ConditionDAO {

	Logger logger = LoggerFactory.getLogger(ConditionDAOImpl.class);

	@Autowired
	FhirDbUtil hibUtil;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cantatahealth.fhir.db.dao.ConditionDAO#findConditionById(java.lang.
	 * Long)
	 */
	@Override
	public ConditionDbEntity findConditionById(Long id) {
		return hibUtil.fetchById(ConditionDbEntity.class, id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cantatahealth.fhir.db.dao.ConditionDAO#findPConditionByParamMap(java.
	 * util.Map)
	 */
	@Override
	public List<ConditionDbEntity> findConditionByParamMap(Map<String, String> paramMap) {

		List<ConditionDbEntity> conditionDbEntList = new ArrayList<ConditionDbEntity>();

		EntityManager myEntityManager = hibUtil.getEntityManager();
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();

		CriteriaQuery<ConditionDbEntity> conditionQuery = builder.createQuery(ConditionDbEntity.class);
		Root<ConditionDbEntity> conditionRoot = conditionQuery.from(ConditionDbEntity.class);
		Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
		List<Predicate> predicateList = new ArrayList<Predicate>();
		while (paramMapKeyItr.hasNext()) {
			String paramKey = paramMapKeyItr.next();
			String paramValue = paramMap.get(paramKey);

			Date paramDateAbate = null,paramDateOnset = null;
			if (paramKey.contains(Condition.SP_ABATEMENT_DATE)) {
				try {
					paramDateAbate = AppUtil.formatToDateString(paramValue);
					String rangePrefixAbatement = paramKey.substring(paramKey.indexOf("_") + 1, paramKey.length());
					Predicate pabate = hibUtil.getPredicate(rangePrefixAbatement, paramDateAbate, "abatementdatetime", builder, conditionRoot);
					if (pabate != null)
						predicateList.add(pabate);
				} catch (ParseException e) {
					logger.error("Invalid date : " + paramValue);
					logger.error("Returning empty condition list!!!");
					return conditionDbEntList;
				}
			}
			
			if (paramKey.contains(Condition.SP_ONSET_DATE)) {
				try {
					paramDateOnset = AppUtil.formatToDateString(paramValue);
					String rangePrefixOnset = paramKey.substring(paramKey.indexOf("_") + 1, paramKey.length());
					Predicate ponset = hibUtil.getPredicate(rangePrefixOnset, paramDateOnset, "onsetdatetime", builder, conditionRoot);
					if(ponset != null)
						predicateList.add(ponset);
				} catch (ParseException e) {
					logger.error("Invalid date : " + paramValue);
					logger.error("Returning empty condition list!!!");
					return conditionDbEntList;
				}
			}
				
			if ((Condition.SP_PATIENT).equalsIgnoreCase(paramKey)) {
				predicateList.add(builder.equal(conditionRoot.get("patientDbEntity").get("id"), paramValue));
			}
		}
		conditionQuery.select(conditionRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));

		conditionDbEntList = hibUtil.findByParamMap(conditionQuery);

		return conditionDbEntList;
	}

}
