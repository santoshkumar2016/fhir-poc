package com.nalashaa.fhir.server.acl.service;

import com.nalashaa.fhir.server.acl.db.model.ClientDetails;

public interface ClientService {

	void saveClient(ClientDetails clientDetails);
}
