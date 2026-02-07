package com.org.jayanth.helper;

public final class MaskingUtil {

	private MaskingUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
	public static String maskEmail(String email) {
	    return email.replaceAll("(?<=.).(?=[^@]*?@)", "*");
	}

}
