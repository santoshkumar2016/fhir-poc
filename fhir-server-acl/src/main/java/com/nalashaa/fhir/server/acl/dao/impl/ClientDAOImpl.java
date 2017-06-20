package com.nalashaa.fhir.server.acl.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nalashaa.fhir.server.acl.dao.ClientDAO;
import com.nalashaa.fhir.server.acl.db.model.ClientDetails;
import com.nalashaa.fhir.server.acl.db.util.HibernateUtil;

@Repository
public class ClientDAOImpl implements ClientDAO {

	//@Autowired
	//private SessionFactory sessionFactory;
	
	@Autowired
	HibernateUtil hibUtil;

	public void createClient(ClientDetails client) {
		//sessionFactory.getCurrentSession().save(client);
		hibUtil.create(client);

	}

}
