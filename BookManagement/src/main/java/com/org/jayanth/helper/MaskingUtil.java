package com.org.jayanth.helper;

public final class MaskingUtil {

	public static String maskEmail(String email) {
	    return email.replaceAll("(?<=.).(?=[^@]*?@)", "*");
	}

}
