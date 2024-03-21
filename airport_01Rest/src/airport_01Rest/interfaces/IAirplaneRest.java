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

import airport_01Model.dto.AirplaneDto;
import customUtils.constants.strings.RestConstants;

public interface IAirplaneRest {

	@GET
	@Path(RestConstants.Airport_01Path.FIND_ALL_AIRPLANE)
	@Produces(MediaType.APPLICATION_JSON)
	Response findAllAirplane();
	
	@POST
	@Path(RestConstants.Airport_01Path.INSERT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response insertAirplane(AirplaneDto airplaneDto);
	
	@DELETE
	@Path(RestConstants.Airport_01Path.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteAirplane(AirplaneDto airplaneDto);
	
	@PUT
	@Path(RestConstants.Airport_01Path.UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateAirplane(AirplaneDto airplaneDto);
}
