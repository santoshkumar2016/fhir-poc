package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Location.LocationStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.ContactPointDbEntity;
import com.cantatahealth.fhir.db.model.LocationDbEntity;
import com.cantatahealth.fhir.db.model.LocationMapDbEntity;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Maps Location data to Location resource
 * 
 * @author santosh
 *
 */
@Component
public class LocationResourceMapper {

	Logger logger = LoggerFactory.getLogger(LocationResourceMapper.class);
	
	@Autowired
	OrganizationResourceMapper organizationResourceMapper;

	public Location getLocationBaseComponents( Location location ,LocationDbEntity locationDbEntity)
	{
		
		/** Setting Id */
		location.setId(String.valueOf(locationDbEntity.getId()));
		
		
		//Status
		if(locationDbEntity.getCodingDbEntityByStatus() != null)
			if(AppUtil.isNotEmpty(locationDbEntity.getCodingDbEntityByStatus().getCode()))
			try {
				location.setStatus(LocationStatus.fromCode(locationDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// Address
		if(locationDbEntity.getAddressDbEntity() != null) {
			Address a = toAddressResource(locationDbEntity.getAddressDbEntity());
			location.setAddress(a);
		}
		
		// Operational Status
		if(locationDbEntity.getCodingDbEntityByOperationalstatus() != null)
			location.setOperationalStatus(new Coding(locationDbEntity.getCodingDbEntityByOperationalstatus().getSystem(),
					locationDbEntity.getCodingDbEntityByOperationalstatus().getCode(),
					locationDbEntity.getCodingDbEntityByOperationalstatus().getDisplay()));
		
		// Name
		if(AppUtil.isNotEmpty(locationDbEntity.getName()))
			location.setName(locationDbEntity.getName());
		
		// Description
		if(AppUtil.isNotEmpty(locationDbEntity.getDescription()))
			location.setDescription(locationDbEntity.getDescription());
		
		// Type
		if(locationDbEntity.getCodingDbEntityByType() != null)
			location.setType(new CodeableConcept().addCoding(
					new Coding(locationDbEntity.getCodingDbEntityByType().getSystem(),
					locationDbEntity.getCodingDbEntityByType().getCode(),
					locationDbEntity.getCodingDbEntityByType().getDisplay())));
		
		return location;
	}
	
	
	/**
	 * 
	 * @param contactPointDbEntity
	 * @return
	 */
	private ContactPoint toContactPointResource(ContactPointDbEntity contactPointDbEntity)
		{
		
		ContactPoint cp = new ContactPoint();		
		// System
		if(AppUtil.isNotEmpty(contactPointDbEntity.getSystem()))
			try {
				cp.setSystem(ContactPointSystem.fromCode(contactPointDbEntity.getSystem().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// Value
		if(AppUtil.isNotEmpty(contactPointDbEntity.getValue()))
			cp.setValue(contactPointDbEntity.getValue());
		
		// Use
		if(AppUtil.isNotEmpty(contactPointDbEntity.getUseAlias()))
			try {
				cp.setUse(ContactPointUse.fromCode(contactPointDbEntity.getUseAlias().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// Rank
		if(AppUtil.isNotEmpty(String.valueOf(contactPointDbEntity.getRank())))
				cp.setRank(contactPointDbEntity.getRank());
		
		return cp;
	}
	
	/**
	 * 
	 * @param adrDbEntity
	 * @return This method converts the DBEntity to Address model class and returns the model instance
	 */
	private Address toAddressResource(AddressDbEntity adrDbEntity) {

		Address addr = new Address();
		
		// City
		if (AppUtil.isNotEmpty(adrDbEntity.getCity()))
			addr.setCity(adrDbEntity.getCity().toString());
		
		// Use
		if(adrDbEntity.getCodingDbEntity() != null)
			try {
				if(AppUtil.isNotEmpty(adrDbEntity.getCodingDbEntity().getCode()))
					addr.setUse(AddressUse.fromCode(adrDbEntity.getCodingDbEntity().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// Country
		if (AppUtil.isNotEmpty(adrDbEntity.getCountry()))
			addr.setCountry(adrDbEntity.getCountry().toString());
		
		// Line/ Type
		if (AppUtil.isNotEmpty(adrDbEntity.getLine())) {
			StringType type = new StringType();
			type.setValue(adrDbEntity.getLine().toString());
			List<StringType> list = addr.getLine();
			list.add(type);
			addr.setLine(list);
		}

		// State
		if (AppUtil.isNotEmpty(adrDbEntity.getState())) {
			addr.setState(adrDbEntity.getState().toString());
		}
		
		// Postal Code
		if (AppUtil.isNotEmpty(adrDbEntity.getPostalcode())) {
			addr.setPostalCode(adrDbEntity.getPostalcode().toString());
		}

		return addr;
	}
	
	/**
	 * 
	 * Convert Location db entity to Location resource
	 * 
	 * @param patientMapDbEntity
	 * @param Location
	 * @return
	 */
	public Location dbModelToResource(LocationMapDbEntity locationMapDbEntity, Location location) {
		
		//Contact Point
		
		if(locationMapDbEntity.getContactPointDbEntity() != null) {
			ContactPoint cp = toContactPointResource(locationMapDbEntity.getContactPointDbEntity());
			location.addTelecom(cp);
		}
		
		
		// Managing Organization
		if(locationMapDbEntity.getOrganizationDbEntity() != null) {
			Organization o = organizationResourceMapper.toOrganizationResource(locationMapDbEntity.getOrganizationDbEntity().getId());
			location.addContained(o);
			location.setManagingOrganization(new Reference(ResourcesLiteralsUtil.Identifier_Hash + ResourcesLiteralsUtil.Organization_Ref 
				+ String.valueOf(locationMapDbEntity.getOrganizationDbEntity().getId())));
		}
		
		return location;
	}

	/**
	 * 
	 * Convert list of Location db entities to list of Location resources
	 * 
	 * @param LocationDbEntityList
	 * @return
	 */
	public List<Location> dbModelToResource(Set<LocationMapDbEntity> LocationDbEntityList) {

		Map<String, Location> locationMap = new HashMap<>();
		Iterator<LocationMapDbEntity> locMapDbSetItr = LocationDbEntityList.iterator();
		while (locMapDbSetItr.hasNext()) {

			LocationMapDbEntity locMapDbEnt = locMapDbSetItr.next();
			if(locMapDbEnt.getLocationDbEntity() != null)
			if (locationMap.containsKey(String.valueOf(locMapDbEnt.getLocationDbEntity().getId()))) {

				Location location = dbModelToResource(locMapDbEnt, locationMap.get(String.valueOf(locMapDbEnt.getLocationDbEntity().getId())));
				location=getLocationBaseComponents(location, locMapDbEnt.getLocationDbEntity());
				locationMap.put(location.getId(),location );
				
			} else {
				Location location = dbModelToResource(locMapDbEnt, new Location());
				location=getLocationBaseComponents(location, locMapDbEnt.getLocationDbEntity());
				locationMap.put(location.getId(),location );
				
			}
		}

		List<Location> LocationList = new ArrayList<Location>(locationMap.values());

		return LocationList;
	}

}
