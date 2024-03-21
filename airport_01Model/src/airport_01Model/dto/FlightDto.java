package airport_01Model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class FlightDto implements Serializable {

	private static final long serialVersionUID = 7845247123822752743L;
	private Long id;
	private Long idFlightRoute;
	private Float distanceKm;
	private Long idDepartureAirport;
	private String nameDepartureAirport;
	private String cityDepartureAirport;
	private Long idArrivalAirport;
	private String nameArrivalAirport;
	private String cityArrivalAirport;
	private Long idAirplane;
	private String model;
	private Integer seats;
	private Float holdCapacity;
	private Float tankCapacity;
	private LocalDateTime departureDate;
	private LocalDateTime arrivalDate;
	private Float price;
	private Integer availableSeats;
	private List<ReservationDto> reservationList;
	
	public FlightDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdFlightRoute() {
		return idFlightRoute;
	}

	public void setIdFlightRoute(Long idFlightRoute) {
		this.idFlightRoute = idFlightRoute;
	}

	public Long getIdAirplane() {
		return idAirplane;
	}

	public void setIdAirplane(Long idAirplane) {
		this.idAirplane = idAirplane;
	}

	public LocalDateTime getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDateTime departureDate) {
		this.departureDate = departureDate;
	}

	public LocalDateTime getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDateTime arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(Integer availableSeats) {
		this.availableSeats = availableSeats;
	}

	public Float getDistanceKm() {
		return distanceKm;
	}

	public void setDistanceKm(Float distanceKm) {
		this.distanceKm = distanceKm;
	}

	public Long getIdDepartureAirport() {
		return idDepartureAirport;
	}

	public void setIdDepartureAirport(Long idDepartureAirport) {
		this.idDepartureAirport = idDepartureAirport;
	}

	public String getNameDepartureAirport() {
		return nameDepartureAirport;
	}

	public void setNameDepartureAirport(String nameDepartureAirport) {
		this.nameDepartureAirport = nameDepartureAirport;
	}

	public String getCityDepartureAirport() {
		return cityDepartureAirport;
	}

	public void setCityDepartureAirport(String cityDepartureAirport) {
		this.cityDepartureAirport = cityDepartureAirport;
	}

	public Long getIdArrivalAirport() {
		return idArrivalAirport;
	}

	public void setIdArrivalAirport(Long idArrivalAirport) {
		this.idArrivalAirport = idArrivalAirport;
	}

	public String getNameArrivalAirport() {
		return nameArrivalAirport;
	}

	public void setNameArrivalAirport(String nameArrivalAirport) {
		this.nameArrivalAirport = nameArrivalAirport;
	}

	public String getCityArrivalAirport() {
		return cityArrivalAirport;
	}

	public void setCityArrivalAirport(String cityArrivalAirport) {
		this.cityArrivalAirport = cityArrivalAirport;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public Float getHoldCapacity() {
		return holdCapacity;
	}

	public void setHoldCapacity(Float holdCapacity) {
		this.holdCapacity = holdCapacity;
	}

	public Float getTankCapacity() {
		return tankCapacity;
	}

	public void setTankCapacity(Float tankCapacity) {
		this.tankCapacity = tankCapacity;
	}

	public List<ReservationDto> getReservationList() {
		return reservationList;
	}

	public void setReservationList(List<ReservationDto> reservationList) {
		this.reservationList = reservationList;
	}

	@Override
	public String toString() {
		return "FlightDto [id=" + id + ", idFlightRoute=" + idFlightRoute + ", distanceKm=" + distanceKm
				+ ", idDepartureAirport=" + idDepartureAirport + ", nameDepartureAirport=" + nameDepartureAirport
				+ ", cityDepartureAirport=" + cityDepartureAirport + ", idArrivalAirport=" + idArrivalAirport
				+ ", nameArrivalAirport=" + nameArrivalAirport + ", cityArrivalAirport=" + cityArrivalAirport
				+ ", idAirplane=" + idAirplane + ", model=" + model + ", seats=" + seats + ", holdCapacity="
				+ holdCapacity + ", tankCapacity=" + tankCapacity + ", departureDate=" + departureDate
				+ ", arrivalDate=" + arrivalDate + ", price=" + price + ", availableSeats=" + availableSeats
				+ ", reservationList=" + (null == reservationList ? 0 : reservationList.size()) + "]";
	}
	
	

}
