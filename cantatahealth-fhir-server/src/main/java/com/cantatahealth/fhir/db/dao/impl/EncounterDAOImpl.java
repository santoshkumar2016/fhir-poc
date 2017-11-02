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
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Procedure;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.EncounterDAO;
import com.cantatahealth.fhir.db.dao.ProcedureDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.EncounterDbEntity;
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
public class EncounterDAOImpl implements EncounterDAO {

	
	Logger logger = Logger.getLogger(EncounterDAOImpl.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ProcedureDAO#findProcedureById(java.lang.Long)
	 */
	@Override
	public EncounterDbEntity findEncounterById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(EncounterDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ProcedureDAO#findPProcedureByParamMap(java.util.Map)
	 */
	@Override
	public List<EncounterDbEntity> findEncounterByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		 List<EncounterDbEntity> results = new ArrayList<EncounterDbEntity>();
			CriteriaQuery<EncounterDbEntity> prMapQuery = builder.createQuery(EncounterDbEntity.class);
			Root<EncounterDbEntity> prmapRoot = prMapQuery.from(EncounterDbEntity.class);			
			
			
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (Encounter.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<EncounterDbEntity, PatientDbEntity> pmapNameJoin = prmapRoot.join("patientDbEntity");
					Predicate p = builder.equal(pmapNameJoin.get("id"), paramValue);
					predicateList.add(p);
				}
				
				if(paramKey.contains(Encounter.SP_DATE)) {
					Join<EncounterDbEntity, PeriodDbEntity> emapPeriodJoin = prmapRoot.join("periodDbEntity");
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
					Predicate p = hibUtil.getPredicate(paramKey, paramDate, "start", "endAlias", builder, emapPeriodJoin);
					if(p != null)
						predicateList.add(p);
					}	
			}	
			
			prMapQuery.select(prmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
			
			results = hibUtil.findByParamMap(prMapQuery);
			return  results;
	}

}
