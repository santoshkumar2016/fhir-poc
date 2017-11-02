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
 * GoalDbEntity generated by hbm2java
 */
@Entity
@Table(name = "Goal", schema = "dbo", catalog = "FHIRDB")
public class GoalDbEntity implements java.io.Serializable {

	private long id;
	private CodingDbEntity codingDbEntityByDescription;
	private CodingDbEntity codingDbEntityByPriority;
	private CodingDbEntity codingDbEntityByStatus;
	private PatientDbEntity patientDbEntity;
	private PractitionerDbEntity practitionerDbEntity;
	private Date startdate;
	private Set<GoalMapDbEntity> goalMapDbEntities = new HashSet<GoalMapDbEntity>(0);

	public GoalDbEntity() {
	}

	public GoalDbEntity(long id) {
		this.id = id;
	}

	public GoalDbEntity(long id, CodingDbEntity codingDbEntityByDescription, CodingDbEntity codingDbEntityByPriority,
			CodingDbEntity codingDbEntityByStatus, PatientDbEntity patientDbEntity,
			PractitionerDbEntity practitionerDbEntity, Date startdate, Set<GoalMapDbEntity> goalMapDbEntities) {
		this.id = id;
		this.codingDbEntityByDescription = codingDbEntityByDescription;
		this.codingDbEntityByPriority = codingDbEntityByPriority;
		this.codingDbEntityByStatus = codingDbEntityByStatus;
		this.patientDbEntity = patientDbEntity;
		this.practitionerDbEntity = practitionerDbEntity;
		this.startdate = startdate;
		this.goalMapDbEntities = goalMapDbEntities;
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
	@JoinColumn(name = "description")
	public CodingDbEntity getCodingDbEntityByDescription() {
		return this.codingDbEntityByDescription;
	}

	public void setCodingDbEntityByDescription(CodingDbEntity codingDbEntityByDescription) {
		this.codingDbEntityByDescription = codingDbEntityByDescription;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority")
	public CodingDbEntity getCodingDbEntityByPriority() {
		return this.codingDbEntityByPriority;
	}

	public void setCodingDbEntityByPriority(CodingDbEntity codingDbEntityByPriority) {
		this.codingDbEntityByPriority = codingDbEntityByPriority;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status")
	public CodingDbEntity getCodingDbEntityByStatus() {
		return this.codingDbEntityByStatus;
	}

	public void setCodingDbEntityByStatus(CodingDbEntity codingDbEntityByStatus) {
		this.codingDbEntityByStatus = codingDbEntityByStatus;
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
	@JoinColumn(name = "practitioner")
	public PractitionerDbEntity getPractitionerDbEntity() {
		return this.practitionerDbEntity;
	}

	public void setPractitionerDbEntity(PractitionerDbEntity practitionerDbEntity) {
		this.practitionerDbEntity = practitionerDbEntity;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startdate", length = 23)
	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goalDbEntity")
	public Set<GoalMapDbEntity> getGoalMapDbEntities() {
		return this.goalMapDbEntities;
	}

	public void setGoalMapDbEntities(Set<GoalMapDbEntity> goalMapDbEntities) {
		this.goalMapDbEntities = goalMapDbEntities;
	}

}
