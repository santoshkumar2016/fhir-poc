package com.nalashaa.fhir.service;

import com.nalashaa.fhir.db.model.OauthClientDetailsEn;

public interface OAuthService {

	void saveClient(OauthClientDetailsEn client);
}
