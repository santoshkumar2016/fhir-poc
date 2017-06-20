package com.nalashaa.fhir.server.acl.db.util;

import com.nalashaa.fhir.server.acl.service.util.EntityTableEnum;
import com.nalashaa.fhir.server.acl.service.util.ServiceUtil;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nalashaa.fhir.server.acl.db.model.PatientEn;

@Repository
public class HibernateUtil {

	@Autowired
	private SessionFactory sessionFactory;

	public <T> Serializable create(final T entity) {
		return sessionFactory.getCurrentSession().save(entity);
	}

	public <T> T update(final T entity) {
		sessionFactory.getCurrentSession().update(entity);
		return entity;
	}

	public <T> void delete(final T entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	public <T> void delete(Serializable id, Class<T> entityClass) {
		T entity = fetchById(id, entityClass);
		delete(entity);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchAll(Class<T> entityClass) {
		return sessionFactory.getCurrentSession().createQuery(" FROM " + entityClass.getName()).list();
	}

	@SuppressWarnings("rawtypes")
	public <T> List fetchAll(String query) {
		return sessionFactory.getCurrentSession().createSQLQuery(query).list();
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchById(Serializable id, Class<T> entityClass) {
		return (T) sessionFactory.getCurrentSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<PatientEn> findAllPatients(String query, List<Integer> cList) {
		return sessionFactory.getCurrentSession().createQuery(query).setParameterList("cidList", cList).list();
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchById(Class<T> entityClass, Serializable id) {
		return (T) sessionFactory.getCurrentSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByParamMap(Class<T> entityClass, Map<String, String> paramMap) {

		Iterator<String> keySet = paramMap.keySet().iterator();

		StringBuilder sqlQuery = new StringBuilder(
				"Select * from " + ServiceUtil.entityTableMap.get(entityClass.getSimpleName()) + " where ");

		String key = "";

		while (keySet.hasNext()) {
			key = keySet.next();
			sqlQuery.append(key).append(" = \'" + paramMap.get(key)).append("\' and ");
		}

		String str = sqlQuery.substring(0, sqlQuery.length() - 5);
		System.out.println("hql.substring(0, hql.length()-5) : " + str);

		return sessionFactory.getCurrentSession().createSQLQuery(str).addEntity(entityClass).list();
	}

}
