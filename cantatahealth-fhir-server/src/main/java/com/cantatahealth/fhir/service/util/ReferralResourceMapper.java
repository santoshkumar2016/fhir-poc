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
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.ReferralRequest;
import org.hl7.fhir.dstu3.model.ReferralRequest.ReferralCategory;
import org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestRequesterComponent;
import org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus;
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
import com.cantatahealth.fhir.db.model.PatientDbEntity;
import com.cantatahealth.fhir.db.model.PractitionerDbEntity;
import com.cantatahealth.fhir.db.model.PractitionerMapDbEntity;
import com.cantatahealth.fhir.db.model.ReactionnDbEntity;
import com.cantatahealth.fhir.db.model.ReferralRequestDbEntity;
import com.cantatahealth.fhir.db.util.FhirDbUtil;
import com.cantatahealth.fhir.util.AppUtil;
import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

/**
 * Utility class for AllergyIntolerance Model
 * @author somadutta
 *
 */
@Component
public class ReferralResourceMapper {
	
	Logger logger = LoggerFactory.getLogger(ReferralResourceMapper.class);
	
	@Autowired
	FhirDbUtil hibUtil;
	
	@Autowired
	PractitionerResourceMapper practitionerResourceMapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ReferralRequest toReferralRequestResource(Long id)
	{
		ReferralRequest referralRequest = new ReferralRequest();

		ReferralRequestDbEntity referralRequestDbEntity = hibUtil.fetchById(ReferralRequestDbEntity.class, id);

		if (referralRequestDbEntity == null)
			return referralRequest;
		/**Mapping base components of careTeam*/
		else{
			referralRequest = getReferralRequestBaseComponents(referralRequest,referralRequestDbEntity);
		}
		
		return referralRequest;
	}
	
	/**
	 *
	 * @param referralRequest
	 * @param referralRequestDbEntity
	 * @return
	 */
	// To be modified.
	private ReferralRequest getReferralRequestBaseComponents(ReferralRequest referralRequest, ReferralRequestDbEntity referralRequestDbEntity)
	{
		
		/** Setting Id */		
		referralRequest.setId(ResourcesLiteralsUtil.Identifier_Hash +ResourcesLiteralsUtil.ReferralRequest_Ref + String.valueOf(referralRequestDbEntity.getId()));
		
		/** Status */
		if(referralRequestDbEntity.getCodingDbEntityByStatus() != null)
		if(AppUtil.isNotEmpty(referralRequestDbEntity.getCodingDbEntityByStatus().getCode()))
			try {
				referralRequest.setStatus(ReferralRequestStatus.fromCode(referralRequestDbEntity.getCodingDbEntityByStatus().getCode().toLowerCase()));
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		/** Subject/Patient */
		if(referralRequestDbEntity.getPatientDbEntity() != null)
			referralRequest.setSubject(new Reference(ResourcesLiteralsUtil.Patient_Ref + referralRequestDbEntity.getPatientDbEntity().getId()));
		
		// Context
		if(referralRequestDbEntity.getEncounterDbEntity() != null)
			referralRequest.setContext(new Reference(ResourcesLiteralsUtil.Encounter_Ref + referralRequestDbEntity.getEncounterDbEntity().getId()));
		
		/*// Occurence Period
		if(referralRequestDbEntity.getOccurenceperiod())*/
			
			
		// Authoredon
		if(referralRequestDbEntity.getAuthoredon() != null)
			referralRequest.setAuthoredOn(referralRequestDbEntity.getAuthoredon());
		
		// Requester
		if(referralRequestDbEntity.getPractitionerDbEntity() != null) {
			Practitioner p = practitionerResourceMapper.toPractitionerResource(referralRequestDbEntity.getPractitionerDbEntity().getId());
			referralRequest.addContained(p);
			referralRequest.setRequester(new ReferralRequestRequesterComponent().setAgent(new Reference(ResourcesLiteralsUtil.Identifier_Hash
					+ResourcesLiteralsUtil.ReferralRequest_Ref+
					referralRequestDbEntity.getPractitionerDbEntity().getId())));
		}
		
		// Recipient
		/*if(referralRequestDbEntity.getCodingDbEntityByRecipient() != null)
			referralRequest.addRecipient(t)*/
		
		// Reason Code
		/*if(referralRequestDbEntity.getReasoncode() != null)
			referralRequest.addReasonCode(new CodeableConcept())*/
		
		// Description
		if(AppUtil.isNotEmpty(referralRequestDbEntity.getDescription()))
			referralRequest.setDescription(referralRequestDbEntity.getDescription());
		
		// Note
		if(AppUtil.isNotEmpty(referralRequestDbEntity.getNote()))
			referralRequest.addNote(new Annotation().setText(referralRequestDbEntity.getNote()));
		
		return referralRequest;
	}	

}
