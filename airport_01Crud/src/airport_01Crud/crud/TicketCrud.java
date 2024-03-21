package airport_01Crud.crud;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import airport_01Model.models.entities.Ticket;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;

public class TicketCrud {

	public Ticket insertTicket(EntityManager entityManager, Ticket ticket) throws UnforeseenException {
		System.out.println("TicketCRUD insert, ticket: " + ticket);
		try {
			return entityManager.merge(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Ticket updateTicket(EntityManager entityManager, Ticket ticket) throws UnforeseenException {
		System.out.println("TicketCRUD update, ticket: " + ticket);
		try {
			return entityManager.merge(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Ticket findTicketById(EntityManager entityManager, Long idTicket) throws DBQueryException, UnforeseenException {
		System.out.println("TicketCRUD findTicketById, idTicket: " + idTicket);
		try {
			String queryString = 	"SELECT t "
									+ "FROM Ticket t "
									+ "WHERE t.id 	= :id ";
			Ticket returnedTicket = (Ticket) entityManager.createQuery(queryString, Ticket.class)
										.setParameter("id", idTicket)
											.getSingleResult();
			System.out.println("RETURN TicketCRUD findById, Ticket: " + returnedTicket);
			return returnedTicket;
		} catch (NoResultException e) {
			throw new DBQueryException("Class TicketCRUD -> Method findById -> Ticket is NULL");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public List<Ticket> findTicketByReservation(EntityManager entityManager, Long idReservation) throws DBQueryException, UnforeseenException {
		System.out.println("TicketCRUD findTicketByReservation, idReservation: " + idReservation);
		try {
			String queryString = 	"SELECT * "
									+ "FROM ticket t "
									+ "WHERE t.id_reservation = " + idReservation;
			@SuppressWarnings("unchecked")
			List<Ticket> returnedTicketList = (List<Ticket>) entityManager.createNativeQuery(queryString, Ticket.class);
			System.out.println("RETURN TicketCRUD findTicketByReservation, Ticket: " + returnedTicketList);
			return returnedTicketList;
		} catch (NoResultException e) {
			throw new DBQueryException("Class TicketCRUD -> Method findById -> Ticket is NULL");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public Ticket deleteTicket(EntityManager entityManager, Ticket ticket) throws UnforeseenException {
		System.out.println("TicketCRUD delete, idTicket: " + ticket);
		try {
			entityManager.remove(ticket);
			return ticket;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
}
