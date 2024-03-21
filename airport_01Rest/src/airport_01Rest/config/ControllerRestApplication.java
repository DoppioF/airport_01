package airport_01Rest.config;


import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import airport_01Rest.endpoint.AirplaneRest;
import airport_01Rest.endpoint.AirportRest;
import airport_01Rest.endpoint.CustomerRest;
import airport_01Rest.endpoint.FlightRest;
import airport_01Rest.endpoint.FlightRouteRest;
import airport_01Rest.endpoint.ReservationRest;
import airport_01Rest.endpoint.TicketRest;


@ApplicationPath("/rest")
public class ControllerRestApplication extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<>();
		set.add(AirplaneRest.class);
		set.add(AirportRest.class);
		set.add(FlightRest.class);
		set.add(FlightRouteRest.class);
		set.add(CustomerRest.class);
		set.add(ReservationRest.class);
		set.add(TicketRest.class);
		return set;
	}


}
