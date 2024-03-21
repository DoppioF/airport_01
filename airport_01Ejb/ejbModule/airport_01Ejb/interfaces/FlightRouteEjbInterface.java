package airport_01Ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.RoleDto;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

@Local
public interface FlightRouteEjbInterface {

	List<FlightRouteDto> findAllFlightRoute(RoleDto roleDto) throws DBQueryException, UnforeseenException;
	
}
