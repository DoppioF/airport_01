package airport_01Crud.crud;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import airport_01Model.models.entities.Flight;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class FlightCrud {

	@SuppressWarnings("unchecked")
	public List<Flight> findAllFlight(EntityManager entityManager) throws DBQueryException, UnforeseenException {
		List<Flight> flightList = new ArrayList<>();
		try {
			String stringQuery = "SELECT f FROM Flight f";
			Query query = entityManager.createQuery(stringQuery, Flight.class);
			flightList = query.getResultList();
			
			return flightList;
		} catch (NoResultException e) {
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Flight findFlightById(EntityManager entityManager, Long idFlight) throws DBQueryException, UnforeseenException {
		System.out.println("FlightCRUD findById, idFlight: " + idFlight);
		try {
			String queryString = 	"SELECT f "
									+ "FROM Flight f "
									+ "WHERE f.id 	= :id ";
			Flight returnedFlight = (Flight) entityManager.createQuery(queryString, Flight.class)
										.setParameter("id", idFlight)
											.getSingleResult();
			System.out.println("RETURN FlightCRUD findById, flight: " + returnedFlight);
			return returnedFlight;
		} catch (NoResultException e) {
			throw new DBQueryException("Class FlightCRUD -> Method findById -> " + GeneralConstants.NO_RESULTS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Flight deleteFlight(EntityManager entityManager, Flight flight) throws DBQueryException, UnforeseenException {
		System.out.println("FlightCRUD delete, flight: " + flight);
		try {
			/*
			String queryString = 	"DELETE "
									+ "FROM Flight f "
									+ "WHERE f.id 	= :id ";
			Flight returnedFlight = (Flight) entityManager.createQuery(queryString, Flight.class)
										.setParameter("id", flight.getId())
											.getSingleResult();
			System.out.println("RETURN FlightCRUD findById, flight: " + flight);
			return returnedFlight;
			*/
			entityManager.remove(entityManager.contains(flight)
									? flight
									: entityManager.merge(flight)
								);
			entityManager.flush();
			entityManager.clear();
			return flight;
		} catch (NoResultException e) {
			throw new DBQueryException("Class FlightCRUD -> Method delete -> flight is NULL");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Flight updateFlight(EntityManager entityManager, Flight flight) throws UnforeseenException {
		System.out.println("FlightCRUD update, flight: " + flight);
		try {
			return entityManager.merge(flight);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Flight insertFlight(EntityManager entityManager, Flight flight) throws UnforeseenException {
		System.out.println("FlightCRUD update, flight: " + flight);
		try {
			return entityManager.merge(flight);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Flight> findFlightByDepartureDate(EntityManager entityManager, LocalDate localDate) throws DBQueryException, UnforeseenException {
		System.out.println("FlightCRUD findByDepartureDate, idFlight: " + localDate);
		try {
			String queryString = 	"SELECT * "
									+ "FROM flight "
									+ "WHERE DATE(departure_date) = :localDate ";
			List<Flight> flights = entityManager.createNativeQuery(queryString, Flight.class)
										.setParameter("localDate", localDate)
											.getResultList();
			System.out.println("RETURN FlightCRUD findByDepartureDate, list: " + flights);
			return flights;
		} catch (NoResultException e) {
			throw new DBQueryException("Class FlightCRUD -> Method findByDepartureDate -> " + GeneralConstants.NO_RESULTS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
}
