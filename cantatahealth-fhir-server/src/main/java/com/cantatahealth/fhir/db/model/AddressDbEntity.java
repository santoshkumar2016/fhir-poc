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
 * AddressDbEntity generated by hbm2java
 */
@Entity
@Table(name = "Address", schema = "dbo", catalog = "FHIRDB")
public class AddressDbEntity implements java.io.Serializable {

	private long id;
	private CodingDbEntity codingDbEntity;
	private String line;
	private String city;
	private String state;
	private String postalcode;
	private String country;
	private Date createddate;
	private String createdby;
	private Date updateddate;
	private String updatedby;
	private Set<LocationDbEntity> locationDbEntities = new HashSet<LocationDbEntity>(0);
	private Set<RelatedPersonDbEntity> relatedPersonDbEntities = new HashSet<RelatedPersonDbEntity>(0);
	private Set<PatientMapDbEntity> patientMapDbEntities = new HashSet<PatientMapDbEntity>(0);
	private Set<PractitionerMapDbEntity> practitionerMapDbEntities = new HashSet<PractitionerMapDbEntity>(0);

	public AddressDbEntity() {
	}

	public AddressDbEntity(long id) {
		this.id = id;
	}

	public AddressDbEntity(long id, CodingDbEntity codingDbEntity, String line, String city, String state,
			String postalcode, String country, Date createddate, String createdby, Date updateddate, String updatedby,
			Set<LocationDbEntity> locationDbEntities, Set<RelatedPersonDbEntity> relatedPersonDbEntities,
			Set<PatientMapDbEntity> patientMapDbEntities, Set<PractitionerMapDbEntity> practitionerMapDbEntities) {
		this.id = id;
		this.codingDbEntity = codingDbEntity;
		this.line = line;
		this.city = city;
		this.state = state;
		this.postalcode = postalcode;
		this.country = country;
		this.createddate = createddate;
		this.createdby = createdby;
		this.updateddate = updateddate;
		this.updatedby = updatedby;
		this.locationDbEntities = locationDbEntities;
		this.relatedPersonDbEntities = relatedPersonDbEntities;
		this.patientMapDbEntities = patientMapDbEntities;
		this.practitionerMapDbEntities = practitionerMapDbEntities;
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
	@JoinColumn(name = "use_alias")
	public CodingDbEntity getCodingDbEntity() {
		return this.codingDbEntity;
	}

	public void setCodingDbEntity(CodingDbEntity codingDbEntity) {
		this.codingDbEntity = codingDbEntity;
	}

	@Column(name = "line")
	public String getLine() {
		return this.line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	@Column(name = "city")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "state")
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "postalcode")
	public String getPostalcode() {
		return this.postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	@Column(name = "country")
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "addressDbEntity")
	public Set<LocationDbEntity> getLocationDbEntities() {
		return this.locationDbEntities;
	}

	public void setLocationDbEntities(Set<LocationDbEntity> locationDbEntities) {
		this.locationDbEntities = locationDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "addressDbEntity")
	public Set<RelatedPersonDbEntity> getRelatedPersonDbEntities() {
		return this.relatedPersonDbEntities;
	}

	public void setRelatedPersonDbEntities(Set<RelatedPersonDbEntity> relatedPersonDbEntities) {
		this.relatedPersonDbEntities = relatedPersonDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "addressDbEntity")
	public Set<PatientMapDbEntity> getPatientMapDbEntities() {
		return this.patientMapDbEntities;
	}

	public void setPatientMapDbEntities(Set<PatientMapDbEntity> patientMapDbEntities) {
		this.patientMapDbEntities = patientMapDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "addressDbEntity")
	public Set<PractitionerMapDbEntity> getPractitionerMapDbEntities() {
		return this.practitionerMapDbEntities;
	}

	public void setPractitionerMapDbEntities(Set<PractitionerMapDbEntity> practitionerMapDbEntities) {
		this.practitionerMapDbEntities = practitionerMapDbEntities;
	}

}
