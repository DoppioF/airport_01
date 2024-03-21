package airport_01Ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import airport_01Model.dto.AirportDto;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Local
public interface AirportEjbInterface {

	List<AirportDto> findAllAirport() throws DBQueryException, UnforeseenException;

	AirportDto insertAirport(AirportDto airportDto) throws ValidatorException, DBQueryException, UnforeseenException;

	AirportDto updateAirport(AirportDto airportDto) throws ValidatorException, DBQueryException, UnforeseenException;

}
