package com.nalashaa.fhir.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;


public abstract class AbstractDAO {
 
    //@Autowired
    //@Qualifier("sessionFactoryBean")
    private static SessionFactory sessionFactory;
    
    public static void setSessionFactory(SessionFactory sFactory){
    	sessionFactory = sFactory;
    }
 
    protected Session getSession() {
    	
//    	return sessionFactory.openSession();
    	Session session;

    	try {
    	    session = sessionFactory.getCurrentSession();
    	} catch (HibernateException e) {
    	    session = sessionFactory.openSession();
    	}
        return session;
    }
 
    public void persist(Object entity) {
        getSession().persist(entity);
    }
 
    public void delete(Object entity) {
        getSession().delete(entity);
    }
}
