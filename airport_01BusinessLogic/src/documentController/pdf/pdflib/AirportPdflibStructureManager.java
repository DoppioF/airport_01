package documentController.pdf.pdflib;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import airport_01.businessLogic.AirportProjectUtil;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.TicketDto;
import customUtils.constants.TicketDocumentConstants;
import documentUtils.constants.DocumentConstants;
import documentUtils.constants.PdfLibConstants.Font;
import models.PdfHeader;
import models.PdfStructureModel;

public class AirportPdflibStructureManager {

	public PdfStructureModel pdfStructureFactoryForReservationSummary(ReservationDto reservationDto) {
		PdfStructureModel pdfStructure = new PdfStructureModel();
		pdfStructure.setHeader(pdfHeaderFactory());
		
		pdfStructure.setBody(
				pdfBodyReservationSummary(reservationDto)
		);
		
		pdfStructure.setFooter(createFooter());
		return pdfStructure;
	}
	
	private ArrayList<String[]> pdfBodyReservationSummary(ReservationDto reservationDto) {
		String[] reservationSummaryTitle = new String[] {
				DocumentConstants.RESERVATION_SUMMARY_TITLE
		};
		String[] reservationSummaryKeys = createReservationSummaryKeys();
		String[] reservationSummaryValues = reservationSummaryValuesFactory(reservationDto);
		String[] ticketListTitle = new String[] {
				DocumentConstants.TICKET_LIST
		};
		String[] ticketListTableHeaders = createTicketListTableHeaders();
		String[] ticketListTableValues = ticketListTableValuesFactory(reservationDto);
		
		return new ArrayList<>() {
			private static final long serialVersionUID = 149138491364186L;
			{
				add(reservationSummaryTitle);
				add(reservationSummaryKeys);
				add(reservationSummaryValues);
				add(ticketListTitle);
				add(ticketListTableHeaders);
				add(ticketListTableValues);
			}
		};
	}
	
	private String[] createFooter() {
		return new String[] {
				"Lufthansia S.p.A."
				, "blablabla"
				, "blablablablablablablablablablablabla"
		};
	}
	
	private String[] ticketListTableValuesFactory(ReservationDto reservationDto) {
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
			text.add(new DecimalFormat("0.00").format(ticketDto.get(index).getPrice()) + "�");
		}
		return text.toArray(new String[0]);
	}
	
	private String writeFlightDetails(String city, String name, LocalDateTime date) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String formattedDate = date.format(dateFormatter);
		String formattedTime = date.format(timeFormatter);
		return city + ", " + formattedDate + " " + formattedTime;
	}
	
	private String[] createTicketListTableHeaders() {
		return new String[] {
				TicketDocumentConstants.HEADER
				, TicketDocumentConstants.PASSENGER
				, TicketDocumentConstants.DEPARTURE_FROM
				, TicketDocumentConstants.ARRIVAL_TO
				, TicketDocumentConstants.PRICE
			};
	}
	
	private String[] reservationSummaryValuesFactory(ReservationDto reservationDto) {
		return new String[] {
				reservationDto.getCustomerName()
					+ " " + reservationDto.getCustomerSurname()
				, reservationDto.getCustomerTaxCode()
				, new AirportProjectUtil().generateReservationNumber(reservationDto)
		};
	}
	
	private String[] createReservationSummaryKeys() {
		return new String[] {
				DocumentConstants.RESERVATION_SUMMARY_CUSTOMER
				, DocumentConstants.RESERVATION_SUMMARY_TAX_CODE
				, DocumentConstants.RESERVATION_SUMMARY_RESERVATION_NUMBER
				, DocumentConstants.RESERVATION_SUMMARY_RESERVATION_DATE
		};
	}
	
	private PdfHeader pdfHeaderFactory() {
		PdfHeader header = new PdfHeader();
		header.setTitle(TicketDocumentConstants.LOGO);
		header.setTitleFont(Font.HELVETICA_BOLD_OBLIQUE);
		header.setTitleFontSize(Font.TITLE_FONT_SIZE);
		return header;
	}
	
	private String[] generateDocumentText(ReservationDto reservationDto) {
		List<TicketDto> ticketDto = reservationDto.getTicketList();
		List<String> text = new ArrayList<>();
		text.add(TicketDocumentConstants.LOGO);
		for (int index = 0; index < ticketDto.size(); index++) {
			text.add(TicketDocumentConstants.HEADER);
			text.add(". #" + ticketDto.get(index).getId());
			text.add(TicketDocumentConstants.PASSENGER);
			text.add(": " + ticketDto.get(index).getHolderName() + " " + ticketDto.get(index).getHolderSurname());
			text.add(TicketDocumentConstants.FLIGHT);
			text.add(writeFlightDetails(reservationDto));
			text.add(TicketDocumentConstants.PRICE);
			text.add(": " + ticketDto.get(index).getPrice() + "�");
		}
		return text.toArray(new String[0]);
	}
	
	private String writeFlightDetails(ReservationDto reservationDto) {
		return TicketDocumentConstants.DEPARTURE_FROM
				+ reservationDto.getCityDepartureAirport()
				+ " ("
				+ reservationDto.getNameDepartureAirport()
				+ ") "
				+ reservationDto.getDepartureDate()
				+ " - "
				+ TicketDocumentConstants.ARRIVAL_TO
				+ reservationDto.getCityArrivalAirport()
				+ " ("
				+ reservationDto.getNameArrivalAirport()
				+ ") "
				+ reservationDto.getArrivalDate();
	}
}
