package airport_01Model.models.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import airport_01Model.models.utilityModels.PaymentType;
import customUtils.constants.db.DbConstants;

@Entity
@Table(name = DbConstants.ReservationTable.TABLE_NAME)
public class Reservation implements Serializable {

	private static final long serialVersionUID = 5845638998656588028L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = DbConstants.COLUMN_PK)
	private Long id;
	
	@Column(name = DbConstants.ReservationTable.COLUMN_DATE)
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	@Column(name = DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD)
	private PaymentType paymentMethod;
	
	@Column(name = DbConstants.COLUMN_VALIDITY)
	private Boolean validity;
	
	@ManyToOne
	@JoinColumn(name = DbConstants.CustomerTable.COLUMN_FK,  insertable = true, updatable = true)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = DbConstants.FlightTable.COLUMN_FK,  insertable = true, updatable = true)
	private Flight flight;
	
	@OneToMany(mappedBy = DbConstants.ReservationTable.TABLE_NAME)
	//@JsonbTransient
	private List<Ticket> ticketList;
	
	
	
	public Reservation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public PaymentType getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentType paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Ticket> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<Ticket> ticketList) {
		this.ticketList = ticketList;
	}
	
	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public Boolean getValidity() {
		return validity;
	}

	public void setValidity(Boolean validity) {
		this.validity = validity;
	}

	@Override
	public boolean equals(Object obj) {
		return
			null != obj
			&& obj instanceof Reservation
			&& ((Reservation) obj).getId() == this.id;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", date=" + date + ", paymentMethod=" + paymentMethod + ", customer="
				+ customer + ", flight=" + flight + ", validity=" + validity +
				", tickets=" + (null == ticketList ? 0 : ticketList.size()) + "]";
	}
	
	
}
