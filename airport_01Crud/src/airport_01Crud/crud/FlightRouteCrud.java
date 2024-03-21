package airport_01Crud.crud;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import airport_01Model.models.entities.FlightRoute;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class FlightRouteCrud {

	@SuppressWarnings("unchecked")
	public List<FlightRoute> findAllFlightRoute(EntityManager entityManager) throws DBQueryException, UnforeseenException {
		try {
			List<FlightRoute> flightRouteList = new ArrayList<>();
			String stringQuery = "SELECT "
									+ "fr "
								+ "FROM "
									+ "FlightRoute fr "
								+ "ORDER BY "
									+ "fr.departureAirport.city";
			Query query = entityManager.createQuery(stringQuery, FlightRoute.class);
			flightRouteList = query.getResultList();
			
			
			return flightRouteList;
		} catch (NoResultException e) {
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public FlightRoute findFlightRouteById(EntityManager entityManager, Long idFlightRoute) throws DBQueryException, UnforeseenException {
		System.out.println("FlightRouteCRUD findById, flightRoute: " + idFlightRoute);
		try {
			String queryString = 	"SELECT fr "
									+ "FROM FlightRoute fr "
									+ "WHERE fr.id 	= :id ";
			FlightRoute returnedFlightRoute = (FlightRoute) entityManager.createQuery(queryString, FlightRoute.class)
										.setParameter("id", idFlightRoute)
											.getSingleResult();
			System.out.println("RETURN FlightRouteCRUD findById, flightRoute: " + idFlightRoute);
			return returnedFlightRoute;
		} catch (NoResultException e) {
			throw new DBQueryException("Class FlightRouteCRUD -> Method findById -> flightRoute is NULL");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FlightRoute> findFlightRouteByAirports(EntityManager entityManager, Long idDeparture, Long idArrival) throws DBQueryException, UnforeseenException {
		try {
			String queryString = "SELECT * FROM flight_route fr "
									+ "WHERE fr.id_departure_airport = :idDep "
									+ "AND fr.id_arrival_airport = :idArr";
			List<FlightRoute> foundFlightRouteList = entityManager.createNativeQuery(queryString, FlightRoute.class)
															.setParameter("idDep", idDeparture)
															.setParameter("idArr", idArrival)
															.getResultList();
			return foundFlightRouteList;
		} catch (NoResultException e) {
			throw new DBQueryException(GeneralConstants.NO_RESULTS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public FlightRoute insertFlightRoute(EntityManager entityManager, FlightRoute flightRoute) throws UnforeseenException {
		System.out.println("FlightRouteCRUD insert, flightRoute: " + flightRoute);
		try {
			return entityManager.merge(flightRoute);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public FlightRoute deleteFlightRoute(EntityManager entityManager, FlightRoute flightRoute) throws Exception {
		System.out.println("FlightRouteCRUD update, flightRoute: " + flightRoute);
		try {
			entityManager.remove(flightRoute);
			return flightRoute;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(GeneralConstants.GENERIC_ERROR);
		}
	}
}
