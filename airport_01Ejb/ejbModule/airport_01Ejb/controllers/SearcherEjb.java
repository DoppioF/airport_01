package airport_01Ejb.controllers;

import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;
import static airport_01Crud.connection.EntityManagerProvider.rollbackEntityTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import airport_01.businessLogic.AirportProjectUtil;
import airport_01.businessLogic.SeatsManager;
import airport_01Crud.crud.SearcherCrud;
import airport_01Ejb.interfaces.SearcherEjbInterface;
import airport_01Ejb.utils.ModelsManagingUtils;
import airport_01Ejb.utils.converters.ModelToDtoConverter;
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
import customUtils.classes.CustomStringUtils;
import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.EjbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.SearcherErrors;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import searcher.Searcher;
import searcher.dto.DbTableStructure;
import searcher.dto.QueryFromSearcher;
import searcher.dto.QueryResultDto;
import utils.constants.SearcherConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Stateless(name = EjbConstants.SEARCHER_EJB)
@LocalBean
public class SearcherEjb implements SearcherEjbInterface{
	
//	@PersistenceContext
//	EntityManager entityManager;

	@Override
	public List<QueryResultDto> invokeSearcher(final String input)
			throws ValidatorException, SearcherException, UnforeseenException {

		try {
			if (CustomStringUtils.isStringNullOrEmpty(input)) {
				throw new ValidatorException(GeneralConstants.MISSING_DATA);
			}
			
			List<QueryFromSearcher> queries = new Searcher(input
															, SearcherConstants.PRIMARY_KEYWORDS
															, SearcherConstants.TABLE_STRUCTURE_LIST)
													.withTheseSecondaryKeywords(SearcherConstants.SECONDARY_KEYWORDS)
													.buildQuery();
			List<List<?>> resultEntitiesList = new ArrayList<>();
			List<QueryResultDto> resultDtoList = new ArrayList<>();
			SearcherCrud searcherCrud = new SearcherCrud();
			beginEntityTransaction();
			
			executeQueriesAndGetResults(queries, resultEntitiesList, resultDtoList, searcherCrud);
			convertResults(resultEntitiesList, resultDtoList);
			
			return resultDtoList;
		} catch (ValidatorException e) {
			throw new ValidatorException(e.getMessage());
		} catch (DBQueryException e) {
			throw new SearcherException(GeneralConstants.NO_RESULTS);
		} catch (SearcherException e) {
			throw new SearcherException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		} finally {
			getEntityManager().close();
		}
	}
	
	private void executeQueriesAndGetResults(List<QueryFromSearcher> queries
											, List<List<?>> resultEntitiesList
											, List<QueryResultDto> resultDtoList
											, SearcherCrud searcherCrud) throws Exception {
		
		for (QueryFromSearcher queryFromSearcher : queries) {
			resultDtoList.add(new QueryResultDto(queryFromSearcher.getDtoType()));
			System.out.println("Query che sto lanciando: " + queryFromSearcher.getQuery());
			if (!stringValueOnColumnNumber(queryFromSearcher.getQuery())) {
				try {
					List<?> result = searcherCrud.executeQuery(getEntityManager()
//							entityManager
																, queryFromSearcher.getQuery()
																, queryFromSearcher.getEntityType());
					resultEntitiesList.add(result);
				} catch (DBQueryException e) {
					rollbackEntityTransaction();
					beginEntityTransaction();
				}
			}
		}
	}

	private void convertResults(List<List<?>> resultEntitiesList, List<QueryResultDto> resultDtoList) throws SearcherException {
		ModelToDtoConverter converter = new ModelToDtoConverter();
		for (int index = 0; index < resultEntitiesList.size(); index++) {
			convertToRightDtoList(resultEntitiesList.get(index), resultDtoList.get(index), converter);
		}
	}
	
	
	private void convertToRightDtoList(List<?> resultEntityList, QueryResultDto resultDto, ModelToDtoConverter converter) throws SearcherException {
		if (AirplaneDto.class.equals(resultDto.getDtoType())) {
			fillDtoListWithAirplaneDto(resultEntityList, resultDto, converter);
		} else if (AirportDto.class.equals(resultDto.getDtoType())) {
			fillDtoListWithAirportDto(resultEntityList, resultDto, converter);
		} else if (CustomerDto.class.equals(resultDto.getDtoType())) {
			fillDtoListWithCustomerDto(resultEntityList, resultDto, converter);
		} else if (FlightDto.class.equals(resultDto.getDtoType())) {
			fillDtoListWithFlightDto(resultEntityList, resultDto, converter);
		} else if (FlightRouteDto.class.equals(resultDto.getDtoType())) {
			resultDto.setDtoList(flightRouteDtoListFactory(resultEntityList, resultDto, converter));
		} else if (ReservationDto.class.equals(resultDto.getDtoType())) {
			fillDtoListWithReservationDto(resultEntityList, resultDto, converter);
		} else {
			throw new SearcherException(SearcherErrors.TYPE_NOT_RECOGNIZED + " " + resultDto.getDtoType());
		}
	}
	
	private void fillDtoListWithAirplaneDto(List<?> resultEntityList
											, QueryResultDto resultDto
											, ModelToDtoConverter converter) {
		
		for (Object entity : resultEntityList) {
			resultDto.getDtoList().add(converter.airplaneDtoFactory((Airplane) entity));
		}
	}
	
	private void fillDtoListWithAirportDto(List<?> resultEntityList
											, QueryResultDto resultDto
											, ModelToDtoConverter converter) {
		
		for (Object entity : resultEntityList) {
			resultDto.getDtoList().add(converter.airportDtoFactoryBasic((Airport) entity));
		}
	}
	
	private void fillDtoListWithCustomerDto(List<?> resultEntityList
											, QueryResultDto resultDto
											, ModelToDtoConverter converter) {
		
		for (Object entity : resultEntityList) {
			resultDto.getDtoList().add(converter.customerDtoFactoryForReminderFlight((Customer) entity));
		}
	}
	
	private void fillDtoListWithFlightDto(List<?> resultEntityList
											, QueryResultDto resultDto
											, ModelToDtoConverter converter) {
		
		SeatsManager seatsManager = new SeatsManager();
		for (Object entity : resultEntityList) {
			resultDto.getDtoList().add(converter.flightDtoFactoryForAdmin((Flight) entity, seatsManager.countAvailableSeats((Flight) entity)));
		}
	}
	
	private void fillDtoListWithReservationDto(List<?> resultEntityList
												, QueryResultDto resultDto
												, ModelToDtoConverter converter) {
		
		for (Object entity : resultEntityList) {
			resultDto.getDtoList().add(converter.reservationDtoFactory((Reservation) entity));
		}
	}
	
	private List<FlightRouteDto> flightRouteDtoListFactory(List<?> resultEntityList
															, QueryResultDto resultDto
															, ModelToDtoConverter converter) {
		
		return new ModelsManagingUtils()
					.generateFlightRouteDtoListWithReservableFlights((List<FlightRoute>) resultEntityList
																	, converter
																	, new AirportProjectUtil()
																	, true);
	}
	
	private boolean stringValueOnColumnNumber(String query) {
		String[] splittedQuery = query.split(" ");
		for (int index = 0; index < splittedQuery.length;) {
			if ((splittedQuery[index].equals("WHERE")
				|| splittedQuery[index].equals("OR"))
				&& isNumericClass(getClassOfCurrentColumn(splittedQuery[index + 1]))) {
				
				if (!CustomStringUtils.isStringParsableToNumber(splittedQuery[index + 3])) {
					return true;
				} else {
					index+= 4;
				}
			} else {
				index++;
			}
		}
		return false;
	}
	
	private Class<?> getClassOfCurrentColumn(String columnName) {
		return SearcherConstants.TABLE_STRUCTURE_LIST.stream()
	               .flatMap(table -> table.getColumns().entrySet().stream())
	               .filter(column -> column.getKey().equals(columnName))
	               .map(Entry::getValue)
	               .findFirst()
	               .orElse(null);
	}
	
	private boolean isNumericClass(Class<?> classType) {
		return Integer.class.equals(classType)
				|| Float.class.equals(classType)
				|| Byte.class.equals(classType)
				|| Long.class.equals(classType)
				|| Double.class.equals(classType);
	}
//	@SuppressWarnings("serial")
//	private List<Class<?>> getAllEntityClass() {
//		return new ArrayList<>() {
//			{
//				for (DbTableStructure tableStructure : SearcherConstants.TABLE_STRUCTURE_LIST) {
//					add(tableStructure.getEntityClass());
//				}
//			}
//		};
//	}
//	
//	private Class<?> findEntityClass(List<Class<?>> allEntityClasses, Class<?> currentClass) {
//		for ()
//	}
}
