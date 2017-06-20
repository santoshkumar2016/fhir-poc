package com.nalashaa.fhir.server.acl.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nalashaa.fhir.server.acl.dao.ImmunizationDAO;
import com.nalashaa.fhir.server.acl.db.model.ImmunizationEn;
import com.nalashaa.fhir.server.acl.service.ImmunizationtService;
import com.nalashaa.fhir.server.acl.service.util.ImmunizationServiceUtil;

import ca.uhn.fhir.model.dstu2.resource.Immunization;

@Service
@Transactional
public class ImmunizationtServiceImpl implements ImmunizationtService {

	@Autowired
	ImmunizationDAO immunizationDAO;

	@Autowired
	ImmunizationServiceUtil modelMapper;

	@Override
	public Integer saveImmunization(Immunization theImmunization) {

		return immunizationDAO.saveImmunization(modelMapper.resourceToEntityConverter(theImmunization));
	}

	@Override
	public List<Immunization> findImmunizationByParamMap(Map<String, String> paramMap) {

		List<Immunization> immunizationList = new ArrayList<Immunization>();

		List<ImmunizationEn> immunizationEntityList = new ArrayList<ImmunizationEn>();
		immunizationEntityList = immunizationDAO.findImmunizationByParamMap(modelMapper.getColumnMap(paramMap));

		Iterator<ImmunizationEn> itr = immunizationEntityList.iterator();

		while (itr.hasNext()) {
			immunizationList.add(modelMapper.entityToResourceConverter(itr.next()));
		}

		return immunizationList;
	}

	@Override
	public Immunization findImmunizationById(int id) {

		return modelMapper.entityToResourceConverter(immunizationDAO.findImmunizationById(id));
	}

	@Override
	public void deleteImmunizationById(int intValue) {
		immunizationDAO.deleteImmunizationById(intValue);

	}

	@Override
	public void updateImmunization(Immunization theImmunization) {
		immunizationDAO.updateImmunization(modelMapper.resourceToEntityConverter(theImmunization));

	}

}
