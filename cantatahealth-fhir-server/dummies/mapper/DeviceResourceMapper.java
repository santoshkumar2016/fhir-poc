package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps Device data to Device resource
 * 
 * @author santosh
 *
 */
@Component
public class DeviceResourceMapper {

	Logger logger = LoggerFactory.getLogger(DeviceResourceMapper.class);

	/**
	 * 
	 * Convert Device db entity to Device resource
	 * 
	 * @param patientMapDbEntity
	 * @param Device
	 * @return
	 */
	public Device dbModelToResource(DummyDbEntity patientMapDbEntity, Device Device) {

		return Device;
	}

	/**
	 * 
	 * Convert list of Device db entities to list of Device resources
	 * 
	 * @param DeviceDbEntityList
	 * @return
	 */
	public List<Device> dbModelToResource(List<DummyDbEntity> DeviceDbEntityList) {

		Map<String, Device> DeviceMap = new HashMap<>();

		List<Device> DeviceList = new ArrayList<Device>(DeviceMap.values());

		return DeviceList;
	}

}
