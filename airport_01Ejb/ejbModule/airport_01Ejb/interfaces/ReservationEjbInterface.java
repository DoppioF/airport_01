package airport_01Ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.ReservationDto;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Local
public interface ReservationEjbInterface {

	List<ReservationDto> findReservationByIdCustomer(CustomerDto customerDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
}
