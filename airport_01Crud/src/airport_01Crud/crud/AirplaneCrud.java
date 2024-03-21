package airport_01Crud.crud;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import airport_01Model.models.entities.Airplane;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class AirplaneCrud {

	@SuppressWarnings("unchecked")
	public List<Airplane> findAllAirplane(EntityManager entityManager) throws DBQueryException, UnforeseenException {
		List<Airplane> airplaneList = new ArrayList<>();
		try {
			String stringQuery = "SELECT a FROM Airplane a";
			Query query = entityManager.createQuery(stringQuery, Airplane.class);
			airplaneList = query.getResultList();
			
			if (airplaneList == null) {
				throw new DBQueryException("Class AirplaneCRUD -> Method findAll -> airplaneList is null");
			} else if (airplaneList.isEmpty()) {
				throw new DBQueryException("Class AirplaneCRUD -> Method findAll -> airplaneList is void");
			}
			return airplaneList;
		} catch (NoResultException e) {
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airplane findAirplaneById(EntityManager entityManager, Long idAirplane) throws DBQueryException, UnforeseenException {
		System.out.println("AirplaneCRUD findById, airplane: " + idAirplane);
		try {
			String queryString = 	"SELECT a "
									+ "FROM Airplane a "
									+ "WHERE a.id 	= :id ";
			Airplane returnedAirplane = (Airplane) entityManager.createQuery(queryString, Airplane.class)
										.setParameter("id", idAirplane)
											.getSingleResult();
			System.out.println("RETURN AirplaneCRUD findById, airplane: " + idAirplane);
			return returnedAirplane;
		} catch (NoResultException e) {
			throw new DBQueryException("Class AirplaneCRUD -> Method findById -> airplane is NULL");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airplane insertAirplane(EntityManager entityManager, Airplane airplane) throws UnforeseenException {
		System.out.println("AirplaneCRUD insert, airplane: " + airplane);
		try {
			return entityManager.merge(airplane);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airplane deleteAirplane(EntityManager entityManager, Airplane airplane) throws UnforeseenException {
		System.out.println("AirplaneCRUD delete, airplane: " + airplane);
		try {
			entityManager.remove(airplane);
			return airplane;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Airplane updateAirplane(EntityManager entityManager, Airplane airplane) throws UnforeseenException {
		System.out.println("AirplaneCRUD update, airplane: " + airplane);
		try {
			return entityManager.merge(airplane);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
}
