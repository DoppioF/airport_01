package test;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.TicketDto;

public class Main {

	public static void main(String[] args) {
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
//		ticketDtoList.add(t1);
//		ticketDtoList.add(t2);
//		ticketDtoList.add(t3);
//		ticketDtoList.add(t4);
//		ticketDtoList.add(t5);
//		ticketDtoList.add(t5);
//		ticketDtoList.add(t5);
//		ticketDtoList.add(t5);
//		r.setTicketList(ticketDtoList);
//		
//		String[] tableHeaders = new String[] {
//			TicketDocumentConstants.HEADER
//			, TicketDocumentConstants.PASSENGER
//			, TicketDocumentConstants.DEPARTURE_FROM
//			, TicketDocumentConstants.ARRIVAL_TO
//			, TicketDocumentConstants.PRICE
//		};
//		String[] text = new Main().generateDocumentText(r);
//		new Test().createPdf(tableHeaders, text);
//		try {
//			new AirportPdfMaker().createPDF(text);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		List<FlightRouteDto> lista = new ArrayList<>();
		FlightRouteDto route1 = new FlightRouteDto();
		route1.setIdDepartureAirport(1L);
		FlightRouteDto route2 = new FlightRouteDto();
		route2.setIdDepartureAirport(1L);
		lista.add(route1);
		lista.add(route2);
		Long id = null;
		List<FlightRouteDto> lista2 = lista.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(FlightRouteDto::getIdDepartureAirport))), ArrayList::new));
		List<FlightRouteDto> lista3 = (List<FlightRouteDto>) (lista.stream().filter(route -> id != route.getId()));
		System.out.println(lista2);
	}
	
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
}

