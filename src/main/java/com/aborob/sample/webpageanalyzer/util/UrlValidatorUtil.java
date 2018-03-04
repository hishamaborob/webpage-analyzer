package com.aborob.sample.webpageanalyzer.util;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Very basic single validator util for the app, no need for Strategy Pattern :) !
 */
public class UrlValidatorUtil {

	private static final String[] SCHEMES = {"http", "https"};
	private static UrlValidator urlValidator;

	static {
		urlValidator = new UrlValidator(SCHEMES);
	}

	public static boolean isValidURL(String url) {

		if (urlValidator.isValid(url)) {
			return true;
		} else {
			return false;
		}
	}
}
