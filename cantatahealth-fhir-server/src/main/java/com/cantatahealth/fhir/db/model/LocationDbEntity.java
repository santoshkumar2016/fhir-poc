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
 * LocationDbEntity generated by hbm2java
 */
@Entity
@Table(name = "Location", schema = "dbo", catalog = "FHIRDB")
public class LocationDbEntity implements java.io.Serializable {

	private long id;
	private AddressDbEntity addressDbEntity;
	private CodingDbEntity codingDbEntityByType;
	private CodingDbEntity codingDbEntityByStatus;
	private CodingDbEntity codingDbEntityByOperationalstatus;
	private String name;
	private String description;
	private Date createddate;
	private String createdby;
	private Date updateddate;
	private String updatedby;
	private Set<ProcedureDbEntity> procedureDbEntities = new HashSet<ProcedureDbEntity>(0);
	private Set<EncounterMapDbEntity> encounterMapDbEntities = new HashSet<EncounterMapDbEntity>(0);
	private Set<ImmunizationMapDbEntity> immunizationMapDbEntities = new HashSet<ImmunizationMapDbEntity>(0);
	private Set<LocationMapDbEntity> locationMapDbEntities = new HashSet<LocationMapDbEntity>(0);

	public LocationDbEntity() {
	}

	public LocationDbEntity(long id) {
		this.id = id;
	}

	public LocationDbEntity(long id, AddressDbEntity addressDbEntity, CodingDbEntity codingDbEntityByType,
			CodingDbEntity codingDbEntityByStatus, CodingDbEntity codingDbEntityByOperationalstatus, String name,
			String description, Date createddate, String createdby, Date updateddate, String updatedby,
			Set<ProcedureDbEntity> procedureDbEntities, Set<EncounterMapDbEntity> encounterMapDbEntities,
			Set<ImmunizationMapDbEntity> immunizationMapDbEntities, Set<LocationMapDbEntity> locationMapDbEntities) {
		this.id = id;
		this.addressDbEntity = addressDbEntity;
		this.codingDbEntityByType = codingDbEntityByType;
		this.codingDbEntityByStatus = codingDbEntityByStatus;
		this.codingDbEntityByOperationalstatus = codingDbEntityByOperationalstatus;
		this.name = name;
		this.description = description;
		this.createddate = createddate;
		this.createdby = createdby;
		this.updateddate = updateddate;
		this.updatedby = updatedby;
		this.procedureDbEntities = procedureDbEntities;
		this.encounterMapDbEntities = encounterMapDbEntities;
		this.immunizationMapDbEntities = immunizationMapDbEntities;
		this.locationMapDbEntities = locationMapDbEntities;
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
	@JoinColumn(name = "address")
	public AddressDbEntity getAddressDbEntity() {
		return this.addressDbEntity;
	}

	public void setAddressDbEntity(AddressDbEntity addressDbEntity) {
		this.addressDbEntity = addressDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	public CodingDbEntity getCodingDbEntityByType() {
		return this.codingDbEntityByType;
	}

	public void setCodingDbEntityByType(CodingDbEntity codingDbEntityByType) {
		this.codingDbEntityByType = codingDbEntityByType;
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
	@JoinColumn(name = "operationalstatus")
	public CodingDbEntity getCodingDbEntityByOperationalstatus() {
		return this.codingDbEntityByOperationalstatus;
	}

	public void setCodingDbEntityByOperationalstatus(CodingDbEntity codingDbEntityByOperationalstatus) {
		this.codingDbEntityByOperationalstatus = codingDbEntityByOperationalstatus;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationDbEntity")
	public Set<ProcedureDbEntity> getProcedureDbEntities() {
		return this.procedureDbEntities;
	}

	public void setProcedureDbEntities(Set<ProcedureDbEntity> procedureDbEntities) {
		this.procedureDbEntities = procedureDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationDbEntity")
	public Set<EncounterMapDbEntity> getEncounterMapDbEntities() {
		return this.encounterMapDbEntities;
	}

	public void setEncounterMapDbEntities(Set<EncounterMapDbEntity> encounterMapDbEntities) {
		this.encounterMapDbEntities = encounterMapDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationDbEntity")
	public Set<ImmunizationMapDbEntity> getImmunizationMapDbEntities() {
		return this.immunizationMapDbEntities;
	}

	public void setImmunizationMapDbEntities(Set<ImmunizationMapDbEntity> immunizationMapDbEntities) {
		this.immunizationMapDbEntities = immunizationMapDbEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationDbEntity")
	public Set<LocationMapDbEntity> getLocationMapDbEntities() {
		return this.locationMapDbEntities;
	}

	public void setLocationMapDbEntities(Set<LocationMapDbEntity> locationMapDbEntities) {
		this.locationMapDbEntities = locationMapDbEntities;
	}

}
