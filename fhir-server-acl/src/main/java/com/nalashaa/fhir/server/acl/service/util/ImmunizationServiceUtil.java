package com.nalashaa.fhir.server.acl.service.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nalashaa.fhir.server.acl.db.model.ImmunizationEn;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;

@Component
public class ImmunizationServiceUtil {

	Logger logger = LoggerFactory.getLogger(ImmunizationServiceUtil.class);

	public static Map<String, String> immunizationFieldToColumnMap = new HashMap<String, String>();

	static {

		immunizationFieldToColumnMap.put(Immunization.SP_IDENTIFIER, "immzId");
		immunizationFieldToColumnMap.put(Immunization.SP_DATE, "dateTimeAdministered");
		//immunizationFieldToColumnMap.put(Immunization.SP_DOSE_SEQUENCE, "immzId");
		immunizationFieldToColumnMap.put(Immunization.SP_LOT_NUMBER, "vaccineLot");
		immunizationFieldToColumnMap.put(Immunization.SP_MANUFACTURER, "manufacturer");
	}

	public Map<String, String> getColumnMap(Map<String, String> paramMap) {

		Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();

		while (iterator.hasNext()) {

			Entry<String, String> paramValue = iterator.next();
			String key = paramValue.getKey();
			String value = paramValue.getValue();
			switch (key) {
			case Immunization.SP_IDENTIFIER:
				logger.debug("Search parameter identifier:{}", paramValue);
				iterator.remove();
				paramMap.put(immunizationFieldToColumnMap.get(Immunization.SP_IDENTIFIER), value);
				break;

			case Immunization.SP_MANUFACTURER:
				logger.debug("Search parameter date:{}", paramValue);
				iterator.remove();
				paramMap.put(immunizationFieldToColumnMap.get(Immunization.SP_MANUFACTURER), value);
				break;

			case Immunization.SP_LOT_NUMBER:
				logger.debug("Search parameter dose-sequence:{}", paramValue);
				iterator.remove();
				paramMap.put(immunizationFieldToColumnMap.get(Immunization.SP_LOT_NUMBER), value);
				break;

			}
		}

		return paramMap;
	}
	
	
	public ImmunizationEn resourceToEntityConverter(Immunization theImmunization) {
		ImmunizationEn immunizationEn = new ImmunizationEn();
		if (!theImmunization.getDateElement().isEmpty()) {
			immunizationEn.setDateTimeAdministered(theImmunization.getDate());
		}

		if (!theImmunization.getExpirationDateElement().isEmpty()) {
			immunizationEn.setExpirationDate(theImmunization.getExpirationDate());
		}

		if (!theImmunization.getIdElement().isEmpty()) {
			immunizationEn.setImmzId(theImmunization.getId().getIdPartAsLong().intValue());
		}

		if (!theImmunization.getLotNumberElement().isEmpty()) {
			immunizationEn.setVaccineLot(theImmunization.getLotNumber());
		}

		if (!theImmunization.getManufacturer().isEmpty()) {
			immunizationEn.setManufacturer(theImmunization.getManufacturer().getElementSpecificId());
		}

		if (!theImmunization.getRoute().isEmpty()) {
			immunizationEn.setAdministrationRoute(theImmunization.getRoute().getText());
		}

		if (!theImmunization.getStatusElement().isEmpty()) {
			immunizationEn.setRegistryStatus(theImmunization.getStatus());
		}

		if (!theImmunization.getVaccineCode().isEmpty()) {
			immunizationEn.setVaccineLot(theImmunization.getVaccineCode().getText());
		}

		return immunizationEn;
	}

	public Immunization entityToResourceConverter(ImmunizationEn immunizationEn) {

		logger.debug("Converting Immunization entity with Id:{} to Immunization resource.", immunizationEn.getImmzId());

		Immunization immunization = new Immunization();
		immunization.setDate(new DateTimeDt(immunizationEn.getDateTimeAdministered()));
		// immunization.setDoseQuantity();
		// immunization.setEncounter(immunizationEn.)
		immunization.setExpirationDate(new DateDt(immunizationEn.getExpirationDate()));
		// immunization.setExplanation(immunizationEn.gete);
		immunization.setId(new IdDt(immunizationEn.getImmzId()));
		// immunization.setLanguage(immunizationEn.get);
		// immunization.setLocation(immunizationEn.getAdministeredLocation());
		immunization.setLotNumber(immunizationEn.getVaccineLot());
		immunization.setManufacturer(new ResourceReferenceDt(immunizationEn.getManufacturer()));
		// immunization.setPatient(new ResourceReferenceDt(immunizationEn.get)
		// immunization.setPerformer(new ResourceReferenceDt(immunizationEn.get)
		// immunization.setReaction(immunizationEn.get)
		// immunization.setReported(immunizationEn.get)
		// immunization.setResourceMetadata(theMap);
		immunization.setRoute(new CodeableConceptDt("system", immunizationEn.getAdministrationRoute()));
		// immunization.setSite(immunizationEn.get)
		immunization.setStatus(immunizationEn.getRegistryStatus());
		// immunization.setText(immunizationEn.getAdministrationNote());
		// immunization.setVaccinationProtocol(theValue);
		immunization.setVaccineCode(new CodeableConceptDt("system", immunizationEn.getVaccineLot()));
		// immunization.setWasNotGiven(theBoolean);

		return immunization;

	}

}
