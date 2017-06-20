package com.nalashaa.fhir.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nalashaa.fhir.dao.FhirDAOUtil;
import com.nalashaa.fhir.dao.PatientDAO;
import com.nalashaa.fhir.db.model.PatientEn;

@Repository
public class PatientDAOImpl implements PatientDAO {
	
	
	//@Autowired
	FhirDAOUtil fhirDAOUtil = new FhirDAOUtil();

	
//	 	@Autowired
//	    //@Qualifier("sessionFactoryBean")
//	    private static SessionFactory sessionFactory;
//	 	
//	 	Session getSession(){
//	 		return sessionFactory.getCurrentSession();
//	 	}
	
	public Integer savePatient(PatientEn patientEn) {
		
		return (Integer) fhirDAOUtil.create(patientEn);

	}

	@SuppressWarnings("unchecked")
	public List<PatientEn> findAllPatients() {

		String hql = "from PatientEn p where p.cid in (:cidList)";
		List<Integer> cList = new LinkedList<Integer>();
		cList.add(10);
		cList.add(11);
		cList.add(2441);
		cList.add(2442);
		cList.add(2443);
		cList.add(2444);

		//return (List<PatientEn>) getSession().createQuery(hql).setParameterList("cidList", cList).list();
		
		return fhirDAOUtil.findAllPatients(hql,cList);

	}

	public void deletePatientById(Integer id) {
		Query query = fhirDAOUtil.getSession().createQuery("delete from PatientEn where cid = :cid");
		query.setInteger("cid", id);
		query.executeUpdate();
		
		//fhirDAOUtil.delete(patient);
	}

	public PatientEn findPatientById(int id) {

		// Criteria cr = session.createCriteria(User.class)
		// .setProjection(Projections.projectionList()
		// .add(Projections.property("id"), "id")
		// .add(Projections.property("Name"), "Name"))
		// .setResultTransformer(Transformers.aliasToBean(User.class));

		// List<User> list = cr.list();

//		Criteria criteria = getSession().createCriteria(PatientEn.class);
//		// criteria.setProjection(Projections.projectionList().add(Projections.property("cid"),
//		// "cid")
//		// .add(Projections.property("city1"),
//		// "city1").add(Projections.property("company"), "company"));
//		// criteria.setResultTransformer(Transformers.aliasToBean(PatientEn.class));
//		criteria.add(Restrictions.eq("cid", id));
//
//		PatientEn patient = (PatientEn) criteria.uniqueResult();
//		// uniqueResult();
//		return patient;
		
		return fhirDAOUtil.fetchById(PatientEn.class,id);
	}

	public List<PatientEn> findPatientByName(String fname) {

//		Criteria criteria = getSession().createCriteria(PatientEn.class);
//		// criteria.setProjection(Projections.projectionList().add(Projections.property("cid"),
//		// "cid")
//		// .add(Projections.property("city1"),
//		// "city1").add(Projections.property("company"), "company"));
//		// criteria.setResultTransformer(Transformers.aliasToBean(PatientEn.class));
//		criteria.add(Restrictions.eq("fname", fname));
//
//		List<PatientEn> patientList = criteria.list();
//		// uniqueResult();
//		return patientList;
		return fhirDAOUtil.findByParam(PatientEn.class, "fname", fname);
	}

	@Override
	public List<PatientEn> findPatientByAddressCity(String addressCity) {
//		Criteria criteria = getSession().createCriteria(PatientEn.class);
//		criteria.add(Restrictions.eq("city1", addressCity));
//
//		List<PatientEn> patientList = criteria.list();
//
//		return patientList;
		
		return fhirDAOUtil.findByParam(PatientEn.class, "city1", addressCity);
	}
	
	@Override
	public List<PatientEn> findPatientByParamMap( HashMap<String, String> paramMap) {
		
		return fhirDAOUtil.findByParamMap(PatientEn.class, paramMap);
	}

	public void updatePatient(PatientEn patient) {
//		
//		Session session = fhirDAOUtil.getSession();
//		Query updateQuery = session.createQuery("update PatientEn p set where cid = :cid")
//		
//		session.close();
		
		fhirDAOUtil.update(patient);


	}

}
