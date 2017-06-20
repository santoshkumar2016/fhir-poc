package com.nalashaa.fhir.dao;

import org.springframework.stereotype.Repository;

import com.nalashaa.fhir.db.model.OauthClientDetailsEn;


public interface OAuthDAO  {
	
	public void createClient(OauthClientDetailsEn client);
}
