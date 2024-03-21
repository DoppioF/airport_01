package scheduler.controller;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import airport_01Ejb.controllers.CommonControllerEjb;
import airport_01Model.dto.CustomerDto;
import customUtils.constants.strings.EjbConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import emailController.AirportEmailManager;

@Stateless(name = EjbConstants.SCHEDULER)
@LocalBean
public class SchedulerControllerEjb implements SchedulerControllerEjbInterface {

//	private Map<String, Timer> tasks = new HashMap<>();
//	
//	public void addTask(String taskName, long delay, long interval) {
//		timerService.createTimer(delay, interval, taskName);
//	}
	
//	@Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
//	public void saluta() {
//		System.out.println("ciao");
//	}
	
	@EJB
	private CommonControllerEjb commonEjb;
	
	//nei catch se ho tempo pensavo di impostare un nuovo tentativo dopo 10 minuti, max 5 tentativi
	
	private void flightReminder() {
		try {
			List<CustomerDto> customersLeavingTomorrow = commonEjb.findCustomersLeavingOnDate(LocalDate.now().plusDays(1));
			AirportEmailManager airportEmailManager = new AirportEmailManager();
			customersLeavingTomorrow.forEach(customer -> airportEmailManager.sendFlightReminder(customer));
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tasksToExecuteAt8() {
		flightReminder();
	}
}
