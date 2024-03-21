package airport_01Ejb.controllers;

import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01Crud.crud.ReservationCrud;
import airport_01Ejb.interfaces.ReservationEjbInterface;
import airport_01Ejb.utils.ModelToDtoConverter;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.models.entities.Reservation;
import customUtils.constants.strings.EjbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Stateless(name = EjbConstants.RESERVATION_EJB)
@LocalBean
public class ReservationEjb implements ReservationEjbInterface {

	@Override
	public List<ReservationDto> findReservationByIdCustomer(CustomerDto customerDto)
			throws ValidatorException, DBQueryException, UnforeseenException {
		System.out.println("ReservationEjb findReservationByIdCustomer, customer:" + customerDto);
		try {
			if (null == customerDto || null == customerDto.getId()) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			
			beginEntityTransaction();
			List<Reservation> reservationEntityList = new ReservationCrud().findReservationByIdCustomer(getEntityManager(), customerDto.getId());
			List<ReservationDto> reservationDtoList = new ArrayList<>();
			ModelToDtoConverter modelToDtoConverter = new ModelToDtoConverter();
			
			reservationEntityList.forEach(reservation -> reservationDtoList.add(modelToDtoConverter.reservationDtoFactory(reservation)));
			
			System.out.println("RETURN ReservationEjb findReservationByIdCustomer, customer:" + reservationDtoList);
			return reservationDtoList;
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}

}
