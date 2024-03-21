package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customUtils.constants.db.DbConstants;
import customUtils.exceptions.SearcherException;
import customUtils.logicTools.DbTableStructure;
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
				put("@", DbConstants.CustomerTable.EMAIL);
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
						setColumns(new ArrayList<>() {
							{
								add(new HashMap<>() {
									{
										put(DbConstants.ReservationTable.COLUMN_DATE, LocalDate.class);
										put(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD, Enum.class);
									}
								});
							}
						});
					}
				});
				add(new DbTableStructure() {
					{
						setTableName(DbConstants.CustomerTable.TABLE_NAME);
						setColumns(new ArrayList<>() {
							{
								add(new HashMap<>() {
									{
										put(DbConstants.NAME, String.class);
										put(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD, Enum.class);
									}
								});
							}
						});
					}
				});
			}
		};
		
//		Map<String, List<String>> tableStructure = new HashMap<>() {
//			{
//				put(DbConstants.ReservationTable.TABLE_NAME, new ArrayList<>() {
//					{
//						add(DbConstants.ReservationTable.COLUMN_DATE);
//						add(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD);
//					}
//				});
//				put(DbConstants.CustomerTable.TABLE_NAME, new ArrayList<>() {
//					{
//						add(DbConstants.ReservationTable.COLUMN_DATE);
//						add(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD);
//					}
//				});
//				put(DbConstants.FlightTable.TABLE_NAME, new ArrayList<>() {
//					{
//						add(DbConstants.ReservationTable.COLUMN_DATE);
//						add(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD);
//					}
//				});
//				put(DbConstants.FlightRouteTable.TABLE_NAME, new ArrayList<>() {
//					{
//						add(DbConstants.ReservationTable.COLUMN_DATE);
//						add(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD);
//					}
//				});
//				put(DbConstants.TicketTable.TABLE_NAME, new ArrayList<>() {
//					{
//						add(DbConstants.ReservationTable.COLUMN_DATE);
//						add(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD);
//					}
//				});
//				put(DbConstants.AirplaneTable.TABLE_NAME, new ArrayList<>() {
//					{
//						add(DbConstants.AirplaneTable.COLUMN_MODEL);
//						add(DbConstants.AirplaneTable.COLUMN_SEATS);
//						add(DbConstants.AirplaneTable.COLUMN_HOLD_CAPACITY);
//					}
//				});
//				put(DbConstants.AirportTable.TABLE_NAME, new ArrayList<>() {
//					{
//						add(DbConstants.ReservationTable.COLUMN_DATE);
//						add(DbConstants.ReservationTable.COLUMN_PAYMENT_METHOD);
//					}
//				});
//			}
//		};
		
		
		
		
		
		String input = "voli che arrivano il 15/09";
//		Map<String, Class<?>> dtoMapper = new HashMap<>() {
//			{
//				put(DbConstants.ReservationTable.TABLE_NAME	, ReservationDto.class);
//				put(DbConstants.CustomerTable.TABLE_NAME	, CustomerDto.class);
//				put(DbConstants.FlightTable.TABLE_NAME		, FlightTable.class);
//				put(DbConstants.FlightRouteTable.TABLE_NAME	, FlightRouteDto.class);
//				put(DbConstants.TicketTable.TABLE_NAME		, TicketDto.class);
//				put(DbConstants.AirplaneTable.TABLE_NAME	, AirplaneDto.class);
//				put(DbConstants.AirportTable.TABLE_NAME		, AirportDto.class);
//			}
//		};
//		String[] secondaryKeywords = new String[] {
//			"part"
//			, "arriv"
//			, "domani"
//		};
		 try {
			String query = new Searcher(input, keywords, null)
							.withTheseSecondaryKeywords(secondaryKeywords)
							.buildQuery();
			
			System.out.println(query);
		} catch (SearcherException e) {
			System.out.println(e.getMessage());
		}

	}
}
