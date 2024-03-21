package scheduler;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimerService;

import scheduler.controller.SchedulerControllerEjbInterface;

@Singleton
@Startup
public class Scheduler {

	@Resource
	private TimerService timerService;
	
	@EJB
	private SchedulerControllerEjbInterface schedulerEjb;
	
	@Schedule(hour = "08", minute = "0", second = "0", persistent = false)
	private void tasksToExecuteAt9() {
		schedulerEjb.tasksToExecuteAt8();
	}
}
