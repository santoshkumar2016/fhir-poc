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
import org.hl7.fhir.dstu3.model.Procedure;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
public class ProcedureDAOImpl implements ProcedureDAO {

	
	Logger logger = Logger.getLogger(ProcedureDAOImpl.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ProcedureDAO#findProcedureById(java.lang.Long)
	 */
	@Override
	public ProcedureDbEntity findProcedureById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(ProcedureDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.ProcedureDAO#findPProcedureByParamMap(java.util.Map)
	 */
	@Override
	public List<ProcedureDbEntity> findProcedureByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		 List<ProcedureDbEntity> results = new ArrayList<ProcedureDbEntity>();
			CriteriaQuery<ProcedureDbEntity> prMapQuery = builder.createQuery(ProcedureDbEntity.class);
			Root<ProcedureDbEntity> prmapRoot = prMapQuery.from(ProcedureDbEntity.class);			
			
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (Procedure.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<ProcedureDbEntity, PatientDbEntity> pmapNameJoin = prmapRoot.join("patientDbEntity");
					Predicate p = builder.equal(pmapNameJoin.get("id"), paramValue);
					predicateList.add(p);
				}
				
				if (Procedure.SP_CONTEXT.equalsIgnoreCase(paramKey)) {
					Join<ProcedureDbEntity, EncounterDbEntity> enmapNameJoin = prmapRoot.join("encounterDbEntity");
					Predicate p1 = builder.equal(enmapNameJoin.get("id"), paramValue);
					predicateList.add(p1);
				}
				
				if(paramKey.contains(Procedure.SP_DATE)) {
					Join<ProcedureDbEntity, PeriodDbEntity> prmapPeriodJoin = prmapRoot.join("periodDbEntity");
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
					Predicate p = hibUtil.getPredicate(paramKey, paramDate, "start", "endAlias", builder, prmapPeriodJoin);
					if(p != null)
						predicateList.add(p);
					}	
			}	
			
			prMapQuery.select(prmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
			
			results = hibUtil.findByParamMap(prMapQuery);
			return  results;
	}

}
