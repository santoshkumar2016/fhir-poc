package com.cantatahealth.fhir.db.util;

import com.cantatahealth.fhir.db.util.DbColumnMap;

public interface DbQueries {

	
	String resourceKeyQuery = "select resourcekey from resource where resourcename =':resourcename'";
	
	String childResourceQuery = "Select resourcekey, resourcename from resource where resourcekey in "
			+ "(select childresource from ResourceMap where parentresource "
			+ "= (select resourcekey from resource where resourcename =':resourcename'))";
	
	String childResourceByResourceKeyQuery = "Select resourcekey, resourcename from resource where resourcekey in "
			+ "(select childresource from ResourceMap_old where parentresource "
			+ "= :resourceKey)";
	
	String extensionQuery = "Select * from Extension where name = :nameValue";

	// select * from Address, HumanName where
	// address.resourceid=humanname.resourceid and address.resourceid=2
	// select * from Address, HumanName where
	// address.resourceid=humanname.resourceid and address.city='Newyork'
	String fullResourceQuery = "Select * from {0} where {1} and {2}";

	String fullPatientQuery = "select Patient." + DbColumnMap.id + "," + DbColumnMap.active + ","
			+ DbColumnMap.birthdate + "," + DbColumnMap.city + "," + DbColumnMap.country + "," + DbColumnMap.createdby
			+ "," + DbColumnMap.createddate + "," + DbColumnMap.deceasedboolean + "," + DbColumnMap.deceaseddatetime
			+ "," + DbColumnMap.district + "," + DbColumnMap.gender + "," + DbColumnMap.identifier + ","
			+ DbColumnMap.line + "," + DbColumnMap.multiplebirthboolean + "," + DbColumnMap.postalcode + ","
			+ DbColumnMap.state + "," + DbColumnMap.text + "," + DbColumnMap.type + "," + DbColumnMap.updatedby + ","
			+ DbColumnMap.updateddate + " from Patient, Address where Patient.id=Address.resourceid and ";

//	String childResourcesQuery = "Select resourcekey, Resourcename from Resource where "
//			+ "resourcekey in (select childresource from ResourceMap as rm left join Resource as r "
//			+ "on rm.parentresource=r.resourcekey where r.resourcename='{0}')";

}
