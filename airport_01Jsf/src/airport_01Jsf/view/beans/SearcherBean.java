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
			cleanAllLists();
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
					airplaneDtoList = resultDto.getDtoList();
				} else if (AirportDto.class.equals(resultDto.getDtoType())) {
					airportDtoList = resultDto.getDtoList();
				} else if (CustomerDto.class.equals(resultDto.getDtoType())) {
					customerDtoList = resultDto.getDtoList();
				} else if (FlightDto.class.equals(resultDto.getDtoType())) {
					flightDtoList = resultDto.getDtoList();
				} else if (FlightRouteDto.class.equals(resultDto.getDtoType())) {
					flightRouteDtoList = resultDto.getDtoList();
				} else if (ReservationDto.class.equals(resultDto.getDtoType())) {
					reservationDtoList = resultDto.getDtoList();
				}
			}
		}
	}
	
	private void cleanAllLists() {
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
