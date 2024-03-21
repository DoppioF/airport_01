package controller;

import static documentUtils.constants.PdfLibConstants.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.pdflib.PDFlibException;
import com.pdflib.pdflib;

import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.TicketDto;
import customUtils.constants.TicketDocumentConstants;
import documentUtils.constants.DocumentConstants;
import documentUtils.constants.PdfLibConstants.Font;
import documentUtils.constants.PdfLibConstants.Options;
import documentUtils.pdf.pdflib.PdfLibUtils;
import models.AirportProjectPdfBodyIndexes;
import models.PdfHeader;
import models.PdfStructureModel;

public class PdfLibManager {
	int currentRowPosition;
//	String keysOptions = Options.FONTNAME + Font.HELVETICA_BOLD
//			+ " " + Font.FONT_SIZE + Font.DEFAULT_FONT_SIZE
//			+ " " + Options.ALIGNMENT + Options.JUSTIFY;
//
//	String valuesOptions = Options.FONTNAME + Font.HELVETICA
//			+ " " + Font.FONT_SIZE + Font.DEFAULT_FONT_SIZE
//			+ " " + Options.ALIGNMENT + Options.JUSTIFY;
	final int TABLE_HEIGHT = 73;
	
	public byte[] createPdf(PdfStructureModel pdfStructure) {
		pdflib pdf = null;
		try {
			pdf = new pdflib();
			PdfLibUtils pdfUtils = new PdfLibUtils(pdf);
			List<String[]> body = pdfStructure.getBody();
			pdf.begin_document("", "");
			
			pdfUtils.beginPage();
			pdfUtils.createHeader(pdfStructure.getHeader());
			pdfUtils.createTitle(body.get(AirportProjectPdfBodyIndexes.INDEX_RESERVATION_SUMMARY_TITLE.getValue())[0]);
			
			
//			String[] keys = new String[] {
//				"Cliente"
//				, "Codice fiscale"
//				, "N. prenotazione"
//				, "Sottoscritta il"
//			};
//			
//			String[] values = new String[] {
//				"Daria Vuoto"
//				, "VTUDRA00A41L741Q"
//				, "U2UEI2"
//				, "31-01-2024"
//			};
			fitReservationData(pdf
								, pdfUtils
								, body.get(AirportProjectPdfBodyIndexes.INDEX_RESERVATION_SUMMARY_KEYS.getValue())
								, body.get(AirportProjectPdfBodyIndexes.INDEX_RESERVATION_SUMMARY_VALUES.getValue())
								);
			pdf.fit_textflow(pdf.create_textflow(body.get(AirportProjectPdfBodyIndexes.INDEX_TICKET_LIST_TITLE.getValue())[0], DocumentConstants.RESERVATION_SUMMARY_KEYS_OPTIONS)
							, MARGIN_SIZE
							, currentRowPosition - Font.DEFAULT_FONT_SIZE
							, PAGE_WIDTH - MARGIN_SIZE
							, currentRowPosition
							, ""
							);
			this.currentRowPosition = pdfUtils.calculateNewRowPosition(currentRowPosition);
			String[] tableHeaders = pdfStructure.getBody().get(AirportProjectPdfBodyIndexes.INDEX_TICKET_LIST_TABLE_HEADERS.getValue());
			String[] tableValues = pdfStructure.getBody().get(AirportProjectPdfBodyIndexes.INDEX_TICKET_LIST_TABLE_VALUES.getValue());
			
			int currentPageNumber = 1;
			int printedTickets = 0, printedTicketsOnThisPage = 0;
			final int TOTAL_COLUMNS = tableHeaders.length - 1;
			int bigRectangleStartPosition = currentRowPosition;
			
			
			do {
				for (int index = printedTickets * tableHeaders.length; index < tableValues.length; index += tableHeaders.length) {
					if (nextTicketWillFit()) {
						int table = pdfUtils.createSimpleTable(tableHeaders, tableValues, index + 1, TOTAL_COLUMNS);
						String tableOptions = Options.HEADER + 0
								+ " " + Options.DEFAULT_ROW_HEIGHT
								+ " " + Options.TABLE_COLOR
								+ " " + Options.STROKE_DEFAULT;
						pdf.fit_table(table
									, MARGIN_SIZE * 2
									, currentRowPosition - MARGIN_SIZE
									, PAGE_WIDTH - MARGIN_SIZE
									, currentRowPosition - MARGIN_SIZE - TABLE_HEIGHT
									, tableOptions);
						printedTickets++;
						printedTicketsOnThisPage++;
						currentRowPosition -= (MARGIN_SIZE + TABLE_HEIGHT);
						
						if (allTicketsArePrinted(printedTickets, tableValues, tableHeaders)) {
							pdf.rect(MARGIN_SIZE
									, bigRectangleStartPosition
									, PAGE_WIDTH - MARGIN_SIZE
									, - (MARGIN_SIZE + printedTicketsOnThisPage * (TABLE_HEIGHT + MARGIN_SIZE))
									);
							pdf.stroke();
							currentRowPosition -= MARGIN_SIZE + DEFAULT_ROW_HEIGHT;
						}
					} else {
						pdfUtils.createPageNumber(currentPageNumber++);
//						pdfUtils.setColorGreen();
//						pdf.rect(MARGIN_SIZE
//								, bigRectanglePosition
//								, MARGIN_SIZE
//								, - (bigRectanglePosition - MARGIN_SIZE)
//								);
//						pdf.fill();
//						pdf.rect(PAGE_WIDTH + 1 - MARGIN_SIZE
//								, bigRectanglePosition
//								, MARGIN_SIZE
//								, - (bigRectanglePosition - MARGIN_SIZE)
//								);
//						pdf.fill();
//						
//						int startingY = bigRectanglePosition;
//						for (int i = 0; i < printedTicketsOnThisPage; i++) {
//							pdf.rect(MARGIN_SIZE
//									, startingY
//									, PAGE_WIDTH - MARGIN_SIZE
//									, - MARGIN_SIZE
//									);
//							pdf.fill();
//							startingY -= MARGIN_SIZE + TABLE_HEIGHT;
//						}
//						pdf.rect(MARGIN_SIZE
//								, startingY
//								, PAGE_WIDTH - MARGIN_SIZE
//								, - (startingY - MARGIN_SIZE)
//								);
//						pdf.fill();
//						pdfUtils.setColorBlack();
						pdf.rect(MARGIN_SIZE
								, bigRectangleStartPosition
								, PAGE_WIDTH - MARGIN_SIZE
								, - (bigRectangleStartPosition - MARGIN_SIZE)
								);
						pdf.stroke();
						pdf.end_page_ext("");
						pdfUtils.beginPage();
						currentRowPosition = PAGE_HEIGHT - MARGIN_SIZE;
						bigRectangleStartPosition = currentRowPosition;
						printedTicketsOnThisPage = 0;
					}
				}
			} while (!allTicketsArePrinted(printedTickets, tableValues, tableHeaders));
			
			String[] footer = new String[] {
					"Lufthansia S.p.A."
					, "blablabla"
					, "blablablablablablablablablablablabla"
			};
			
			int FOOTER_HEIGHT = pdfUtils.calculateFooterHeight(footer.length);
			
			if (currentRowPosition - FOOTER_HEIGHT < MARGIN_SIZE) {
				pdfUtils.createPageNumber(currentPageNumber++);
				pdf.end_page_ext("");
				pdfUtils.beginPage();
				currentRowPosition = PAGE_HEIGHT - MARGIN_SIZE;
			}
			pdfUtils.createFooter(footer, currentRowPosition);
			
			pdfUtils.createPageNumber(currentPageNumber);
			pdf.end_page_ext("");
			
			pdf.end_document("");
			
			
//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//			stream.write(pdf.get_buffer());
//			byte[] file = stream.toByteArray();
//			stream.close();
            System.out.println("PDF CREATO");
            return pdf.get_buffer();
		} catch (PDFlibException e) {
			e.printStackTrace();
			return null;
		} 
	}

//	public static void main(String[] args) {
//		PdfStructureModel pdfStructureModel = new PdfStructureModel();
//		PdfHeader header = new PdfHeader();
//		header.setTitle("LUFTHANSIA");
//		header.setTitleFont(Font.HELVETICA_BOLD_OBLIQUE);
//		header.setTitleFontSize(22);
//		pdfStructureModel.setHeader(header);
//		pdfStructureModel.setDocumentName("TEST");
//		
//		ReservationDto r = new ReservationDto();
//		String strdate = "2025-01-31 15:40:00";
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		LocalDateTime d1 = LocalDateTime.parse(strdate, formatter);
//		r.setArrivalDate(d1);
//		r.setCityArrivalAirport("Roma Fiumicino");
//		r.setCityDepartureAirport("Genova");
//		r.setDate(LocalDate.parse("2024-02-11"));
//		strdate = "2025-01-31 15:00:00";
//		LocalDateTime d2 = LocalDateTime.parse(strdate, formatter);
//		r.setDepartureDate(d2);
//		r.setId(79L);
//		r.setIdCustomer(1L);
//		r.setIdFlight(61L);
//		r.setNameArrivalAirport("Leonardo da Vinci");
//		r.setNameDepartureAirport("Cristoforo Colombo");
//		List<TicketDto> ticketDtoList = new ArrayList<>();
//		TicketDto t1 = new TicketDto();
//		t1.setHolderName("Numa");
//		t1.setHolderSurname("Pompilio");
//		t1.setId(147L);
//		t1.setPrice(92.979996F);
//		TicketDto t2 = new TicketDto();
//		t2.setHolderName("Tarquinio");
//		t2.setHolderSurname("Prisco");
//		t2.setId(148L);
//		t2.setPrice(92.979996F);
//		TicketDto t3 = new TicketDto();
//		t3.setHolderName("Tullo");
//		t3.setHolderSurname("Ostilio");
//		t3.setId(149L);
//		t3.setPrice(92.979996F);
//		TicketDto t4 = new TicketDto();
//		t4.setHolderName("Anco");
//		t4.setHolderSurname("Marzio");
//		t4.setId(150L);
//		t4.setPrice(92.979996F);
//		TicketDto t5 = new TicketDto();
//		t5.setHolderName("Servio");
//		t5.setHolderSurname("Tullio");
//		t5.setId(151L);
//		t5.setPrice(92.979996F);
//		TicketDto t6 = new TicketDto();
//		t6.setHolderName("Servio");
//		t6.setHolderSurname("Tullio");
//		t6.setId(151L);
//		t6.setPrice(92.979996F);
//		TicketDto t7 = new TicketDto();
//		t7.setHolderName("Servio");
//		t7.setHolderSurname("Tullio");
//		t7.setId(151L);
//		t7.setPrice(92.979996F);
//		TicketDto t8 = new TicketDto();
//		t8.setHolderName("Servio");
//		t8.setHolderSurname("Tullio");
//		t8.setId(151L);
//		t8.setPrice(92.979996F);
//		TicketDto t9 = new TicketDto();
//		t9.setHolderName("Servio");
//		t9.setHolderSurname("Tullio");
//		t9.setId(151L);
//		t9.setPrice(92.979996F);
//		TicketDto t10 = new TicketDto();
//		t10.setHolderName("Servio");
//		t10.setHolderSurname("Tullio");
//		t10.setId(151L);
//		t10.setPrice(92.979996F);
//		TicketDto t11 = new TicketDto();
//		t11.setHolderName("Servio");
//		t11.setHolderSurname("Tullio");
//		t11.setId(151L);
//		t11.setPrice(92.979996F);
//		ticketDtoList.add(t1);
//		ticketDtoList.add(t2);
//		ticketDtoList.add(t3);
//		ticketDtoList.add(t4);
//		ticketDtoList.add(t5);
//		ticketDtoList.add(t6);
//		ticketDtoList.add(t7);
//		ticketDtoList.add(t8);
//		ticketDtoList.add(t9);
//		ticketDtoList.add(t10);
//		ticketDtoList.add(t11);
//		r.setTicketList(ticketDtoList);
//		
//		String[] tableHeaders = new String[] {
//			TicketDocumentConstants.HEADER
//			, TicketDocumentConstants.PASSENGER
//			, TicketDocumentConstants.DEPARTURE_FROM
//			, TicketDocumentConstants.ARRIVAL_TO
//			, TicketDocumentConstants.PRICE
//		};
//		
//		PdfLibManager test = new PdfLibManager();
//		String[] text = test.generateDocumentText(r);
//		
//		
//		List<String[]> body = new ArrayList<>(); //chi l'avrebbe mai detto
//		body.add(tableHeaders);
//		body.add(text);
//		pdfStructureModel.setBody(body);
//		byte[] file = test.createPdf(pdfStructureModel);
//		new EmailSender().send(file);
//	}
	
	private String[] generateDocumentText(ReservationDto reservationDto) {
		List<TicketDto> ticketDto = reservationDto.getTicketList();
		List<String> text = new ArrayList<>();
		for (int index = 0; index < ticketDto.size(); index++) {
			text.add("#" + ticketDto.get(index).getId());
			text.add(ticketDto.get(index).getHolderName() + " " + ticketDto.get(index).getHolderSurname());
			text.add(writeFlightDetails(reservationDto.getCityDepartureAirport()
										, reservationDto.getNameDepartureAirport()
										, reservationDto.getDepartureDate()));
			text.add(writeFlightDetails(reservationDto.getCityArrivalAirport()
										, reservationDto.getNameArrivalAirport()
										, reservationDto.getArrivalDate()));
			text.add(new DecimalFormat("0.00").format(ticketDto.get(index).getPrice()) + "€");
		}
		return text.toArray(new String[0]);
	}
	
	private String writeFlightDetails(String city, String name, LocalDateTime date) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String formattedDate = date.format(dateFormatter);
		String formattedTime = date.format(timeFormatter);
		return city
//				+ " ("
//				+ name
//				+ ") "
				+ ", " + formattedDate + " " + formattedTime;
	}

	private void fitReservationData(pdflib pdf, PdfLibUtils utils, String[] keys, String[] values) throws PDFlibException {
		int currentUpperRightYPosition = PAGE_TITLE_POSITION - DEFAULT_ROW_HEIGHT;
		int currentLowerLeftYPosition = currentUpperRightYPosition - Font.DEFAULT_FONT_SIZE;
		for (int index = 0; index < keys.length; index++) {
			int currentRow = pdf.create_textflow(keys[index], DocumentConstants.RESERVATION_SUMMARY_KEYS_OPTIONS);
			currentRow = pdf.add_textflow(currentRow, ": " + values[index], DocumentConstants.RESERVATION_SUMMARY_VALUES_OPTIONS);
			pdf.fit_textflow(currentRow
							, MARGIN_SIZE
							, currentLowerLeftYPosition
							, PAGE_WIDTH - MARGIN_SIZE
							, currentUpperRightYPosition
							, ""
							);
			currentUpperRightYPosition = currentLowerLeftYPosition - Font.DEFAULT_FONT_SIZE;
			currentLowerLeftYPosition = currentUpperRightYPosition - Font.DEFAULT_FONT_SIZE;
		}
		this.currentRowPosition = utils.calculateNewRowPosition(currentLowerLeftYPosition);
	}
	
	private boolean nextTicketWillFit() {
		return (currentRowPosition - MARGIN_SIZE - TABLE_HEIGHT - MARGIN_SIZE) > MARGIN_SIZE;
	}
	
	private boolean allTicketsArePrinted(int printedTickets, String[] tableValues, String[] tableKeys) {
		return printedTickets == (tableValues.length / tableKeys.length);
	}
}
