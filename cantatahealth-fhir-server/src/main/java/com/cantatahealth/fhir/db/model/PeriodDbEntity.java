package com.cantatahealth.fhir.db.model;
// Generated Oct 31, 2017 5:51:36 PM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PeriodDbEntity generated by hbm2java
 */
@Entity
@Table(name = "Period", schema = "dbo", catalog = "FHIRDB")
public class PeriodDbEntity implements java.io.Serializable {

	private long id;
	private Date start;
	private Date endAlias;
	private Date createddate;
	private String createdby;
	private Date updateddate;
	private String updatedby;
	private Set<CareTeamDbEntity> careTeamDbEntities = new HashSet<CareTeamDbEntity>(0);
	private ReferralRequestDbEntity referralRequestDbEntity;
	private Set<EncounterDbEntity> encounterDbEntities = new HashSet<EncounterDbEntity>(0);
	private Set<ProcedureDbEntity> procedureDbEntities = new HashSet<ProcedureDbEntity>(0);
	private Set<ProcedureDbEntity> procedureDbEntities_1 = new HashSet<ProcedureDbEntity>(0);
	private Set<AllergyIntoleranceDbEntity> allergyIntoleranceDbEntities = new HashSet<AllergyIntoleranceDbEntity>(0);
	private Set<ObservationDbEntity> observationDbEntities = new HashSet<ObservationDbEntity>(0);
	private Set<MedicationAdministrationDbEntity> medicationAdministrationDbEntities = new HashSet<MedicationAdministrationDbEntity>(
			0);

	public PeriodDbEntity() {
	}

	public PeriodDbEntity(long id) {
		this.id = id;
	}

	public PeriodDbEntity(long id, Date start, Date endAlias, Date createddate, String createdby, Date updateddate,
			String updatedby, Set<CareTeamDbEntity> careTeamDbEntities, ReferralRequestDbEntity referralRequestDbEntity,
			Set<EncounterDbEntity> encounterDbEntities, Set<ProcedureDbEntity> procedureDbEntities,
			Set<ProcedureDbEntity> procedureDbEntities_1, Set<AllergyIntoleranceDbEntity> allergyIntoleranceDbEntities,
			Set<ObservationDbEntity> observationDbEntities,
			Set<MedicationAdministrationDbEntity> medicationAdministrationDbEntities) {
		this.id = id;
		this.start = start;
		this.endAlias = endAlias;
		this.createddate = createddate;
		this.createdby = createdby;
		this.updateddate = updateddate;
		this.updatedby = updatedby;
		this.careTeamDbEntities = careTeamDbEntities;
		this.referralRequestDbEntity = referralRequestDbEntity;
		this.encounterDbEntities = encounterDbEntities;
		this.procedureDbEntities = procedureDbEntities;
		this.procedureDbEntities_1 = procedureDbEntities_1;
		this.allergyIntoleranceDbEntities = allergyIntoleranceDbEntities;
		this.observationDbEntities = observationDbEntities;
		this.medicationAdministrationDbEntities = medicationAdministrationDbEntities;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false, precision = 18, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start", length = 23)
	public Date getStart() {
		return this.start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_alias", length = 23)
	public Date getEndAlias() {
		return this.endAlias;
	}

	public void setEndAlias(Date endAlias) {
		this.endAlias = endAlias;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createddate", length = 23)
	public Date getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	@Column(name = "createdby")
	public String getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateddate", length = 23)
	public Date getUpdateddate() {
		return this.updateddate;
	}

	public void setUpdateddate(Date updateddate) {
		this.updateddate = updateddate;
	}

	@Column(name = "updatedby")
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public Set<CareTeamDbEntity> getCareTeamDbEntities() {
		return this.careTeamDbEntities;
	}

	public void setCareTeamDbEntities(Set<CareTeamDbEntity> careTeamDbEntities) {
		this.careTeamDbEntities = careTeamDbEntities;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public ReferralRequestDbEntity getReferralRequestDbEntity() {
		return this.referralRequestDbEntity;
	}

	public void setReferralRequestDbEntity(ReferralRequestDbEntity referralRequestDbEntity) {
		this.referralRequestDbEntity = referralRequestDbEntity;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public Set<EncounterDbEntity> getEncounterDbEntities() {
		return this.encounterDbEntities;
	}

	public void setEncounterDbEntities(Set<EncounterDbEntity> encounterDbEntities) {
		this.encounterDbEntities = encounterDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public Set<ProcedureDbEntity> getProcedureDbEntities() {
		return this.procedureDbEntities;
	}

	public void setProcedureDbEntities(Set<ProcedureDbEntity> procedureDbEntities) {
		this.procedureDbEntities = procedureDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public Set<ProcedureDbEntity> getProcedureDbEntities_1() {
		return this.procedureDbEntities_1;
	}

	public void setProcedureDbEntities_1(Set<ProcedureDbEntity> procedureDbEntities_1) {
		this.procedureDbEntities_1 = procedureDbEntities_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public Set<AllergyIntoleranceDbEntity> getAllergyIntoleranceDbEntities() {
		return this.allergyIntoleranceDbEntities;
	}

	public void setAllergyIntoleranceDbEntities(Set<AllergyIntoleranceDbEntity> allergyIntoleranceDbEntities) {
		this.allergyIntoleranceDbEntities = allergyIntoleranceDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public Set<ObservationDbEntity> getObservationDbEntities() {
		return this.observationDbEntities;
	}

	public void setObservationDbEntities(Set<ObservationDbEntity> observationDbEntities) {
		this.observationDbEntities = observationDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodDbEntity")
	public Set<MedicationAdministrationDbEntity> getMedicationAdministrationDbEntities() {
		return this.medicationAdministrationDbEntities;
	}

	public void setMedicationAdministrationDbEntities(
			Set<MedicationAdministrationDbEntity> medicationAdministrationDbEntities) {
		this.medicationAdministrationDbEntities = medicationAdministrationDbEntities;
	}

}
