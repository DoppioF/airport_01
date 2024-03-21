package airport_01.businessLogic;

import airport_01Model.models.entities.FlightRoute;
import customUtils.exceptions.UnforeseenException;

public class PriceManager {

	private final float DISTANCE_COEFF = 0.25F;
	private final float BASE_COST = 49.99F;
	private final float RESERVATION_COST = 2.99F;
//	private final float L_LUGGAGE = 30F;
//	private final float XL_LUGGAGE = 45F;
	
	public float priceFlightCalculator(FlightRoute flightRoute) throws UnforeseenException, Exception {
		try {
			return BASE_COST + (flightRoute.getDistanceKm() * DISTANCE_COEFF);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnforeseenException();
		}
	}
	
	public float priceTicketCalculator(Float flightPrice) {
		return flightPrice + RESERVATION_COST;
	}

}
