package com.cantatahealth.fhir.db.model;
// Generated Oct 31, 2017 5:51:36 PM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConditionDbEntity generated by hbm2java
 */
@Entity
@Table(name = "Condition", schema = "dbo", catalog = "FHIRDB")
public class ConditionDbEntity implements java.io.Serializable {

	private long id;
	private CodingDbEntity codingDbEntityByCode;
	private CodingDbEntity codingDbEntityByClinicalstatus;
	private CodingDbEntity codingDbEntityByVerificationstatus;
	private CodingDbEntity codingDbEntityByCategory;
	private CodingDbEntity codingDbEntityBySeverity;
	private EncounterDbEntity encounterDbEntity;
	private PatientDbEntity patientDbEntity;
	private PractitionerDbEntity practitionerDbEntity;
	private Date onsetdatetime;
	private Date abatementdatetime;
	private String note;
	private Set<GoalMapDbEntity> goalMapDbEntities = new HashSet<GoalMapDbEntity>(0);
	private Set<EncounterMapDbEntity> encounterMapDbEntities = new HashSet<EncounterMapDbEntity>(0);
	private Set<ProcedureMapDbEntity> procedureMapDbEntities = new HashSet<ProcedureMapDbEntity>(0);

	public ConditionDbEntity() {
	}

	public ConditionDbEntity(long id) {
		this.id = id;
	}

	public ConditionDbEntity(long id, CodingDbEntity codingDbEntityByCode,
			CodingDbEntity codingDbEntityByClinicalstatus, CodingDbEntity codingDbEntityByVerificationstatus,
			CodingDbEntity codingDbEntityByCategory, CodingDbEntity codingDbEntityBySeverity,
			EncounterDbEntity encounterDbEntity, PatientDbEntity patientDbEntity,
			PractitionerDbEntity practitionerDbEntity, Date onsetdatetime, Date abatementdatetime, String note,
			Set<GoalMapDbEntity> goalMapDbEntities, Set<EncounterMapDbEntity> encounterMapDbEntities,
			Set<ProcedureMapDbEntity> procedureMapDbEntities) {
		this.id = id;
		this.codingDbEntityByCode = codingDbEntityByCode;
		this.codingDbEntityByClinicalstatus = codingDbEntityByClinicalstatus;
		this.codingDbEntityByVerificationstatus = codingDbEntityByVerificationstatus;
		this.codingDbEntityByCategory = codingDbEntityByCategory;
		this.codingDbEntityBySeverity = codingDbEntityBySeverity;
		this.encounterDbEntity = encounterDbEntity;
		this.patientDbEntity = patientDbEntity;
		this.practitionerDbEntity = practitionerDbEntity;
		this.onsetdatetime = onsetdatetime;
		this.abatementdatetime = abatementdatetime;
		this.note = note;
		this.goalMapDbEntities = goalMapDbEntities;
		this.encounterMapDbEntities = encounterMapDbEntities;
		this.procedureMapDbEntities = procedureMapDbEntities;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false, precision = 18, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	public CodingDbEntity getCodingDbEntityByCode() {
		return this.codingDbEntityByCode;
	}

	public void setCodingDbEntityByCode(CodingDbEntity codingDbEntityByCode) {
		this.codingDbEntityByCode = codingDbEntityByCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clinicalstatus")
	public CodingDbEntity getCodingDbEntityByClinicalstatus() {
		return this.codingDbEntityByClinicalstatus;
	}

	public void setCodingDbEntityByClinicalstatus(CodingDbEntity codingDbEntityByClinicalstatus) {
		this.codingDbEntityByClinicalstatus = codingDbEntityByClinicalstatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verificationstatus")
	public CodingDbEntity getCodingDbEntityByVerificationstatus() {
		return this.codingDbEntityByVerificationstatus;
	}

	public void setCodingDbEntityByVerificationstatus(CodingDbEntity codingDbEntityByVerificationstatus) {
		this.codingDbEntityByVerificationstatus = codingDbEntityByVerificationstatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	public CodingDbEntity getCodingDbEntityByCategory() {
		return this.codingDbEntityByCategory;
	}

	public void setCodingDbEntityByCategory(CodingDbEntity codingDbEntityByCategory) {
		this.codingDbEntityByCategory = codingDbEntityByCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "severity")
	public CodingDbEntity getCodingDbEntityBySeverity() {
		return this.codingDbEntityBySeverity;
	}

	public void setCodingDbEntityBySeverity(CodingDbEntity codingDbEntityBySeverity) {
		this.codingDbEntityBySeverity = codingDbEntityBySeverity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "context")
	public EncounterDbEntity getEncounterDbEntity() {
		return this.encounterDbEntity;
	}

	public void setEncounterDbEntity(EncounterDbEntity encounterDbEntity) {
		this.encounterDbEntity = encounterDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject")
	public PatientDbEntity getPatientDbEntity() {
		return this.patientDbEntity;
	}

	public void setPatientDbEntity(PatientDbEntity patientDbEntity) {
		this.patientDbEntity = patientDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asserter")
	public PractitionerDbEntity getPractitionerDbEntity() {
		return this.practitionerDbEntity;
	}

	public void setPractitionerDbEntity(PractitionerDbEntity practitionerDbEntity) {
		this.practitionerDbEntity = practitionerDbEntity;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "onsetdatetime", length = 23)
	public Date getOnsetdatetime() {
		return this.onsetdatetime;
	}

	public void setOnsetdatetime(Date onsetdatetime) {
		this.onsetdatetime = onsetdatetime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "abatementdatetime", length = 23)
	public Date getAbatementdatetime() {
		return this.abatementdatetime;
	}

	public void setAbatementdatetime(Date abatementdatetime) {
		this.abatementdatetime = abatementdatetime;
	}

	@Column(name = "note")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conditionDbEntity")
	public Set<GoalMapDbEntity> getGoalMapDbEntities() {
		return this.goalMapDbEntities;
	}

	public void setGoalMapDbEntities(Set<GoalMapDbEntity> goalMapDbEntities) {
		this.goalMapDbEntities = goalMapDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conditionDbEntity")
	public Set<EncounterMapDbEntity> getEncounterMapDbEntities() {
		return this.encounterMapDbEntities;
	}

	public void setEncounterMapDbEntities(Set<EncounterMapDbEntity> encounterMapDbEntities) {
		this.encounterMapDbEntities = encounterMapDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conditionDbEntity")
	public Set<ProcedureMapDbEntity> getProcedureMapDbEntities() {
		return this.procedureMapDbEntities;
	}

	public void setProcedureMapDbEntities(Set<ProcedureMapDbEntity> procedureMapDbEntities) {
		this.procedureMapDbEntities = procedureMapDbEntities;
	}

}
