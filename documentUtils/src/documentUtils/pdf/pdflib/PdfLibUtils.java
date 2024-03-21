package documentUtils.pdf.pdflib;

import com.pdflib.PDFlibException;
import com.pdflib.pdflib;

import documentUtils.constants.PdfLibConstants;
import models.PdfHeader;

import static documentUtils.constants.PdfLibConstants.*;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class PdfLibUtils {
	
	private pdflib pdf;
	
	
	public PdfLibUtils() {
		super();
	}
	
	public PdfLibUtils(pdflib pdf) {
		this();
		this.pdf = pdf;
	}
	
	public void beginPage() throws PDFlibException {
		pdf.begin_page_ext(0, 0, PdfLibConstants.DEFAULT_PAGE_HEIGHT_AND_WIDTH);
	}

	public void createHeader (PdfHeader header) throws PDFlibException {
		pdf.rect(Header.X_POSITION.getValue()
				, Header.Y_POSITION.getValue()
				, Header.WIDTH.getValue()
				, Header.HEIGHT.getValue()
				);
		int headerFont = pdf.load_font(String.valueOf(header.getTitleFont())
										, Font.UNICODE
										, ""
										);
		pdf.stroke();
		pdf.setfont(headerFont
					, header.getTitleFontSize());
		pdf.fit_textline(header.getTitle()
						, HORIZONTAL_CENTER
						, Header.Y_POSITION.getValue() + HEADER_HEIGHT - MARGIN_SIZE
						, Options.POSITION + Position.CENTER
						);
	}
	
	public void createTitle (String text) throws PDFlibException {
		int titleFont = pdf.load_font(Font.HELVETICA_BOLD_OBLIQUE, Font.UNICODE, "");
		pdf.setfont(titleFont, Font.PAGE_TITLE_FONT_SIZE);
		pdf.fit_textline(text
				, HORIZONTAL_CENTER
				, PAGE_TITLE_POSITION
				, Options.POSITION + Position.CENTER
				);
	}
	
	public void createFooter (String[] footer, int currentRowPosition) throws PDFlibException {
		int titleFont = pdf.load_font(Font.HELVETICA_OBLIQUE, Font.UNICODE, "");
		pdf.setfont(titleFont, Font.PAGE_NUMBER_FONT_SIZE);
		currentRowPosition = MARGIN_SIZE + calculateFooterHeight(footer.length);
		for (String row : footer) {
			currentRowPosition -= Font.PAGE_NUMBER_FONT_SIZE;
			pdf.fit_textline(row
					, HORIZONTAL_CENTER
					, currentRowPosition
					, Options.POSITION + Position.CENTER
					);
		}
	}
	
	public void createPageNumber (int pageNumber) throws PDFlibException {
		setColorWhite();
		pdf.rect(MARGIN_SIZE
				, MARGIN_SIZE
				, PAGE_WIDTH
				, DEFAULT_ROW_HEIGHT
				);
		pdf.fill();
		setColorBlack();
		int pageNumberFont = pdf.load_font(Font.HELVETICA, Font.UNICODE, "");
		pdf.setfont(pageNumberFont, Font.PAGE_NUMBER_FONT_SIZE);
		pdf.fit_textline("" + pageNumber
						, HORIZONTAL_CENTER
						, MARGIN_SIZE / 2
						, ""
						);
	}
	
	public int createSimpleTable(String[] keys, String[] values, int startingIndex, final int TOTAL_COLUMNS) throws PDFlibException {
		int table = -1, row = 1, col = 1, totalColumns = (keys.length - 1);
		
		table = createSimpleTableTitle(table, col, row, keys, values, totalColumns, startingIndex);
		row++;
		table = createSimpleTableKeys(table, col, row, keys, values, totalColumns);
		row++;
		table = createSimpleTableValues(table, col, row, keys, values, totalColumns, startingIndex, TOTAL_COLUMNS);
		
		//pdf.stroke();
		return table;
	}
	
	public byte[] saveAsByteArray() throws IOException, PDFlibException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(pdf.get_buffer());
		byte[] file = stream.toByteArray();
		stream.close();
		return file;
	}
	
	public void setColorWhite() throws PDFlibException {
		pdf.setcolor(FILL, RGB, 1., 1., 1., 1.);
	}
	
	public void setColorBlack() throws PDFlibException {
		pdf.setcolor(FILL, RGB, 0., 0., 0., 0.);
	}
	
	public void setColorRed() throws PDFlibException {
		pdf.setcolor(FILL, RGB, 1., 0., 0., 0.);
	}
	
	public void setColorGreen() throws PDFlibException {
		pdf.setcolor(FILL, RGB, 0., 1., 0., 0.5);
	}
	
	public void setColorBlue() throws PDFlibException {
		pdf.setcolor(FILL, RGB, 0., 0., 1., 0.);
	}
	
	public void setColorCustomized(int red, int green, int blue, int opacity) throws PDFlibException {
		pdf.setcolor(FILL, RGB, rgbToFloat(red), rgbToFloat(green), rgbToFloat(blue), rgbToFloat(opacity));
	}
	
	public int calculateNewRowPosition(int currentLowerYPosition) {
		return currentLowerYPosition - DEFAULT_ROW_HEIGHT;
	}
	
	public int calculateFooterHeight(int rowsToPrint) {
		return rowsToPrint * DEFAULT_ROW_HEIGHT;
	}
	
	private float rgbToFloat(int color) {
		return color / 255;
	}
	
	private int createSimpleTableTitle(int table, int col, int row, String[] keys, String[] values, int totalColumns, int startingIndex) throws PDFlibException {
		int font = pdf.load_font(Font.HELVETICA_BOLD_OBLIQUE, Font.UNICODE, "");
		String titleCellOptions = Options.FIT_TEXTLINE + "{"
				+ Options.POSITION + Position.CENTER.getValue()
				+ " " + Font.FONT + font
				+ " " + Font.FONT_SIZE + "18}"
				+ " " + Options.COLUMN_SPAN + totalColumns
				;
		table = pdf.add_table_cell(table, 1, 1, keys[0] + " " + values[startingIndex - 1], titleCellOptions);
		return table;
	}
	
	private int createSimpleTableKeys(int table, int col, int row, String[] keys, String[] values, int totalColumns) throws PDFlibException {
		int font = pdf.load_font(Font.HELVETICA_BOLD, Font.UNICODE, "");
		String keysCellOptions = Options.FIT_TEXTLINE + "{"
				+ " " + Font.FONT + font
				+ " " + Font.FONT_SIZE + "14}"
				+ " " + Options.MARGIN + "2"
				+ " " + Options.COLUMN_WIDTH + (100 / totalColumns) + "%";
		for (int index = 1; index < keys.length; index++, col++) {
			table = pdf.add_table_cell(table, col, row, keys[index], keysCellOptions);
		}
		return table;
	}
	
	private int createSimpleTableValues(int table, int col, int row, String[] keys, String[] values, int totalColumns, int startingIndex, final int TOTAL_COLUMNS) throws PDFlibException {
		int font = pdf.load_font(Font.HELVETICA, Font.UNICODE, "");
		String valuesCellOptions = Options.FIT_TEXTLINE + "{"
				+ " " + Font.FONT + font
				+ " " + Font.FONT_SIZE + "10}"
				+ " " + Options.MARGIN + "4";
		for (int index = 0; index < TOTAL_COLUMNS; index++, col++) {
			table = pdf.add_table_cell(table, col, row, values[index + startingIndex], valuesCellOptions);
		}
		return table;
	}
}
