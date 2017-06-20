package com.nalashaa.fhir.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nalashaa.fhir.db.model.PatientEn;

@Repository
public class FhirDAOUtil {

	@Autowired
	private static SessionFactory sessionFactory;

	public static void setSessionFactory(SessionFactory sFactory) {
		sessionFactory = sFactory;
	}

	public Session getSession() {
		
		return sessionFactory.openSession();
		
//		Session session;
//		try {
//			session = sessionFactory.getCurrentSession();
//		} catch (HibernateException e) {
//			session = sessionFactory.openSession();
//		}
//		return session;
	}

	public <T> Serializable create(final T entity) {
		return getSession().save(entity);
	}

	public <T> void update(final T entity) {
		//getSession().update(entity);
		
		Session session = getSession();
		Transaction trx = session.beginTransaction();		
		session.update(entity);
		trx.commit();
		
		//session.flush();
		//return entity;
	}

	public <T> void delete(final T entity) {
		getSession().delete(entity);
	}

	public <T> void delete(Serializable id, Class<T> entityClass) {
		T entity = fetchById(entityClass, id);
		delete(entity);
	}
	
	public <T> void deleteByParam(Class<T> entityClass, String paramName, String paramValue) {
//		Query query = getSession().createQuery("delete from "+entityClass+" where " + paramName +" = :" +paramName);
//		query.setInteger(paramName, paramValue);
//		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchAll(Class<T> entityClass) {
		return getSession().createQuery(" FROM " + entityClass.getName()).list();
	}

	@SuppressWarnings("unchecked")
	public List<PatientEn> findAllPatients(String query, List<Integer> cList) {
		return getSession().createQuery(query).setParameterList("cidList", cList).list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByParam(Class<T> entityClass, String paramName, String paramValue) {
		return getSession().createCriteria(entityClass).add(Restrictions.eq(paramName, paramValue)).list();

	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByParamMap(Class<T> entityClass, HashMap<String, String> paramMap) {
		
		Iterator<String> keySet = paramMap.keySet().iterator();
		
		
		StringBuilder sqlQuery = new StringBuilder("Select * from contacts " +"where ");
		
		String key = "";
		
		while(keySet.hasNext()){
			key = keySet.next();
			sqlQuery.append(key).append(" = \'"+paramMap.get(key)).append("\' and ");
		}
		
		String str = sqlQuery.substring(0, sqlQuery.length()-5);
		System.out.println("hql.substring(0, hql.length()-5) : " + str);

		return getSession().createSQLQuery(str).addEntity(entityClass).list();
	}

	@SuppressWarnings("rawtypes")
	public <T> List fetchAll(String query) {
		return getSession().createQuery(query).list();
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchById(Class<T> entityClass, Serializable id) {
		return (T) getSession().get(entityClass, id);
	}

}
