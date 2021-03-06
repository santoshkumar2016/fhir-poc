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
 * ObservationDbEntity generated by hbm2java
 */
@Entity
@Table(name = "Observation", schema = "dbo", catalog = "FHIRDB")
public class ObservationDbEntity implements java.io.Serializable {

	private long id;
	private CodingDbEntity codingDbEntityByStatus;
	private CodingDbEntity codingDbEntityByInterpretation;
	private CodingDbEntity codingDbEntityByBodysite;
	private CodingDbEntity codingDbEntityByCategory;
	private CodingDbEntity codingDbEntityByCode;
	private EncounterDbEntity encounterDbEntity;
	private PatientDbEntity patientDbEntity;
	private PeriodDbEntity periodDbEntity;
	private QuantityDbEntity quantityDbEntity;
	private String valueString;
	private Set<ProcedureMapDbEntity> procedureMapDbEntities = new HashSet<ProcedureMapDbEntity>(0);
	private Set<GoalMapDbEntity> goalMapDbEntities = new HashSet<GoalMapDbEntity>(0);
	private Set<ObservationMapDbEntity> observationMapDbEntities = new HashSet<ObservationMapDbEntity>(0);

	public ObservationDbEntity() {
	}

	public ObservationDbEntity(long id) {
		this.id = id;
	}

	public ObservationDbEntity(long id, CodingDbEntity codingDbEntityByStatus,
			CodingDbEntity codingDbEntityByInterpretation, CodingDbEntity codingDbEntityByBodysite,
			CodingDbEntity codingDbEntityByCategory, CodingDbEntity codingDbEntityByCode,
			EncounterDbEntity encounterDbEntity, PatientDbEntity patientDbEntity, PeriodDbEntity periodDbEntity,
			QuantityDbEntity quantityDbEntity, String valueString, Set<ProcedureMapDbEntity> procedureMapDbEntities,
			Set<GoalMapDbEntity> goalMapDbEntities, Set<ObservationMapDbEntity> observationMapDbEntities) {
		this.id = id;
		this.codingDbEntityByStatus = codingDbEntityByStatus;
		this.codingDbEntityByInterpretation = codingDbEntityByInterpretation;
		this.codingDbEntityByBodysite = codingDbEntityByBodysite;
		this.codingDbEntityByCategory = codingDbEntityByCategory;
		this.codingDbEntityByCode = codingDbEntityByCode;
		this.encounterDbEntity = encounterDbEntity;
		this.patientDbEntity = patientDbEntity;
		this.periodDbEntity = periodDbEntity;
		this.quantityDbEntity = quantityDbEntity;
		this.valueString = valueString;
		this.procedureMapDbEntities = procedureMapDbEntities;
		this.goalMapDbEntities = goalMapDbEntities;
		this.observationMapDbEntities = observationMapDbEntities;
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
	@JoinColumn(name = "interpretation")
	public CodingDbEntity getCodingDbEntityByInterpretation() {
		return this.codingDbEntityByInterpretation;
	}

	public void setCodingDbEntityByInterpretation(CodingDbEntity codingDbEntityByInterpretation) {
		this.codingDbEntityByInterpretation = codingDbEntityByInterpretation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bodysite")
	public CodingDbEntity getCodingDbEntityByBodysite() {
		return this.codingDbEntityByBodysite;
	}

	public void setCodingDbEntityByBodysite(CodingDbEntity codingDbEntityByBodysite) {
		this.codingDbEntityByBodysite = codingDbEntityByBodysite;
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
	@JoinColumn(name = "effectivePeriod")
	public PeriodDbEntity getPeriodDbEntity() {
		return this.periodDbEntity;
	}

	public void setPeriodDbEntity(PeriodDbEntity periodDbEntity) {
		this.periodDbEntity = periodDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "valueQuantity")
	public QuantityDbEntity getQuantityDbEntity() {
		return this.quantityDbEntity;
	}

	public void setQuantityDbEntity(QuantityDbEntity quantityDbEntity) {
		this.quantityDbEntity = quantityDbEntity;
	}

	@Column(name = "valueString")
	public String getValueString() {
		return this.valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "observationDbEntity")
	public Set<ProcedureMapDbEntity> getProcedureMapDbEntities() {
		return this.procedureMapDbEntities;
	}

	public void setProcedureMapDbEntities(Set<ProcedureMapDbEntity> procedureMapDbEntities) {
		this.procedureMapDbEntities = procedureMapDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "observationDbEntity")
	public Set<GoalMapDbEntity> getGoalMapDbEntities() {
		return this.goalMapDbEntities;
	}

	public void setGoalMapDbEntities(Set<GoalMapDbEntity> goalMapDbEntities) {
		this.goalMapDbEntities = goalMapDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "observationDbEntity")
	public Set<ObservationMapDbEntity> getObservationMapDbEntities() {
		return this.observationMapDbEntities;
	}

	public void setObservationMapDbEntities(Set<ObservationMapDbEntity> observationMapDbEntities) {
		this.observationMapDbEntities = observationMapDbEntities;
	}

}
