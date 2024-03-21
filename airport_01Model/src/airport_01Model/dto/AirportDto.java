package airport_01Model.dto;

import java.io.Serializable;
import java.util.List;

import airport_01Model.models.entities.Airport;

public class AirportDto implements Serializable {

	private static final long serialVersionUID = -2908823443544637581L;
	private Long id;
	private String airportName;
	private String airportCity;
	
	private List<FlightRouteDto> departureAirport;
	private List<FlightRouteDto> arrivalAirport;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAirportName() {
		return airportName;
	}
	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}
	public String getAirportCity() {
		return airportCity;
	}
	public void setAirportCity(String airportCity) {
		this.airportCity = airportCity;
	}
	public List<FlightRouteDto> getDepartureAirport() {
		return departureAirport;
	}
	public void setDepartureAirport(List<FlightRouteDto> departureAirport) {
		this.departureAirport = departureAirport;
	}
	public List<FlightRouteDto> getArrivalAirport() {
		return arrivalAirport;
	}
	public void setArrivalAirport(List<FlightRouteDto> arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}
	@Override
	public String toString() {
		return "AirportDto [id=" + id + ", airportName=" + airportName + ", airportCity=" + airportCity
				+ ", departureAirport=" + (null == departureAirport ? 0 : departureAirport.size())
				+ ", arrivalAirport=" + (null == arrivalAirport ? 0 : arrivalAirport.size()) + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airportCity == null) ? 0 : airportCity.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		return
				null != obj
				&& obj instanceof AirportDto
				&& ((AirportDto) obj).getId() == this.id;
	}
	
	
	
	
	
}
