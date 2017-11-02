package com.cantatahealth.fhir.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cantatahealth.fhir.db.model.DummyDbEntity;

/**
 * Maps DiagnosticReport data to DiagnosticReport resource
 * 
 * @author santosh
 *
 */
@Component
public class DiagnosticReportResourceMapper {

	Logger logger = LoggerFactory.getLogger(DiagnosticReportResourceMapper.class);

	/**
	 * 
	 * Convert DiagnosticReport db entity to DiagnosticReport resource
	 * 
	 * @param patientMapDbEntity
	 * @param DiagnosticReport
	 * @return
	 */
	public DiagnosticReport dbModelToResource(DummyDbEntity patientMapDbEntity, DiagnosticReport DiagnosticReport) {

		return DiagnosticReport;
	}

	/**
	 * 
	 * Convert list of DiagnosticReport db entities to list of DiagnosticReport resources
	 * 
	 * @param DiagnosticReportDbEntityList
	 * @return
	 */
	public List<DiagnosticReport> dbModelToResource(List<DummyDbEntity> DiagnosticReportDbEntityList) {

		Map<String, DiagnosticReport> DiagnosticReportMap = new HashMap<>();

		List<DiagnosticReport> DiagnosticReportList = new ArrayList<DiagnosticReport>(DiagnosticReportMap.values());

		return DiagnosticReportList;
	}

}
