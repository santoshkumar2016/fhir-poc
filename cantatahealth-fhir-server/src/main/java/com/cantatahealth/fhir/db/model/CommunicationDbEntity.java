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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CommunicationDbEntity generated by hbm2java
 */
@Entity
@Table(name = "Communication", schema = "dbo", catalog = "FHIRDB")
public class CommunicationDbEntity implements java.io.Serializable {

	private long id;
	private String language;
	private Boolean preferred;
	private Date createddate;
	private String createdby;
	private Date updateddate;
	private String updatedby;
	private Set<PatientMapDbEntity> patientMapDbEntities = new HashSet<PatientMapDbEntity>(0);

	public CommunicationDbEntity() {
	}

	public CommunicationDbEntity(long id) {
		this.id = id;
	}

	public CommunicationDbEntity(long id, String language, Boolean preferred, Date createddate, String createdby,
			Date updateddate, String updatedby, Set<PatientMapDbEntity> patientMapDbEntities) {
		this.id = id;
		this.language = language;
		this.preferred = preferred;
		this.createddate = createddate;
		this.createdby = createdby;
		this.updateddate = updateddate;
		this.updatedby = updatedby;
		this.patientMapDbEntities = patientMapDbEntities;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false, precision = 18, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "language")
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "preferred")
	public Boolean getPreferred() {
		return this.preferred;
	}

	public void setPreferred(Boolean preferred) {
		this.preferred = preferred;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "communicationDbEntity")
	public Set<PatientMapDbEntity> getPatientMapDbEntities() {
		return this.patientMapDbEntities;
	}

	public void setPatientMapDbEntities(Set<PatientMapDbEntity> patientMapDbEntities) {
		this.patientMapDbEntities = patientMapDbEntities;
	}

}
