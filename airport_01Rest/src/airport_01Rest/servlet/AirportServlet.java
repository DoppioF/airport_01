//package airport_01Rest.servlet;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//import javax.ejb.EJB;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import airport_01Ejb.interfaces.AirplaneEjbInterface;
//import airport_01Ejb.interfaces.AirportEjbInterface;
//import airport_01Ejb.interfaces.CustomerEjbInterface;
//import airport_01Ejb.interfaces.FlightEjbInterface;
//import airport_01Ejb.interfaces.FlightRouteEjbInterface;
//import airport_01Ejb.interfaces.ReservationEjbInterface;
//import airport_01Ejb.interfaces.RoleEjbInterface;
//import airport_01Ejb.interfaces.TicketEjbInterface;
//
///**
//http://localhost:8080/airport_01Rest/serv
// */
//@WebServlet("/serv")
//public class AirportServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	@EJB
//	private AirplaneEjbInterface airplaneEI;
//	@EJB
//	private AirportEjbInterface airportEI;
//	@EJB
//	private CustomerEjbInterface customerEI;
//	@EJB
//	private FlightEjbInterface flightEI;
//	@EJB
//	private FlightRouteEjbInterface flightRouteEI;
//	@EJB
//	private ReservationEjbInterface reservationEI;
//	@EJB
//	private RoleEjbInterface roleEI;
//	@EJB
//	private TicketEjbInterface ticketEI;
//       
//    public AirportServlet() {
//        super();
//    }
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		try {
//			response.getWriter().print(
////					airplaneEI.findAll() + "\n"
////					+ airportEI.findAll() + "\n"
////					+ customerEI.findAll() + "\n"
////					+ flightEI.findAll() + "\n"
//					flightRouteEI.findAll() + "\n"
////					+ reservationEI.findAll() + "\n"
////					+ roleEI.findAll() + "\n"
////					+ ticketEI.findAll()
//					);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
//	}
//
//}
