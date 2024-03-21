package airport_01Rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import airport_01Model.dto.FlightDto;
import airport_01Model.dto.RoleDto;
import customUtils.constants.strings.RestConstants;

public interface IFlightRest {

	@POST
	@Path(RestConstants.Airport_01Path.FIND_ALL_FLIGHT)
	@Produces(MediaType.APPLICATION_JSON)
	Response findAllFlight(RoleDto roleDto);
	
	@DELETE
	@Path(RestConstants.Airport_01Path.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteFlight(FlightDto flightDto);
	
	@PUT
	@Path(RestConstants.Airport_01Path.UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateFlight(FlightDto flightDto);
	
	@POST
	@Path(RestConstants.Airport_01Path.INSERT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response insertFlight(FlightDto flightDto);
	
//	@GET
//	@Path("/findById/{idFlight}")
//	@Produces(MediaType.APPLICATION_JSON)
//	Response findFlightById(@PathParam("idFlight") Long idFlight);
}
