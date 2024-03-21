package scheduler.controller;

import javax.ejb.Local;

@Local
public interface SchedulerControllerEjbInterface {

	void tasksToExecuteAt8();
}
