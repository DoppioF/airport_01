package airport_01Model.dto;

import java.io.Serializable;
import java.util.List;

public class AirplaneDto implements Serializable {

	private static final long serialVersionUID = -4830749753056702220L;
	private Long id;
	private String model;
	private Integer seats;
	private Float holdCapacity;
	private Float tankCapacity;
	private List<FlightDto> flightList;
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public List<FlightDto> getFlightList() {
		return flightList;
	}
	public void setFlightList(List<FlightDto> flightList) {
		this.flightList = flightList;
	}
	@Override
	public String toString() {
		return "AirplaneDto [id=" + id + ", model=" + model + ", seats=" + seats + ", holdCapacity=" + holdCapacity
				+ ", tankCapacity=" + tankCapacity + ", flightList=" + (null == flightList ? 0 : flightList.size())+ "]";
	}
	
	
}
