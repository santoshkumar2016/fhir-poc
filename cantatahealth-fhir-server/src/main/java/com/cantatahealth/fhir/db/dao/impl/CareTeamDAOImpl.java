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
import org.hl7.fhir.dstu3.model.CareTeam;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.CareTeamDAO;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
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
public class CareTeamDAOImpl implements CareTeamDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.CareTeamDAO#findCareTeamById(java.lang.Long)
	 */
	Logger logger = Logger.getLogger(CareTeamDAOImpl.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	
	@Override
	public CareTeamDbEntity findCareTeamById(Long id) {
		// TODO Auto-generated method stub	
		return hibUtil.fetchById(CareTeamDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.CareTeamDAO#findPCareTeamByParamMap(java.util.Map)
	 */
	@Override
	public List<CareTeamDbEntity> findCareTeamByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		 List<CareTeamDbEntity> results = new ArrayList<CareTeamDbEntity>();
			CriteriaQuery<CareTeamDbEntity> ctMapQuery = builder.createQuery(CareTeamDbEntity.class);
			Root<CareTeamDbEntity> ctmapRoot = ctMapQuery.from(CareTeamDbEntity.class);			
			
			
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (CareTeam.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<CareTeamDbEntity, PatientDbEntity> pmapNameJoin = ctmapRoot.join("patientDbEntity");
					Predicate p = builder.equal(pmapNameJoin.get("id"), paramValue);
					predicateList.add(p);
				}
				
				if(paramKey.contains(CareTeam.SP_DATE)) {
					Join<CareTeamDbEntity, PeriodDbEntity> ctmapPeriodJoin = ctmapRoot.join("periodDbEntity");
					Date paramDate = null;
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
					Predicate p = hibUtil.getPredicate(paramKey, paramDate, "start", "endAlias", builder, ctmapPeriodJoin);
					if(p != null)
						predicateList.add(p);
					}	
			}	
			
			ctMapQuery.select(ctmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
			
			results = hibUtil.findByParamMap(ctMapQuery);
			
			return  results;
			}

	}
