package airport_01Rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import airport_01Model.dto.CustomerDto;
import customUtils.constants.strings.RestConstants;

public interface ICustomerRest {

//	@GET
//	@Path(RestConstants.Airport_01Path.FIND_ALL_CUSTOMER)
//	@Produces(MediaType.APPLICATION_JSON)
//	Response findAllCustomer();
	
	@POST
	@Path(RestConstants.Airport_01Path.FIND_BY_EMAIL_AND_PASSWORD)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response findCustomerByEmailAndPassword(CustomerDto customerDto);
	
	@POST
	@Path(RestConstants.Airport_01Path.INSERT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response insertCustomer(CustomerDto customerDto);
	
	@PUT
	@Path(RestConstants.Airport_01Path.UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateCustomer(CustomerDto customerDto);
	
	@DELETE
	@Path(RestConstants.Airport_01Path.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteCustomer(CustomerDto customerDto);
	
	@POST
	@Path(RestConstants.Airport_01Path.FIND_CUSTOMER_BY_ID)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response findCustomerById(CustomerDto customerDto);
}
