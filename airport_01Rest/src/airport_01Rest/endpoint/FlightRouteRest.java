package airport_01Rest.endpoint;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.interfaces.FlightRouteEjbInterface;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ResponseDto;
import airport_01Model.dto.RoleDto;
import airport_01Rest.config.EJBFactory;
import airport_01Rest.interfaces.IFlightRouteRest;
import airport_01Rest.utils.RestUtils;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RestConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Path(RestConstants.Airport_01Path.FLIGHT_ROUTE)
public class FlightRouteRest implements IFlightRouteRest {

	@SuppressWarnings("finally")
	@Override
	public Response findAllFlightRoute() {
		System.out.println("FlightRouteRest findAll, start");

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<FlightRouteEjbInterface>(FlightRouteEjbInterface.class).getEJB().findAllFlightRoute(null));
				responseStatus = Status.OK;
			} catch (ValidatorException e) {
				responseDto.setMessage(e.getMessage());
			} catch (DBQueryException e) {
				if (e.getMessage().endsWith(GeneralConstants.VOID)) {
					responseDto.setMessage(GeneralConstants.NO_RESULTS);
					responseStatus = Status.OK;
				} else {
					responseDto.setMessage(e.getMessage());
				}
			} catch (UnforeseenException e) {
				responseDto.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(GeneralConstants.GENERIC_ERROR);
			} finally {
				System.out.println("RETURN FlightRouteRest findAll, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	//									Esempio: ?idDeparture=1&idArrival=5
	public Response findFlightRouteByAirports(
												@QueryParam("idDeparture") Long idDepartureAirport,
									            @QueryParam("idArrival") Long idArrivalAirport,
									            RoleDto roleDto
									         ) {
		System.out.println("FlightRouteRest findFlightRouteByAirports, airports: " 
									         + idDepartureAirport + ", " + idArrivalAirport);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB()
						.findFlightRouteByAirports(idDepartureAirport, idArrivalAirport, roleDto));
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
				System.out.println("RETURN FlightRouteRest findFlightRouteByEmailAndPassword, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response insertFlightRoute(FlightRouteDto flightRouteDto) {
		System.out.println("FlightRouteRest insert, flightRoute:" + flightRouteDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().insertFlightRoute(flightRouteDto));
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
				System.out.println("RETURN FlightRouteRest insert, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response deleteFlightRoute(FlightRouteDto flightRouteDto) {
		System.out.println("FlightRouteRest delete, flightRoute:" + flightRouteDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().deleteFlightRoute(flightRouteDto));
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
				System.out.println("RETURN FlightRouteRest delete, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}

}
