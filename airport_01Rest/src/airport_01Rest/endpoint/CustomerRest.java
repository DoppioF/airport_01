package airport_01Rest.endpoint;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import airport_01Ejb.interfaces.CustomerEjbInterface;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.ResponseDto;
import airport_01Rest.config.EJBFactory;
import airport_01Rest.interfaces.ICustomerRest;
import airport_01Rest.utils.RestUtils;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RestConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Path(RestConstants.Airport_01Path.CUSTOMER)
public class CustomerRest implements ICustomerRest {

//	@Override
//	public Response findAllCustomer() {
//		return null;
//	}

	@SuppressWarnings("finally")
	@Override
	public Response findCustomerByEmailAndPassword(CustomerDto customerDto) {
		System.out.println("CustomerRest findCustomerByEmailAndPassword, customer:" + customerDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CustomerEjbInterface>(CustomerEjbInterface.class).getEJB().findCustomerByEmailAndPassword(customerDto));
				responseStatus = Status.OK;
			} catch (ValidatorException e) {
				responseDto.setMessage(e.getMessage());
			} catch (DBQueryException e) {
				if (e.getMessage().endsWith(GeneralConstants.NULL)) {
					responseDto.setMessage(GeneralConstants.WRONG_CREDENTIALS);
				} else {
					responseDto.setMessage(e.getMessage());
				}
			} catch (UnforeseenException e) {
				responseDto.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(GeneralConstants.GENERIC_ERROR);
			} finally {
				System.out.println("RETURN CustomerRest findCustomerByEmailAndPassword, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response findCustomerById(CustomerDto customerDto) {
		System.out.println("CustomerRest findCustomerById, customer:" + customerDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CustomerEjbInterface>(CustomerEjbInterface.class).getEJB().findCustomerById(customerDto));
				responseStatus = Status.OK;
				System.out.println("RETURN CustomerRest findCustomerById, DTO:" + responseDto);
			} catch (ValidatorException e) {
				responseDto.setMessage(e.getMessage());
			} catch (DBQueryException e) {
				responseDto.setMessage(e.getMessage());
			} catch (UnforeseenException e) {
				responseDto.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(GeneralConstants.GENERIC_ERROR);
			} finally {
				System.out.println("RETURN CustomerRest findCustomerById, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}

	@SuppressWarnings("finally")
	@Override
	public Response insertCustomer(CustomerDto customerDto) {
		System.out.println("CustomerRest insert, customer:" + customerDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CustomerEjbInterface>(CustomerEjbInterface.class).getEJB().insertCustomer(customerDto));
				responseStatus = Status.OK;
				System.out.println("RETURN CustomerRest insert, DTO:" + responseDto);
			} catch (ValidatorException e) {
				responseDto.setMessage(e.getMessage());
			} catch (DBQueryException e) {
				if (e.getMessage().endsWith(GeneralConstants.ALREADY_EXISTS)) {
					responseDto.setMessage("Customer " + GeneralConstants.ALREADY_EXISTS);
				} else {
					responseDto.setMessage(e.getMessage());
				}
			} catch (UnforeseenException e) {
				responseDto.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(GeneralConstants.GENERIC_ERROR);
			} finally {
				System.out.println("RETURN CustomerRest insert, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response updateCustomer(CustomerDto customerDto) {
		System.out.println("CustomerRest update, customer:" + customerDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CustomerEjbInterface>(CustomerEjbInterface.class).getEJB().updateCustomer(customerDto));
				responseStatus = Status.OK;
				System.out.println("RETURN CustomerRest update, DTO:" + responseDto);
			} catch (ValidatorException e) {
				responseDto.setMessage(e.getMessage());
			} catch (DBQueryException e) {
				if (e.getMessage().endsWith(GeneralConstants.ALREADY_EXISTS)) {
					responseDto.setMessage("Customer " + GeneralConstants.ALREADY_EXISTS);
				} else {
					responseDto.setMessage(e.getMessage());
				}
			} catch (UnforeseenException e) {
				responseDto.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(GeneralConstants.GENERIC_ERROR);
			} finally {
				System.out.println("RETURN CustomerRest update, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response deleteCustomer(CustomerDto customerDto) {
		System.out.println("CustomerRest delete, airport:" + customerDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CustomerEjbInterface>(CustomerEjbInterface.class).getEJB().deleteCustomer(customerDto));
				responseStatus = Status.OK;
			} catch (ValidatorException e) {
				responseDto.setMessage(e.getMessage());
			} catch (DBQueryException e) {
				responseDto.setMessage(e.getMessage());
			} catch (UnforeseenException e) {
				responseDto.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(GeneralConstants.GENERIC_ERROR);
			} finally {
				System.out.println("RETURN CustomerRest delete, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
}
