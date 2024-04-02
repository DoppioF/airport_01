package airport_01Model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import airport_01Model.models.utilityModels.PaymentType;

public class ReservationDto implements Serializable {

	private static final long serialVersionUID = 7845247123822752743L;
	private Long id;
	private LocalDate date;
	private PaymentType paymentMethod;
	private Boolean validity;
	private Long idCustomer;
	private String customerName;
	private String customerSurname;
	private String customerTaxCode;
	private Long idFlight;
	private String nameDepartureAirport;
	private String cityDepartureAirport;
	private String nameArrivalAirport;
	private String cityArrivalAirport;
	private LocalDateTime departureDate;
	private LocalDateTime arrivalDate;
	private String checkingAccountNumber;
	private Float totalToPay;
	
	private List<TicketDto> ticketList;
	private Integer numberOfTickets;
	
	public ReservationDto() {
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

	public Boolean getValidity() {
		return validity;
	}

	public void setValidity(Boolean validity) {
		this.validity = validity;
	}

	public Long getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(Long idCustomer) {
		this.idCustomer = idCustomer;
	}

	public Long getIdFlight() {
		return idFlight;
	}

	public void setIdFlight(Long idFlight) {
		this.idFlight = idFlight;
	}

	public List<TicketDto> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<TicketDto> ticketList) {
		this.ticketList = ticketList;
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


	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(Integer numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public String getCustomerSurname() {
		return customerSurname;
	}

	public void setCustomerSurname(String customerSurname) {
		this.customerSurname = customerSurname;
	}

	public String getCustomerTaxCode() {
		return customerTaxCode;
	}

	public void setCustomerTaxCode(String customerTaxCode) {
		this.customerTaxCode = customerTaxCode;
	}

	@Override
	public String toString() {
		return "ReservationDto [id=" + id + ", date=" + date + ", paymentMethod=" + paymentMethod + ", validity="
				+ validity + ", idCustomer=" + idCustomer + ", customerName=" + customerName + ", customerSurname="
				+ customerSurname + ", customerTaxCode=" + customerTaxCode + ", idFlight=" + idFlight
				+ ", nameDepartureAirport=" + nameDepartureAirport + ", cityDepartureAirport=" + cityDepartureAirport
				+ ", nameArrivalAirport=" + nameArrivalAirport + ", cityArrivalAirport=" + cityArrivalAirport
				+ ", departureDate=" + departureDate + ", arrivalDate=" + arrivalDate + ", numberOfTickets=" + numberOfTickets
				+ ", ticketList=" + (null == ticketList ? 0 : ticketList.size()) + ", checkingAccountNumber=" + checkingAccountNumber 
				+", totalToPay=" + totalToPay + "]";
	}

	public String getCheckingAccountNumber() {
		return checkingAccountNumber;
	}

	public void setCheckingAccountNumber(String checkingAccountNumber) {
		this.checkingAccountNumber = checkingAccountNumber;
	}

	public Float getTotalToPay() {
		return totalToPay;
	}

	public void setTotalToPay(Float totalToPay) {
		this.totalToPay = totalToPay;
	}

	
	
}
