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
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.MedicationAdministrationDAO;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationDbEntity;
import com.cantatahealth.fhir.db.model.MedicationAdministrationMapDbEntity;
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
public class MedicationAdministrationDAOImpl implements MedicationAdministrationDAO {
	
	Logger logger = Logger.getLogger(MedicationAdministrationDAOImpl.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.MedicationAdministrationDAO#findMedicationById(java.lang.Long)
	 */
	@Override
	public MedicationAdministrationDbEntity findMedicationAdministrationById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(MedicationAdministrationDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.MedicationAdministrationDAO#findPMedicationByParamMap(java.util.Map)
	 */
	/**
	 * This method list of  MedicationAdministrationDbEntities based on search parameters provided in URL.
	 */
	@Override
	public List<MedicationAdministrationDbEntity> findMedicationAdministrationByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		 EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();

			CriteriaQuery<MedicationAdministrationDbEntity> mAMapQuery = builder.createQuery(MedicationAdministrationDbEntity.class);
			Root<MedicationAdministrationDbEntity> mAmapRoot = mAMapQuery.from(MedicationAdministrationDbEntity.class);			
			List<MedicationAdministrationDbEntity> results = new ArrayList<MedicationAdministrationDbEntity>();
			
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			List<Predicate> predicateList = new ArrayList<Predicate>();
			
			/*Date startDateInclude = null;
			Date startDateExclude = null;
			Date endDateInclude = null;
			Date endDateExclude = null;*/

			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (MedicationAdministration.SP_PATIENT.equalsIgnoreCase(paramKey)) {
					Join<AllergyIntoleranceDbEntity, PatientDbEntity> pmapNameJoin = mAmapRoot.join("patientDbEntity");
					Predicate p = builder.equal(pmapNameJoin.get("id"), paramValue);
					predicateList.add(p);
				}
			
				if(paramKey.contains(MedicationAdministration.SP_EFFECTIVE_TIME)) {
					Join<MedicationAdministrationDbEntity, PeriodDbEntity> mAmapPeriodJoin = mAmapRoot.join("periodDbEntity");
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
					Predicate p = hibUtil.getPredicate(paramKey, paramDate, "start", "endAlias", builder, mAmapPeriodJoin);
					if(p != null)
						predicateList.add(p);
					}
			}

			mAMapQuery.select(mAmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
			
			results = hibUtil.findByParamMap(mAMapQuery);
			
			return  results;
	}
	
	


}
