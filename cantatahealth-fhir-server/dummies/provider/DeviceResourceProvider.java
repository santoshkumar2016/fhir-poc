package com.cantatahealth.fhir.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cantatahealth.fhir.service.DeviceService;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * 
 * Provider for Device resource
 * 
 * @author santosh
 *
 */
public class DeviceResourceProvider implements IResourceProvider {

	Logger logger = LoggerFactory.getLogger(DeviceResourceProvider.class);

	@Autowired
	private DeviceService DeviceService;

	/**
	 * Constructor
	 */
	public DeviceResourceProvider() {
		logger.info("************************* DeviceResourceProvider Instantiated *************************");
	}

	/**
	 * 
	 * read Device by id
	 * 
	 * @param theId
	 *            of Device to read
	 * 
	 * @return
	 */
	@Read
	// (version = true)
	public Device readDevice(@IdParam IdType theId) {

		logger.debug("Reading Device resource with Id:{}", theId.getIdPartAsLong());

		Device Device = DeviceService.findDeviceById(theId.getIdPartAsLong());

		if (Device == null || Device.isEmpty()) {
			throw new ResourceNotFoundException("Device Resource not found : " + theId.getValueAsString());
		}
		
		return Device;

	}
	
	@Search()
	public List<Device> findDevicesBy(@OptionalParam(name = Device.SP_CODE) StringDt activ){
		
		List<Device> DeviceList = new ArrayList<Device>();
		
		return DeviceList;
	}
			

	/**
	 * 
	 */
	@Override
	public Class<Device> getResourceType() {
		return Device.class;
	}

}
