/**
 * 
 */
package com.cantatahealth.fhir.db.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.dao.DeviceDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * @author santosh
 *
 */
@Repository
public class DeviceDAOImpl implements DeviceDAO {

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.DeviceDAO#findDeviceById(java.lang.Long)
	 */
	@Override
	public DummyDbEntity findDeviceById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cantatahealth.fhir.db.dao.DeviceDAO#findPDeviceByParamMap(java.util.Map)
	 */
	@Override
	public List<DummyDbEntity> findDeviceByParamMap(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
