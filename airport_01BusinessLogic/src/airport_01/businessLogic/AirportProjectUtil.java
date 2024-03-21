package airport_01.businessLogic;

import java.time.LocalDateTime;
import java.util.List;

import airport_01Model.dto.PassengerDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.TicketDto;
import airport_01Model.models.entities.Flight;
import airport_01Model.models.entities.FlightRoute;
import airport_01Model.models.entities.Reservation;

public class AirportProjectUtil {
	
	public String generateReservationNumber(ReservationDto reservationDto) {
		return "U"
				+ reservationDto.getIdCustomer()
				+ "R"
				+ reservationDto.getId()
				+ "D"
				+ reservationDto.getDate().getDayOfMonth();
	}
	
	public void fillWithReservableFlights(List<Flight> reservableFlights, FlightRoute flightRoute) {
		flightRoute.getFlightList().forEach(flight -> {
			if (isReservableFlight(flight)) {
				reservableFlights.add(flight);
			}
		});
	}
	
	public boolean isReservableFlight(Flight flight) {
		return flight.getDepartureDate().isAfter(LocalDateTime.now())
				&& flightIsNotFull(flight);
	}

	public boolean flightIsNotFull(Flight flight) {
		return ticketSoldForAFlight(flight) < flight.getAirplane().getSeats();
	}
	
	private int ticketSoldForAFlight(Flight flight) {
		int count = 0;
		List<Reservation> reservations = flight.getReservationList();
		for (Reservation reservation : reservations) {
			count += (null != reservation.getTicketList()
					? reservation.getTicketList().size()
					: 0);
		}
		return count;
	}
	
	public PassengerDto[] getPassengersName(ReservationDto reservationDto) {
		List<TicketDto> ticketDtoList = reservationDto.getTicketList();
		PassengerDto[] passengers = new PassengerDto[ticketDtoList.size()];
		for (int index = 0; index < ticketDtoList.size(); index++) {
			PassengerDto passengerDto = new PassengerDto();
			passengerDto.setName(ticketDtoList.get(index).getHolderName());
			passengerDto.setSurname(ticketDtoList.get(index).getHolderSurname());
			passengers[index] = passengerDto;
		}
		return passengers;
	}
}
