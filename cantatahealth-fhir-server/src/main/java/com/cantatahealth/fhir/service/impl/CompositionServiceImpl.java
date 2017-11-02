package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.CompositionDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.CompositionService;
import com.cantatahealth.fhir.service.util.CompositionResourceMapper;

/**
 * 
 * Implementation class for Composition service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class CompositionServiceImpl implements CompositionService {

	@Autowired
	CompositionDAO CompositionDAO;

	@Autowired
	CompositionResourceMapper CompositionMapper;

	@Override
	public Composition findCompositionById(Long id) {

		Composition Composition = new Composition();

		DummyDbEntity CompositionDbEntity = CompositionDAO.findCompositionById(id);

		if (CompositionDbEntity == null)
			return Composition;

		Composition = CompositionMapper.dbModelToResource(CompositionDbEntity, Composition);

		return Composition;

	}

	public List<Composition> findCompositionByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = CompositionDAO.findCompositionByParamMap(paramMap);

		List<Composition> CompositionList = CompositionMapper.dbModelToResource(patMapDbEntityList);

		return CompositionList;
	}

}
