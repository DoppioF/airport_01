package customUtils.classes;

import customUtils.constants.strings.RegexConstants;

public class CustomStringUtils {

	public static boolean isStringNullOrEmpty(final String string) {
		return (null == string || string.trim().isEmpty());
	}
	
	public static boolean isStringParsableToByte(final String string) {
		try {
			Byte.parseByte(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isStringParsableToInteger(final String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isStringParsableToFloat(final String string) {
		try {
			Float.parseFloat(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isStringParsableToLong(final String string) {
		try {
			Long.parseLong(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isStringParsableToDouble(final String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isStringParsableToNumber(final String string) {
		return isStringParsableToDouble(string)
				|| isStringParsableToFloat(string)
				|| isStringParsableToInteger(string)
				|| isStringParsableToByte(string)
				|| isStringParsableToLong(string);
	}
	
	public static boolean isADate(final String string) {
		return string.matches(RegexConstants.DATE_DASH_PATTERN)
				|| string.matches(RegexConstants.DATE_SLASH_PATTERN);
	}
	
	public static String formatADateToDbCompatible(final String date) {
		String[] splittedInputDate = date.replaceAll("/", "-").split("-");
		String[] yodaDate = new String[splittedInputDate.length];
		for (int index = 0; index < splittedInputDate.length; index++) {
			String currentWord = splittedInputDate[splittedInputDate.length - 1 - index];
			yodaDate[index] = (1 == currentWord.length() ? "0" : "")
								+ currentWord;
		}
		return String.join("-", yodaDate);
	}
}
