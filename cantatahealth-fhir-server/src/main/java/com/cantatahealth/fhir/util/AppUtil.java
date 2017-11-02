package com.cantatahealth.fhir.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class AppUtil {

	private static final SimpleDateFormat STRING_DATE_FORMAT_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat STRING_DATE_FORMAT_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

	public static Date formatToDateString(String stringDate) throws ParseException {

		if (StringUtils.isEmpty(stringDate)) {
			return null;
		} else {
			return STRING_DATE_FORMAT_yyyy_MM_dd.parse(stringDate);
		}
	}

	public static boolean isNotEmpty(String value) {
		boolean isNotEmpty = true;

		if (value == null)
			return false;

		if (value.isEmpty())
			return false;

		return isNotEmpty;

	}
}
