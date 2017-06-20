package com.nalashaa.fhir.server.acl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nalashaa.fhir.server.acl.dao.ClientDAO;
import com.nalashaa.fhir.server.acl.db.model.ClientDetails;
import com.nalashaa.fhir.server.acl.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientDAO clientDAO;

	public void saveClient(ClientDetails clientDetails) {

		clientDAO.createClient(clientDetails);

	}

}
