package airport_01Model.dto;

import java.io.Serializable;
import java.util.List;

public class FlightRouteDto implements Serializable {

	private static final long serialVersionUID = 1791293047960835926L;
	private Long id;
	private Float distanceKm;
	private Long idDepartureAirport;
	private Long idArrivalAirport;
	private String departureAirportName;
	private String arrivalAirportName;
	private String departureAirportCity;
	private String arrivalAirportCity;
	private List<FlightDto> flightList;
	
	public FlightRouteDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getDistanceKm() {
		return distanceKm;
	}

	public void setDistanceKm(Float distanceKm) {
		this.distanceKm = distanceKm;
	}

	public String getDepartureAirportName() {
		return departureAirportName;
	}

	public void setDepartureAirportName(String departureAirportName) {
		this.departureAirportName = departureAirportName;
	}

	public String getArrivalAirportName() {
		return arrivalAirportName;
	}

	public void setArrivalAirportName(String arrivalAirportName) {
		this.arrivalAirportName = arrivalAirportName;
	}
	
	public Long getIdDepartureAirport() {
		return idDepartureAirport;
	}

	public void setIdDepartureAirport(Long idDepartureAirport) {
		this.idDepartureAirport = idDepartureAirport;
	}

	public Long getIdArrivalAirport() {
		return idArrivalAirport;
	}

	public void setIdArrivalAirport(Long idArrivalAirport) {
		this.idArrivalAirport = idArrivalAirport;
	}

	public List<FlightDto> getFlightList() {
		return flightList;
	}

	public void setFlightList(List<FlightDto> flightList) {
		this.flightList = flightList;
	}
	
	public String getDepartureAirportCity() {
		return departureAirportCity;
	}

	public void setDepartureAirportCity(String departureAirportCity) {
		this.departureAirportCity = departureAirportCity;
	}

	public String getArrivalAirportCity() {
		return arrivalAirportCity;
	}

	public void setArrivalAirportCity(String arrivalAirportCity) {
		this.arrivalAirportCity = arrivalAirportCity;
	}

	@Override
	public String toString() {
		return "FlightRouteDto [id=" + id + ", distanceKm=" + distanceKm + ", idDepartureAirport=" + idDepartureAirport
				+ ", idArrivalAirport=" + idArrivalAirport + ", departureAirportName=" + departureAirportName
				+ ", arrivalAirportName=" + arrivalAirportName + ", departureAirportCity=" + departureAirportCity
				+ ", arrivalAirportCity=" + arrivalAirportCity + ", flightList=" + (null == flightList ? 0 : flightList.size()) + "]";
	}

	
	
	
}
