package com.nalashaa.fhir.server.acl.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nalashaa.fhir.server.acl.dao.ImmunizationDAO;
import com.nalashaa.fhir.server.acl.db.model.ImmunizationEn;
import com.nalashaa.fhir.server.acl.db.util.HibernateUtil;

@Repository
public class ImmunizationDAOImpl implements ImmunizationDAO {
	
	@Autowired
	HibernateUtil hibUtil;

	public Integer saveImmunization(ImmunizationEn theImmunization){
		return (Integer) hibUtil.create(theImmunization);
	}

	public List<ImmunizationEn> findImmunizationByParamMap(Map<String, String> paramMap){
		return hibUtil.findByParamMap(ImmunizationEn.class, paramMap);
	}

	public ImmunizationEn findImmunizationById(int intValue){
		return hibUtil.fetchById(ImmunizationEn.class, intValue);
	}

	public void deleteImmunizationById(int id){
		hibUtil.delete(id, ImmunizationEn.class);
	}

	public void updateImmunization(ImmunizationEn theImmunization){
		hibUtil.update(theImmunization);
	}
}
