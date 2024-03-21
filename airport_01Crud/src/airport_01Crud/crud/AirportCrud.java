package airport_01Crud.crud;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import airport_01Model.models.entities.Airport;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class AirportCrud {

	@SuppressWarnings("unchecked")
	public List<Airport> findAllAirport(EntityManager entityManager) throws DBQueryException, UnforeseenException {
		List<Airport> airportList = new ArrayList<>();
		try {
			
			Query query = entityManager.createNativeQuery("SELECT * FROM airport ORDER BY city", Airport.class);
			airportList = query.getResultList();
			
			return airportList;
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new DBQueryException("Class AirportCRUD -> Method findAll -> " + GeneralConstants.NO_RESULTS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airport findAirportById(EntityManager entityManager, Long idAirport) throws DBQueryException, UnforeseenException {
		System.out.println("AirportCRUD findById, idAirport: " + idAirport);
		try {
			String queryString = 	"SELECT a "
									+ "FROM Airport a "
									+ "WHERE a.id 	= :id ";
			Airport returnedAirport = (Airport) entityManager.createQuery(queryString, Airport.class)
										.setParameter("id", idAirport)
											.getSingleResult();
			System.out.println("RETURN AirportCRUD findById, airport: " + returnedAirport);
			return returnedAirport;
		} catch (NoResultException e) {
			throw new DBQueryException("Class AirportCRUD -> Method findById -> " + GeneralConstants.NO_RESULTS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airport insertAirport(EntityManager entityManager, Airport airport) throws UnforeseenException {
		System.out.println("AirportCRUD insert, airport: " + airport);
		try {
			return entityManager.merge(airport);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airport deleteAirport(EntityManager entityManager, Airport airport) throws UnforeseenException {
		System.out.println("AirportCRUD delete, airport: " + airport);
		try {
			entityManager.remove(airport);
			return airport;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airport updateAirport(EntityManager entityManager, Airport airport) throws UnforeseenException {
		System.out.println("AirportCRUD update, airport: " + airport);
		try {
			return entityManager.merge(airport);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
}
