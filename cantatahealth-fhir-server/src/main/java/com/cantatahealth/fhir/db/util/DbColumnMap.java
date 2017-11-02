package com.cantatahealth.fhir.db.util;

import java.util.Date;

public interface DbColumnMap {
	
	// Patient
	 String  id = "id"; //long
	 String  identifier = "identifier"; //Long
	 String gender = "gender";
	 String birthdate = "birthdate"; //Date
	 String deceasedboolean = "deceasedboolean"; //Boolean
	 String deceaseddatetime = "deceaseddatetime"; // Date
	 String multiplebirthboolean = "multiplebirthboolean"; //Boolean
	
	 // Address
	 String type = "type";
	 String text = "text"; //Serializable
	 String line = "line";
	 String city = "city";
	 String district = "district";
	 String state = "state";
	 String postalcode = "postalcode";
	 String country = "country";
	 String createddate = "createddate"; //Date
	 String createdby = "createdby";
	 String updateddate = "updateddate"; //Date
	 String updatedby =  "updatedby";
	 String active = "active"; //Boolean
	 

}
