package airport_01Rest.endpoint;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Model.dto.ResponseDto;
import airport_01Model.dto.TicketDto;
import airport_01Rest.config.EJBFactory;
import airport_01Rest.interfaces.ITicketRest;
import airport_01Rest.utils.RestUtils;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RestConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Path(RestConstants.Airport_01Path.TICKET)
public class TicketRest implements ITicketRest{

	@SuppressWarnings("finally")
	@Override
	public Response deleteTicket(TicketDto ticketDto) {
		System.out.println("TicketRest delete, ticket: " + ticketDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().deleteTicket(ticketDto));
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
				System.out.println("RETURN TicketRest delete, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}


//	@SuppressWarnings("finally")
//	@Override
//	public Response deleteReservation(Reservation reservation) {
//		System.out.println("ReservationRest delete, reservation: " + reservation);
//
//		ResponseDto responseDto = new ResponseDto();
//		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
//			try {
//				responseDto = new EJBFactory<ReservationEjbInterface>(ReservationEjbInterface.class).getEJB().deleteReservation(reservation);
//				responseStatus = Status.OK;
//			} catch (ValidatorException e) {
//				responseDto.setMessage(e.getMessage());
//			} catch (DBQueryException e) {
//				if (e.getMessage().endsWith(GeneralConstants.VOID)) {
//					responseDto.setMessage(GeneralConstants.NO_RESULTS);
//					responseStatus = Status.OK;
//				} else {
//					responseDto.setMessage(e.getMessage());
//				}
//			} catch (Exception e) {
//				responseDto.setMessage(e.getMessage());
//			} finally {
//				System.out.println("RETURN ReservationRest delete, DTO:" + responseDto);
//				return Response.status(responseStatus).entity(responseDto).build();
//			}
//	}

	
}
