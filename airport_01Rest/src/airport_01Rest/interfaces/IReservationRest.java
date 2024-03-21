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
import airport_01Model.dto.ReservationDto;
import customUtils.constants.strings.RestConstants;

public interface IReservationRest {

//	@GET
//	@Path("/getFlightList")
//	@Produces(MediaType.APPLICATION_JSON)
//	Response findAllReservation();
//	
	@DELETE
	@Path(RestConstants.Airport_01Path.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteReservation(ReservationDto reservationDto);
	
	@PUT
	@Path(RestConstants.Airport_01Path.UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateReservation(ReservationDto reservationDto);
	
	@POST
	@Path(RestConstants.Airport_01Path.INSERT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response insertReservation(ReservationDto reservationDto);
	
	@POST
	@Path(RestConstants.Airport_01Path.FIND_RESERVATION_BY_ID_CUSTOMER)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response findReservationByIdCustomer(CustomerDto customerDto);
	
//	@GET
//	@Path("/findById/{idFlight}")
//	@Produces(MediaType.APPLICATION_JSON)
//	Response findReservationById(@PathParam("idFlight") Long idFlight);
}
