package airport_01Jsf.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.interfaces.FlightRouteEjbInterface;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.RoleDto;
import customUtils.constants.db.DbConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@SessionScoped
@Named
public class FlightRouteBean implements Serializable {

	private static final long serialVersionUID = 1853145301413966769L;

	@EJB
	private FlightRouteEjbInterface flightRouteEjb;
	
	@EJB
	private CommonControllerEjbInterface commonEjb;
	
	@Inject
	private AirportBean airportBean;
	
	@Inject
	private FlightBean flightBean;
	
	@Inject
	private FlightRouteBean flightRouteBean;
	
	@Inject
	private RenderingBean renderingBean;
	
	@Inject
	private UtilsBean utilsBean;
	
	private List<FlightRouteDto> flightRouteList;
	
	private List<FlightDto> selectedFlights;
	private FlightRouteDto flightRoute;
	private Long idFlightRouteSelected;
	private Long idSelectedDepartureAirport;
	private Long idSelectedArrivalAirport;
	private Float distanceKm;
	private boolean selectedRouteExists;
	
	@PostConstruct
	public void init() {
		initFlightRouteList();
		flightRoute = new FlightRouteDto();
		selectedFlights = null;
	}
	
	private void initFlightRouteList() {
		try {
			flightRouteList = flightRouteEjb.findAllFlightRoute(null);
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public void initFlightRouteListForAdmin() {
		try {
			flightRouteList = flightRouteEjb.findAllFlightRoute(new RoleDto() {
				{
					setId(DbConstants.RoleTable.ID_ADMIN);
				}
			});
			if (null == airportBean.getSelectedAirportArrival().getId()
				|| null == airportBean.getSelectedAirportDeparture().getId()) {
				
				getFlightsByAirports();
			}
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void validateRoute() {
		try {
			for(FlightRouteDto route : flightRouteList) {
				if (route.getId() == flightRoute.getId()) {
					selectedFlights = route.getFlightList();
					System.out.println(route.getId() == flightRoute.getId());
					break;
				}
			}
			System.out.println(selectedFlights);
		} catch (Exception e) {
			System.out.println("errore");
		}
	}
	
	public void getFlightsByAirports() {
		selectedRouteExists = false;
		if (null != airportBean.getSelectedAirportArrival().getId()
			&& null != airportBean.getSelectedAirportDeparture().getId()) {
			
			checkIfRouteExists();
			outcomeActionAfterSearchFlight();
		}
		flightRouteBean.valorizeFlightRouteWithCities();
	}
	
	public void valorizeFlightRouteWithCities() {
		if (null != airportBean.getSelectedAirportDeparture().getId()
			&& null != airportBean.getSelectedAirportArrival().getId()) {
			
			for (AirportDto airportDto : airportBean.getAirportListDeparture()) {
				if (airportDto.getId() == airportBean.getSelectedAirportDeparture().getId()) {
					flightRoute.setDepartureAirportCity(airportDto.getAirportCity());
					break;
				}
			}
			for (AirportDto airportDto : airportBean.getAirportListArrival()) {
				if (airportDto.getId() == airportBean.getSelectedAirportArrival().getId()) {
					flightRoute.setArrivalAirportCity(airportDto.getAirportCity());
					break;
				}
			}
		}
	}
	
	private void checkIfRouteExists() {
		for (FlightRouteDto route : flightRouteList) {
			if (route.getIdArrivalAirport() == airportBean.getSelectedAirportArrival().getId()
				&& route.getIdDepartureAirport() == airportBean.getSelectedAirportDeparture().getId()) {
				
				selectedRouteExists = true;
				selectedFlights = route.getFlightList();
				idFlightRouteSelected = route.getId();
				flightBean.getFlightDto().setIdFlightRoute(idFlightRouteSelected);
				break;
			}
		}
	}
	
	private void outcomeActionAfterSearchFlight() {
		if (!selectedRouteExists) {
			utilsBean.showModal("dialogInsertRoute");
		}
	}
	
	public void handleInsertFlightRouteRequest() {
		utilsBean.showModal("dialogInsertDistance");
	}
	
	public void handleInsertFlightRoute() {
		try {
			FlightRouteDto flightRouteDto = new FlightRouteDto();
			flightRouteDto.setIdArrivalAirport(airportBean.getSelectedAirportArrival().getId());
			flightRouteDto.setIdDepartureAirport(airportBean.getSelectedAirportDeparture().getId());
			flightRouteDto.setDistanceKm(distanceKm);
			flightRouteList.add(commonEjb.insertFlightRoute(flightRouteDto));
			utilsBean.showModal("dialogRouteInserted");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public void handleDeleteFlightRoute() {
		try {
			commonEjb.deleteFlightRoute(new FlightRouteDto() {
				{
					setId(idFlightRouteSelected);
				}
			});
			removeRouteFromFlightRouteList();
			utilsBean.showModal("dialogRouteDeleted");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}
	
	private void removeRouteFromFlightRouteList() {
		for (FlightRouteDto route : flightRouteList) {
			if (idFlightRouteSelected == route.getId()) {
				flightRouteList.remove(route);
				selectedRouteExists = false;
				break;
			}
		}
	}
	
//	public void rebuildArrivalRoutes() {
//		routesToShowOnSelectArrival = new ArrayList<>() {
//			private static final long serialVersionUID = 4750852274765575350L;
//			{
//				flightRouteList.forEach(route -> {
//					if (route.getIdDepartureAirport() == idSelectedDepartureAirport) {
//						add(route);
//					}
//				});
//			}
//		};
//	}
	
	
	public List<FlightRouteDto> getFlightRouteList() {
		return flightRouteList;
	}

	public void setFlightRouteList(List<FlightRouteDto> flightRouteList) {
		this.flightRouteList = flightRouteList;
	}

	public FlightRouteDto getFlightRoute() {
		return flightRoute;
	}

	public void setFlightRoute(FlightRouteDto flightRoute) {
		this.flightRoute = flightRoute;
	}

	public Long getIdFlightRouteSelected() {
		return idFlightRouteSelected;
	}

	public void setIdFlightRouteSelected(Long idFlightRouteSelected) {
		this.idFlightRouteSelected = idFlightRouteSelected;
	}

	public Long getIdSelectedDepartureAirport() {
		return idSelectedDepartureAirport;
	}

	public void setIdSelectedDepartureAirport(Long idSelectedDepartureAirport) {
		this.idSelectedDepartureAirport = idSelectedDepartureAirport;
	}

	public Long getIdSelectedArrivalAirport() {
		return idSelectedArrivalAirport;
	}

	public void setIdSelectedArrivalAirport(Long idSelectedArrivalAirport) {
		this.idSelectedArrivalAirport = idSelectedArrivalAirport;
	}

	public List<FlightDto> getSelectedFlights() {
		System.out.println("SELECTED FLIGHTS: " + selectedFlights);
		return selectedFlights;
	}

	public void setSelectedFlights(List<FlightDto> selectedFlights) {
		this.selectedFlights = selectedFlights;
	}

	public RenderingBean getRenderingBean() {
		return renderingBean;
	}

	public void setRenderingBean(RenderingBean renderingBean) {
		this.renderingBean = renderingBean;
	}

	public boolean isSelectedRouteExists() {
		return selectedRouteExists;
	}

	public void setSelectedRouteExists(boolean selectedRouteExists) {
		this.selectedRouteExists = selectedRouteExists;
	}

	public FlightBean getFlightBean() {
		return flightBean;
	}

	public void setFlightBean(FlightBean flightBean) {
		this.flightBean = flightBean;
	}

	public Float getDistanceKm() {
		return distanceKm;
	}

	public void setDistanceKm(Float distanceKm) {
		this.distanceKm = distanceKm;
	}

	
}
