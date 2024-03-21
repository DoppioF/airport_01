package airport_01Model.models.entities;

import java.io.Serializable;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import customUtils.constants.db.DbConstants;

@Entity
@Table(name = DbConstants.AirportTable.TABLE_NAME)
public class Airport implements Serializable {

	private static final long serialVersionUID = 8100214710204079942L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = DbConstants.COLUMN_PK)
	private Long id;
	
	@Column(name = DbConstants.NAME)
	private String name;
	
	@Column(name = DbConstants.AirportTable.COLUMN_CITY)
	private String city;
	
	@OneToMany(mappedBy = DbConstants.AirportTable.MAP_DEPARTURE)
	@JsonbTransient
	private List<FlightRoute> departureAirport;
	
	@OneToMany(mappedBy = DbConstants.AirportTable.MAP_ARRIVAL, fetch = FetchType.LAZY)
	@JsonbTransient
	private List<FlightRoute> arrivalAirport;
	
	
	
	public Airport() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<FlightRoute> getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(List<FlightRoute> departureAirport) {
		this.departureAirport = departureAirport;
	}

	public List<FlightRoute> getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(List<FlightRoute> arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	@Override
	public String toString() {
		return "Airport [id=" + id + ", name=" + name + ", city=" + city + ", departureAirportSize=" + (departureAirport == null ? 0 : departureAirport.size())
				+ ", arrivalAirport=" + (arrivalAirport == null ? 0 : arrivalAirport.size()) + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return
			null != obj
			&& obj instanceof Airport
			&& ((Airport) obj).getId() == this.id;
	}
}
