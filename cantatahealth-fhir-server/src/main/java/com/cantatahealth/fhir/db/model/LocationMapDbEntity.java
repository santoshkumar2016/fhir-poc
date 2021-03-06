package com.cantatahealth.fhir.db.model;
// Generated Oct 31, 2017 5:51:36 PM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LocationMapDbEntity generated by hbm2java
 */
@Entity
@Table(name = "LocationMap", schema = "dbo", catalog = "FHIRDB")
public class LocationMapDbEntity implements java.io.Serializable {

	private long id;
	private ContactPointDbEntity contactPointDbEntity;
	private LocationDbEntity locationDbEntity;
	private OrganizationDbEntity organizationDbEntity;

	public LocationMapDbEntity() {
	}

	public LocationMapDbEntity(long id, LocationDbEntity locationDbEntity) {
		this.id = id;
		this.locationDbEntity = locationDbEntity;
	}

	public LocationMapDbEntity(long id, ContactPointDbEntity contactPointDbEntity, LocationDbEntity locationDbEntity,
			OrganizationDbEntity organizationDbEntity) {
		this.id = id;
		this.contactPointDbEntity = contactPointDbEntity;
		this.locationDbEntity = locationDbEntity;
		this.organizationDbEntity = organizationDbEntity;
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
	@JoinColumn(name = "contactpoint")
	public ContactPointDbEntity getContactPointDbEntity() {
		return this.contactPointDbEntity;
	}

	public void setContactPointDbEntity(ContactPointDbEntity contactPointDbEntity) {
		this.contactPointDbEntity = contactPointDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location", nullable = false)
	public LocationDbEntity getLocationDbEntity() {
		return this.locationDbEntity;
	}

	public void setLocationDbEntity(LocationDbEntity locationDbEntity) {
		this.locationDbEntity = locationDbEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization")
	public OrganizationDbEntity getOrganizationDbEntity() {
		return this.organizationDbEntity;
	}

	public void setOrganizationDbEntity(OrganizationDbEntity organizationDbEntity) {
		this.organizationDbEntity = organizationDbEntity;
	}

}
