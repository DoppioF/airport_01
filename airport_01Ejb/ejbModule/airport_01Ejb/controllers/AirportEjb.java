package airport_01Ejb.controllers;

import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.commitEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;
import static airport_01Crud.connection.EntityManagerProvider.rollbackEntityTransaction;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01Crud.crud.AirportCrud;
import airport_01Ejb.interfaces.AirportEjbInterface;
import airport_01Ejb.utils.converters.DtoToModelConverter;
import airport_01Ejb.utils.converters.ModelToDtoConverter;
import airport_01Ejb.utils.validators.Validator;
import airport_01Model.dto.AirportDto;
import airport_01Model.models.entities.Airport;
import customUtils.constants.strings.EjbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import validatorUtils.ErrorsLogManager;

@Stateless(name = EjbConstants.AIRPORT_EJB)
@LocalBean
public class AirportEjb implements AirportEjbInterface {
	private static final String CLASSNAME = "AirportEjb";
	
	@Override
	public List<AirportDto> findAllAirport() throws DBQueryException, UnforeseenException {
		System.out.println("AirportEjb findAll, start");
		try {
			
			beginEntityTransaction();
			
			List<Airport> returnedAirportEntityList = new AirportCrud().findAllAirport(getEntityManager());
			List<AirportDto> airportDtoList = new ArrayList<>();
			ModelToDtoConverter modelToDtoConverter = new ModelToDtoConverter();
			returnedAirportEntityList.forEach(airport 
					-> airportDtoList.add(modelToDtoConverter.airportDtoFactoryBasic(airport)));
			
			System.out.println("RETURN AirportEjb findAll");
			return airportDtoList;
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
	public AirportDto insertAirport(AirportDto airportDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("AirportEjb insert, airportDto: " + airportDto);
		try {
			List<String> errors = new Validator().airportInsert(airportDto);
			new ErrorsLogManager().dtoValidationErrorMessagesHandler(errors, CLASSNAME, "insertAirport", GeneralConstants.VALIDATOR_ERROR);
			Airport airportEntity = new DtoToModelConverter().airportFactory(airportDto);
			
			beginEntityTransaction();
			
			Airport insertedAirportEntity = new AirportCrud().insertAirport(getEntityManager(), airportEntity);
			
			commitEntityTransaction();
			
			System.out.println("RETURN AirportEjb insert, insertedAirport: " + insertedAirportEntity);
			return new ModelToDtoConverter().airportDtoFactoryBasic(insertedAirportEntity);
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
	public AirportDto updateAirport(AirportDto airportDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("AirportEjb update, airportDto: " + airportDto);
		try {
			List<String> errors = new Validator().airportUpdate(airportDto);
			new ErrorsLogManager().dtoValidationErrorMessagesHandler(errors, CLASSNAME, "updateAirport", GeneralConstants.VALIDATOR_ERROR);
		
			beginEntityTransaction();
			AirportCrud airportCrud = new AirportCrud();
			Airport foundAirportEntity = airportCrud.findAirportById(getEntityManager(), airportDto.getId());
			Airport airportEntity = new DtoToModelConverter()
									.airportFactory(airportDto
													, foundAirportEntity.getArrivalAirport()
													, foundAirportEntity.getDepartureAirport());
			Airport updatedAirportEntity = airportCrud.updateAirport(getEntityManager(), airportEntity);
			
			commitEntityTransaction();
			
			System.out.println("RETURN AirportEjb update, updatedAirport: " + updatedAirportEntity);
			return new ModelToDtoConverter().airportDtoFactoryBasic(updatedAirportEntity);
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
}
