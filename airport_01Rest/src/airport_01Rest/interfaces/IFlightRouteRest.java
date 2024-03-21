package airport_01Rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.RoleDto;
import customUtils.constants.strings.RestConstants;

public interface IFlightRouteRest {

	@GET
	@Path(RestConstants.Airport_01Path.FIND_ALL_FLIGHT_ROUTE)
	@Produces(MediaType.APPLICATION_JSON)
	Response findAllFlightRoute();
	
	@POST
	@Path(RestConstants.Airport_01Path.FIND_FLIGHT_ROUTE_BY_AIRPORTS)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response findFlightRouteByAirports(
			@QueryParam("idDeparture") Long idDepartureAirport,
            @QueryParam("idArrival") Long idArrivalAirport,
            RoleDto roleDto);
	
	@POST
	@Path(RestConstants.Airport_01Path.INSERT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response insertFlightRoute(FlightRouteDto flightRouteDto);
	

	@DELETE
	@Path(RestConstants.Airport_01Path.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteFlightRoute(FlightRouteDto flightRouteDto);
}
