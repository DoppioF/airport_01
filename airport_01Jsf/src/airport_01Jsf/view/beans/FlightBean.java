package airport_01Jsf.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.interfaces.FlightEjbInterface;
import airport_01Jsf.utils.NavigationMapper;
import airport_01Model.dto.FlightDto;
import customUtils.constants.airport_01.ViewJsfConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@SessionScoped
@Named
public class FlightBean implements Serializable {
	
	private static final long serialVersionUID = 6194013553413844393L;

	@EJB
	private FlightEjbInterface flightEjb;
	
	@EJB
	private CommonControllerEjbInterface commonEjb;
	
	@Inject
	private FlightRouteBean flightRouteBean;
	
	@Inject
	private CustomerBean customerBean;
	
	@Inject
	private RenderingBean renderingBean;
	
	@Inject
	private MessagesBean messagesBean;
	
	@Inject
	private UtilsBean utilsBean;

	private FlightDto flightDto;
	
	@PostConstruct
	public void init() {
		flightDto = new FlightDto();
	}
	
	public void handleSelectedFlight() {
		try {
			if (null != flightDto.getId()) {
				flightDto = flightEjb.findFlightById(flightDto);
			}
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			e.printStackTrace();
			messagesBean.unexpectedErrorMessage();
		} catch (UnforeseenException e) {
			e.printStackTrace();
			messagesBean.unexpectedErrorMessage();
		}
	}
	
	public void handleInsertFlight() {
		handleInsertUpdateFlight("Insert");
	}
	
	public void handleUpdateFlight() {
		handleInsertUpdateFlight("Update");
	}
	
	private void handleInsertUpdateFlight(String action) {
		System.out.println("VOLO ATTUALE: " + flightDto);
		List<String> errors = new ArrayList<>();
		checkFlightErrors(errors);
		if (errors.isEmpty()) {
			try {
				if ("Insert".equals(action)) {
					flightDto.setId(null);
					flightDto = commonEjb.insertFlight(flightDto);
					addFlightToList();
					System.out.println("VOLO INSERITO: " + flightDto);
				} else {
					flightDto = commonEjb.updateFlight(flightDto);
					System.out.println("VOLO AGGIORNATO: " + flightDto);
					modifyFlightInList();
				}
				
				utilsBean.showModal("dialogFlight" + action + ("Update".equals(action) ? "" : "e") + "d");
			} catch (ValidatorException e) {
				e.printStackTrace();
			} catch (DBQueryException e) {
				e.printStackTrace();
				messagesBean.unexpectedErrorMessage();
			} catch (UnforeseenException e) {
				e.printStackTrace();
				messagesBean.unexpectedErrorMessage();
			} finally {
				utilsBean.hideModal("dialog" + action + "Flight");
			}
		} else {
			utilsBean.showModal("dialog" + action + "Flight");
			showErrors(errors);
		}
	}
	
	public void handleDeleteFlight() {
		try {
			commonEjb.deleteFlight(flightDto);
			removeFlightFromList();
			init();
			
			utilsBean.showModal("dialogFlightDeleted");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		} finally {
			utilsBean.hideModal("dialogDeleteFlight");
		}
	}
	
	public void valorizeFlightBasedOnSelected() {
		for (FlightDto flightDto : flightRouteBean.getSelectedFlights()) {
			if (flightDto.getId() == this.flightDto.getId()) {
				this.flightDto = flightDto;
				break;
			}
		}
	}
	
	private void removeFlightFromList() {
		for (FlightDto flightDto : flightRouteBean.getSelectedFlights()) {
			if (flightDto.getId() == this.flightDto.getId()) {
				flightRouteBean.getSelectedFlights().remove(flightDto);
				//flightDto = this.flightDto;
				break;
			}
		}
	}
	
	private void addFlightToList() {
		flightRouteBean.getSelectedFlights().add(flightDto);
	}
	
	private void modifyFlightInList() {
		removeFlightFromList();
		addFlightToList();
	}
	
	private void showErrors(List<String> errors) {
		for (String error : errors) {
			messagesBean.newMessage(FacesMessage.SEVERITY_ERROR, error, error);
		}
	}
	
	private void checkFlightErrors(List<String> errors) {
		if (null == flightDto.getIdAirplane()) {
			errors.add(ViewJsfConstants.Errors.ERROR_FLIGHT_AIRPLANE);
		}
		
		if (null == flightDto.getDepartureDate()) {
			errors.add(ViewJsfConstants.Errors.ERROR_FLIGHT_DEPARTURE_DATE_REQUIRED);
		} else if (flightDto.getDepartureDate().isAfter(flightDto.getArrivalDate())) {
			errors.add(ViewJsfConstants.Errors.ERROR_FLIGHT_ARRIVAL_DATE_INVALID);
		}
		
		if (null == flightDto.getArrivalDate()) {
			errors.add(ViewJsfConstants.Errors.ERROR_FLIGHT_ARRIVAL_DATE_REQUIRED);
		}
	}
	
	public void handlePrenotaVoloButton(Long idFlight) {
		flightDto.setId(idFlight);;
		if (customerBean.isLogged()) {
			renderingBean.setPageToRender(NavigationMapper.RESERVATION_SUBPAGE);
		} else {
			renderingBean.setPageToRender(NavigationMapper.LOGIN_SUBPAGE);
		}
	}
	
	public FlightDto getFlightDto() {
		return flightDto;
	}

	public void setFlightDto(FlightDto flightDto) {
		this.flightDto = flightDto;
	}
}
