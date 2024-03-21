package customUtils.constants.db;

import java.time.temporal.ChronoUnit;

public class DbConstants {
	public final static String NAME 			= "name";
	public final static String COLUMN_PK 		= "id";
	public final static String COLUMN_VALIDITY 	= "validity";
	
	public final static int AIRPLANE_TABLE_CODE 	= 1;
	public final static int AIRPORT_TABLE_CODE 		= 2;
	public final static int FLIGHT_ROUTE_TABLE_CODE = 3;
	public final static int FLIGHT_TABLE_CODE 		= 4;
	public final static int CUSTOMER_TABLE_CODE 	= 5;
	public final static int RESERVATION_TABLE_CODE 	= 6;
	public final static int TICKET_TABLE_CODE 		= 7;

	public static class AirplaneTable {
		public final static Integer MODEL_LENGTH 	= 20;
		public final static Integer MIN_SEATS 		= 50;
		public final static Integer MAX_SEATS 		= 500;
		
		public final static Float MIN_HOLD_CAPACITY = 1000F;
		public final static Float MAX_HOLD_CAPACITY = 50000F;
		public final static Float MIN_TANK_CAPACITY = 1000F;
		public final static Float MAX_TANK_CAPACITY = 50000F;
		
		public final static String TABLE_NAME 			= "airplane";
		public final static String COLUMN_FK 			= "id_airplane";
		public final static String COLUMN_MODEL 		= "model";
		public final static String COLUMN_SEATS 		= "seats";
		public final static String COLUMN_HOLD_CAPACITY = "hold_capacity";
		public final static String COLUMN_TANK_CAPACITY = "tank_capacity";
		public final static String MODEL_NAME			= "Airplane";
		
		public final static boolean MODEL_IS_REQUIRED_ON_DB 		= true;
		public final static boolean SEATS_IS_REQUIRED_ON_DB 		= true;
		public final static boolean HOLD_CAPACITY_IS_REQUIRED_ON_DB = true;
		public final static boolean TANK_CAPACITY_IS_REQUIRED_ON_DB = true;
	}
	
	public static class AirportTable {
		public final static int NAME_LENGTH 		= 30;
		public final static int CITY_LENGTH 		= 30;
		public final static String TABLE_NAME 		= "airport";
		public final static String COLUMN_CITY 		= "city";
		public final static String MAP_DEPARTURE 	= "departureAirport";
		public final static String MAP_ARRIVAL 		= "arrivalAirport";
		public final static String MODEL_NAME		= "Airport";
		
		public final static boolean AIRPORT_NAME_IS_REQUIRED_ON_DB = true;
		public final static boolean AIRPORT_CITY_IS_REQUIRED_ON_DB = true;
	}
	
	public static class ReservationTable {
		public final static int PAYMENT_METHOD_LENGTH 		= 20;
		
		public final static String TABLE_NAME 				= "reservation";
		public final static String COLUMN_FK 				= "id_reservation";
		public final static String COLUMN_DATE 				= "date";
		public final static String COLUMN_PAYMENT_METHOD 	= "payment_method";
		
		public final static boolean DATE_IS_REQUIRED_ON_DB		= true;
		public final static boolean CUSTOMER_IS_REQUIRED_ON_DB	= false;
		public final static boolean VALIDITY_IS_REQUIRED_ON_DB	= true;
	}
	
	public static class TicketTable {
		public final static int HOLDER_NAME_LENGTH 			= 30;
		public final static int HOLDER_SURNAME_LENGTH 		= 30;
		
		public final static String TABLE_NAME 				= "ticket";
		public final static String COLUMN_HOLDER_NAME 		= "holder_name";
		public final static String COLUMN_HOLDER_SURNAME 	= "holder_surname";
		
		public final static boolean HOLDER_NAME_IS_REQUIRED_ON_DB 		= true;
		public final static boolean HOLDER_SURNAME_IS_REQUIRED_ON_DB 	= true;
	}
	
	public static class CustomerTable {
		public final static int NAME_LENGTH 				= 30;
		public final static int SURNAME_LENGTH 				= 30;
		public final static int EMAIL_LENGTH 				= 30;
		public final static int PASSWORD_LENGTH 			= 20;
		public final static int PHONE_NUMBER_LENGTH 		= 20;
		public final static int BIRTH_CITY_LENGTH 			= 30;
		public final static int RESIDENTIAL_ADDRESS_LENGTH 	= 50;
		public final static int RESIDENCE_CITY_LENGTH 		= 30;
		public final static int RESIDENCE_PROVINCE_LENGTH 	= 30;
		public final static int RESIDENCE_POSTCODE_LENGTH 	= 10;
		public final static int TAX_CODE_LENGTH 			= 20;
		public final static int IDENTITY_CARD_NUMBER_LENGTH = 20;
		
		public final static String EMAIL 				= "email";
		public final static String SURNAME 				= "surname";
		public final static String PASSWORD 			= "password";
		public final static String PHONE_NUMBER 		= "phoneNumber";
		public final static String BIRTH_CITY 			= "birthCity";
		public final static String BIRTH_DATE 			= "birthDate";
		public final static String RESIDENTIAL_ADDRESS 	= "residentialAddress";
		public final static String RESIDENCE_CITY 		= "residenceCity";
		public final static String RESIDENCE_PROVINCE 	= "residenceProvince";
		public final static String RESIDENCE_POSTCODE 	= "residencePostcode";
		public final static String TAX_CODE 			= "taxCode";
		public final static String IDENTITY_CARD_NUMBER = "identityCardNumber";
		public final static String ROLE 				= "role";
		
		public final static String TABLE_NAME 					= "customer";
		public final static String COLUMN_FK 					= "id_customer";
		public final static String COLUMN_PHONE_NUMBER 			= "phone_number";
		public final static String COLUMN_BIRTH_DATE 			= "birth_date";
		public final static String COLUMN_BIRTH_CITY 			= "birth_city";
		public final static String COLUMN_RESIDENTIAL_ADDRESS 	= "residential_address";
		public final static String COLUMN_RESIDENCE_CITY 		= "residence_city";
		public final static String COLUMN_RESIDENCE_PROVINCE	= "residence_province";
		public final static String COLUMN_RESIDENCE_POSTCODE 	= "residence_postcode";
		public final static String COLUMN_TAX_CODE 				= "tax_code";
		public final static String COLUMN_IDENTITY_CARD_NUMBER 	= "identity_card_number";
		
		public final static boolean EMAIL_IS_REQUIRED_ON_DB 				= true;
		public final static boolean SURNAME_IS_REQUIRED_ON_DB 				= true;
		public final static boolean NAME_IS_REQUIRED_ON_DB 					= true;
		public final static boolean PASSWORD_IS_REQUIRED_ON_DB 				= true;
		public final static boolean PHONE_NUMBER_IS_REQUIRED_ON_DB			= false;
		public final static boolean BIRTH_CITY_IS_REQUIRED_ON_DB 			= true;
		public final static boolean BIRTH_DATE_IS_REQUIRED_ON_DB 			= true;
		public final static boolean RESIDENTIAL_ADDRESS_IS_REQUIRED_ON_DB 	= true;
		public final static boolean RESIDENCE_CITY_IS_REQUIRED_ON_DB 		= true;
		public final static boolean RESIDENCE_PROVINCE_IS_REQUIRED_ON_DB 	= false;
		public final static boolean RESIDENCE_POSTCODE_IS_REQUIRED_ON_DB 	= true;
		public final static boolean TAX_CODE_IS_REQUIRED_ON_DB 				= true;
		public final static boolean IDENTITY_CARD_NUMBER_IS_REQUIRED_ON_DB 	= true;
		public final static boolean ROLE_IS_REQUIRED_ON_DB 					= true;
	}
	
	public static class RoleTable {
		public final static Long ID_ADMIN 		= 1L;
		public final static Long ID_USER		= 2L;
		public final static String TABLE_NAME 	= "role";
		public final static String COLUMN_FK 	= "id_role";
	}
	
	public static class FlightTable {
		public final static String TABLE_NAME 		= "flight";
		public final static String COLUMN_FK 		= "id_flight";
		public final static String COLUMN_DEPARTURE = "departure_date";
		public final static String COLUMN_ARRIVAL 	= "arrival_date";
		public final static String COLUMN_PRICE 	= "price";
		
		public final static boolean FLIGHT_ROUTE_IS_REQUIRED_ON_DB 		= true;
		public final static boolean DEPARTURE_DATE_IS_REQUIRED_ON_DB 	= true;
		public final static boolean ARRIVAL_DATE_IS_REQUIRED_ON_DB 		= false;
		public final static boolean AIRPLANE_IS_REQUIRED_ON_DB 			= false;
		
		public final static Integer MAX_YEARS_FORWARD_RESERVABLE 	= 1;
		public final static Integer MIN_FLIGHT_DURATION_UNIT 		= 1;
		public final static Integer MAX_FLIGHT_DURATION_UNIT 		= 16;
		
		public final static ChronoUnit MIN_FLIGHT_DURATION_UNIT_TYPE = ChronoUnit.HOURS;
		public final static ChronoUnit MAX_FLIGHT_DURATION_UNIT_TYPE = ChronoUnit.HOURS;
	}
	public static class FlightRouteTable {
		public final static String TABLE_NAME 		= "flight_route";
		public final static String COLUMN_FK 		= "id_flight_route";
		public final static String COLUMN_DISTANCE 	= "distance_km";
		public final static String COLUMN_DEPARTURE = "id_departure_airport";
		public final static String COLUMN_ARRIVAL 	= "id_arrival_airport";
		public final static String MAP_DISTANCE 	= "distanceKm";
		
		public final static boolean DISTANCE_KM_IS_REQUIRED_ON_DB 		= true;
		
		public final static Float MIN_DISTANCE_BETWEEN_AIRPORTS = 100F;
		public final static Float MAX_DISTANCE_BETWEEN_AIRPORTS = 10000F;
	}
}
