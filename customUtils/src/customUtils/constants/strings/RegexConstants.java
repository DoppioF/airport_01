package customUtils.constants.strings;

public class RegexConstants {
	
	public static final String EMAIL_PATTERN = 
			"^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$";
	
	public static final String NAMES_PATTERN =
			"^[A-Za-z\\s\\-\\']+$";
	
	public static final String ADDRESS_PATTERN =
			"[\\w',-\\/\\.\\s]+";
	
	public static final String PASSWORD_PATTERN =
			"^[a-zA-Z0-9\\_\\-\\.\\?\\!]+$";
	
	public static final String GENERIC_ANTI_QUERY_PATTERN = 
			"^[a-zA-Z0-9\\_\\-\\.\\?\\!\\s]*$";
	
	public static final String ONE_OR_MORE_NUMBERS =
			"\\d+";
	
	public static final String TAX_CODE_PATTERN =
			"^[A-Z0-9]{16}$";
	
	public static final String IDENTITY_CARD_PATTERN =
			"^[a-zA-Z0-9\\_\\-\\.]+$";
	
	public static final String PHONE_NUMBER_PATTERN =
			"^[0-9\\-\\+]*$";
	
	public static final String DATE_SLASH_PATTERN =
			"^[\\d]{1,2}[/][\\d]{1,2}(([/][\\d]{4})|([/][\\d]{2}))?$";
	
	public static final String DATE_DASH_PATTERN =
			"^[\\d]{1,2}[-][\\d]{1,2}(([-][\\d]{4})|([-][\\d]{2}))?$";
}
