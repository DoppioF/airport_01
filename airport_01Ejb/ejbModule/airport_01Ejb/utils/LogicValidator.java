package airport_01Ejb.utils;

import java.util.List;

import airport_01.businessLogic.SeatsManager;
import airport_01Model.models.entities.Airplane;
import airport_01Model.models.entities.Flight;
import airport_01Model.models.entities.FlightRoute;
import customUtils.constants.strings.GeneralConstants;

public class LogicValidator {
	
	public void validateNumberOfTicketBasedOnAvailableSeats (int availableSeats, int ticketNumber, List<String> errors) {
		if(availableSeats < ticketNumber) {
			errors.add("Posti disponibili: " + availableSeats
				+ ", biglietti della prenotazione: " + ticketNumber
				+ ". " + GeneralConstants.INSERT_FAILURE);
		}
	}
	
	public void checkIfFlightIsUpdatable (Flight flight
											, Airplane currentAirplane
											, FlightRoute currentFlightRoute
											, Airplane incomingAirplane
											, FlightRoute incomingFlightRoute
											, List<String> errors) {
		checkAirplane(flight, currentAirplane, incomingAirplane, errors);
		checkFlightRoute(flight, currentFlightRoute, incomingFlightRoute, errors);
	}
	
	private void checkAirplane (Flight flight, Airplane currentAirplane, Airplane incomingAirplane, List<String> errors) {
		if (currentAirplane.getId() != incomingAirplane.getId()
			&& incomingAirplane.getSeats() < currentAirplane.getSeats()) {
				
				int availableSeats = new SeatsManager().countAvailableSeats(flight);
				if (incomingAirplane.getSeats() < currentAirplane.getSeats() - availableSeats) {
					errors.add("Non ci sono abbastanza posti per ospitare i passeggeri del volo. " + GeneralConstants.UPDATE_FAILURE);
				}
			}
	}
	
	private void checkFlightRoute (Flight flight, FlightRoute currentFlightRoute, FlightRoute incomingFlightRoute, List<String> errors) {
		if (currentFlightRoute.getId() != incomingFlightRoute.getId()
			&& null != flight.getReservationList()
			&& !flight.getReservationList().isEmpty()) {
			
				errors.add("Non si può modificare la tratta di un volo che ha prenotazioni. " + GeneralConstants.UPDATE_FAILURE);
		}
	}

}
