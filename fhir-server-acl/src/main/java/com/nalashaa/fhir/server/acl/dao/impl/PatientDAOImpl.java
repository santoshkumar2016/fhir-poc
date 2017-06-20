package com.nalashaa.fhir.server.acl.dao.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nalashaa.fhir.server.acl.dao.PatientDAO;
import com.nalashaa.fhir.server.acl.db.model.PatientEn;
import com.nalashaa.fhir.server.acl.db.util.HibernateUtil;

@Repository
public class PatientDAOImpl implements PatientDAO {

	@Autowired
	HibernateUtil hibUtil;

	public Integer savePatient(PatientEn patientEn) {

		return (Integer) hibUtil.create(patientEn);

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

		return hibUtil.findAllPatients(hql, cList);

	}

	public void deletePatientById(Integer id) {
		// Query query = hibUtil.getSession().createQuery("delete from PatientEn
		// where cid = :cid");
		// query.setInteger("cid", id);
		// query.executeUpdate();

		hibUtil.delete(id, PatientEn.class);
	}

	public PatientEn findPatientById(int id) {

		return hibUtil.fetchById(PatientEn.class, id);
	}

	@Override
	public List<PatientEn> findPatientByParamMap(Map<String, String> paramMap) {

		return hibUtil.findByParamMap(PatientEn.class, paramMap);
	}

	public void updatePatient(PatientEn patient) {
		hibUtil.update(patient);

	}

}
