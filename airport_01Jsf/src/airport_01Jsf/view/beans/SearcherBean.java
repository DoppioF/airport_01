package airport_01Jsf.view.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import airport_01Ejb.interfaces.AirplaneEjbInterface;
import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.interfaces.SearcherEjbInterface;
import airport_01Jsf.utils.NavigationMapper;
import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.models.entities.FlightRoute;
import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import searcher.dto.QueryResultDto;
import validatorUtils.ErrorsLogManager;

@SuppressWarnings("rawtypes")
@SessionScoped
@Named
public class SearcherBean implements Serializable {

	private static final long serialVersionUID = -8822495905976587563L;

	@EJB
	private SearcherEjbInterface searcherEjb;
	
	@Inject
	private RenderingBean renderingBean;
	
	@Inject
	private MessagesBean messagesBean;
	
	private List<QueryResultDto> resultDtoList;
	private List<AirplaneDto> airplaneDtoList;
	private List<AirportDto> airportDtoList;
	private List<CustomerDto> customerDtoList;
	private List<FlightDto> flightDtoList;
	private List<FlightRouteDto> flightRouteDtoList;
	private List<ReservationDto> reservationDtoList;
	
	private String input;
	
	public void handleSearch() {
		try {
			nullAllLists();
			System.out.println(input);
			resultDtoList = searcherEjb.invokeSearcher(input);
			
			for (QueryResultDto q : resultDtoList) {
				System.out.println(q.getDtoList());
			}
			setResults();
		} catch (ValidatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearcherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnforeseenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			renderingBean.switchPage(NavigationMapper.SEARCH_SUBPAGE);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setResults() {
		for (QueryResultDto resultDto : resultDtoList) {
			System.out.println(resultDto.getDtoList());
			if (!resultDto.getDtoList().isEmpty()) {
				if (AirplaneDto.class.equals(resultDto.getDtoType())) {
					manageResults(resultDto.getDtoList(), (AirplaneDto) resultDto.getDtoList().get(0));
				} else if (AirportDto.class.equals(resultDto.getDtoType())) {
					manageResults(resultDto.getDtoList(), (AirportDto) resultDto.getDtoList().get(0));
				} else if (CustomerDto.class.equals(resultDto.getDtoType())) {
					manageResults(resultDto.getDtoList(), (CustomerDto) resultDto.getDtoList().get(0));
				} else if (FlightDto.class.equals(resultDto.getDtoType())) {
					manageResults(resultDto.getDtoList(), (FlightDto) resultDto.getDtoList().get(0));
				} else if (FlightRouteDto.class.equals(resultDto.getDtoType())) {
					manageResults(resultDto.getDtoList(), (FlightRouteDto) resultDto.getDtoList().get(0));
				} else if (ReservationDto.class.equals(resultDto.getDtoType())) {
					manageResults(resultDto.getDtoList(), (ReservationDto) resultDto.getDtoList().get(0));
				}
			}
		}
	}
	
	private void manageResults(List<FlightRouteDto> newList, FlightRouteDto firstElement) {
		if (null == flightRouteDtoList) {
			flightRouteDtoList = newList;
		} else {
			addResultsToExistingList(newList, firstElement);
		}
	}
	
	private void addResultsToExistingList(List<FlightRouteDto> newList, FlightRouteDto firstElement) {
		for (FlightRouteDto route : newList) {
			flightRouteDtoList.add(route);
		}
	}
	
	private void manageResults(List<FlightDto> newList, FlightDto firstElement) {
		if (null == flightDtoList) {
			flightDtoList = newList;
		} else {
			addResultsToExistingList(newList, firstElement);
		}
	}
	
	private void addResultsToExistingList(List<FlightDto> newList, FlightDto firstElement) {
		for (FlightDto flight : newList) {
			flightDtoList.add(flight);
		}
	}
	
	private void manageResults(List<CustomerDto> newList, CustomerDto firstElement) {
		if (null == customerDtoList) {
			customerDtoList = newList;
		} else {
			addResultsToExistingList(newList, firstElement);
		}
	}
	
	private void addResultsToExistingList(List<CustomerDto> newList, CustomerDto firstElement) {
		for (CustomerDto customer : newList) {
			customerDtoList.add(customer);
		}
	}
	
	private void manageResults(List<ReservationDto> newList, ReservationDto firstElement) {
		if (null == reservationDtoList) {
			reservationDtoList = newList;
		} else {
			addResultsToExistingList(newList, firstElement);
		}
	}
	
	private void addResultsToExistingList(List<ReservationDto> newList, ReservationDto firstElement) {
		for (ReservationDto reservation : newList) {
			reservationDtoList.add(reservation);
		}
	}
	
	private void manageResults(List<AirplaneDto> newList, AirplaneDto firstElement) {
		if (null == airplaneDtoList) {
			airplaneDtoList = newList;
		} else {
			addResultsToExistingList(newList, firstElement);
		}
	}
	
	private void addResultsToExistingList(List<AirplaneDto> newList, AirplaneDto firstElement) {
		for (AirplaneDto airplane : newList) {
			airplaneDtoList.add(airplane);
		}
	}
	
	private void manageResults(List<AirportDto> newList, AirportDto firstElement) {
		if (null == airportDtoList) {
			airportDtoList = newList;
		} else {
			addResultsToExistingList(newList, firstElement);
		}
	}
	
	private void addResultsToExistingList(List<AirportDto> newList, AirportDto firstElement) {
		for (AirportDto airport : newList) {
			airportDtoList.add(airport);
		}
	}
	
	private void nullAllLists() {
		airplaneDtoList = null;
		airportDtoList = null;
		customerDtoList = null;
		flightDtoList = null;
		flightRouteDtoList = null;
		reservationDtoList = null;
	}
	
	public boolean allValorizedListsAreEmpty() {
		return (null == airplaneDtoList 		|| airplaneDtoList.isEmpty())
				&& (null == airportDtoList 		|| airportDtoList.isEmpty())
				&& (null == customerDtoList 	|| customerDtoList.isEmpty())
				&& (null == flightDtoList 		|| flightDtoList.isEmpty())
				&& (null == flightRouteDtoList 	|| flightRouteDtoList.isEmpty())
				&& (null == reservationDtoList 	|| reservationDtoList.isEmpty())
				&& !allListsAreNull();
	}
	
	public boolean allListsAreNull() {
		return null == airplaneDtoList
				&& null == airportDtoList
				&& null == customerDtoList
				&& null == flightDtoList
				&& null == flightRouteDtoList
				&& null == reservationDtoList;
	}
	
	public void provaStampa() {
		System.out.println(resultDtoList);
	}

	public List<QueryResultDto> getResultDtoList() {
		return resultDtoList;
	}

	public void setResultDtoList(List<QueryResultDto> resultDtoList) {
		this.resultDtoList = resultDtoList;
	}

	public RenderingBean getRenderingBean() {
		return renderingBean;
	}

	public void setRenderingBean(RenderingBean renderingBean) {
		this.renderingBean = renderingBean;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public List<AirplaneDto> getAirplaneDtoList() {
		return airplaneDtoList;
	}

	public void setAirplaneDtoList(List<AirplaneDto> airplaneDtoList) {
		this.airplaneDtoList = airplaneDtoList;
	}

	public List<AirportDto> getAirportDtoList() {
		return airportDtoList;
	}

	public void setAirportDtoList(List<AirportDto> airportDtoList) {
		this.airportDtoList = airportDtoList;
	}

	public List<CustomerDto> getCustomerDtoList() {
		return customerDtoList;
	}

	public void setCustomerDtoList(List<CustomerDto> customerDtoList) {
		this.customerDtoList = customerDtoList;
	}

	public List<FlightDto> getFlightDtoList() {
		return flightDtoList;
	}

	public void setFlightDtoList(List<FlightDto> flightDtoList) {
		this.flightDtoList = flightDtoList;
	}

	public List<FlightRouteDto> getFlightRouteDtoList() {
		return flightRouteDtoList;
	}

	public void setFlightRouteDtoList(List<FlightRouteDto> flightRouteDtoList) {
		this.flightRouteDtoList = flightRouteDtoList;
	}

	public List<ReservationDto> getReservationDtoList() {
		return reservationDtoList;
	}

	public void setReservationDtoList(List<ReservationDto> reservationDtoList) {
		this.reservationDtoList = reservationDtoList;
	}

	public MessagesBean getMessagesBean() {
		return messagesBean;
	}

	public void setMessagesBean(MessagesBean messagesBean) {
		this.messagesBean = messagesBean;
	}
}
