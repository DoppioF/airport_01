package airport_01Crud.crud;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import airport_01Model.models.entities.Customer;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class CustomerCrud {
	
	@SuppressWarnings("unchecked")
	public List<Customer> findAllCustomer(EntityManager entityManager) throws DBQueryException, UnforeseenException {
		List<Customer> customerList = new ArrayList<>();
		try {

			Query query = entityManager.createNativeQuery("SELECT * FROM customer", Customer.class);
			customerList = query.getResultList();
			
			return customerList;
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new DBQueryException("Class CustomerCRUD -> Method findAll -> customer " + GeneralConstants.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Customer findCustomerByEmailAndPassword(EntityManager entityManager, Customer customer) throws DBQueryException, UnforeseenException {
		System.out.println("CustomerCRUD findByEmailAndPassword, customer:" + customer);
		try {
			String queryString = 	"SELECT c "
									+ "FROM Customer c "
									+ "WHERE c.email = :em "
									+ "AND c.password = :pa";
			Query query = entityManager.createQuery(queryString, Customer.class)
					.setParameter("em", customer.getEmail())
					.setParameter("pa", customer.getPassword());
			Customer found = (Customer) query.getSingleResult();
//			String queryString = "SELECT c FROM Customer c";
//			List<Customer> customers = entityManager.createQuery(queryString).getResultList();
//			Customer found = null;
//			for (Customer c : customers) {
//				System.out.println(customer.getEmail() + " - " + c.getEmail());
//				System.out.println(customer.getPassword() + " - " + c.getPassword());
//			    if (c.getEmail().equals(customer.getEmail()) && c.getPassword().equals(customer.getPassword())) {
//			        found = c;
//			        break; 
//			    }
//			}
			return found;
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new DBQueryException("Class CustomerCRUD -> Method findByEmailAndPassword -> customer " + GeneralConstants.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	//TODO: Riscrivere questo metodo, catch insensati
	public Customer insertCustomer(EntityManager entityManager, Customer customer) throws DBQueryException, UnforeseenException {
		System.out.println("CustomerCRUD insert, customer:" + customer);
		try {
			if (!entityManager.contains(customer)) {
				entityManager.persist(customer);
			}
			
			System.out.println("RETURN CustomerCRUD insert, customer:" + customer);
			return customer;
		} catch (NoResultException e) {
			String error = "Class CustomerCRUD -> Method insert -> customer IS NULL";
			System.out.println(error);
			throw new DBQueryException(error);
		} catch (PersistenceException e) {
			throw new DBQueryException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}

	public Customer findCustomerById(EntityManager entityManager, Long idCustomer) throws DBQueryException, UnforeseenException {
		System.out.println("CustomerCRUD findById, idCustomer: " + idCustomer);
		try {
			String queryString = 	"SELECT c "
									+ "FROM Customer c "
									+ "WHERE c.id 	= :id ";
			Customer returnedCustomer = (Customer) entityManager.createQuery(queryString, Customer.class)
										.setParameter("id", idCustomer)
											.getSingleResult();
			System.out.println("RETURN CustomerCRUD findById, customer: " + returnedCustomer);
			return returnedCustomer;
		} catch (NoResultException e) {
			throw new DBQueryException("Class CustomerCRUD -> Method findById -> customer is NULL");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Customer deleteCustomer(EntityManager entityManager, Customer customer) throws UnforeseenException {
		System.out.println("CustomerCRUD delete, idCustomer: " + customer);
		try {
			entityManager.remove(customer);
			return customer;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Customer updateCustomer(EntityManager entityManager, Customer customer) throws UnforeseenException {
		System.out.println("CustomerCRUD update, customer: " + customer);
		try {
			return entityManager.merge(customer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
}
