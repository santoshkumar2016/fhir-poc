package com.cantatahealth.fhir.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.DeviceDAO;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.DeviceDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.DeviceService;
import com.cantatahealth.fhir.service.util.DeviceResourceMapper;

/**
 * 
 * Implementation class for Device service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	DeviceDAO DeviceDAO;

	@Autowired
	DeviceResourceMapper DeviceMapper;

	@Override
	public Device findDeviceById(Long id) {

		Device Device = new Device();

		DeviceDbEntity DeviceDbEntity = DeviceDAO.findDeviceById(id);

		if (DeviceDbEntity == null)
			return Device;
		else{
			Device = DeviceMapper.dbModelToResource(DeviceDbEntity, Device);
		}

		return Device;

	}

	public List<Device> findDeviceByParamMap(Map<String, String> paramMap) {

		List<DeviceDbEntity> devMapDbEntityList = DeviceDAO.findDeviceByParamMap(paramMap);
		List<Device> DeviceList = DeviceMapper.dbModelToResource(devMapDbEntityList);

		return DeviceList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findResourceById(Long id) {
		return (T) findDeviceById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findResourceByParamMap(Map<String, String> paramMap) {
		return (List<T>) findDeviceByParamMap(paramMap);
	}
}
