package airport_01Ejb.controllers;

import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01.businessLogic.SeatsManager;
import airport_01Crud.crud.FlightCrud;
import airport_01Ejb.interfaces.FlightEjbInterface;
import airport_01Ejb.utils.converters.ModelToDtoConverter;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.RoleDto;
import airport_01Model.models.entities.Flight;
import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.EjbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Stateless(name = EjbConstants.FLIGHT_EJB)
@LocalBean
public class FlightEjb implements FlightEjbInterface {

	@SuppressWarnings("serial")
	@Override
	public List<FlightDto> findAllFlight(RoleDto roleDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("FlightEjb findAll, start");
		try {
			if (null == roleDto || null == roleDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			beginEntityTransaction();
			
			List<Flight> returnedFlightEntityList = new FlightCrud().findAllFlight(getEntityManager());
			
			if (0 == returnedFlightEntityList.size()) {
				throw new DBQueryException(GeneralConstants.NO_RESULTS);
			}
			
			SeatsManager seatsManager = new SeatsManager();
			ModelToDtoConverter modelToDtoConverter = new ModelToDtoConverter();
			
			List<FlightDto> flightDtoList = new ArrayList<>() {
				{
					for (Flight flight : returnedFlightEntityList) {
						if (null != flight.getDepartureDate() && flight.getDepartureDate().isAfter(LocalDateTime.now())) {
							FlightDto flightDto = (roleDto.getId() != DbConstants.RoleTable.ID_ADMIN
														? modelToDtoConverter
															.flightDtoFactoryForCustomerFlightChoice(flight, seatsManager.countAvailableSeats(flight))
														: modelToDtoConverter.flightDtoFactoryForAdmin(flight, seatsManager.countAvailableSeats(flight)));
							add(flightDto);
						}
					}
				}
			};
			System.out.println("RETURN FlightEjb findAll: " + flightDtoList);
			return flightDtoList;
		} catch (ValidatorException e) {
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

	@Override
	public FlightDto findFlightById(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("FlightEjb findFlightById, flight:" + flightDto);
		try {
			if (null == flightDto || null == flightDto.getId()) {
				System.out.println("RETURN FlightEjb findFlightById, VALIDATOR ERROR");
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			beginEntityTransaction();
			Flight foundFlightEntity = new FlightCrud().findFlightById(getEntityManager(), flightDto.getId());
			
			FlightDto responseFlightDto = new ModelToDtoConverter().flightDtoFactoryForCustomerFlightChoice(foundFlightEntity, new SeatsManager().countAvailableSeats(foundFlightEntity));

			System.out.println("RETURN FlightEjb findFlightById, flightDto:" + responseFlightDto);
			return responseFlightDto;
		} catch (ValidatorException e) {
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