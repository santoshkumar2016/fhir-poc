/**
 * 
 */
package com.cantatahealth.fhir.util;

/**
 * Utility class for resource literals
 * @author somadutta
 *
 */
public class ResourcesLiteralsUtil {
	
	/** Resource Names **/
	public static final String Resource_Patient = "Patient";
	public static final String Resource_Organization = "Organization";
	public static final String Resource_Practitioner = "Practitioner";
	public static final String Resource_MedicationAdministration = "MedicationAdministration";
	public static final String Resource_Immunization = "Immunization";
	public static final String Resource_Encounter = "Encounter";
	public static final String Resource_Condition = "Condition";
	public static final String Resource_Observation = "Observation";
	public static final String Resource_Location = "Location";
	public static final String Resource_Procedure = "Procedure";
	public static final String Resource_AllergyIntolerance = "AllergyIntolerance";
	public static final String Resource_Goal = "Goal";
	public static final String Resource_CareTeam = "CareTeam";
	public static final String Resource_Device = "Device";
	public static final String Resource_Others = "Others";
	
	/** Special Characters Literals */
	public static final String Identifier_Hash = "#";
	public static final String Identifier_Slash_Forward = "/";
	
	/**Reference Literals*/
	public static final String Patient_Ref = Resource_Patient+Identifier_Slash_Forward;
	public static final String Organization_Ref = Resource_Organization+Identifier_Slash_Forward;
	public static final String Practitioner_Ref = Resource_Practitioner+Identifier_Slash_Forward;
	public static final String Relation_Ref = "Relation/";
	public static final String Related_Ref = "RelatedPerson/";
	public static final String MedicationRequest_Ref = "MedicationRequest/";
	public static final String ActivityDefinition_Ref = "ActivityDefinition/";
	public static final String Medication_Ref = "Medication/";
	public static final String Encounter_Ref = Resource_Encounter+Identifier_Slash_Forward;
	public static final String Condition_Ref = Resource_Condition+Identifier_Slash_Forward;
	public static final String Observation_Ref = Resource_Observation+Identifier_Slash_Forward;
	public static final String Location_Ref = Resource_Location+Identifier_Slash_Forward;
	public static final String ReferralRequest_Ref = "ReferralRequest/";
	
	/**Query Literals*/
	public static final String Patient_Query = "Select * from Resource where resourcename = 'Patient'";
	public static final String AllergyIntolerance_Query = "Select * from Resource where resourcename = 'AllergyIntolerance'";
	

}
