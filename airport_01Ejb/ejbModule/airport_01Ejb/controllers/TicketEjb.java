package airport_01Ejb.controllers;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01Ejb.interfaces.TicketEjbInterface;
import customUtils.constants.strings.EjbConstants;
/*
import static airport_01Crud.connection.EntityManagerProvider.beginEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.commitEntityTransaction;
import static airport_01Crud.connection.EntityManagerProvider.getEntityManager;
import static airport_01Crud.connection.EntityManagerProvider.rollbackEntityTransaction;

import airport_01Crud.crud.ReservationCrud;
import airport_01Crud.crud.TicketCrud;
import airport_01Ejb.utils.ModelToDtoConverter;
import airport_01Model.dto.ResponseDto;
import airport_01Model.dto.TicketDto;
import airport_01Model.models.entities.Ticket;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
*/
@Stateless(name = EjbConstants.TICKET_EJB)
@LocalBean
public class TicketEjb implements TicketEjbInterface {

	
}
