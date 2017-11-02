package com.cantatahealth.fhir.db.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.cantatahealth.fhir.db.model.AddressDbEntity;
import com.cantatahealth.fhir.db.model.HumanNameDbEntity;
import com.cantatahealth.fhir.db.model.PatientDbEntity;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;

@Repository
public class FhirDbUtil {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	
	public EntityManager getEntityManager(){
		return myEntityManager;
	}

	public List<Object[]> executeSQLQuery(String query) {
		List<Object[]> objList = myEntityManager.createNativeQuery(query).getResultList();

		return objList;
	}

	
	public  <T> void createResource(T entity) {
	   myEntityManager.persist(entity);

		//return objList;
	}
	
	
	public <T> T executeSQLQuery(String query, Class<T> entityClass) {
		T t = (T) myEntityManager.createNativeQuery(query, entityClass).getSingleResult();

		return t;
	}
	
	public <T> T executeSQLQuery(String query,String param,Object value, Class<T> entityClass) {
		T t = (T) myEntityManager.createNativeQuery(query, entityClass).setParameter(param, value).getSingleResult();

		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchById(Class<T> entityClass, Serializable id) {
		T t = (T) myEntityManager.find(entityClass, id);
		
		
//		PatientDbEntity map = new PatientDbEntity();
//		map.setBirthdate(new Date());
		//createResource(map);
		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchByName(Class<T> entityClass, String resourceName) {
		T t = (T) myEntityManager.createNamedQuery("resourceByName", entityClass)
				.setParameter("resourceName", resourceName).getSingleResult();
		return t;
	}

	public List<Object[]> getChildResources(String resourceName) {

		List<Object[]> objectList = executeSQLQuery(
				DbQueries.childResourceQuery.replace(":resourcename", resourceName));
		return objectList;
	}

	public List<Object[]> getChildResourcesByResourceKey(Long resourceKey) {

		List<Object[]> objectList = executeSQLQuery(
				DbQueries.childResourceByResourceKeyQuery.replace(":resourceKey", String.valueOf(resourceKey)));
		return objectList;
	}

	public List<HumanNameDbEntity> getHumanNames(Long resKey, Long id) {
		List<HumanNameDbEntity> addrDbList = myEntityManager
				.createNamedQuery("humanNameByTypeAndId", HumanNameDbEntity.class).setParameter("resourceType", resKey)
				.setParameter("resourceId", id).getResultList();
		return addrDbList;
	}

	public List<AddressDbEntity> getAddress(Long resKey, Long id) {
		List<AddressDbEntity> addrDbList = myEntityManager.createNamedQuery("addressByTypeAndId", AddressDbEntity.class)
				.setParameter("resourceType", resKey).setParameter("resourceId", id).getResultList();
		return addrDbList;
	}

	/**
	 * 
	 * @param paramMap
	 * @param patMapQuery
	 * @return
	 */
	public <T> List<T> findByParamMap(CriteriaQuery<T> patMapQuery) {

		TypedQuery<T> query = (TypedQuery<T>)myEntityManager.createQuery(patMapQuery);

		List<T> results = query.getResultList();

		return results != null ? results :new ArrayList<>();
	}
	
	/**
	 * This method is for period search parameters
	 * @param paramKey
	 * @param paramDate
	 * @param startColumnName
	 * @param endColumnName
	 * @param builder
	 * @param conditionRoot
	 * @return
	 */
	public <T,X> Predicate getPredicate(String paramKey, Date paramDate, String startColumnName,String endColumnName, CriteriaBuilder builder,
			Join<T,X> conditionRoot)
	{	
		Predicate p = null;
		if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS.name().equalsIgnoreCase(paramKey)) 
			p=builder.or(builder.isNull(conditionRoot.get(endColumnName)),builder.greaterThanOrEqualTo(conditionRoot.get(endColumnName),paramDate));
				
		if (ParamPrefixEnum.GREATERTHAN.name().equalsIgnoreCase(paramKey)) 
			p=builder.or(builder.isNull(conditionRoot.get(endColumnName)),builder.greaterThan(conditionRoot.get(endColumnName),paramDate));
			
		if (ParamPrefixEnum.LESSTHAN_OR_EQUALS.name().equalsIgnoreCase(paramKey)) 
			p=builder.lessThanOrEqualTo(conditionRoot.get(startColumnName),paramDate);
		
		if(ParamPrefixEnum.EQUAL.name().equalsIgnoreCase(paramKey))
			p=builder.equal(conditionRoot.get(startColumnName),paramDate);		
				
		if (ParamPrefixEnum.LESSTHAN.name().equalsIgnoreCase(paramKey)) 
			p=builder.lessThan(conditionRoot.get(startColumnName),paramDate);
		
		if(ParamPrefixEnum.EQUAL.name().equals(paramKey))
			p=builder.and(builder.lessThanOrEqualTo(conditionRoot.get(startColumnName),paramDate),builder.or(
				builder.isNull(conditionRoot.get(endColumnName)),builder.greaterThanOrEqualTo(conditionRoot.get(endColumnName),paramDate)));
					
		return p;
		}

	/**
	 * 
	 * Creates daterange predicate
	 * 
	 * @param rangePrefix
	 * @param paramDate
	 * @param columnName
	 * @param builder
	 * @param conditionRoot
	 * @return
	 */
	public <T> Predicate getPredicate(String rangePrefix, Date paramDate, String columnName, CriteriaBuilder builder,
			Root<T> conditionRoot) {
		Predicate p = null;
		if (rangePrefix.equals(ParamPrefixEnum.EQUAL.name())) {
			p = builder.equal(conditionRoot.get(columnName), paramDate);
		}

		if (rangePrefix.equals(ParamPrefixEnum.GREATERTHAN.name())) {
			p = builder.or(builder.isNull(conditionRoot.get(columnName)),
					builder.greaterThan(conditionRoot.get(columnName), paramDate));
		}

		if (rangePrefix.equals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS.name())) {
			p = builder.or(builder.isNull(conditionRoot.get(columnName)),
					builder.greaterThanOrEqualTo(conditionRoot.get(columnName), paramDate));
		}

		if (rangePrefix.equals(ParamPrefixEnum.LESSTHAN.name())) {
			p = builder.or(builder.isNull(conditionRoot.get(columnName)),
					builder.lessThan(conditionRoot.get(columnName), paramDate));
		}

		if (rangePrefix.equals(ParamPrefixEnum.LESSTHAN_OR_EQUALS.name())) {
			p = builder.or(builder.isNull(conditionRoot.get(columnName)),
					builder.lessThanOrEqualTo(conditionRoot.get(columnName), paramDate));
		}

		if (rangePrefix.equals(ParamPrefixEnum.NOT_EQUAL.name())) {
			p = builder.or(builder.isNull(conditionRoot.get(columnName)),
					builder.notEqual(conditionRoot.get(columnName), paramDate));
		}

		return p;
	}
	
	/**
	 * 
	 * @param paramMap
	
	public  List<PatientMapDbEntity> findPatientByParamMap(Map<String, String> paramMap){
		
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();

		CriteriaQuery<PatientMapDbEntity> patMapQuery = builder.createQuery(PatientMapDbEntity.class);
		Root<PatientMapDbEntity> pmapRoot = patMapQuery.from(PatientMapDbEntity.class);

		Join<PatientMapDbEntity, PatientDbEntity> pmapPatientJoin = pmapRoot.join("patientDbEntity");

		Iterator<String> paramMapKeyItr = paramMap.keySet().iterator();
		List<Predicate> predicateList = new ArrayList<Predicate>();

		while (paramMapKeyItr.hasNext()) {
			String paramKey = paramMapKeyItr.next();
			String paramValue = paramMap.get(paramKey);

			if (Patient.SP_FAMILY.equalsIgnoreCase(paramKey)) {
				Join<PatientMapDbEntity, HumanNameDbEntity> pmapNameJoin = pmapRoot.join("humanNameDbEntity");
				Predicate p = builder.equal(pmapNameJoin.get(Patient.SP_FAMILY), paramValue);
				predicateList.add(p);
			}

			if (Patient.SP_GIVEN.equalsIgnoreCase(paramKey)) {
				Join<PatientMapDbEntity, HumanNameDbEntity> patNameJoin1 = pmapRoot.join("humanNameDbEntity");
				Predicate p = builder.equal(patNameJoin1.get(Patient.SP_GIVEN), paramValue);
				predicateList.add(p);
			}

			if (Patient.SP_IDENTIFIER.equalsIgnoreCase(paramKey)) {
				Join<PatientMapDbEntity, IdentifierDbEntity> patIdentifierJoin = pmapRoot.join("identifierDbEntity");
				Predicate p = builder.equal(patIdentifierJoin.get("value"), paramValue);
				predicateList.add(p);
			}

			if (Patient.SP_GENDER.equalsIgnoreCase(paramKey)) {

				Predicate p = builder.equal(pmapPatientJoin.get(Patient.SP_GENDER), paramValue);
				predicateList.add(p);
			}

			if (Patient.SP_BIRTHDATE.equalsIgnoreCase(paramKey)) {

				Predicate p;
				try {
					p = builder.equal(pmapPatientJoin.get(Patient.SP_BIRTHDATE),
							AppUtil.formatToDateString(paramValue));
					predicateList.add(p);
				} catch (ParseException e) {

					e.printStackTrace();
				}
			}
		}

		Predicate p1 = builder.equal(pmapPatientJoin.get("id"), pmapRoot.get("patientDbEntity"));
		predicateList.add(p1);

		patMapQuery.select(pmapRoot).where(predicateList.toArray(new Predicate[predicateList.size()]));
		
		return findByParamMap(patMapQuery);
	}
	 */
}
