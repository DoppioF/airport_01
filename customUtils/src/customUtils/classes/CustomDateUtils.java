package customUtils.classes;

import java.time.LocalDate;
import java.time.Period;

public class CustomDateUtils {

	public static boolean isAdult(final LocalDate date, final int adultAge) {
		return date.isBefore(LocalDate.now().minus(Period.ofYears(adultAge)));
	}
	
	public static boolean isTooOldAge(final LocalDate date, final int maxAge) {
		return date.isBefore(LocalDate.now().minus(Period.ofYears(maxAge)));
	}
	
	public static boolean isFutureDate(final LocalDate date) {
		return date.isAfter(LocalDate.now());
	}
}
