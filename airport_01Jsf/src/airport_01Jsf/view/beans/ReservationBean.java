package airport_01Jsf.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.interfaces.ReservationEjbInterface;
import airport_01Jsf.utils.NavigationMapper;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.TicketDto;
import airport_01Model.models.utilityModels.PaymentType;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@Named
@SessionScoped
public class ReservationBean implements Serializable {

	private static final long serialVersionUID = -8315096420881466624L;
	
	@EJB
	private ReservationEjbInterface reservationEjb;
	
	@EJB
	private CommonControllerEjbInterface commonEjb;
	
	@Inject
	private FlightBean flightBean;
	
	@Inject
	private FlightRouteBean flightRouteBean;
	
	@Inject
	private CustomerBean customerBean;
	
	@Inject
	private UtilsBean utilsBean;
	
	@Inject
	private RenderingBean renderingBean;
	
	@Inject
	private MessagesBean messagesBean;

	private int passengersNumber;
	private List<TicketDto> passengers;
	private String passengerName;
	private String passengerSurname;
	private List<ReservationDto> reservationList;

	public void handleChoosePassengersNumber() {
		if (1 > passengersNumber) {
			passengersNumber = 1;
		} 
		utilsBean.setDisablePassengersNumberInput(false);
	}
	
	@SuppressWarnings("serial")
	public void handleConfirmPassengerNumber() {
		passengers = new ArrayList<>() {
			{
				for (int counter = 0; counter < passengersNumber; counter++) {
					add(new TicketDto());
				}
			}
		};
		utilsBean.setDisablePassengersNumberInput(true);
	}
	
	public void getCustomerReservations() {
		try {
			reservationList = reservationEjb.findReservationByIdCustomer(customerBean.getCustomerDto());
			System.out.println(reservationList);
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		} catch (UnforeseenException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public void handleDeleteReservation(Long id) {
		try {
			commonEjb.deleteReservation(new ReservationDto() {
				{
					setId(id);
				}
			});
			deleteReservationFromReservationList(id);
			PrimeFaces.current().executeScript("PF('dialogDeleteReservation').show()");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		} catch (UnforeseenException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		}
	}
	
	public void handleInsertReservation() {
		try {
			commonEjb.insertReservation(reservationDtoFactory());
			utilsBean.setDisablePassengersNumberInput(false);
			passengersNumber = 1;
			flightRouteBean.init();
			renderingBean.setPageToRender(NavigationMapper.HOME_PAGE);
			PrimeFaces.current().executeScript("PF('dlg2').show()");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		} catch (UnforeseenException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		}
	}
	
	private ReservationDto reservationDtoFactory() {
		ReservationDto reservationDto = new ReservationDto();
		reservationDto.setIdCustomer(customerBean.getCustomerDto().getId());
		reservationDto.setIdFlight(flightBean.getFlightDto().getId());
		reservationDto.setPaymentMethod(PaymentType.CARTA);
		reservationDto.setTicketList(passengers);
		return reservationDto;
	}
	
	private void deleteReservationFromReservationList(Long id) {
		for (ReservationDto reservation : reservationList) {
			if (id == reservation.getId()) {
				reservationList.remove(reservation);
				break;
			}
		}
	}
	
	public int getPassengersNumber() {
		return passengersNumber;
	}

	public void setPassengersNumber(int passengersNumber) {
		this.passengersNumber = passengersNumber;
	}

	public List<TicketDto> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<TicketDto> passengers) {
		this.passengers = passengers;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getPassengerSurname() {
		return passengerSurname;
	}

	public void setPassengerSurname(String passengerSurname) {
		this.passengerSurname = passengerSurname;
	}

	public List<ReservationDto> getReservationList() {
		return reservationList;
	}

	public void setReservationList(List<ReservationDto> reservationList) {
		this.reservationList = reservationList;
	}

	public FlightRouteBean getFlightRouteBean() {
		return flightRouteBean;
	}

	public void setFlightRouteBean(FlightRouteBean flightRouteBean) {
		this.flightRouteBean = flightRouteBean;
	}
	
	
}
