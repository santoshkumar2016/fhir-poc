/**
 * 
 */
package com.cantatahealth.fhir.service.util;


import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.RelatedPerson;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.ContactPointDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.RelatedPersonDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Utility class for AllergyIntolerance Model
 * @author somadutta
 *
 */
@Component
public class RelatedPersonResourceMapper {
	
	Logger logger = LoggerFactory.getLogger(RelatedPersonResourceMapper.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public RelatedPerson toRelatedPersonResource(Long id)
	{
		RelatedPerson relatedPerson = new RelatedPerson();

		RelatedPersonDbEntity relatedPersonDbEntity = hibUtil.fetchById(RelatedPersonDbEntity.class, id);

		if (relatedPersonDbEntity == null)
			return relatedPerson;
		/**Mapping base components of careTeam*/
		else{
			relatedPerson = getRelatedPersonBaseComponents(relatedPerson,relatedPersonDbEntity);
		}
		
		return relatedPerson;
	}
	
	/**
	 *
	 * @param referralRequest
	 * @param referralRequestDbEntity
	 * @return
	 */
	// To be modified.
	private RelatedPerson getRelatedPersonBaseComponents(RelatedPerson relatedPerson, RelatedPersonDbEntity relatedPersonDbEntity)
	{
		
		/** Setting Id */		
		relatedPerson.setId(ResourcesLiteralsUtil.Identifier_Hash +ResourcesLiteralsUtil.Related_Ref + String.valueOf(relatedPersonDbEntity.getId()));
		
		// Gender
		if(relatedPersonDbEntity.getCodingDbEntityByGender() != null)
			if(AppUtil.isNotEmpty(relatedPersonDbEntity.getCodingDbEntityByGender().getCode()))
			try {
				relatedPerson.setGender(AdministrativeGender.fromCode(relatedPersonDbEntity.getCodingDbEntityByGender().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// BirthDate
		if(relatedPersonDbEntity.getBirthdate() != null)
			relatedPerson.setBirthDate(relatedPersonDbEntity.getBirthdate());
		
		// Contact Point
		if(relatedPersonDbEntity.getContactPointDbEntity() != null){
			ContactPoint cp =  toContactPointResource(relatedPersonDbEntity.getContactPointDbEntity());
			relatedPerson.addTelecom(cp);
		}
		
		// Name
		if(relatedPersonDbEntity.getHumanNameDbEntity() != null) {
			HumanName hn = toHumanNameResource(relatedPersonDbEntity.getHumanNameDbEntity());
			relatedPerson.addName(hn);
		}
		
		// Relationship
		if(relatedPersonDbEntity.getCodingDbEntityByRelationship() != null)
				relatedPerson.setRelationship(new CodeableConcept().addCoding(new Coding(relatedPersonDbEntity.getCodingDbEntityByRelationship().getSystem(),
						relatedPersonDbEntity.getCodingDbEntityByRelationship().getCode(),
						relatedPersonDbEntity.getCodingDbEntityByRelationship().getDisplay())));
		
		// Address
		if(relatedPersonDbEntity.getAddressDbEntity() != null) {
			Address ad = toAddressResource(relatedPersonDbEntity.getAddressDbEntity());
			relatedPerson.addAddress(ad);
		}
			
		
		return relatedPerson;
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

		if (adrDbEntity == null) {
			return addr;
		}
		if (AppUtil.isNotEmpty(adrDbEntity.getCity()))
			addr.setCity(adrDbEntity.getCity().toString());
		if (AppUtil.isNotEmpty(adrDbEntity.getCountry()))
			addr.setCountry(adrDbEntity.getCountry().toString());

		if (AppUtil.isNotEmpty(adrDbEntity.getLine())) {
			StringType type = new StringType();
			type.setValue(adrDbEntity.getLine().toString());
			List<StringType> list = addr.getLine();
			list.add(type);
			addr.setLine(list);
		}
		
		if(adrDbEntity.getCodingDbEntity() != null)
			try {
				if(AppUtil.isNotEmpty(adrDbEntity.getCodingDbEntity().getCode()))
					addr.setUse(AddressUse.fromCode(adrDbEntity.getCodingDbEntity().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (AppUtil.isNotEmpty(adrDbEntity.getState())) {
			addr.setState(adrDbEntity.getState().toString());
		}

		if (AppUtil.isNotEmpty(adrDbEntity.getPostalcode())) {
			addr.setPostalCode(adrDbEntity.getPostalcode().toString());
		}

		return addr;
	}
	
	/**
	 * 
	 * @param humanNameDbEntity
	 * @return This method converts dbEntity to HumanName model class and returns the model object.
	 */
	public HumanName toHumanNameResource(HumanNameDbEntity humanNameDbEntity) {

		HumanName name = new HumanName();

		if (humanNameDbEntity == null) {
			return name;
		}

		if (AppUtil.isNotEmpty(humanNameDbEntity.getFamily())) {
			name.setFamily(humanNameDbEntity.getFamily().toString());
		}

		List<StringType> strTypList = new ArrayList<StringType>();
		if (AppUtil.isNotEmpty(humanNameDbEntity.getGiven())) {
			name.addGiven(humanNameDbEntity.getGiven());
		}

		strTypList.clear();
		if (AppUtil.isNotEmpty(humanNameDbEntity.getPrefix())) {

			name.addPrefix(humanNameDbEntity.getPrefix());
		}

		strTypList.clear();
		if (AppUtil.isNotEmpty(humanNameDbEntity.getSuffix())) {

			name.addSuffix(humanNameDbEntity.getSuffix());
		}

		return name;
	}

}
