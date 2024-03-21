package airport_01Rest.endpoint;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import airport_01Ejb.interfaces.AirportEjbInterface;
import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.ResponseDto;
import airport_01Rest.config.EJBFactory;
import airport_01Rest.interfaces.IAirportRest;
import airport_01Rest.utils.RestUtils;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RestConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Path(RestConstants.Airport_01Path.AIRPORT)
public class AirportRest implements IAirportRest {

	@SuppressWarnings("finally")
	@Override
	public Response findAllAirport() {
		System.out.println("AirportRest findAll, start");

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<AirportEjbInterface>(AirportEjbInterface.class).getEJB().findAllAirport());
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
				System.out.println("RETURN AirportRest findAll, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response insertAirport(AirportDto airportDto) {
		System.out.println("AirportRest insert, airport:" + airportDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<AirportEjbInterface>(AirportEjbInterface.class).getEJB().insertAirport(airportDto));
				responseStatus = Status.OK;
				System.out.println("RETURN AirportRest insert, DTO:" + responseDto);
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
				System.out.println("RETURN AirportRest insert, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}

	@SuppressWarnings("finally")
	@Override
	public Response deleteAirport(AirportDto airportDto) {
		System.out.println("AirportRest delete, airport:" + airportDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().deleteAirport(airportDto));
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
				System.out.println("RETURN AirportRest delete, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response updateAirport(AirportDto airportDto) {
		System.out.println("AirportRest update, airport:" + airportDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<AirportEjbInterface>(AirportEjbInterface.class).getEJB().updateAirport(airportDto));
				responseStatus = Status.OK;
				System.out.println("RETURN AirportRest update, DTO:" + responseDto);
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
				System.out.println("RETURN AirportRest update, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
}
