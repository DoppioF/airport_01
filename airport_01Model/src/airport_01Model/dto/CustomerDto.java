package airport_01Model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class CustomerDto implements Serializable {

	private static final long serialVersionUID = 7618096602167304404L;
	private Long id;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String phoneNumber;
	private LocalDate birthDate;
	private String birthCity;
	private String residentialAddress;
	private String residenceCity;
	private String residenceProvince;
	private String residencePostcode;
	private String taxCode;
	private String identityCardNumber;
	private Long idRole;
	private List<ReservationDto> reservationList;
	
	
	
	
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
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public String getBirthCity() {
		return birthCity;
	}
	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}
	public String getResidentialAddress() {
		return residentialAddress;
	}
	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}
	public String getResidenceCity() {
		return residenceCity;
	}
	public void setResidenceCity(String residenceCity) {
		this.residenceCity = residenceCity;
	}
	public String getResidenceProvince() {
		return residenceProvince;
	}
	public void setResidenceProvince(String residenceProvince) {
		this.residenceProvince = residenceProvince;
	}
	public String getResidencePostcode() {
		return residencePostcode;
	}
	public void setResidencePostcode(String residencePostcode) {
		this.residencePostcode = residencePostcode;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public String getIdentityCardNumber() {
		return identityCardNumber;
	}
	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}
	public Long getIdRole() {
		return idRole;
	}
	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}
	public List<ReservationDto> getReservationList() {
		return reservationList;
	}
	public void setReservationList(List<ReservationDto> reservationList) {
		this.reservationList = reservationList;
	}
	@Override
	public String toString() {
		return "CustomerDto [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", password="
				+ password + ", phoneNumber=" + phoneNumber + ", birthDate=" + birthDate + ", birthCity=" + birthCity
				+ ", residentialAddress=" + residentialAddress + ", residenceCity=" + residenceCity
				+ ", residenceProvince=" + residenceProvince + ", residencePostcode=" + residencePostcode + ", taxCode="
				+ taxCode + ", identityCardNumber=" + identityCardNumber + ", idRole=" + idRole + ", reservationList="
				+ (null == reservationList ? 0 : reservationList.size())+ "]";
	}
	
	
	

}
