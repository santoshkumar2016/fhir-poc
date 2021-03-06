package com.cantatahealth.fhir.db.model;
// Generated Oct 31, 2017 5:51:36 PM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CodingSystemDbEntity generated by hbm2java
 */
@Entity
@Table(name = "CodingSystem", schema = "dbo", catalog = "FHIRDB")
public class CodingSystemDbEntity implements java.io.Serializable {

	private long id;
	private String system;
	private String value;
	private String oid;

	public CodingSystemDbEntity() {
	}

	public CodingSystemDbEntity(long id) {
		this.id = id;
	}

	public CodingSystemDbEntity(long id, String system, String value, String oid) {
		this.id = id;
		this.system = system;
		this.value = value;
		this.oid = oid;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false, precision = 18, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "system")
	public String getSystem() {
		return this.system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	@Column(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "oid")
	public String getOid() {
		return this.oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

}
