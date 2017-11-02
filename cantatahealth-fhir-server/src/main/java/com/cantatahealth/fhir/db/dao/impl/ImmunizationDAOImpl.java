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
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Immunization;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.ImmunizationDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.GoalDbEntity;
import com.cantatahealth.fhir.db.model.ImmunizationDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * @author santosh
 *
 */
@Repository
public class ImmunizationDAOImpl implements ImmunizationDAO {

	Logger logger = Logger.getLogger(ImmunizationDAOImpl.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ImmunizationDAO#findImmunizationById(java.lang.Long)
	 */
	@Override
	public ImmunizationDbEntity findImmunizationById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(ImmunizationDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ImmunizationDAO#findPImmunizationByParamMap(java.util.Map)
	 */
	@Override
	public List<ImmunizationDbEntity> findImmunizationByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		
		EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();

			CriteriaQuery<ImmunizationDbEntity> immMapQuery = builder.createQuery(ImmunizationDbEntity.class);
			Root<ImmunizationDbEntity> immapRoot = immMapQuery.from(ImmunizationDbEntity.class);			
			List<ImmunizationDbEntity> results = new ArrayList<ImmunizationDbEntity>();
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (AllergyIntolerance.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<ImmunizationDbEntity, PatientDbEntity> pmapNameJoin = immapRoot.join("patientDbEntity");
					predicateList.add(builder.equal(pmapNameJoin.get("id"), paramValue));
				}
				
				Date paramDate = null;
				if (paramKey.contains(Immunization.SP_DATE)) {
					try {
						paramDate = AppUtil.formatToDateString(paramValue);
					} catch (ParseException e) {
						logger.error("Invalid date : " + paramValue);
						logger.error("Returning empty Immunization list!!!");
						return results;
					}
				}
				String rangePrefix = paramKey.substring(paramKey.indexOf("_") + 1, paramKey.length());
				Predicate p = hibUtil.getPredicate(rangePrefix, paramDate, "date", builder, immapRoot);
				if (p != null)
					predicateList.add(p);
			
			}
	
			immMapQuery.select(immapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));

			results = hibUtil.findByParamMap(immMapQuery);

			return  results;
	}

}
