package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.Device.DeviceUdiComponent;
import org.hl7.fhir.dstu3.model.Device.FHIRDeviceStatus;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.exceptions.FHIRException;
/*import org.hl7.fhir.dstu3.model.codesystems.Udi;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.ConditionDbEntity;
import com.cantatahealth.fhir.db.model.DeviceDbEntity;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps Device data to Device resource
 * 
 * @author santosh
 *
 */
@Component
public class DeviceResourceMapper {

	Logger logger = LoggerFactory.getLogger(DeviceResourceMapper.class);
	
	@Autowired
	OrganizationResourceMapper organizationResourceMapper;
	/**
	 * 
	 * Convert Device db entity to Device resource
	 * 
	 * @param patientMapDbEntity
	 * @param Device
	 * @return
	 */
	public Device dbModelToResource(DeviceDbEntity deviceDbEntity, Device device) {

		// Setting ID
		if(AppUtil.isNotEmpty(String.valueOf(deviceDbEntity.getId())))
			device.setId(String.valueOf(deviceDbEntity.getId()));
		
		// device identifier - To be modified
		DeviceUdiComponent dvc = new DeviceUdiComponent();
		if(AppUtil.isNotEmpty(deviceDbEntity.getDeviceidentifier()))
			device.setUdi(dvc.setDeviceIdentifier(deviceDbEntity.getDeviceidentifier()));
		
		// Name to be discussed
		if(AppUtil.isNotEmpty(deviceDbEntity.getName()))
			device.setUdi(dvc.setName(deviceDbEntity.getName()));
		
		// Carrierhrf
		if(AppUtil.isNotEmpty(deviceDbEntity.getCarrierhrf()))
			device.setUdi(dvc.setCarrierHRF(deviceDbEntity.getCarrierhrf()));
		
		// Issuer
		if(AppUtil.isNotEmpty(deviceDbEntity.getIssuer()))
			device.setUdi(dvc.setIssuer(deviceDbEntity.getIssuer()));
				
		// Patient
		if(deviceDbEntity.getPatientDbEntity() != null)
			device.setPatient(new Reference(ResourcesLiteralsUtil.Patient_Ref + 
					String.valueOf(deviceDbEntity.getPatientDbEntity().getId())));
		
		// Owner
		if(deviceDbEntity.getOrganizationDbEntity() != null) {
			Organization o = organizationResourceMapper.toOrganizationResource(deviceDbEntity.getOrganizationDbEntity().getId());
			device.addContained(o);
			device.setOwner(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Organization_Ref
					+deviceDbEntity.getOrganizationDbEntity().getId()));
		}
		
		// Status
		if(deviceDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(deviceDbEntity.getCodingDbEntityByStatus().getCode()))
			try {
				device.setStatus(FHIRDeviceStatus.fromCode(deviceDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// Type
		if(deviceDbEntity.getCodingDbEntityByType() != null)
			device.setType(new CodeableConcept().addCoding(new Coding(deviceDbEntity.getCodingDbEntityByType().getSystem(),
					deviceDbEntity.getCodingDbEntityByType().getCode(),
					deviceDbEntity.getCodingDbEntityByType().getDisplay())));
		
		//lotNumber
		if(AppUtil.isNotEmpty(deviceDbEntity.getLotnumber()))
			device.setLotNumber(deviceDbEntity.getLotnumber());
		
		//Manufacturer
		if(AppUtil.isNotEmpty(deviceDbEntity.getManufacturer()))
			device.setManufacturer(deviceDbEntity.getManufacturer());
		
		//ManufactureDate
		if(deviceDbEntity.getManufacturedate() != null)
			device.setManufactureDate(deviceDbEntity.getManufacturedate());
		
		// ExpirationDate
		if(deviceDbEntity.getExpirationdate() != null)
			device.setExpirationDate(deviceDbEntity.getExpirationdate());
		
		//Model
		if(AppUtil.isNotEmpty(deviceDbEntity.getModel()))
			device.setModel(deviceDbEntity.getModel());
		
		// Version
		if(AppUtil.isNotEmpty(deviceDbEntity.getVersion()))
			device.setVersion(deviceDbEntity.getVersion());
		
		// Note
		if(AppUtil.isNotEmpty(deviceDbEntity.getNote()))
			device.addNote(new Annotation().setText(deviceDbEntity.getNote()));
			
		return device;
	}

	/**
	 * 
	 * Convert list of Device db entities to list of Device resources
	 * 
	 * @param DeviceDbEntityList
	 * @return
	 */
	public List<Device> dbModelToResource(List<DeviceDbEntity> DeviceDbEntityList) {

//		Map<String, Device> DeviceMap = new HashMap<>();
		List<Device> deviceList = new ArrayList<Device>();

		for (DeviceDbEntity devDbEnt : DeviceDbEntityList)
			deviceList.add(dbModelToResource(devDbEnt, new Device()));
//		List<Device> DeviceList = new ArrayList<Device>(DeviceMap.values());

		return deviceList;
	}

}
