package airport_01Crud.crud;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class SearcherCrud {

	public List<?> executeQuery(EntityManager entityManager, String query, Class<?> entityType) throws DBQueryException {
		try {
			return entityManager.createNativeQuery(query, entityType).getResultList();
		} catch (NoResultException e) {
			throw new DBQueryException(e.getMessage());
		} catch (PersistenceException e) {
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DBQueryException(e.getMessage());
		}
	}
}
