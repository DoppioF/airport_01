package airport_01Ejb.utils;

import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import airport_01.businessLogic.AirportProjectUtil;
import airport_01.businessLogic.PriceManager;
import airport_01Client.models.PagamentoDto;
import airport_01Crud.crud.CustomerCrud;
import airport_01Crud.crud.FlightCrud;
import airport_01Crud.crud.FlightRouteCrud;
import airport_01Crud.crud.ReservationCrud;
import airport_01Crud.crud.TicketCrud;
import airport_01Ejb.utils.converters.DtoToModelConverter;
import airport_01Ejb.utils.converters.ModelToDtoConverter;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.RoleDto;
import airport_01Model.dto.TicketDto;
import airport_01Model.models.entities.Customer;
import airport_01Model.models.entities.Flight;
import airport_01Model.models.entities.FlightRoute;
import airport_01Model.models.entities.Reservation;
import airport_01Model.models.entities.Ticket;
import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class ModelsManagingUtils {
	
	@SuppressWarnings("serial")
	public List<Ticket> createTicketEntityListOfInsertedTickets(TicketCrud ticketCrud, List<TicketDto> ticketDtoList, Reservation insertedReservationEntity, Flight foundFlightEntity, PriceManager priceManager, DtoToModelConverter dtoToModelConverter, EntityManager entityManager) throws UnforeseenException {
		return new ArrayList<>() {
			{
				for (TicketDto ticketDto : ticketDtoList) {
					Ticket convertedTicketEntity = dtoToModelConverter.ticketFactory(ticketDto, insertedReservationEntity, priceManager.priceTicketCalculator(foundFlightEntity.getPrice()));
					Ticket insertedTicketEntity = ticketCrud.insertTicket(entityManager, convertedTicketEntity);
					add(insertedTicketEntity);
				}
			}
		};
	}
	
	public void invalidateTickets(List<Ticket> ticketEntityList, TicketCrud ticketCrud, EntityManager entityManager) throws DBQueryException, UnforeseenException {
		for (int index = 0; index < ticketEntityList.size(); index++) {
			invalidateTicket(ticketEntityList, ticketCrud, entityManager, index);
		}
	}
	
	private void invalidateTicket(List<Ticket> ticketEntityList, TicketCrud ticketCrud, EntityManager entityManager, int index) throws DBQueryException, UnforeseenException {
		Ticket foundTicketEntity = ticketCrud.findTicketById(entityManager, ticketEntityList.get(index).getId());
		foundTicketEntity.setValidity(false);
		ticketCrud.updateTicket(entityManager, foundTicketEntity);
	}
	
	public void invalidateReservations(List<Reservation> reservationEntityList, ReservationCrud reservationCrud, TicketCrud ticketCrud, EntityManager entityManager) throws DBQueryException, UnforeseenException {
		for (int index = 0; index < reservationEntityList.size(); index++) {
			invalidateReservation(reservationEntityList, reservationCrud, ticketCrud, entityManager, index);
		}
	}
	
	private void invalidateReservation(List<Reservation> reservationEntityList, ReservationCrud reservationCrud, TicketCrud ticketCrud, EntityManager entityManager, int index) throws DBQueryException, UnforeseenException {
		Reservation foundReservationEntity = reservationCrud.findReservationById(entityManager, reservationEntityList.get(index).getId());
		foundReservationEntity.setValidity(false);
		Reservation updatedReservationEntity = reservationCrud.updateReservation(entityManager, foundReservationEntity);
		invalidateTickets(updatedReservationEntity.getTicketList(), ticketCrud, entityManager);
	}
	
	public void deleteTickets(List<Ticket> ticketEntityList, EntityManager entityManager) throws DBQueryException, UnforeseenException {
		TicketCrud ticketCrud = new TicketCrud();
		int deletedTicketCount = 0;
		for (Ticket ticket : ticketEntityList) {
			ticketCrud.deleteTicket(entityManager, ticket);
			deletedTicketCount++;
		}
		if (deletedTicketCount != ticketEntityList.size()) {
			throw new DBQueryException(GeneralConstants.QUERY_ERROR);
		}
	}
	
	@SuppressWarnings("serial")
	public List<FlightRouteDto> createFlightDtoList(List<FlightRoute> foundFlightRouteEntityList, RoleDto roleDto, AirportProjectUtil airportUtil, ModelToDtoConverter modelToDtoConverter) {
		return new ArrayList<>() {
			{
				for (FlightRoute flightRoute : foundFlightRouteEntityList) {
					List<Flight> reservableFlights = new ArrayList<>();
					airportUtil.fillWithReservableFlights(reservableFlights, flightRoute);
					boolean isAdminRequest = roleDto != null && roleDto.getId() == DbConstants.RoleTable.ID_ADMIN;
					add(modelToDtoConverter.flightRouteDtoFactory(flightRoute, reservableFlights, isAdminRequest));
				}
			}
		};
	}
	
	public void deleteFlights(List<Flight> flightEntityList, EntityManager entityManager) throws DBQueryException, Exception {
		FlightDto flightDto = new FlightDto();
		for (Flight flightEntity : flightEntityList) {
			flightDto.setId(flightEntity.getId());
			manageDeleteFlight(flightDto, entityManager);
		}
	}
	
	public void deleteFlightRoutes(List<FlightRoute> flightRouteEntityList, EntityManager entityManager) throws DBQueryException, Exception {
		FlightRouteDto flightRouteDto = new FlightRouteDto();

		for (FlightRoute flightRoute : flightRouteEntityList) {
			flightRouteDto.setId(flightRoute.getId());
		    manageDeleteFlightRoute(flightRouteDto, entityManager);
		}
	}
	
	public Flight manageDeleteFlight(FlightDto flightDto, EntityManager entityManager) throws DBQueryException, Exception {
		FlightCrud flightCrud = new FlightCrud();
		Flight foundFlightEntity = flightCrud.findFlightById(entityManager, flightDto.getId());
		
		setReservationsInvalid(foundFlightEntity);
		Flight deletedFlightEntity = flightCrud.deleteFlight(entityManager, foundFlightEntity);
		
		return deletedFlightEntity;
	}
	
	public FlightRoute manageDeleteFlightRoute(FlightRouteDto flightRouteDto, EntityManager entityManager) throws DBQueryException, Exception {
		FlightRouteCrud flightRouteCrud = new FlightRouteCrud();
		FlightRoute foundFlightRouteEntity = flightRouteCrud.findFlightRouteById(entityManager, flightRouteDto.getId());
		
		new ModelsManagingUtils().deleteFlights(foundFlightRouteEntity.getFlightList(), entityManager);
		
		return flightRouteCrud.deleteFlightRoute(entityManager, foundFlightRouteEntity);
	}
	
	private void setReservationsInvalid(Flight returnedFlightEntity) throws DBQueryException, Exception {
		List<Reservation> reservationEntityList = returnedFlightEntity.getReservationList();
		ReservationCrud reservationCrud = new ReservationCrud();
		TicketCrud ticketCrud = new TicketCrud();
		if (null != reservationEntityList && !reservationEntityList.isEmpty()) {
			new ModelsManagingUtils().invalidateReservations(reservationEntityList, reservationCrud, ticketCrud, getEntityManager());
		}
	}
	
	@SuppressWarnings("serial")
	public List<Customer> findCustomersLeavingOnDate(List<Flight> foundFlightEntityList, CustomerCrud customerCrud, EntityManager entityManager) {
		return new ArrayList<>() {
			{
				foundFlightEntityList.forEach(flight -> {
					flight.getReservationList().forEach(reservation -> {
						try {
							add(customerCrud.findCustomerById(entityManager, reservation.getCustomer().getId()));
						} catch (DBQueryException e) {
							e.printStackTrace();
						} catch (UnforeseenException e) {
							e.printStackTrace();
						}
					});
				});
			}
		};
	}
	
	@SuppressWarnings("serial")
	public List<FlightRouteDto> generateFlightRouteDtoListWithReservableFlights(List<FlightRoute> flightRouteEntityList, ModelToDtoConverter modelToDtoConverter, AirportProjectUtil utils, boolean isAdminRequest) {
		return new ArrayList<>() {
			{
				for (FlightRoute flightRoute : flightRouteEntityList) {
					List<Flight> reservableFlights = new ArrayList<>();
					utils.fillWithReservableFlights(reservableFlights, flightRoute);
					if (isAdminRequest || !reservableFlights.isEmpty()) {
						add(modelToDtoConverter.flightRouteDtoFactory(flightRoute, reservableFlights, isAdminRequest));
					}
				}
			}
		};
	}

	public PagamentoDto pagamentoDtoFactory(ReservationDto reservationDto) {
		PagamentoDto pagamentoDto = new PagamentoDto();
		pagamentoDto.setCognomeCliente(reservationDto.getCustomerSurname());
		pagamentoDto.setNomeCliente(reservationDto.getCustomerName());
		pagamentoDto.setNumeroContoCorrente(reservationDto.getCheckingAccountNumber());
		pagamentoDto.setTotaleDaPagare(reservationDto.getTotalToPay());
		return pagamentoDto;
	}
}
