package airport_01Ejb.interfaces;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.Local;

import airport_01Client.utils.ClientException;
import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.ResponseDto;
import airport_01Model.dto.RoleDto;
import airport_01Model.dto.TicketDto;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Local
public interface CommonControllerEjbInterface {

	AirplaneDto deleteAirplane(AirplaneDto airplaneDto) throws ValidatorException, DBQueryException, UnforeseenException;

	AirportDto deleteAirport(AirportDto airportDto) throws ValidatorException, DBQueryException, UnforeseenException;

	List<FlightRouteDto> findFlightRouteByAirports(Long idDeparture, Long idArrival, RoleDto roleDto) throws ValidatorException, DBQueryException, UnforeseenException;
	FlightRouteDto insertFlightRoute(FlightRouteDto flightRouteDto) throws ValidatorException, DBQueryException, UnforeseenException;
	FlightRouteDto deleteFlightRoute(FlightRouteDto flightRouteDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
	List<CustomerDto> findCustomersLeavingOnDate(LocalDate localDate) throws ValidatorException, DBQueryException, UnforeseenException;

	FlightDto insertFlight(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException;
	FlightDto updateFlight(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException;
	FlightDto deleteFlight(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
	ReservationDto insertReservation(ReservationDto reservationDto) throws ValidatorException, DBQueryException, UnforeseenException, ClientException;
	ResponseDto updateReservation(ReservationDto reservationDto) throws ValidatorException, DBQueryException, UnforeseenException;
	ReservationDto deleteReservation(ReservationDto reservationDto) throws ValidatorException, DBQueryException, UnforeseenException;

	TicketDto deleteTicket(TicketDto ticketDto) throws ValidatorException, DBQueryException, UnforeseenException;
}
