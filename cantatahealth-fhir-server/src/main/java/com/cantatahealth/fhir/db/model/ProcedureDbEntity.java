package com.cantatahealth.fhir.db.model;
// Generated Oct 31, 2017 5:51:36 PM by Hibernate Tools 4.3.1.Final

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

/**
 * ProcedureDbEntity generated by hbm2java
 */
@Entity
@Table(name = "[Procedure]", schema = "dbo", catalog = "FHIRDB")
public class ProcedureDbEntity implements java.io.Serializable {

	private long id;
	private CodingDbEntity codingDbEntityByStatus;
	private CodingDbEntity codingDbEntityByReasoncode;
	private CodingDbEntity codingDbEntityByCategory;
	private CodingDbEntity codingDbEntityByCode;
	private CodingDbEntity codingDbEntityByNotdonereason;
	private EncounterDbEntity encounterDbEntity;
	private LocationDbEntity locationDbEntity;
	private PatientDbEntity patientDbEntity;
	private PeriodDbEntity periodDbEntity;
	private Boolean notdone;
	private Set<ProcedureMapDbEntity> procedureMapDbEntities = new HashSet<ProcedureMapDbEntity>(0);

	public ProcedureDbEntity() {
	}

	public ProcedureDbEntity(long id, PatientDbEntity patientDbEntity) {
		this.id = id;
		this.patientDbEntity = patientDbEntity;
	}

	public ProcedureDbEntity(long id, CodingDbEntity codingDbEntityByStatus, CodingDbEntity codingDbEntityByReasoncode,
			CodingDbEntity codingDbEntityByCategory, CodingDbEntity codingDbEntityByCode,
			CodingDbEntity codingDbEntityByNotdonereason, EncounterDbEntity encounterDbEntity,
			LocationDbEntity locationDbEntity, PatientDbEntity patientDbEntity, PeriodDbEntity periodDbEntity,
			Boolean notdone, Set<ProcedureMapDbEntity> procedureMapDbEntities) {
		this.id = id;
		this.codingDbEntityByStatus = codingDbEntityByStatus;
		this.codingDbEntityByReasoncode = codingDbEntityByReasoncode;
		this.codingDbEntityByCategory = codingDbEntityByCategory;
		this.codingDbEntityByCode = codingDbEntityByCode;
		this.codingDbEntityByNotdonereason = codingDbEntityByNotdonereason;
		this.encounterDbEntity = encounterDbEntity;
		this.locationDbEntity = locationDbEntity;
		this.patientDbEntity = patientDbEntity;
		this.periodDbEntity = periodDbEntity;
		this.notdone = notdone;
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
	@JoinColumn(name = "status")
	public CodingDbEntity getCodingDbEntityByStatus() {
		return this.codingDbEntityByStatus;
	}

	public void setCodingDbEntityByStatus(CodingDbEntity codingDbEntityByStatus) {
		this.codingDbEntityByStatus = codingDbEntityByStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reasoncode")
	public CodingDbEntity getCodingDbEntityByReasoncode() {
		return this.codingDbEntityByReasoncode;
	}

	public void setCodingDbEntityByReasoncode(CodingDbEntity codingDbEntityByReasoncode) {
		this.codingDbEntityByReasoncode = codingDbEntityByReasoncode;
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
	@JoinColumn(name = "code")
	public CodingDbEntity getCodingDbEntityByCode() {
		return this.codingDbEntityByCode;
	}

	public void setCodingDbEntityByCode(CodingDbEntity codingDbEntityByCode) {
		this.codingDbEntityByCode = codingDbEntityByCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notdonereason")
	public CodingDbEntity getCodingDbEntityByNotdonereason() {
		return this.codingDbEntityByNotdonereason;
	}

	public void setCodingDbEntityByNotdonereason(CodingDbEntity codingDbEntityByNotdonereason) {
		this.codingDbEntityByNotdonereason = codingDbEntityByNotdonereason;
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
	@JoinColumn(name = "location")
	public LocationDbEntity getLocationDbEntity() {
		return this.locationDbEntity;
	}

	public void setLocationDbEntity(LocationDbEntity locationDbEntity) {
		this.locationDbEntity = locationDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject", nullable = false)
	public PatientDbEntity getPatientDbEntity() {
		return this.patientDbEntity;
	}

	public void setPatientDbEntity(PatientDbEntity patientDbEntity) {
		this.patientDbEntity = patientDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performedperiod")
	public PeriodDbEntity getPeriodDbEntity() {
		return this.periodDbEntity;
	}

	public void setPeriodDbEntity(PeriodDbEntity periodDbEntity) {
		this.periodDbEntity = periodDbEntity;
	}

	@Column(name = "notdone")
	public Boolean getNotdone() {
		return this.notdone;
	}

	public void setNotdone(Boolean notdone) {
		this.notdone = notdone;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procedureDbEntity")
	public Set<ProcedureMapDbEntity> getProcedureMapDbEntities() {
		return this.procedureMapDbEntities;
	}

	public void setProcedureMapDbEntities(Set<ProcedureMapDbEntity> procedureMapDbEntities) {
		this.procedureMapDbEntities = procedureMapDbEntities;
	}

}
