/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.DeviceDAO;
import com.cantatahealth.fhir.db.model.ConditionDbEntity;
import com.cantatahealth.fhir.db.model.DeviceDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;

/**
 * @author santosh
 *
 */
@Repository
public class DeviceDAOImpl implements DeviceDAO {

	@Autowired
	FhirDbUtil hibUtil;
	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.DeviceDAO#findDeviceById(java.lang.Long)
	 */
	@Override
	public DeviceDbEntity findDeviceById(Long id) {
		// TODO Auto-generated method stub
		return hibUtil.fetchById(DeviceDbEntity.class, id);
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.DeviceDAO#findPDeviceByParamMap(java.util.Map)
	 */
	@Override
	public List<DeviceDbEntity> findDeviceByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		List<DeviceDbEntity> deviceDbEntList = new ArrayList<DeviceDbEntity>();

		EntityManager myEntityManager = hibUtil.getEntityManager();
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();

		CriteriaQuery<DeviceDbEntity> deviceQuery = builder.createQuery(DeviceDbEntity.class);
		Root<DeviceDbEntity> deviceRoot = deviceQuery.from(DeviceDbEntity.class);
		Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
		List<Predicate> predicateList = new ArrayList<Predicate>();
		while (paramMapKeyItr.hasNext()) {
			String paramKey = paramMapKeyItr.next();
			String paramValue = paramMap.get(paramKey);
			if ((Device.SP_PATIENT).equalsIgnoreCase(paramKey)) {
				predicateList.add(builder.equal(deviceRoot.get("patientDbEntity").get("id"), paramValue));
			}
		}
		deviceQuery.select(deviceRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));

		deviceDbEntList = hibUtil.findByParamMap(deviceQuery);

		return deviceDbEntList;

	}

}
