/**
 * 
 */
package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceClinicalStatus;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceType;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.dao.CareTeamDAO;
import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceDbEntity;
import com.cantatahealth.fhir.db.model.AllergyIntoleranceMapDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamDbEntity;
import com.cantatahealth.fhir.db.model.CareTeamMapDbEntity;
import com.cantatahealth.fhir.db.model.CodingDbEntity;
import com.cantatahealth.fhir.db.model.ContactPointDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.LocationMapDbEntity;
import com.cantatahealth.fhir.db.model.OrganizationDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PractitionerDbEntity;
import com.cantatahealth.fhir.db.model.PractitionerMapDbEntity;
import com.cantatahealth.fhir.db.model.ReactionnDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Utility class for AllergyIntolerance Model
 * @author somadutta
 *
 */
@Component
public class OrganizationResourceMapper {
	
	Logger logger = LoggerFactory.getLogger(OrganizationResourceMapper.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Organization toOrganizationResource(Long id)
	{
		Organization organization = new Organization();

		OrganizationDbEntity organizationDbEntity = hibUtil.fetchById(OrganizationDbEntity.class, id);

		if (organizationDbEntity == null)
			return organization;
		else{
			organization = getOrganizationBaseComponents(organization,organizationDbEntity);
		}
		return organization;

	}
	
	/**
	 * 
	 * @param organization
	 * @param organizationDbEntity
	 * @return
	 */
	public Organization getOrganizationBaseComponents(Organization organization, OrganizationDbEntity organizationDbEntity)
	{
		
		/** Setting Id */		
		organization.setId(ResourcesLiteralsUtil.Identifier_Hash +ResourcesLiteralsUtil.Organization_Ref + String.valueOf(organizationDbEntity.getId()));
		
		/** Name */
		if(AppUtil.isNotEmpty(organizationDbEntity.getName()))
			organization.setName(organizationDbEntity.getName());
		
		return organization;
	}
}
