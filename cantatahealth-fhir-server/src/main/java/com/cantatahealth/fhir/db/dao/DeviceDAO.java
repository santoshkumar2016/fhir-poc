package com.cantatahealth.fhir.db.dao;

import java.util.List;
import java.util.Map;

import com.cantatahealth.fhir.db.model.DeviceDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.db.model.PatientMapDbEntity;

/**
 * DAO class for Device resource
 * 
 * @author santosh
 *
 */
public interface DeviceDAO {

	/**
	 * fetch Device resource by id
	 *
	 * @param id
	 * @return
	 */
	DeviceDbEntity findDeviceById(Long id);

	/**
	 * search Device resource
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DeviceDbEntity> findDeviceByParamMap(Map<String, String> paramMap);
	
}
