package com.nalashaa.fhir.server.acl.dao;

import com.nalashaa.fhir.server.acl.db.model.ClientDetails;


public interface ClientDAO  {
	
	public void createClient(ClientDetails client);
}
