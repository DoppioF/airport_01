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
@Table(name = DbConstants.AirplaneTable.TABLE_NAME)
public class Airplane implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = DbConstants.COLUMN_PK)
	private Long id;
	
	@Column(name = DbConstants.AirplaneTable.COLUMN_MODEL)
	private String model;
	
	@Column(name = DbConstants.AirplaneTable.COLUMN_SEATS)
	private Integer seats;
	
	@Column(name = DbConstants.AirplaneTable.COLUMN_HOLD_CAPACITY)
	private Float holdCapacity;
	
	@Column(name = DbConstants.AirplaneTable.COLUMN_TANK_CAPACITY)
	private Float tankCapacity;
	
	@OneToMany(mappedBy = DbConstants.AirplaneTable.TABLE_NAME, fetch = FetchType.LAZY)
	@JsonbTransient
	private List<Flight> flightList;
	
	
	
	public Airplane() {
		super();
	}
	
	public List<Flight> getFlightList() {
		return flightList;
	}

	public void setFlightList(List<Flight> flightList) {
		this.flightList = flightList;
	}


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
	
	@Override
	public String toString() {
		return "Airplane [id=" + id + ", model=" + model + ", seats=" + seats + ", holdCapacity=" + holdCapacity
				+ ", tankCapacity=" + tankCapacity + ", flightListSize=" + (flightList == null ? 0 : flightList.size()) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return
			null != obj
			&& obj instanceof Airplane
			&& ((Airplane) obj).getId() == this.id;
	}
}
