package airport_01Ejb.utils.validators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.dto.TicketDto;
import customUtils.classes.CustomDateUtils;
import customUtils.classes.CustomStringUtils;
import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RegexConstants;
import customUtils.exceptions.ValidatorException;

public class Validator {
	private final static String THIS_CLASS_NAME = "Class Validator -> ";
	
	private void validateReservationDate(LocalDate date, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (date != null) {
			if (CustomDateUtils.isFutureDate(date)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.ReservationTable.COLUMN_DATE + " " + GeneralConstants.INVALID_DATA));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.ReservationTable.COLUMN_DATE + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateDateTime(LocalDateTime dateTime, String dateName, List<String> errors, String methodName, LocalDateTime maxDate, LocalDateTime minDate, boolean isRequiredOnDb) {
		if (null != dateTime) {
			if (dateTime.isAfter(minDate) || dateTime.isBefore(maxDate)) {
				errors.add(stringErrorBuilder(methodName, dateName + " " + GeneralConstants.INVALID_DATA));
			}
		} else if (isRequiredOnDb) {
			errors.add(stringErrorBuilder(methodName, dateName + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateDepartureDate(LocalDateTime departureDateTime, List<String> errors, String methodName) {
		if (null == departureDateTime) {
			errors.add(stringErrorBuilder(methodName, DbConstants.FlightTable.COLUMN_DEPARTURE + GeneralConstants.NULL + ", arrival date not calculable"));
		} else if (departureDateTime.isBefore(LocalDateTime.now())) {
			errors.add(stringErrorBuilder(methodName, DbConstants.FlightTable.COLUMN_DEPARTURE + " " + GeneralConstants.INVALID_DATA));
		}
		/*
		validateDateTime(departureDateTime
						, DbConstants.FlightTable.COLUMN_DEPARTURE
						, errors
						, methodName
						, LocalDateTime.now()
							.plus(Period.ofYears(DbConstants.FlightTable.MAX_YEARS_FORWARD_RESERVABLE))
						, LocalDateTime.now()
						, DbConstants.FlightTable.DEPARTURE_DATE_IS_REQUIRED_ON_DB
						);
		*/
	}
	
	private void validateArrivalDate(LocalDateTime arrivalDateTime, LocalDateTime departureDateTime, List<String> errors, String methodName) {
		int errorSize = errors.size();
		if (null == departureDateTime || null == arrivalDateTime) {
			errors.add(stringErrorBuilder(methodName, DbConstants.FlightTable.COLUMN_DEPARTURE + GeneralConstants.NULL + ", arrival date not calculable"));
		} else {
			validateDepartureDate(departureDateTime, errors, methodName);
			if (errors.size() == errorSize && departureDateTime.isAfter(arrivalDateTime)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.FlightTable.COLUMN_ARRIVAL + " " + GeneralConstants.INVALID_DATA));
			}
		}
	}
	
	private void validateRequiredFK(Long id, String idName, List<String> errors, String methodName) {
		if (null == id) {
			errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + idName + " " + GeneralConstants.NULL));
		} else {
			if (null != id && id < 1) {
				errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + idName + " " + GeneralConstants.INVALID_DATA));
			}
		}
	}
	
	private void validateIdFK(Long id, String idName, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (isRequiredOnDB && null == id) {
			errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + idName + " " + GeneralConstants.NULL));
		} else {
			if (null != id && id < 1) {
				errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + idName + " " + GeneralConstants.INVALID_DATA));
			}
		}
	}
	
	private void validateSeats(Integer seats, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (null != seats) {
			if (seats < DbConstants.AirplaneTable.MIN_SEATS
				|| seats > DbConstants.AirplaneTable.MAX_SEATS) {
				errors.add(stringErrorBuilder(methodName, DbConstants.AirplaneTable.COLUMN_SEATS + " " + GeneralConstants.INVALID_DATA));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.AirplaneTable.COLUMN_SEATS + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateHoldCapacity(Float holdCapacity, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (null != holdCapacity) {
			if (holdCapacity < DbConstants.AirplaneTable.MIN_HOLD_CAPACITY
				|| holdCapacity > DbConstants.AirplaneTable.MAX_HOLD_CAPACITY) {
				errors.add(stringErrorBuilder(methodName, DbConstants.AirplaneTable.COLUMN_HOLD_CAPACITY + " " + GeneralConstants.INVALID_DATA));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.AirplaneTable.COLUMN_HOLD_CAPACITY + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateTankCapacity(Float tankCapacity, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (null != tankCapacity) {
			if (tankCapacity < DbConstants.AirplaneTable.MIN_TANK_CAPACITY
				|| tankCapacity > DbConstants.AirplaneTable.MAX_TANK_CAPACITY) {
				errors.add(stringErrorBuilder(methodName, DbConstants.AirplaneTable.COLUMN_TANK_CAPACITY + " " + GeneralConstants.INVALID_DATA));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.AirplaneTable.COLUMN_TANK_CAPACITY + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateEmail(String email, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(email)) {
			if (email.length() > DbConstants.CustomerTable.EMAIL_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.EMAIL + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!email.matches(RegexConstants.EMAIL_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.EMAIL + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB){
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.EMAIL + " " + GeneralConstants.NULL));
		}
	}
	
	private void validatePassword(String password, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(password)) {
			if (password.length() > DbConstants.CustomerTable.PASSWORD_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.PASSWORD + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!password.matches(RegexConstants.GENERIC_ANTI_QUERY_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.PASSWORD + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB){
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.PASSWORD + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateName(String name, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(name)) {
			if (name.length() > DbConstants.CustomerTable.NAME_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.NAME + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!name.matches(RegexConstants.NAMES_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.NAME + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB){
			errors.add(stringErrorBuilder(methodName, DbConstants.NAME + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateSurname(String surname, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(surname)) {
			if (surname.length() > DbConstants.CustomerTable.NAME_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.SURNAME + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!surname.matches(RegexConstants.NAMES_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.SURNAME + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB){
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.SURNAME + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateCity(String city, List<String> errors, String methodName, String attributeName, int maxLength, String regex, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(city)) {
			if (city.length() > maxLength) {
				errors.add(stringErrorBuilder(methodName, attributeName + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!city.matches(regex)) {
				errors.add(stringErrorBuilder(methodName, attributeName + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, attributeName + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateModel(String airplaneModel, List<String> errors, String methodName, String attributeName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(airplaneModel)) {
			if (airplaneModel.length() > DbConstants.AirplaneTable.MODEL_LENGTH) {
				errors.add(stringErrorBuilder(methodName, attributeName + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!airplaneModel.matches(RegexConstants.GENERIC_ANTI_QUERY_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, attributeName + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, attributeName + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateAddress(String address, List<String> errors, String methodName, boolean isRequiredOnDB) {
		validateGenericLogisticName(address, errors, methodName, DbConstants.CustomerTable.RESIDENTIAL_ADDRESS, isRequiredOnDB);
	}
	
	private void validateAirportName(String airportName, List<String> errors, String methodName, boolean isRequiredOnDB) {
		validateGenericLogisticName(airportName, errors, methodName, DbConstants.NAME, isRequiredOnDB);
	}
	
	private void validateGenericLogisticName(String logisticName, List<String> errors, String methodName, String fieldName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(logisticName)) {
			if (logisticName.length() > DbConstants.CustomerTable.RESIDENTIAL_ADDRESS_LENGTH) {
				errors.add(stringErrorBuilder(methodName, fieldName + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!logisticName.matches(RegexConstants.ADDRESS_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, fieldName + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB){
			errors.add(stringErrorBuilder(methodName, fieldName + " " + GeneralConstants.NULL));
		}
	}
	
	private void validatePostcode(String postcode, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(postcode)) {
			if (postcode.length() > DbConstants.CustomerTable.RESIDENCE_POSTCODE_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.RESIDENCE_POSTCODE + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!postcode.matches(RegexConstants.GENERIC_ANTI_QUERY_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.RESIDENCE_POSTCODE + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB){
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.RESIDENCE_POSTCODE + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateTaxCode(String taxCode, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(taxCode)) {
			if (taxCode.length() > DbConstants.CustomerTable.TAX_CODE_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.TAX_CODE + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!taxCode.matches(RegexConstants.TAX_CODE_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.TAX_CODE + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.TAX_CODE + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateIdentityCardNumber(String identityCardNumber, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(identityCardNumber)) {
			if (identityCardNumber.length() > DbConstants.CustomerTable.IDENTITY_CARD_NUMBER_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.IDENTITY_CARD_NUMBER + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!identityCardNumber.matches(RegexConstants.IDENTITY_CARD_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.IDENTITY_CARD_NUMBER + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.IDENTITY_CARD_NUMBER + " " + GeneralConstants.NULL));
		}
	}
	
	private void validatePhoneNumber(String phoneNumber, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (!CustomStringUtils.isStringNullOrEmpty(phoneNumber)) {
			if (phoneNumber.length() > DbConstants.CustomerTable.PHONE_NUMBER_LENGTH) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.PHONE_NUMBER + " " + GeneralConstants.TOO_LONG_STRING));
			}
			if (!phoneNumber.matches(RegexConstants.PHONE_NUMBER_PATTERN)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.PHONE_NUMBER + " " + GeneralConstants.INVALID_FORMAT));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.PHONE_NUMBER + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateBirthDate(LocalDate birthDate, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (null != birthDate) {
			if (!CustomDateUtils.isAdult(birthDate, GeneralConstants.ADULT_AGE)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.BIRTH_DATE + " " + GeneralConstants.NOT_ADULT));
			}
			if (CustomDateUtils.isFutureDate(birthDate)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.BIRTH_CITY + " " + GeneralConstants.FUTURE_DATE));
			}
			if (CustomDateUtils.isTooOldAge(birthDate, GeneralConstants.MAX_AGE)) {
				errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.BIRTH_CITY + " " + GeneralConstants.OLD_DATE));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.CustomerTable.BIRTH_CITY + " " + GeneralConstants.NULL));
		}
	}
	
	private void validateDistanceKm(Float distanceKm, List<String> errors, String methodName, boolean isRequiredOnDB) {
		if (null != distanceKm) {
			if (distanceKm < DbConstants.FlightRouteTable.MIN_DISTANCE_BETWEEN_AIRPORTS
				|| distanceKm > DbConstants.FlightRouteTable.MAX_DISTANCE_BETWEEN_AIRPORTS) {
					errors.add(stringErrorBuilder(methodName, DbConstants.FlightRouteTable.COLUMN_DISTANCE + " " + GeneralConstants.NOT_ADULT));
			}
		} else if (isRequiredOnDB) {
			errors.add(stringErrorBuilder(methodName, DbConstants.FlightRouteTable.COLUMN_DISTANCE + " " + GeneralConstants.NULL));
		}
	}
	
	public List<String> customerEmailAndPassword(CustomerDto customerDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method customerEmailAndPassword";
		try {
			if (null == customerDto) {
				throw new ValidatorException(stringErrorBuilder(THIS_METHOD_NAME, "customerDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				validateEmail(customerDto.getEmail(), errors, THIS_METHOD_NAME, DbConstants.CustomerTable.EMAIL_IS_REQUIRED_ON_DB);
				validatePassword(customerDto.getPassword(), errors, THIS_METHOD_NAME, DbConstants.CustomerTable.PASSWORD_IS_REQUIRED_ON_DB);
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	public List<String> customerInsert(CustomerDto customerDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method customerInsert";
		return customerInsertUpdate(customerDto, GeneralConstants.INSERT, THIS_METHOD_NAME);
	}
	
	public List<String> customerUpdate(CustomerDto customerDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method customerUpdate";
		return customerInsertUpdate(customerDto, GeneralConstants.UPDATE, THIS_METHOD_NAME);
	}
	
	public List<String> customerInsertUpdate(CustomerDto customerDto, String operation, String methodName ) throws ValidatorException, Exception {
		try {
			if (null == customerDto) {
				throw new ValidatorException(stringErrorBuilder(methodName, "customerDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				
				if (null != customerDto.getId() && GeneralConstants.INSERT.equals(operation) ) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.IS_NOT_NULL));
				} else if (null == customerDto.getId() && GeneralConstants.UPDATE.equals(operation)) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.NULL));
				}
				validateName(customerDto.getName(), errors, methodName, DbConstants.CustomerTable.NAME_IS_REQUIRED_ON_DB);
				validateSurname(customerDto.getSurname(), errors, methodName, DbConstants.CustomerTable.SURNAME_IS_REQUIRED_ON_DB);
				if(GeneralConstants.INSERT.equals(operation)) {
					validateEmail(customerDto.getEmail(), errors, methodName, DbConstants.CustomerTable.EMAIL_IS_REQUIRED_ON_DB);
				}
				if(GeneralConstants.INSERT.equals(operation)) {
					validatePassword(customerDto.getPassword(), errors, methodName, DbConstants.CustomerTable.PASSWORD_IS_REQUIRED_ON_DB);
				}
				validatePhoneNumber(customerDto.getPhoneNumber(), errors, methodName, DbConstants.CustomerTable.PHONE_NUMBER_IS_REQUIRED_ON_DB);
				if(GeneralConstants.INSERT.equals(operation)) {
					validateBirthDate(customerDto.getBirthDate(), errors, methodName, DbConstants.CustomerTable.BIRTH_DATE_IS_REQUIRED_ON_DB);
				}
				if(GeneralConstants.INSERT.equals(operation)) {
					validateCity(customerDto.getBirthCity(), errors, methodName, 
						DbConstants.CustomerTable.BIRTH_CITY, DbConstants.CustomerTable.BIRTH_CITY_LENGTH, 
						RegexConstants.GENERIC_ANTI_QUERY_PATTERN, DbConstants.CustomerTable.BIRTH_CITY_IS_REQUIRED_ON_DB);
				}
				validateAddress(customerDto.getResidentialAddress(), errors, methodName, DbConstants.CustomerTable.RESIDENTIAL_ADDRESS_IS_REQUIRED_ON_DB);
				validateCity(customerDto.getResidenceCity(), errors, methodName, 
						DbConstants.CustomerTable.RESIDENCE_CITY, DbConstants.CustomerTable.RESIDENCE_CITY_LENGTH, 
						RegexConstants.GENERIC_ANTI_QUERY_PATTERN, DbConstants.CustomerTable.RESIDENCE_CITY_IS_REQUIRED_ON_DB);
				validateCity(customerDto.getResidenceProvince(), errors, methodName, 
						DbConstants.CustomerTable.RESIDENCE_PROVINCE, DbConstants.CustomerTable.RESIDENCE_PROVINCE_LENGTH, 
						RegexConstants.GENERIC_ANTI_QUERY_PATTERN, DbConstants.CustomerTable.RESIDENCE_PROVINCE_IS_REQUIRED_ON_DB);
				validateTaxCode(customerDto.getTaxCode(), errors, methodName, DbConstants.CustomerTable.TAX_CODE_IS_REQUIRED_ON_DB);
				validatePostcode(customerDto.getResidencePostcode(), errors, methodName, DbConstants.CustomerTable.RESIDENCE_POSTCODE_IS_REQUIRED_ON_DB);
				validateIdentityCardNumber(customerDto.getIdentityCardNumber(), errors, methodName, DbConstants.CustomerTable.IDENTITY_CARD_NUMBER_IS_REQUIRED_ON_DB);
				validateIdFK(customerDto.getIdRole(), "Role", errors, methodName, DbConstants.CustomerTable.ROLE_IS_REQUIRED_ON_DB);
				
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	public List<String> reservationInsert(ReservationDto reservationDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method reservationInsert";
		try {
			if (null == reservationDto) {
				throw new ValidatorException(stringErrorBuilder(THIS_METHOD_NAME, "reservationDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				if (null != reservationDto.getId()) {
					errors.add(stringErrorBuilder(THIS_METHOD_NAME, DbConstants.COLUMN_PK + " " + GeneralConstants.IS_NOT_NULL));
				}
				validateRequiredFK(reservationDto.getIdCustomer(), DbConstants.CustomerTable.TABLE_NAME, errors, THIS_METHOD_NAME);
				validateRequiredFK(reservationDto.getIdFlight(), DbConstants.FlightTable.TABLE_NAME, errors, THIS_METHOD_NAME);
				if (null == reservationDto.getPaymentMethod()) {
					errors.add(stringErrorBuilder(THIS_METHOD_NAME, "paymentMethod " + GeneralConstants.NULL));
				}
				
				List<TicketDto> ticketDtoList = reservationDto.getTicketList();
				if (null == ticketDtoList || ticketDtoList.isEmpty()) {
					errors.add(stringErrorBuilder(THIS_METHOD_NAME, "ticketList " + GeneralConstants.MISSING_DATA));
				} else {
					for (TicketDto ticketDto : ticketDtoList) {
						validateName(ticketDto.getHolderName(), errors, THIS_METHOD_NAME, DbConstants.TicketTable.HOLDER_NAME_IS_REQUIRED_ON_DB);
						validateName(ticketDto.getHolderSurname(), errors, THIS_METHOD_NAME, DbConstants.TicketTable.HOLDER_SURNAME_IS_REQUIRED_ON_DB);
					}
				}
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	private String stringErrorBuilder(String beforeArrow, String afterArrow) {
		return (beforeArrow + " -> " + afterArrow);
	}
	
	public List<String> flightInsertUpdate(FlightDto flightDto, String operation, String methodName) throws ValidatorException, Exception {
		try {
			if (null == flightDto) {
				throw new ValidatorException(stringErrorBuilder(methodName, "flightDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				if (null != flightDto.getId() && GeneralConstants.INSERT.equals(operation) ) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.IS_NOT_NULL));
				} else if (null == flightDto.getId() && GeneralConstants.UPDATE.equals(operation)) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.NULL));
				}
				validateRequiredFK(flightDto.getIdFlightRoute(), DbConstants.FlightRouteTable.COLUMN_FK, errors, methodName);
				validateDepartureDate(flightDto.getDepartureDate(), errors, methodName);
				validateArrivalDate(flightDto.getArrivalDate(), flightDto.getDepartureDate(), errors, methodName);
				validateIdFK(flightDto.getIdAirplane(), DbConstants.AirplaneTable.TABLE_NAME, errors, methodName, DbConstants.FlightTable.AIRPLANE_IS_REQUIRED_ON_DB);
				
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	public List<String> flightInsert(FlightDto flightDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method flightInsert";
		return flightInsertUpdate(flightDto, GeneralConstants.INSERT, THIS_METHOD_NAME);
	}
	
	public List<String> flightUpdate(FlightDto flightDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method flightUpdate";
		return flightInsertUpdate(flightDto, GeneralConstants.UPDATE, THIS_METHOD_NAME);
	}

	public List<String> flightRouteInsert(FlightRouteDto flightRouteDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method flightRouteInsert";
		try {
			if (null == flightRouteDto) {
				throw new ValidatorException(stringErrorBuilder(THIS_METHOD_NAME, "flightRouteDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				if (null != flightRouteDto.getId()) {
					errors.add(stringErrorBuilder(THIS_METHOD_NAME, DbConstants.COLUMN_PK + " " + GeneralConstants.IS_NOT_NULL));
				}
				validateRequiredFK(flightRouteDto.getIdDepartureAirport(), DbConstants.AirportTable.MAP_DEPARTURE, errors, THIS_METHOD_NAME);
				validateRequiredFK(flightRouteDto.getIdArrivalAirport(), DbConstants.AirportTable.MAP_ARRIVAL, errors, THIS_METHOD_NAME);
				validateDistanceKm(flightRouteDto.getDistanceKm(), errors, THIS_METHOD_NAME, DbConstants.FlightRouteTable.DISTANCE_KM_IS_REQUIRED_ON_DB);
				
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	private List<String> airportInsertUpdate(AirportDto airportDto, String operation, String methodName) throws ValidatorException, Exception {
		try {
			if (null == airportDto) {
				throw new ValidatorException(stringErrorBuilder(methodName, "airportDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				if (null != airportDto.getId() && GeneralConstants.INSERT.contentEquals(operation)) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.IS_NOT_NULL));
				} else if (null == airportDto.getId() && GeneralConstants.UPDATE.equals(operation)) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.NULL));
				}
				validateAirportName(airportDto.getAirportName(), errors, methodName, DbConstants.AirportTable.AIRPORT_NAME_IS_REQUIRED_ON_DB);
				validateCity(airportDto.getAirportCity(), errors, methodName, 
						DbConstants.AirportTable.COLUMN_CITY, DbConstants.AirportTable.CITY_LENGTH, 
						RegexConstants.GENERIC_ANTI_QUERY_PATTERN, DbConstants.AirportTable.AIRPORT_CITY_IS_REQUIRED_ON_DB);
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	public List<String> airportInsert(AirportDto airportDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method airportInsert";
		return airportInsertUpdate(airportDto, GeneralConstants.INSERT, THIS_METHOD_NAME);
	}
	
	public List<String> airportUpdate(AirportDto airportDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method airportUpdate";
		return airportInsertUpdate(airportDto, GeneralConstants.UPDATE, THIS_METHOD_NAME);
	}
	
	private List<String> airplaneInsertUpdate(AirplaneDto airplaneDto, String operation, String methodName) throws ValidatorException, Exception {
		try {
			if (null == airplaneDto) {
				throw new ValidatorException(stringErrorBuilder(methodName, "airplaneDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				if (null != airplaneDto.getId() && GeneralConstants.INSERT.contentEquals(operation)) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.IS_NOT_NULL));
				} else if (null == airplaneDto.getId() && GeneralConstants.UPDATE.equals(operation)) {
					errors.add(stringErrorBuilder(methodName, DbConstants.COLUMN_PK + " " + GeneralConstants.NULL));
				}
				validateModel(airplaneDto.getModel(), errors, methodName, DbConstants.AirplaneTable.COLUMN_MODEL, DbConstants.AirplaneTable.MODEL_IS_REQUIRED_ON_DB);
				validateSeats(airplaneDto.getSeats(), errors, methodName, DbConstants.AirplaneTable.SEATS_IS_REQUIRED_ON_DB);
				validateHoldCapacity(airplaneDto.getHoldCapacity(), errors, methodName, DbConstants.AirplaneTable.HOLD_CAPACITY_IS_REQUIRED_ON_DB);
				validateTankCapacity(airplaneDto.getTankCapacity(), errors, methodName, DbConstants.AirplaneTable.TANK_CAPACITY_IS_REQUIRED_ON_DB);
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	public List<String> airplaneInsert(AirplaneDto airplaneDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method airplaneInsert";
		return airplaneInsertUpdate(airplaneDto, GeneralConstants.INSERT, THIS_METHOD_NAME);
	}
	
	public List<String> airplaneUpdate(AirplaneDto airplaneDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method airplaneUpdate";
		return airplaneInsertUpdate(airplaneDto, GeneralConstants.UPDATE, THIS_METHOD_NAME);
	}
		
	public List<String> reservationUpdate(ReservationDto reservationDto) throws ValidatorException, Exception {
		final String THIS_METHOD_NAME = THIS_CLASS_NAME + "Method reservationUpdate";
		try {
			if (null == reservationDto) {
				throw new ValidatorException(stringErrorBuilder(THIS_METHOD_NAME, "reservationDto " + GeneralConstants.NULL));
			} else {
				List<String> errors = new ArrayList<>();
				if (null == reservationDto.getId()) {
					errors.add(stringErrorBuilder(THIS_METHOD_NAME, DbConstants.COLUMN_PK + " " + GeneralConstants.NULL));
				}
				validateIdFK(reservationDto.getIdCustomer(), DbConstants.CustomerTable.TABLE_NAME, errors, THIS_METHOD_NAME, DbConstants.ReservationTable.CUSTOMER_IS_REQUIRED_ON_DB);
				validateRequiredFK(reservationDto.getIdFlight(), DbConstants.FlightTable.TABLE_NAME, errors, THIS_METHOD_NAME);
				validateReservationDate(reservationDto.getDate(), errors, THIS_METHOD_NAME, DbConstants.ReservationTable.DATE_IS_REQUIRED_ON_DB);	
				if (null == reservationDto.getPaymentMethod()) {
					errors.add(stringErrorBuilder(THIS_METHOD_NAME, "paymentMethod " + GeneralConstants.NULL));
				}
				if (null == reservationDto.getValidity()) {
					errors.add(stringErrorBuilder(THIS_METHOD_NAME, "validity " + GeneralConstants.NULL));
				}
				return errors;
			}
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage()); 
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
}
