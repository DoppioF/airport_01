package airport_01Rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import airport_01Model.dto.AirportDto;
import customUtils.constants.strings.RestConstants;

public interface IAirportRest {

	@GET
	@Path(RestConstants.Airport_01Path.FIND_ALL_AIRPORT)
	@Produces(MediaType.APPLICATION_JSON)
	Response findAllAirport();
	
	@POST
	@Path(RestConstants.Airport_01Path.INSERT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response insertAirport(AirportDto airportDto);
	
	@DELETE
	@Path(RestConstants.Airport_01Path.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteAirport(AirportDto airportDto);
	
	@PUT
	@Path(RestConstants.Airport_01Path.UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateAirport(AirportDto airportDto);
}
