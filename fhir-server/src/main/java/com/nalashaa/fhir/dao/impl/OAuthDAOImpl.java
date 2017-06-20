package com.nalashaa.fhir.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nalashaa.fhir.dao.FhirDAOUtil;
import com.nalashaa.fhir.dao.OAuthDAO;
import com.nalashaa.fhir.db.model.OauthClientDetailsEn;

@Repository("oAuthDAO")
public class OAuthDAOImpl implements OAuthDAO {
	
//	@Autowired
//    private static SessionFactory sessionFactory;
// 	
// 	Session getSession(){
// 		return sessionFactory.getCurrentSession();
// 	}
	
	//@Autowired
	FhirDAOUtil fhirDAOUtil = new FhirDAOUtil();

	@Override
	public void createClient(OauthClientDetailsEn client) {
//		Session session = getSession();
//		session.beginTransaction();
//		session.save(client);
//		session.getTransaction().commit();
		
		fhirDAOUtil.create(client);
		

	}

}
