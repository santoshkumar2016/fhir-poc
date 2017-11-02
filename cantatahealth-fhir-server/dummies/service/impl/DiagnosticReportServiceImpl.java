package com.cantatahealth.fhir.service.impl;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cantatahealth.fhir.db.dao.DiagnosticReportDAO;
import com.cantatahealth.fhir.db.model.DummyDbEntity;
import com.cantatahealth.fhir.service.DiagnosticReportService;
import com.cantatahealth.fhir.service.util.DiagnosticReportResourceMapper;

/**
 * 
 * Implementation class for DiagnosticReport service
 * 
 * @author santosh
 *
 */
@Service
@Transactional
public class DiagnosticReportServiceImpl implements DiagnosticReportService {

	@Autowired
	DiagnosticReportDAO DiagnosticReportDAO;

	@Autowired
	DiagnosticReportResourceMapper DiagnosticReportMapper;

	@Override
	public DiagnosticReport findDiagnosticReportById(Long id) {

		DiagnosticReport DiagnosticReport = new DiagnosticReport();

		DummyDbEntity DiagnosticReportDbEntity = DiagnosticReportDAO.findDiagnosticReportById(id);

		if (DiagnosticReportDbEntity == null)
			return DiagnosticReport;

		DiagnosticReport = DiagnosticReportMapper.dbModelToResource(DiagnosticReportDbEntity, DiagnosticReport);

		return DiagnosticReport;

	}

	public List<DiagnosticReport> findDiagnosticReportByParamMap(Map<String, String> paramMap) {

		List<DummyDbEntity> patMapDbEntityList = DiagnosticReportDAO.findDiagnosticReportByParamMap(paramMap);

		List<DiagnosticReport> DiagnosticReportList = DiagnosticReportMapper.dbModelToResource(patMapDbEntityList);

		return DiagnosticReportList;
	}

}
