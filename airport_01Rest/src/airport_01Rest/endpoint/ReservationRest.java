package airport_01Rest.endpoint;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.interfaces.ReservationEjbInterface;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.ResponseDto;
import airport_01Rest.config.EJBFactory;
import airport_01Rest.interfaces.IReservationRest;
import airport_01Rest.utils.RestUtils;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RestConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Path(RestConstants.Airport_01Path.RESERVATION)
public class ReservationRest implements IReservationRest{

//	@SuppressWarnings("finally")
//	@Override
//	public Response findAllReservation() {
//		System.out.println("ReservationRest findAll, start");
//
//		ResponseDto responseDto = new ResponseDto();
//		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
//			try {
//				responseDto = new EJBFactory<ReservationEjbInterface>(ReservationEjbInterface.class).getEJB().findAllReservation();
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
//				System.out.println("RETURN ReservationRest findAll, DTO:" + responseDto);
//				return Response.status(responseStatus).entity(responseDto).build();
//			}
//	}
	
	@SuppressWarnings("finally")
	@Override
	public Response updateReservation(ReservationDto reservationDto) {
		System.out.println("ReservationRest update, reservationDto: " + reservationDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().updateReservation(reservationDto));
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
				System.out.println("RETURN ReservationRest delete, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}

	@SuppressWarnings("finally")
	@Override
	public Response deleteReservation(ReservationDto reservationDto) {
		System.out.println("ReservationRest delete, reservation: " + reservationDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().deleteReservation(reservationDto));
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
			} catch (Exception e) {
				responseDto.setMessage(e.getMessage());
			} finally {
				System.out.println("RETURN ReservationRest delete, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}

	@SuppressWarnings("finally")
	@Override
	public Response insertReservation(ReservationDto reservationDto) {
		System.out.println("ReservationRest insert, reservation: " + reservationDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<CommonControllerEjbInterface>(CommonControllerEjbInterface.class).getEJB().insertReservation(reservationDto));
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
				System.out.println("RETURN ReservationRest insert, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
	
	@SuppressWarnings("finally")
	public Response findReservationByIdCustomer(CustomerDto customerDto) {
		System.out.println("ReservationRest findReservationByIdCustomer, customer: " + customerDto);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
		try {
			new RestUtils().generateOkDto(responseDto, new EJBFactory<ReservationEjbInterface>(ReservationEjbInterface.class).getEJB().findReservationByIdCustomer(customerDto));
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
			System.out.println("RETURN ReservationRest findReservationByIdCustomer, DTO:" + responseDto);
			return Response.status(responseStatus).entity(responseDto).build();
		}
	}

//	@Override
//	public Response findReservationById(Long idReservation) {
//		System.out.println("ReservationRest findByID, start");
//
//		ResponseDto responseDto = new ResponseDto();
//		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
//			try {
//				responseDto = new EJBFactory<ReservationEjbInterface>(ReservationEjbInterface.class).getEJB().findReservationById(idReservation);
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
//				System.out.println("RETURN ReservationRest findById, DTO:" + responseDto);
//				return Response.status(responseStatus).entity(responseDto).build();
//			}
//	}
}
