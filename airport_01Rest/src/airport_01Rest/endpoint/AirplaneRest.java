package airport_01Rest.endpoint;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import airport_01Ejb.interfaces.AirplaneEjbInterface;
import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.ResponseDto;
import airport_01Rest.config.EJBFactory;
import airport_01Rest.interfaces.IAirplaneRest;
import airport_01Rest.utils.RestUtils;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RestConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Path(RestConstants.Airport_01Path.AIRPLANE)
public class AirplaneRest implements IAirplaneRest {

	@SuppressWarnings("finally")
	@Override
	public Response findAllAirplane() {
		System.out.println("AirplaneRest findAll, start");

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<AirplaneEjbInterface>(AirplaneEjbInterface.class).getEJB().findAllAirplane());
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
				System.out.println("RETURN AirplaneRest findAll, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response insertAirplane(AirplaneDto airplaneDto) {
		System.out.println("AirplaneRest insert, airplane:" + airplaneDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<AirplaneEjbInterface>(AirplaneEjbInterface.class).getEJB().insertAirplane(airplaneDto));
				responseStatus = Status.OK;
				System.out.println("RETURN AirplaneRest insert, DTO:" + responseDto);
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
				System.out.println("RETURN AirplaneRest insert, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}

	@SuppressWarnings("finally")
	@Override
	public Response deleteAirplane(AirplaneDto airplaneDto) {
		System.out.println("AirplaneRest delete, airplane:" + airplaneDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().deleteAirplane(airplaneDto));
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
				System.out.println("RETURN AirplaneRest delete, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	@Override
	public Response updateAirplane(AirplaneDto airplaneDto) {
		System.out.println("AirplaneRest update, airplane:" + airplaneDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto,  new EJBFactory<AirplaneEjbInterface>(AirplaneEjbInterface.class).getEJB().updateAirplane(airplaneDto));
				responseStatus = Status.OK;
				System.out.println("RETURN AirplaneRest update, DTO:" + responseDto);
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
				System.out.println("RETURN AirplaneRest update, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
}
