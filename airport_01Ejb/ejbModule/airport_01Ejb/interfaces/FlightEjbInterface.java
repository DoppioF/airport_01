package airport_01Ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import airport_01Model.dto.FlightDto;
import airport_01Model.dto.RoleDto;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Local
public interface FlightEjbInterface {

	List<FlightDto> findAllFlight(RoleDto roleDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
	FlightDto findFlightById(FlightDto flightDto) throws ValidatorException, DBQueryException, UnforeseenException;
}
