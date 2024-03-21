package airport_01Ejb.utils.converters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import airport_01.businessLogic.SeatsManager;
import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.TicketDto;
import airport_01Model.models.entities.Airplane;
import airport_01Model.models.entities.Airport;
import airport_01Model.models.entities.Customer;
import airport_01Model.models.entities.Flight;
import airport_01Model.models.entities.FlightRoute;
import airport_01Model.models.entities.Reservation;
import airport_01Model.models.entities.Ticket;

@SuppressWarnings("serial")
public class ModelToDtoConverter {

	public CustomerDto customerDtoFactoryBasic(Customer customerEntity) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(customerEntity.getId());
		customerDto.setIdRole(customerEntity.getRole().getId());
		customerDto.setName(customerEntity.getName());
		
		if (null != customerEntity.getReservationList()) {
			List<ReservationDto> reservationDtoList = new ArrayList<>();
			List<Reservation> reservationEntityList = customerEntity.getReservationList();
			for (int indexReservationEntityList = 0; indexReservationEntityList < reservationEntityList.size(); indexReservationEntityList++) {
				reservationDtoList.add(reservationDtoFactory(reservationEntityList.get(indexReservationEntityList)));
			}
			customerDto.setReservationList(reservationDtoList);
		}
		return customerDto;
	}
	
	public CustomerDto customerDtoFactoryDetailed(Customer customerEntity) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(customerEntity.getId());
		customerDto.setIdRole(customerEntity.getRole().getId());
		customerDto.setName(customerEntity.getName());
		customerDto.setSurname(customerEntity.getSurname());
		customerDto.setBirthCity(customerEntity.getBirthCity());
		customerDto.setBirthDate(customerEntity.getBirthDate());
		customerDto.setIdentityCardNumber(customerEntity.getIdentityCardNumber());
		customerDto.setPhoneNumber(customerEntity.getPhoneNumber());
		customerDto.setResidenceCity(customerEntity.getResidenceCity());
		customerDto.setResidencePostcode(customerEntity.getResidencePostcode());
		customerDto.setResidenceProvince(customerEntity.getResidenceProvince());
		customerDto.setResidentialAddress(customerEntity.getResidentialAddress());
		customerDto.setTaxCode(customerEntity.getTaxCode());
		return customerDto;
	}
	
	public CustomerDto customerDtoFactoryForReminderFlight(Customer customerEntity) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setEmail(customerEntity.getEmail());
		customerDto.setName(customerEntity.getName());
		customerDto.setSurname(customerEntity.getSurname());
		customerDto.setTaxCode(customerEntity.getTaxCode());
		customerDto.setIdentityCardNumber(customerEntity.getIdentityCardNumber());
		
		customerDto.setReservationList(new ArrayList<ReservationDto>() {
			{
				customerEntity.getReservationList().forEach(reservation -> {
					Flight flight = reservation.getFlight();
					if (null != flight && flight.getDepartureDate().toLocalDate().equals(LocalDate.now().plusDays(1))) {
						ReservationDto reservationDto = new ReservationDto();
						reservationDto.setArrivalDate(flight.getArrivalDate());
						reservationDto.setDepartureDate(flight.getDepartureDate());
						reservationDto.setCityArrivalAirport(flight.getFlightRoute().getArrivalAirport().getCity());
						reservationDto.setCityDepartureAirport(flight.getFlightRoute().getDepartureAirport().getCity());
						reservationDto.setNameArrivalAirport(flight.getFlightRoute().getArrivalAirport().getName());
						reservationDto.setNameDepartureAirport(flight.getFlightRoute().getDepartureAirport().getName());
						
						List<TicketDto> ticketDtoList = new ArrayList<>();
						reservation.getTicketList().forEach(ticket -> {
							TicketDto ticketDto = new TicketDto();
							ticketDto.setHolderName(ticket.getHolderName());
							ticketDto.setHolderSurname(ticket.getHolderSurname());
							ticketDtoList.add(ticketDto);
						});
						reservationDto.setTicketList(ticketDtoList);
						add(reservationDto);
					}
				});
			}
		});
		return customerDto;
	}
	
	public TicketDto ticketDtoFactory(Ticket ticketEntity) {
		TicketDto ticketDto = new TicketDto();
		ticketDto.setHolderName(ticketEntity.getHolderName());
		ticketDto.setHolderSurname(ticketEntity.getHolderSurname());
		ticketDto.setId(ticketEntity.getId());
		ticketDto.setIdReservation(ticketEntity.getReservation().getId());
		ticketDto.setPrice(ticketEntity.getPrice());
		ticketDto.setValidity(ticketEntity.isValidity());
		return ticketDto;
	}
	
	public ReservationDto reservationDtoFactory(Reservation reservationEntity) {
		ReservationDto reservationDto = new ReservationDto();
		reservationDto.setDate(reservationEntity.getDate());
		reservationDto.setId(reservationEntity.getId());
		reservationDto.setIdCustomer(reservationEntity.getCustomer().getId());
		reservationDto.setCustomerName(reservationEntity.getCustomer().getName());
		reservationDto.setCustomerSurname(reservationEntity.getCustomer().getSurname());
		reservationDto.setPaymentMethod(reservationEntity.getPaymentMethod());
		reservationDto.setValidity(reservationEntity.getValidity());
		
		if (reservationDto.getValidity()) {
			Flight flightEntity = reservationEntity.getFlight();
			reservationDto.setIdFlight(reservationEntity.getFlight().getId());
			reservationDto.setArrivalDate(flightEntity.getArrivalDate());
			reservationDto.setCityArrivalAirport(flightEntity.getFlightRoute().getArrivalAirport().getCity());
			reservationDto.setCityDepartureAirport(flightEntity.getFlightRoute().getDepartureAirport().getCity());
			reservationDto.setDepartureDate(flightEntity.getDepartureDate());
			reservationDto.setNameArrivalAirport(flightEntity.getFlightRoute().getArrivalAirport().getName());
			reservationDto.setNameDepartureAirport(flightEntity.getFlightRoute().getDepartureAirport().getName());
		}
		
		List<TicketDto> ticketDtoList = new ArrayList<>();
		List<Ticket> ticketEntityList = reservationEntity.getTicketList();
		for (int indexTicketList = 0; indexTicketList < ticketEntityList.size(); indexTicketList++) {
			ticketDtoList.add(ticketDtoFactory(ticketEntityList.get(indexTicketList)));
		}
		reservationDto.setTicketList(ticketDtoList);
		return reservationDto;
	}
	
	public FlightDto flightDtoFactoryForAdmin(Flight flightEntity, Integer availableSeats) {
		FlightDto flightDto = flightDtoFactoryBasic(flightEntity, availableSeats);
		Airplane airplaneEntity = flightEntity.getAirplane();
		
		flightDto.setModel(airplaneEntity.getModel());
		flightDto.setTankCapacity(airplaneEntity.getTankCapacity());
		
		if (null != flightEntity.getReservationList()) {
			List<Reservation> reservationList = flightEntity.getReservationList();
			List<ReservationDto> reservationDtoList = new ArrayList<>() {
				{
					for (int indexReservationList = 0; indexReservationList < reservationList.size(); indexReservationList++) {
						add(reservationDtoFactory(reservationList.get(indexReservationList)));
					}
				}
			};
			flightDto.setReservationList(reservationDtoList);
		}
		return flightDto;
	}
	
	public FlightDto flightDtoFactoryForCustomerFlightChoice(Flight flightEntity, Integer availableSeats) {
		FlightDto flightDto = flightDtoFactoryBasic(flightEntity, availableSeats);
		
		if (null != flightEntity.getReservationList()) {
			List<Reservation> reservationEntityList = flightEntity.getReservationList();
			List<ReservationDto> reservationDtoList = new ArrayList<>() {
				{
					reservationEntityList.forEach(reservationEntity -> {
						ReservationDto reservationDto = new ReservationDto();
						reservationDto.setNumberOfTickets(reservationEntity.getTicketList().size());
						add(reservationDto);
					});
				}
			};
			flightDto.setReservationList(reservationDtoList);
		}
		
		return flightDto;
	}
	
	private FlightDto flightDtoFactoryBasic(Flight flightEntity, int availableSeats) {
		FlightDto flightDto = new FlightDto();
		FlightRoute flightRouteEntity = flightEntity.getFlightRoute();
		Airplane airplaneEntity = flightEntity.getAirplane();
		Airport departureAirportEntity = flightRouteEntity.getDepartureAirport();
		Airport arrivalAirportEntity = flightRouteEntity.getArrivalAirport();
		
		flightDto.setArrivalDate(flightEntity.getArrivalDate());
		flightDto.setDepartureDate(flightEntity.getDepartureDate());
		flightDto.setId(flightEntity.getId());
		flightDto.setIdAirplane(airplaneEntity.getId());
		flightDto.setIdFlightRoute(flightRouteEntity.getId());
		flightDto.setCityArrivalAirport(arrivalAirportEntity.getCity());
		flightDto.setCityDepartureAirport(departureAirportEntity.getCity());
		flightDto.setIdArrivalAirport(arrivalAirportEntity.getId());
		flightDto.setIdDepartureAirport(departureAirportEntity.getId());
		flightDto.setNameArrivalAirport(arrivalAirportEntity.getName());
		flightDto.setNameDepartureAirport(departureAirportEntity.getName());
		flightDto.setPrice(flightEntity.getPrice());
		flightDto.setSeats(airplaneEntity.getSeats());
		flightDto.setHoldCapacity(airplaneEntity.getHoldCapacity());
		flightDto.setAvailableSeats(availableSeats);
		
		return flightDto;
	}
	
	public AirportDto airportDtoFactoryBasic(Airport airportEntity) {
		AirportDto airportDto = new AirportDto();
		airportDto.setId(airportEntity.getId());
		airportDto.setAirportName(airportEntity.getName());
		airportDto.setAirportCity(airportEntity.getCity());
		
		return airportDto;
	}
	
	public FlightRouteDto flightRouteDtoFactory(FlightRoute flightRouteEntity, List<Flight> reservableFlights, boolean isAdminRequest) {
		FlightRouteDto flightRouteDto = new FlightRouteDto();
		flightRouteDto.setId(flightRouteEntity.getId());
		flightRouteDto.setDistanceKm(flightRouteEntity.getDistanceKm());
		flightRouteDto.setIdArrivalAirport(flightRouteEntity.getArrivalAirport().getId());
		flightRouteDto.setIdDepartureAirport(flightRouteEntity.getDepartureAirport().getId());
		flightRouteDto.setDepartureAirportName(flightRouteEntity.getDepartureAirport().getName());
		flightRouteDto.setArrivalAirportName(flightRouteEntity.getArrivalAirport().getName());
		flightRouteDto.setArrivalAirportCity(flightRouteEntity.getArrivalAirport().getCity());
		flightRouteDto.setDepartureAirportCity(flightRouteEntity.getDepartureAirport().getCity());
		
		if (null != reservableFlights) {
			List<FlightDto> flightDtoList = new ArrayList<FlightDto>();
			SeatsManager seatsManager = new SeatsManager();
			for (Flight flightEntity : reservableFlights) {
				flightDtoList.add(isAdminRequest
								? flightDtoFactoryForAdmin(flightEntity, seatsManager.countAvailableSeats(flightEntity))
								: flightDtoFactoryForCustomerFlightChoice(flightEntity, seatsManager.countAvailableSeats(flightEntity)));
			}
			flightRouteDto.setFlightList(flightDtoList);
		}
//		flightRouteDto.setFlightList(
//				reservableFlights.stream()
//			              .map(flight -> flightDtoFactoryForCustomerFlightChoice(flight, new SeatsManager().countAvailableSeats(flight)))
//			              .collect(Collectors.toList()));
		return flightRouteDto;
	}
	
//	public FlightDto flightDtoFactoryForAdmin(Flight flight) {
//		FlightDto flightDto = new FlightDto();
//		FlightRoute flightRoute = flight.getFlightRoute();
//		Airplane airplane = flight.getAirplane();
//		Airport departureAirport = flightRoute.getDepartureAirport();
//		Airport arrivalAirport = flightRoute.getArrivalAirport();
//		
//		flightDto.setId(flight.getId());
//		flightDto.setIdFlightRoute(flightRoute.getId());
//		flightDto.setDistanceKm(flightRoute.getDistanceKm());
//		flightDto.setIdDepartureAirport(departureAirport.getId());
//		flightDto.setNameDepartureAirport(departureAirport.getName());
//		flightDto.setCityDepartureAirport(departureAirport.getCity());
//		flightDto.setIdArrivalAirport(arrivalAirport.getId());
//		flightDto.setNameArrivalAirport(arrivalAirport.getName());
//		flightDto.setCityArrivalAirport(arrivalAirport.getCity());
//		flightDto.setIdAirplane(airplane.getId());
//		flightDto.setModel(airplane.getModel());
//		flightDto.setSeats(airplane.getSeats());
//		//flightDto.setAvailableSeats(availableSeats);
//		flightDto.setHoldCapacity(airplane.getHoldCapacity());
//		flightDto.setTankCapacity(airplane.getTankCapacity());
//		flightDto.setDepartureDate(flight.getDepartureDate());
//		flightDto.setArrivalDate(flight.getArrivalDate());
//		flightDto.setPrice(flight.getPrice());
//		
//		if (null != flight.getReservationList()) {
//			List<ReservationDto> reservationDtoList = new ArrayList<>();
//			List<Reservation> reservationList = flight.getReservationList();
//			for (int indexReservationList = 0; indexReservationList < reservationList.size(); indexReservationList++) {
//				reservationDtoList.add(reservationDtoFactory(reservationList.get(indexReservationList)));
//			}
//			flightDto.setReservationList(reservationDtoList);
//		}
//		return flightDto;
//	}
	
	public AirplaneDto airplaneDtoFactory(Airplane airplaneEntity) {
		AirplaneDto airplaneDto = new AirplaneDto();
		airplaneDto.setHoldCapacity(airplaneEntity.getHoldCapacity());
		airplaneDto.setId(airplaneEntity.getId());
		airplaneDto.setTankCapacity(airplaneEntity.getTankCapacity());
		airplaneDto.setSeats(airplaneEntity.getSeats());
		airplaneDto.setModel(airplaneEntity.getModel());
		return airplaneDto;
	}
}
