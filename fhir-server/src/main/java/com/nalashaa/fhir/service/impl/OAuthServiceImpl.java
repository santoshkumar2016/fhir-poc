package com.nalashaa.fhir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nalashaa.fhir.dao.OAuthDAO;
import com.nalashaa.fhir.dao.impl.OAuthDAOImpl;
import com.nalashaa.fhir.db.model.OauthClientDetailsEn;
import com.nalashaa.fhir.service.OAuthService;


@Service
@Transactional
public class OAuthServiceImpl implements OAuthService {
	
	//@Autowired
	OAuthDAO oAuthDAO = new OAuthDAOImpl(); 

	@Override
	public void saveClient(OauthClientDetailsEn client) {
		oAuthDAO.createClient(client);

	}

}
