package airport_01Ejb.interfaces;

import javax.ejb.Local;

import airport_01Model.dto.CustomerDto;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Local
public interface CustomerEjbInterface {

	CustomerDto findCustomerByEmailAndPassword(CustomerDto customerDto) throws ValidatorException, DBQueryException, UnforeseenException;

	CustomerDto findCustomerById(CustomerDto customerDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
	CustomerDto insertCustomer(CustomerDto customerDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
	CustomerDto updateCustomer(CustomerDto customerDto) throws ValidatorException, DBQueryException, UnforeseenException;
	
	CustomerDto deleteCustomer(CustomerDto customerDto) throws ValidatorException, DBQueryException, UnforeseenException;
}
