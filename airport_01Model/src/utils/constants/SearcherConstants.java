package utils.constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import searcher.dto.DbTableStructure;
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

@SuppressWarnings("serial")
public class SearcherConstants {
	
	public final static Map<String, String> PRIMARY_KEYWORDS = new HashMap<>() {
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

	public final static Map<String, String> SECONDARY_KEYWORDS = new HashMap<>() {
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
	
	public final static List<DbTableStructure> TABLE_STRUCTURE_LIST = new ArrayList<>() {
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
							put(DbConstants.NAME, String.class);
							put(DbConstants.CustomerTable.SURNAME, String.class);
							put(DbConstants.CustomerTable.EMAIL, String.class);
							put(DbConstants.CustomerTable.COLUMN_IDENTITY_CARD_NUMBER, String.class);
							put(DbConstants.CustomerTable.COLUMN_TAX_CODE, String.class);
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
							put(DbConstants.AirplaneTable.COLUMN_HOLD_CAPACITY, Float.class);
							put(DbConstants.AirplaneTable.COLUMN_TANK_CAPACITY, Float.class);
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
}
