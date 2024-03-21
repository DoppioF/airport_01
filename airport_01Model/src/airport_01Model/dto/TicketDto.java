package airport_01Model.dto;

import java.io.Serializable;

public class TicketDto implements Serializable {

	private static final long serialVersionUID = -6464452060741936009L;

	private Long id;
	private Float price;
	private String holderName;
	private String holderSurname;
	private Long idReservation;
	private boolean validity;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	public String getHolderSurname() {
		return holderSurname;
	}
	public void setHolderSurname(String holderSurname) {
		this.holderSurname = holderSurname;
	}
	public Long getIdReservation() {
		return idReservation;
	}
	public void setIdReservation(Long idReservation) {
		this.idReservation = idReservation;
	}
	public boolean isValidity() {
		return validity;
	}
	public void setValidity(boolean validity) {
		this.validity = validity;
	}
	@Override
	public String toString() {
		return "TicketDto [id=" + id + ", price=" + price + ", holderName=" + holderName + ", holderSurname="
				+ holderSurname + ", idReservation=" + idReservation + ", validity=" + validity + "]";
	}
}
