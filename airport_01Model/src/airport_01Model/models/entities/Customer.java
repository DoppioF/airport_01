package airport_01Model.models.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import customUtils.constants.db.DbConstants;

@Entity
@Table(name = DbConstants.CustomerTable.TABLE_NAME)
public class Customer implements Serializable {

	private static final long serialVersionUID = 6207430893434319343L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = DbConstants.COLUMN_PK)
	private Long id;
	
	@Column(name = DbConstants.NAME)
	private String name;
	
	@Column(name = DbConstants.CustomerTable.SURNAME)
	private String surname;
	
	@Column(name = DbConstants.CustomerTable.EMAIL)
	private String email;
	
	@Column(name = DbConstants.CustomerTable.PASSWORD)
	private String password;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_PHONE_NUMBER)
	private String phoneNumber;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_BIRTH_DATE)
	private LocalDate birthDate;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_BIRTH_CITY)
	private String birthCity;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_RESIDENTIAL_ADDRESS)
	private String residentialAddress;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_RESIDENCE_CITY)
	private String residenceCity;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_RESIDENCE_PROVINCE)
	private String residenceProvince;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_RESIDENCE_POSTCODE)
	private String residencePostcode;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_TAX_CODE)
	private String taxCode;
	
	@Column(name = DbConstants.CustomerTable.COLUMN_IDENTITY_CARD_NUMBER)
	private String identityCardNumber;
	
	@ManyToOne
	@JoinColumn(name = DbConstants.RoleTable.COLUMN_FK)
	private Role role;
	
	@OneToMany(mappedBy = DbConstants.CustomerTable.TABLE_NAME)
	@JsonbTransient
	@OrderBy("date DESC, id DESC")
	private List<Reservation> reservationList;

	public Customer() {
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

	public String getIdentityCardNumber() {
		return identityCardNumber;
	}

	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}
	
	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Reservation> getReservationList() {
		return reservationList;
	}

	public void setReservationList(List<Reservation> reservationList) {
		this.reservationList = reservationList;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", password="
				+ password + ", phoneNumber=" + phoneNumber + ", birthDate=" + birthDate
				+ ", birthCity=" + birthCity + ", residentialAddress=" + residentialAddress + ", residenceCity="
				+ residenceCity + ", residenceProvince=" + residenceProvince + ", residencePostcode="
				+ residencePostcode + ", taxCode=" + taxCode + ", identityCardNumber=" + identityCardNumber + ", role="
				+ role + ", reservationListSize=" + (reservationList == null ? 0 : reservationList.size()) + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return
			null != obj
			&& obj instanceof Customer
			&& ((Customer) obj).getId() == this.id;
	}
}
