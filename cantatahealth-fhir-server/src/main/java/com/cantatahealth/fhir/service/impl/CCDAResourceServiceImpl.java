package com.cantatahealth.fhir.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.model.CCDAResource;
import com.cantatahealth.fhir.service.CCDAResourceService;

/**
 * 
 * Implementation class for CCDAResource service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class CCDAResourceServiceImpl implements CCDAResourceService {

//	@Autowired
//	CCDAResourceDAO CCDAResourceDAO;
//
//	@Autowired
//	CCDAResourceResourceMapper CCDAResourceMapper;

	@Override
	public CCDAResource findCCDAResourceById(Long id) {

		CCDAResource CCDAResource = new CCDAResource();

//		DummyDbEntity CCDAResourceDbEntity = CCDAResourceDAO.findCCDAResourceById(id);
//
//		if (CCDAResourceDbEntity == null)
//			return CCDAResource;
//
//		CCDAResource = CCDAResourceMapper.dbModelToResource(CCDAResourceDbEntity, CCDAResource);

		return CCDAResource;

	}

	public List<CCDAResource> findCCDAResourceByParamMap(Map<String, String> paramMap) {

//		List<DummyDbEntity> patMapDbEntityList = CCDAResourceDAO.findCCDAResourceByParamMap(paramMap);
//
//		List<CCDAResource> CCDAResourceList = CCDAResourceMapper.dbModelToResource(patMapDbEntityList);

		return new ArrayList<CCDAResource>();
	}

}
