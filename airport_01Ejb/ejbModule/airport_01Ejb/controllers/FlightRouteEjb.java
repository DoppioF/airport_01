package airport_01Ejb.controllers;

import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01.businessLogic.AirportProjectUtil;
import airport_01Crud.crud.FlightRouteCrud;
import airport_01Ejb.interfaces.FlightRouteEjbInterface;
import airport_01Ejb.utils.ModelsManagingUtils;
import airport_01Ejb.utils.converters.ModelToDtoConverter;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.RoleDto;
import airport_01Model.models.entities.FlightRoute;
import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.EjbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

@Stateless(name = EjbConstants.FLIGHT_ROUTE_EJB)
@LocalBean
public class FlightRouteEjb implements FlightRouteEjbInterface {

	@Override
	public List<FlightRouteDto> findAllFlightRoute(RoleDto roleDto) throws DBQueryException, UnforeseenException {
		System.out.println("FlightRouteEjb findAll, start");
		try {
			beginEntityTransaction();
			List<FlightRoute> returnedFlightRouteEntityList = new FlightRouteCrud().findAllFlightRoute(getEntityManager());
			
			if (0 == returnedFlightRouteEntityList.size()) {
				throw new DBQueryException(GeneralConstants.NO_RESULTS);
			}
			
			List<FlightRouteDto> flightRouteDtoList = new ModelsManagingUtils()
														.generateFlightRouteDtoListWithReservableFlights(returnedFlightRouteEntityList
																										, new ModelToDtoConverter()
																										, new AirportProjectUtil()
																										, null != roleDto 
																											&& DbConstants.RoleTable.ID_ADMIN == roleDto.getId());
			
			System.out.println("RETURN FlightRouteEjb findAll " + flightRouteDtoList);
			return flightRouteDtoList;
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
