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
import org.hl7.fhir.dstu3.model.Location;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.LocationDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.LocationDbEntity;
import com.cantatahealth.fhir.db.model.LocationMapDbEntity;
import com.cantatahealth.fhir.db.model.OrganizationDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PeriodDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;

/**
 * @author santosh
 *
 */
@Repository
public class LocationDAOImpl implements LocationDAO {
	
	
	Logger logger = Logger.getLogger(LocationDAOImpl.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.LocationDAO#findLocationById(java.lang.Long)
	 */
	@Override
	public LocationDbEntity findLocationById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(LocationDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.LocationDAO#findPLocationByParamMap(java.util.Map)
	 */
	@Override
	public List<LocationDbEntity> findLocationByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		EntityManager myEntityManager = hibUtil.getEntityManager();
		 CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		 List<LocationDbEntity> results = new ArrayList<LocationDbEntity>();
			CriteriaQuery<LocationDbEntity> ctMapQuery = builder.createQuery(LocationDbEntity.class);
			Root<LocationDbEntity> locationmapRoot = ctMapQuery.from(LocationDbEntity.class);			
			List<Predicate> predicateList = new ArrayList<Predicate>();
			Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
			while (paramMapKeyItr.hasNext()) {
				String paramKey = paramMapKeyItr.next();
				String paramValue = paramMap.get(paramKey);

				if (Location.SP_ORGANIZATION.equalsIgnoreCase(paramKey)) {
					Join<LocationDbEntity, LocationMapDbEntity> locmapNameJoin = locationmapRoot.join("locationMapDbEntities");
					Join<LocationMapDbEntity, OrganizationDbEntity> orgmapNameJoin = locmapNameJoin.join("organizationDbEntity");
					Predicate p = builder.equal(orgmapNameJoin.get("id"), paramValue);
					predicateList.add(p);
				}
				
			}	
			
			ctMapQuery.select(locationmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
			
			results = hibUtil.findByParamMap(ctMapQuery);
			
			return  results;

	}

}
