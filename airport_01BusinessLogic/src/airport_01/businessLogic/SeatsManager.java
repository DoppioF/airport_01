package airport_01.businessLogic;

import java.util.List;

import airport_01Model.models.entities.Flight;
import airport_01Model.models.entities.Reservation;

public class SeatsManager {

	public Integer countAvailableSeats(Flight flight) {
		int totalSeats = flight.getAirplane().getSeats();
		List<Reservation> reservationList = flight.getReservationList();
		int count = 0;
		if (null != reservationList) {
			for (Reservation reservation : reservationList) {
				count += reservation.getTicketList().size();
			}
		}
		return totalSeats - count;
	}
	
	
}
