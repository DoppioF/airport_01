package airport_01Ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import airport_01Model.dto.AirplaneDto;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Local
public interface AirplaneEjbInterface {

	List<AirplaneDto> findAllAirplane() throws DBQueryException, UnforeseenException;
	
	AirplaneDto insertAirplane(AirplaneDto airplaneDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
	AirplaneDto updateAirplane(AirplaneDto airplaneDto) throws ValidatorException, DBQueryException, UnforeseenException;
}
