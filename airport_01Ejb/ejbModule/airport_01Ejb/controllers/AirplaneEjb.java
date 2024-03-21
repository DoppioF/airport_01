package airport_01Ejb.controllers;

import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.commitEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;
import static airport_01Crud.connection.EntityManagerProvider.rollbackEntityTransaction;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01Crud.crud.AirplaneCrud;
import airport_01Ejb.interfaces.AirplaneEjbInterface;
import airport_01Ejb.utils.converters.DtoToModelConverter;
import airport_01Ejb.utils.converters.ModelToDtoConverter;
import airport_01Ejb.utils.validators.Validator;
import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.ResponseDto;
import airport_01Model.models.entities.Airplane;
import customUtils.constants.strings.EjbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import validatorUtils.ErrorsLogManager;

@Stateless(name = EjbConstants.AIRPLANE_EJB)
@LocalBean
public class AirplaneEjb implements AirplaneEjbInterface {
	private static final String CLASSNAME = "AirplaneEjb";
	
	@SuppressWarnings("serial")
	@Override
	public List<AirplaneDto> findAllAirplane() throws DBQueryException, UnforeseenException {
		System.out.println("AirplaneEjb findAll, start");
		try {
			beginEntityTransaction();
			List<Airplane> returnedAirplaneList = new AirplaneCrud().findAllAirplane(getEntityManager());
			
			if (returnedAirplaneList.size() == 0) {
				throw new DBQueryException(GeneralConstants.NO_RESULTS);
			}
			ModelToDtoConverter modelToDtoConverter = new ModelToDtoConverter();
			
			System.out.println("RETURN AirplaneEjb findAll");
			return new ArrayList<>() {
				{
					returnedAirplaneList.forEach(airplaneEntity -> add(modelToDtoConverter.airplaneDtoFactory(airplaneEntity)));
				}
			};
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
	public AirplaneDto insertAirplane(AirplaneDto airplaneDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("AirplaneEjb insert, airplaneDto: " + airplaneDto);
		try {
			List<String> errors = new Validator().airplaneInsert(airplaneDto);
			new ErrorsLogManager().dtoValidationErrorMessagesHandler(errors, CLASSNAME, "insertAirplane", GeneralConstants.VALIDATOR_ERROR);
			Airplane airplaneEntity = new DtoToModelConverter().airplaneFactory(airplaneDto);
			
			beginEntityTransaction();
			
			Airplane insertedAirplaneEntity = new AirplaneCrud().insertAirplane(getEntityManager(), airplaneEntity);
			
			commitEntityTransaction();
			
			System.out.println("RETURN AirplaneEjb insert, insertedAirplane: " + insertedAirplaneEntity);
			return new ModelToDtoConverter().airplaneDtoFactory(insertedAirplaneEntity);
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
	public AirplaneDto updateAirplane(AirplaneDto airplaneDto) throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("AirplaneEjb update, airplaneDto: " + airplaneDto);
		try {
			List<String> errors = new Validator().airplaneUpdate(airplaneDto);
			new ErrorsLogManager().dtoValidationErrorMessagesHandler(errors, CLASSNAME, "updateAirplane", GeneralConstants.VALIDATOR_ERROR);

			beginEntityTransaction();
			AirplaneCrud airplaneCrud = new AirplaneCrud();
			Airplane foundAirplaneEntity = airplaneCrud.findAirplaneById(getEntityManager(), airplaneDto.getId());
			Airplane airplaneEntity = new DtoToModelConverter().airplaneFactory(airplaneDto, 
																				foundAirplaneEntity.getFlightList());
			Airplane updatedAirplaneEntity = airplaneCrud.updateAirplane(getEntityManager(), airplaneEntity);
			
			commitEntityTransaction();
			getEntityManager().clear();
			
			System.out.println("RETURN AirplaneEjb update, updatedAirplane: " + updatedAirplaneEntity);
			return new ModelToDtoConverter().airplaneDtoFactory(updatedAirplaneEntity);
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
