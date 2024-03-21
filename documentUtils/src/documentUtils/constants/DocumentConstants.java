package documentUtils.constants;

import documentUtils.constants.PdfLibConstants.Font;
import documentUtils.constants.PdfLibConstants.Options;

public class DocumentConstants {

	public final static String RESERVATION_SUMMARY_TITLE 				= "DATI PRENOTAZIONE";
	public final static String RESERVATION_SUMMARY_CUSTOMER 			= "Cliente";
	public final static String RESERVATION_SUMMARY_TAX_CODE 			= "Codice fiscale";
	public final static String RESERVATION_SUMMARY_RESERVATION_NUMBER 	= "N. prenotazione";
	public final static String RESERVATION_SUMMARY_RESERVATION_DATE 	= "Sottoscritta il";
	public final static String TICKET_LIST								= "Elenco biglietti";
	
	public final static String RESERVATION_SUMMARY_KEYS_OPTIONS		= Options.FONTNAME + Font.HELVETICA_BOLD
																		+ " " + Font.FONT_SIZE + Font.DEFAULT_FONT_SIZE
																		+ " " + Options.ALIGNMENT + Options.JUSTIFY;
	public final static String RESERVATION_SUMMARY_VALUES_OPTIONS 	= Options.FONTNAME + Font.HELVETICA
																		+ " " + Font.FONT_SIZE + Font.DEFAULT_FONT_SIZE
																		+ " " + Options.ALIGNMENT + Options.JUSTIFY;
	
}
