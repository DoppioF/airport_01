package documentUtils.constants;

import static documentUtils.constants.PdfLibConstants.MARGIN_SIZE;
import static documentUtils.constants.PdfLibConstants.PAGE_WIDTH;

import documentUtils.constants.PdfLibConstants.Header;

public class PdfLibConstants {

	public final static int PAGE_HEIGHT 		= 800;
	public final static int PAGE_WIDTH 			= 550;
	public final static int DEFAULT_ROW_HEIGHT 	= 25;
	public final static int MARGIN_SIZE 		= 50;
	public final static int HEADER_HEIGHT 		= 150;
	public final static int HORIZONTAL_CENTER 	= (MARGIN_SIZE + PAGE_WIDTH) / 2;
	public final static int PAGE_TITLE_POSITION = Header.Y_POSITION.getValue() - MARGIN_SIZE;
	
	public final static String DEFAULT_PAGE_HEIGHT_AND_WIDTH 	= "width=a4.width height=a4.height";
	public final static String EXTENSION_PDF 					= ".pdf";
	public final static String FILL 							= "fill";
	public final static String RGB 								= "rgb";
	
	public final static class Font {
		public final static String HELVETICA 				= "Helvetica";
		public final static String HELVETICA_BOLD 			= "Helvetica-Bold";
		public final static String HELVETICA_OBLIQUE		= "Helvetica-Oblique";
		public final static String HELVETICA_BOLD_OBLIQUE 	= "Helvetica-BoldOblique";
		public final static String UNICODE 					= "unicode";
		public final static String FONT 					= "font=";
		public final static String FONT_SIZE 				= "fontsize=";
		
		public final static int PAGE_NUMBER_FONT_SIZE 	= 10;
		public final static int PAGE_TITLE_FONT_SIZE 	= 16;
		public final static int DEFAULT_FONT_SIZE 		= 12;
		public final static int TITLE_FONT_SIZE			= 22;
	}
	
	public final static class Options {
		public final static String FIT_TEXTLINE 		= "fittextline=";
		public final static String POSITION 			= "position=";
		public final static String COLUMN_SPAN 			= "colspan=";
		public final static String HEADER 				= "header=";
		public final static String STROKE_DEFAULT 		= "stroke={{line=other}}";
		public final static String DEFAULT_ROW_HEIGHT 	= "rowheightdefault=auto";
		public final static String COLUMN_WIDTH 		= "colwidth=";
		public final static String MARGIN 				= "margin=";
		public final static String ALIGNMENT			= "alignment=";
		public final static String JUSTIFY				= "justify";
		public final static String FONTNAME				= "fontname=";
		public final static String TABLE_COLOR			= "fill={{area=rowodd fillcolor={gray 0.9}}}";
	}
	
	public final static class Result {
		public final static String BOXFULL	= "_boxfull";
		public final static String STOP 	= "_stop";
	}
	
	public static enum Position {
		CENTER ("center");
		
		String value;
		
		Position(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public static enum Header {
		X_POSITION (MARGIN_SIZE)
		, Y_POSITION (PAGE_HEIGHT - HEADER_HEIGHT)
		, HEIGHT (HEADER_HEIGHT)
		, WIDTH (PAGE_WIDTH - MARGIN_SIZE);

		int value;
		
		Header(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
}
