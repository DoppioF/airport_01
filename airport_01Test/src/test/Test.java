package test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.AirportDto;
import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.FlightDto;
import airport_01Model.dto.FlightRouteDto;
import airport_01Model.dto.ReservationDto;
import airport_01Model.models.entities.Airplane;
import airport_01Model.models.entities.Airport;
import airport_01Model.models.entities.Customer;
import airport_01Model.models.entities.Flight;
import airport_01Model.models.entities.FlightRoute;
import airport_01Model.models.entities.Reservation;
import customUtils.constants.db.DbConstants;
import customUtils.exceptions.SearcherException;
import customUtils.logicTools.DbTableStructure;
import customUtils.logicTools.QueryFromSearcher;
import customUtils.logicTools.QueryResultDto;
import customUtils.logicTools.Searcher;

public class Test {

	
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		Map<String, String> keywords = new HashMap<>() {
			{
				put("prenotazione"	, DbConstants.ReservationTable.TABLE_NAME);
				put("prenotazioni"	, DbConstants.ReservationTable.TABLE_NAME);
				put("cliente"		, DbConstants.CustomerTable.TABLE_NAME);
				put("clienti"		, DbConstants.CustomerTable.TABLE_NAME);
				put("volo"			, DbConstants.FlightTable.TABLE_NAME);
				put("voli"			, DbConstants.FlightTable.TABLE_NAME);
				put("tratta"		, DbConstants.FlightRouteTable.TABLE_NAME);
				put("tratte"		, DbConstants.FlightRouteTable.TABLE_NAME);
				put("biglietti"		, DbConstants.TicketTable.TABLE_NAME);
				put("aereo"			, DbConstants.AirplaneTable.TABLE_NAME);
				put("aerei"			, DbConstants.AirplaneTable.TABLE_NAME);
				put("aeroporto"		, DbConstants.AirportTable.TABLE_NAME);
				put("aeroporti"		, DbConstants.AirportTable.TABLE_NAME);
			}
		};
		Map<String, String> secondaryKeywords = new HashMap<>() {
			{
				put("model", DbConstants.AirplaneTable.COLUMN_MODEL);
				put("post", DbConstants.AirplaneTable.COLUMN_SEATS);
				put("sedu", DbConstants.AirplaneTable.COLUMN_SEATS);
				put("stiv", DbConstants.AirplaneTable.COLUMN_HOLD_CAPACITY);
				put("serbat", DbConstants.AirplaneTable.COLUMN_TANK_CAPACITY);
				put("nome", DbConstants.NAME);
				put("citt", DbConstants.AirportTable.COLUMN_CITY);
				put("chiam", DbConstants.CustomerTable.SURNAME);
				put("email", DbConstants.CustomerTable.EMAIL);
				put("e-mail", DbConstants.CustomerTable.EMAIL);
				put("ident", DbConstants.CustomerTable.COLUMN_IDENTITY_CARD_NUMBER);
				put("prezz", DbConstants.FlightTable.COLUMN_PRICE);
				put("cost", DbConstants.FlightTable.COLUMN_PRICE);
				put("part", DbConstants.FlightTable.COLUMN_DEPARTURE);
				put("arriv", DbConstants.FlightTable.COLUMN_ARRIVAL);
				put("distan", DbConstants.FlightRouteTable.COLUMN_DISTANCE);
				put("data", DbConstants.ReservationTable.COLUMN_DATE);
				put("pagamen", DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD);
			}
		};
		
		List<DbTableStructure> tables = new ArrayList<>() {
			{
				add(new DbTableStructure() {
					{
						setTableName(DbConstants.ReservationTable.TABLE_NAME);
						setEntityClass(Reservation.class);
						setDtoClass(ReservationDto.class);
						setColumns(new HashMap<>() {
							{
								put(DbConstants.ReservationTable.COLUMN_DATE, LocalDate.class);
								put(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD, Enum.class);
							}
						});
						setPkPrefix("");
						setPkName("id");
						setFkPrefixes(new String[] { "id_" });
						setForeignKeys(new ArrayList<>() {
							{
								add("flight");
								add("customer");
							}
						});
					}
				});
				add(new DbTableStructure() {
					{
						setTableName(DbConstants.CustomerTable.TABLE_NAME);
						setEntityClass(Customer.class);
						setDtoClass(CustomerDto.class);
						setColumns(new HashMap<>() {
							{
								put(DbConstants.CustomerTable.SURNAME, String.class);
								put(DbConstants.CustomerTable.EMAIL, String.class);
								put(DbConstants.CustomerTable.COLUMN_IDENTITY_CARD_NUMBER, String.class);
							}
						});
						setPkPrefix("");
						setPkName("id");
					}
				});
				add(new DbTableStructure() {
					{
						setTableName(DbConstants.AirportTable.TABLE_NAME);
						setEntityClass(Airport.class);
						setDtoClass(AirportDto.class);
						setColumns(new HashMap<>() {
							{
								put(DbConstants.NAME, String.class);
								put(DbConstants.AirportTable.COLUMN_CITY, String.class);
							}
						});
						setPkPrefix("");
						setPkName("id");
					}
				});
				add(new DbTableStructure() {
					{
						setTableName(DbConstants.AirplaneTable.TABLE_NAME);
						setEntityClass(Airplane.class);
						setDtoClass(AirplaneDto.class);
						setColumns(new HashMap<>() {
							{
								put(DbConstants.AirplaneTable.COLUMN_MODEL, String.class);
								put(DbConstants.AirplaneTable.COLUMN_SEATS, Integer.class);
								put(DbConstants.AirplaneTable.COLUMN_HOLD_CAPACITY, String.class);
								put(DbConstants.AirplaneTable.COLUMN_TANK_CAPACITY, String.class);
							}
						});
						setPkPrefix("");
						setPkName("id");
					}
				});
				add(new DbTableStructure() {
					{
						setTableName(DbConstants.FlightTable.TABLE_NAME);
						setEntityClass(Flight.class);
						setDtoClass(FlightDto.class);
						setColumns(new HashMap<>() {
							{
								put(DbConstants.FlightTable.COLUMN_PRICE, Float.class);
								put(DbConstants.FlightTable.COLUMN_DEPARTURE, String.class);
								put(DbConstants.FlightTable.COLUMN_ARRIVAL, String.class);
							}
						});
						setPkPrefix("");
						setPkName("id");
						setFkPrefixes(new String[] { "id_" });
						setForeignKeys(new ArrayList<>() {
							{
								add("flight_route");
								add("airplane");
							}
						});
					}
				});
				add(new DbTableStructure() {
					{
						setTableName(DbConstants.FlightRouteTable.TABLE_NAME);
						setEntityClass(FlightRoute.class);
						setDtoClass(FlightRouteDto.class);
						setColumns(new HashMap<>() {
							{
								put(DbConstants.FlightRouteTable.COLUMN_DISTANCE, Float.class);
							}
						});
						setPkPrefix("");
						setPkName("id");
						setFkPrefixes(new String[] { 
							"id_departure_"
							, "id_arrival_"
						});
						setForeignKeys(new ArrayList<>() {
							{
								add("airport");
							}
						});
					}
				});
			}
		};
		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd");
//		formatter = formatter.withLocale(Locale.ITALIAN);  
//		LocalDate date = LocalDate.parse("2005-dicembre-12", formatter);
//		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
//		String formattedDate = date.format(dateFormatter);
//		System.out.println(formattedDate);
//		System.out.println(LocalDate.now().getYear());
		
		String s = "11/1/1111";
		String formatted = s.replaceAll("/", "-");
		String[] splitted = formatted.split("-");
		String[] formattedArray = new String[splitted.length];
		for (int index = 0; index < splitted.length; index++) {
			String currentWord = splitted[splitted.length - 1 - index];
			formattedArray[index] = (1 == currentWord.length() ? "0" : "")
									+ currentWord;
		}
		String finalOutput = String.join("-", formattedArray);
		
		Class<?> classe = Integer.class;
		System.out.println(classe.getCanonicalName());
		System.out.println(classe.getName());
		System.out.println(classe.getSimpleName());
		System.out.println(classe.getTypeName());
	}
	
}