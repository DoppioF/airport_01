package airport_01Ejb.controllers;

import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.commitEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;
import static airport_01Crud.connection.EntityManagerProvider.rollbackEntityTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01.businessLogic.AirportProjectUtil;
import airport_01.businessLogic.PriceManager;
import airport_01.businessLogic.SeatsManager;
import airport_01Crud.crud.AirplaneCrud;
import airport_01Crud.crud.AirportCrud;
import airport_01Crud.crud.CustomerCrud;
import airport_01Crud.crud.FlightCrud;
import airport_01Crud.crud.FlightRouteCrud;
import airport_01Crud.crud.ReservationCrud;
import airport_01Crud.crud.TicketCrud;
import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.utils.ModelsManagingUtils;
import airport_01Ejb.utils.converters.DtoToModelConverter;
import airport_01Ejb.utils.converters.ModelToDtoConverter;
import airport_01Ejb.utils.validators.LogicValidator;
import airport_01Ejb.utils.validators.Validator;
import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.ResponseDto;
import airport_01Model.dto.RoleDto;
import airport_01Model.dto.TicketDto;
import airport_01Model.models.entities.Airplane;
import airport_01Model.models.entities.Airport;
import airport_01Model.models.entities.Customer;
import airport_01Model.models.entities.Flight;
import airport_01Model.models.entities.FlightRoute;
import airport_01Model.models.entities.Reservation;
import airport_01Model.models.entities.Ticket;
import customUtils.classes.CustomStreamUtils;
import customUtils.constants.strings.EjbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import validatorUtils.ErrorsLogManager;

@Stateless(name = EjbConstants.COMMON_EJB)
@LocalBean
public class CommonControllerEjb implements CommonControllerEjbInterface {
	private static final String CLASSNAME = "CommonEjb";

	@Override
	public FlightDto updateFlight(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException	 {
		System.out.println("CommonControllerEjb updateFlight, flightDto: " + flightDto);
		try {
			List<String> errors = new Validator().flightUpdate(flightDto);
			ErrorsLogManager errorsLogManager = new ErrorsLogManager();
			errorsLogManager.dtoValidationErrorMessagesHandler(errors, CLASSNAME, "updateFlight", GeneralConstants.VALIDATOR_ERROR);
			
			beginEntityTransaction();
			
			FlightCrud flightCrud = new FlightCrud();
			AirplaneCrud airplaneCrud = new AirplaneCrud();
			FlightRouteCrud flightRouteCrud = new FlightRouteCrud();
			
			//volo, aereo e rotta che sono attualmente sul db
			Flight currentFlightEntity = flightCrud.findFlightById(getEntityManager(), flightDto.getId());
			Airplane currentAirplaneEntity = currentFlightEntity.getAirplane();
			FlightRoute currentFlightRouteEntity = currentFlightEntity.getFlightRoute();
			
			//aereo e rotta "in formato entity" che l'user ha scelto per l'aggiornamento
			Airplane incomingAirplaneEntity = airplaneCrud.findAirplaneById(getEntityManager(), flightDto.getIdAirplane());
			FlightRoute incomingFlightRouteEntity = flightRouteCrud.findFlightRouteById(getEntityManager(), flightDto.getIdFlightRoute());
			
			new LogicValidator().checkIfFlightIsUpdatable(currentFlightEntity, currentAirplaneEntity, currentFlightRouteEntity, incomingAirplaneEntity, incomingFlightRouteEntity, errors);
			errorsLogManager.dtoValidationErrorMessagesHandler(errors, CLASSNAME, "updateFlight", GeneralConstants.LOGIC_VALIDATOR_ERROR);
			
			Flight flightEntity = new DtoToModelConverter().flightFactory(flightDto, incomingFlightRouteEntity, incomingAirplaneEntity, currentFlightEntity.getPrice());
			Flight updatedFlightEntity = flightCrud.updateFlight(getEntityManager(), flightEntity);
			commitEntityTransaction();
			
			System.out.println("RETURN CommonControllerEjb updateFlight, DTO: " + updatedFlightEntity);
			return new ModelToDtoConverter().flightDtoFactoryForAdmin(updatedFlightEntity, new SeatsManager().countAvailableSeats(updatedFlightEntity));
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

	@Override
	public FlightDto insertFlight(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonControllerEjb insertFlight, flight: " + flightDto);
		try {
			List<String> errors = new Validator().flightInsert(flightDto);
			new ErrorsLogManager().dtoValidationErrorMessagesHandler(errors, CLASSNAME, "insertFlight", GeneralConstants.VALIDATOR_ERROR);
			
			beginEntityTransaction();
			
			//mi dichiaro l'aereo qui perché l'id aereo sul db non è not null, quindi l'aereo può essere null
			Airplane airplaneEntity = null;
			if (null != flightDto.getIdAirplane()) {
				airplaneEntity = new AirplaneCrud().findAirplaneById(getEntityManager(), flightDto.getIdAirplane());
			}
			FlightRoute foundFlightRouteEntity = new FlightRouteCrud().findFlightRouteById(getEntityManager(), flightDto.getIdFlightRoute());
			
			//appoggio il prezzo su una variabile solo per rendere più leggibile la chiamata del metodo dtoToModelConverter
			Float flightPrice = new PriceManager().priceFlightCalculator(foundFlightRouteEntity);
			Flight flightEntity = new DtoToModelConverter().flightFactory(flightDto, foundFlightRouteEntity, airplaneEntity, flightPrice);
			Flight insertedFlightEntity = new FlightCrud().insertFlight(getEntityManager(), flightEntity);
			
			commitEntityTransaction();
			
			//calcolo il prezzo solo se l'aereo inserito non è null
			Integer availableSeats = (null == insertedFlightEntity.getAirplane() ? 0 : insertedFlightEntity.getAirplane().getSeats());

			System.out.println("RETURN CommonControllerEjb insertFlight, DTO: " + insertedFlightEntity);
			return new ModelToDtoConverter().flightDtoFactoryForCustomerFlightChoice(insertedFlightEntity, availableSeats);
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

	@Override
	public ReservationDto insertReservation(ReservationDto reservationDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb insertReservation, reservation: ");
		try {
			List<String> errors = new Validator().reservationInsert(reservationDto);
			ErrorsLogManager errorsLogManager = new ErrorsLogManager();
			errorsLogManager.dtoValidationErrorMessagesHandler(errors, CLASSNAME, "insertReservation", GeneralConstants.VALIDATOR_ERROR);
			
			beginEntityTransaction();
			
			Flight foundFlightEntity = new FlightCrud().findFlightById(getEntityManager(), reservationDto.getIdFlight());
			new LogicValidator().validateNumberOfTicketBasedOnAvailableSeats(foundFlightEntity.getAirplane().getSeats(), reservationDto.getTicketList().size(), errors);
			errorsLogManager.dtoValidationErrorMessagesHandler(errors, CLASSNAME, "insertReservation", GeneralConstants.LOGIC_VALIDATOR_ERROR);
			new CustomerCrud().findCustomerById(getEntityManager(), reservationDto.getIdCustomer());

			reservationDto.setDate(LocalDate.now());
			reservationDto.setValidity(true);
			ReservationCrud reservationCrud = new ReservationCrud();
			DtoToModelConverter dtoToModelConverter = new DtoToModelConverter();
			Reservation insertedReservationEntity = reservationCrud.insertReservation(getEntityManager(), dtoToModelConverter.reservationFactoryForInsert(reservationDto));
			
			System.out.println("ID PRENOTAZIONE INSERITA: " + insertedReservationEntity.getId());
			
			insertedReservationEntity.setTicketList(new ModelsManagingUtils()
														.createTicketEntityListOfInsertedTickets(new TicketCrud()
																								, reservationDto.getTicketList()
																								, insertedReservationEntity
																								, foundFlightEntity
																								, new PriceManager()
																								, dtoToModelConverter
																								, getEntityManager()));
			commitEntityTransaction();
			
			//new AirportEmailManager().generateTicketPdf(responseReservationDto);
			
			System.out.println("RETURN CommonControllerEjb insertReservation, DTO: " + insertedReservationEntity);
			return new ModelToDtoConverter().reservationDtoFactory(insertedReservationEntity);
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

	@Override
	public FlightDto deleteFlight(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("FlightEjb delete, flight: " + flightDto);
		try {
			if (null == flightDto || null == flightDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			beginEntityTransaction();
			
			Flight deletedFlightEntity = new ModelsManagingUtils().manageDeleteFlight(flightDto, getEntityManager());
			
			commitEntityTransaction();
			
			System.out.println("RETURN FlightEjb delete, DTO: " + deletedFlightEntity);
			return new ModelToDtoConverter().flightDtoFactoryForAdmin(deletedFlightEntity, 0);
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}
	
	

	@Override
	public ReservationDto deleteReservation(ReservationDto reservationDto)
			throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb deleteReservation, reservationDto:" + reservationDto);
		try {
			if (null == reservationDto || null == reservationDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			beginEntityTransaction();
			ReservationCrud reservationCrud = new ReservationCrud();
			Reservation foundReservationEntity = reservationCrud.findReservationById(getEntityManager(), reservationDto.getId());
			
			new ModelsManagingUtils().deleteTickets(foundReservationEntity.getTicketList(), getEntityManager());
			
			Reservation deletedReservationEntity = reservationCrud.deleteReservation(getEntityManager(), foundReservationEntity);
			commitEntityTransaction();
			
			System.out.println("RETURN CommonEjb deleteReservation, deletedReservation:" + deletedReservationEntity);
			return new ModelToDtoConverter().reservationDtoFactory(deletedReservationEntity);
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}
	
	@Override
	public List<FlightRouteDto> findFlightRouteByAirports(Long idDeparture, Long idArrival, RoleDto roleDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb findFlightRouteByAirports, start");
		try {
			if(null == idDeparture || null == idArrival) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			beginEntityTransaction();
			
			AirportCrud airportCrud = new AirportCrud();
			airportCrud.findAirportById(getEntityManager(), idDeparture);
			airportCrud.findAirportById(getEntityManager(), idArrival);
			List<FlightRoute> foundFlightRouteEntityList = new FlightRouteCrud().findFlightRouteByAirports(getEntityManager(), idDeparture, idArrival);
			
			List<FlightRouteDto> flightRouteDtoList = new ModelsManagingUtils()
														.createFlightDtoList(foundFlightRouteEntityList
																			, roleDto
																			, new AirportProjectUtil()
																			, new ModelToDtoConverter());
			
			System.out.println("RETURN CommonEjb findFlightRouteByAirports, list: " + flightRouteDtoList);
			return flightRouteDtoList;
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}
	
	@Override
	public FlightRouteDto insertFlightRoute(FlightRouteDto flightRouteDto)
			throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb insertFlightRoute, flightRouteDto:" + flightRouteDto);
		try {
			List<String> errors = new Validator().flightRouteInsert(flightRouteDto);
			new ErrorsLogManager().dtoValidationErrorMessagesHandler(errors, CLASSNAME, "insertFlightRoute", GeneralConstants.VALIDATOR_ERROR);
			
			beginEntityTransaction();
			
			AirportCrud airportCrud = new AirportCrud();
			Airport departureAirportEntity = airportCrud.findAirportById(getEntityManager(), flightRouteDto.getIdDepartureAirport());
			Airport arrivalAirportEntity = airportCrud.findAirportById(getEntityManager(), flightRouteDto.getIdArrivalAirport());
			FlightRoute flightRouteEntity = new DtoToModelConverter().flightRouteFactory(flightRouteDto, departureAirportEntity, arrivalAirportEntity);
			
			FlightRoute returnedFlightRouteEntity = new FlightRouteCrud().insertFlightRoute(getEntityManager(), flightRouteEntity);
			
			commitEntityTransaction();
			
			System.out.println("RETURN CommonEjb insertFlightRoute, dto:" + returnedFlightRouteEntity);
			return new ModelToDtoConverter().flightRouteDtoFactory(returnedFlightRouteEntity, null, true);
		} catch (ValidatorException e) {
			System.out.println(e.getMessage());
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

	@Override
	public FlightRouteDto deleteFlightRoute(FlightRouteDto flightRouteDto)
			throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb delete, flightRouteDto:" + flightRouteDto);
		try {
			if (null == flightRouteDto || null == flightRouteDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			} 
			beginEntityTransaction();
			
			FlightRoute deletedFlightRouteEntity = new ModelsManagingUtils().manageDeleteFlightRoute(flightRouteDto, getEntityManager());
			
			commitEntityTransaction();
			
			System.out.println("RETURN CommonEjb delete, deletedRoute:" + deletedFlightRouteEntity);
			return new ModelToDtoConverter().flightRouteDtoFactory(deletedFlightRouteEntity, null, true);
		} catch (ValidatorException e) {
			System.out.println(e.getMessage());
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}
	
	@Override
	public AirportDto deleteAirport(AirportDto airportDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb delete, airportDto:" + airportDto);
		try {
			if (null == airportDto || null == airportDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			} 
			
			beginEntityTransaction();
			
			AirportCrud airportCrud = new AirportCrud();
			Airport foundAirportEntity = airportCrud.findAirportById(getEntityManager(), airportDto.getId());
			
			List<FlightRoute> entityRoutesWhereThisAirportIsDeparture = foundAirportEntity.getDepartureAirport();
			List<FlightRoute> entityrRoutesWhereThisAirportIsArrival = foundAirportEntity.getArrivalAirport();
			
			List<FlightRoute> combinedFlightRouteEntityList = Stream.concat(
													        CustomStreamUtils
													        	.nullpointerSafeStream(entityRoutesWhereThisAirportIsDeparture), 
													        CustomStreamUtils
													        	.nullpointerSafeStream(entityrRoutesWhereThisAirportIsArrival)
													    	).collect(Collectors.toList());
			new ModelsManagingUtils().deleteFlightRoutes(combinedFlightRouteEntityList, getEntityManager());
			
			Airport deletedAirportEntity = airportCrud.deleteAirport(getEntityManager(), foundAirportEntity);
			commitEntityTransaction();
			
			System.out.println("RETURN CommonEjb delete, deletedAirport:" + deletedAirportEntity);
			return new ModelToDtoConverter().airportDtoFactoryBasic(deletedAirportEntity);
		} catch (ValidatorException e) {
			System.out.println(e.getMessage());
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			System.out.println(e.getMessage());
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

	@Override
	public AirplaneDto deleteAirplane(AirplaneDto airplaneDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb delete, airplaneDto:" + airplaneDto);
		try {
			if (null == airplaneDto || null == airplaneDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			} 
			
			beginEntityTransaction();
			
			AirplaneCrud airplaneCrud = new AirplaneCrud();
			Airplane foundAirplaneEntity = airplaneCrud.findAirplaneById(getEntityManager(), airplaneDto.getId());
			
			if (null != foundAirplaneEntity.getFlightList()) {
				new ModelsManagingUtils().deleteFlights(foundAirplaneEntity.getFlightList(), getEntityManager());
			}
			Airplane deletedAirplaneEntity = airplaneCrud.deleteAirplane(getEntityManager(), foundAirplaneEntity);
			commitEntityTransaction();
			
			System.out.println("RETURN CommonEjb delete, deletedAirplane:" + deletedAirplaneEntity);
			return new ModelToDtoConverter().airplaneDtoFactory(deletedAirplaneEntity);
		} catch (ValidatorException e) {
			System.out.println(e.getMessage());
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			System.out.println(e.getMessage());
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

	//Per il momento non implementato
	@Override
	public ResponseDto updateReservation(ReservationDto reservationDto)
			throws ValidatorException, DBQueryException, UnforeseenException {
				return null;
		/*
		System.out.println("CommonEjb updateReservation, reservation: " + reservationDto);
		try {
			List<String> errors = new Validator().reservationUpdate(reservationDto);
			if (!errors.isEmpty()) {
				System.out.println("RETURN CommonEjb updateReservation, VALIDATOR ERROR");
				String errorString = String.join(" ### ", errors);
				System.out.println(errorString);
				throw new ValidatorException(errorString);
			}
			beginEntityTransaction();
			
			ReservationCrud reservationCrud = new ReservationCrud();
			Reservation foundReservation = reservationCrud.findReservationById(getEntityManager(), reservationDto.getId());
			Flight foundFlight = new FlightCrud().findFlightById(getEntityManager(), reservationDto.getIdFlight());
			Customer customer = (reservationDto.getIdCustomer() == null 
								? null 
								: new CustomerCrud()
									.findCustomerById(getEntityManager(), reservationDto.getIdCustomer()));
			

			DtoToModelConverter dtoToModelConverter = new DtoToModelConverter();
			Reservation reservation = new DtoToModelConverter()
											.reservationFactoryForUpdate(
													reservationDto
													, customer
													, foundFlight
													, foundReservation.getTicketList()
											);
			Reservation returnedReservation = reservationCrud.updateReservation(getEntityManager(), reservation);
			
			if (null == returnedReservation) {
				throw new DBQueryException(GeneralConstants.QUERY_ERROR);
			}
			
			commitEntityTransaction();
			getEntityManager().flush();
			
			ResponseDto dto = new ResponseDto();
			dto.setData(new ModelToDtoConverter().reservationDtoFactory(returnedReservation));
			dto.setMessage(GeneralConstants.OK);
			dto.setState(true);
			
			System.out.println("RETURN CommonControllerEjb updateReservation, DTO: " + dto);
			return dto;
		} catch (ValidatorException e) {
			rollbackEntityTransaction();
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
		
			 */
	}
	
//	@Override
//	public ResponseDto updateFlightRoute(FlightRouteDto flightRouteDto)
//			throws ValidatorException, DBQueryException, Exception {
//		System.out.println("CommonEjb updateFlightRoute, flightRoute: " + flightRouteDto);
//		try {
//			List<String> errors = new Validator().flightRouteUpdate(flightRouteDto);
//			if (!errors.isEmpty()) {
//				System.out.println("RETURN CommonEjb updateFlightRoute, VALIDATOR ERROR");
//				String errorString = String.join(" ### ", errors);
//				System.out.println(errorString);
//				throw new ValidatorException(errorString);
//			}
//			
//			beginEntityTransaction();
//			
//			FlightRouteCrud flightRouteCrud = new FlightRouteCrud();
//			FlightRoute foundFlightRoute = flightRouteCrud.findFlightRouteById(getEntityManager(), flightRouteDto.getId());
//			if (null == foundFlightRoute || null == foundFlightRoute.getTicketList()) {
//				throw new DBQueryException(GeneralConstants.QUERY_ERROR);
//			}
//			Flight foundFlight = new FlightCrud().findFlightById(getEntityManager(), flightRouteDto.getIdFlight());
//			if (null == foundFlight || null == foundFlight.getAirplane()) {
//				throw new DBQueryException(GeneralConstants.QUERY_ERROR);
//			}
//			Customer customer = (flightRouteDto.getIdCustomer() == null 
//								? null 
//								: new CustomerCrud()
//									.findCustomerById(getEntityManager(), flightRouteDto.getIdCustomer()));
//			
//
//			DtoToModelConverter dtoToModelConverter = new DtoToModelConverter();
//			FlightRoute flightRoute = new DtoToModelConverter()
//											.flightRouteFactoryForUpdate(
//													flightRouteDto
//													, customer
//													, foundFlight
//													, foundFlightRoute.getTicketList()
//											);
//			FlightRoute returnedFlightRoute = flightRouteCrud.updateFlightRoute(getEntityManager(), flightRoute);
//			
//			if (null == returnedFlightRoute) {
//				throw new DBQueryException(GeneralConstants.QUERY_ERROR);
//			}
//			
//			commitEntityTransaction();
//			getEntityManager().flush();
//			
//			ResponseDto dto = new ResponseDto();
//			dto.setData(new ModelToDtoConverter().flightRouteDtoFactory(returnedFlightRoute));
//			dto.setMessage(GeneralConstants.OK);
//			dto.setState(true);
//			
//			System.out.println("RETURN CommonControllerEjb updateFlightRoute, DTO: " + dto);
//			return dto;
//		} catch (ValidatorException e) {
//			rollbackEntityTransaction();
//			throw new ValidatorException(e.getMessage());
//		} catch (DBQueryException e) {
//			rollbackEntityTransaction();
//			throw new DBQueryException(e.getMessage());
//		} catch (Exception e) {
//			rollbackEntityTransaction();
//			throw new Exception(e.getMessage());
//		} finally {
//			getEntityManager().close();
//		}
//	}
	
	@Override
	public TicketDto deleteTicket(TicketDto ticketDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("TicketEjb deleteTicket, ticketDto:" + ticketDto);
		try {
			if (null == ticketDto || null == ticketDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			
			beginEntityTransaction();
			
			TicketCrud ticketCrud = new TicketCrud();
			Ticket foundTicketEntity = ticketCrud.findTicketById(getEntityManager(), ticketDto.getId());
			boolean isLastTicket = 1 == foundTicketEntity.getReservation().getTicketList().size();
			Ticket deletedTicketEntity = ticketCrud.deleteTicket(getEntityManager(), foundTicketEntity);
			if (isLastTicket) {
				new ReservationCrud().deleteReservation(getEntityManager(), foundTicketEntity.getReservation());
			}
			commitEntityTransaction();
			
			System.out.println("RETURN TicketEjb delete, deletedTicket:" + deletedTicketEntity);
			return new ModelToDtoConverter().ticketDtoFactory(deletedTicketEntity);
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
			rollbackEntityTransaction();
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			rollbackEntityTransaction();
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

	@SuppressWarnings("serial")
	@Override
	public List<CustomerDto> findCustomersLeavingOnDate(LocalDate localDate) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("CommonEjb findCustomersLeavingOnDate, flight:" + localDate);
		try {
			if (null == localDate) {
				System.out.println("RETURN CommonEjb findCustomersLeavingOnDate, VALIDATOR ERROR");
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			beginEntityTransaction();
			List<Flight> foundFlightEntityList = new FlightCrud().findFlightByDepartureDate(getEntityManager(), localDate);
			System.out.println(foundFlightEntityList);
			List<Customer> customerEntityList = new ModelsManagingUtils().findCustomersLeavingOnDate(foundFlightEntityList
																									, new CustomerCrud()
																									, getEntityManager());
			System.out.println(customerEntityList);
			ModelToDtoConverter modelToDtoConverter = new ModelToDtoConverter();
			List<CustomerDto> customerDtoList = new ArrayList<>() {
				{
					customerEntityList.forEach(customerEntity 
									-> add(modelToDtoConverter.customerDtoFactoryForReminderFlight(customerEntity)));
				}
			};

			System.out.println("RETURN CommonEjb findCustomersLeavingOnDate, flightDto:" + customerDtoList);
			return customerDtoList;
		} catch (ValidatorException e) {
			if (e.getMessage().contains(GeneralConstants.LOGIC_VALIDATOR_ERROR)) {
				rollbackEntityTransaction();
			}
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}
}
