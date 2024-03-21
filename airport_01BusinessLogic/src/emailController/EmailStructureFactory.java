package emailController;

import airport_01Model.dto.CustomerDto;
import airport_01Model.dto.ReservationDto;
import customUtils.constants.email.SenderConstants;
import customUtils.constants.email.SubjectConstants;
import model.EmailStructure;

public class EmailStructureFactory {

	public EmailStructure forFlightReminder(CustomerDto customerDto, ReservationDto reservationDto) {
		EmailStructure emailStructure = new EmailStructure();
		emailStructure.setSender(SenderConstants.EMAIL);
		emailStructure.setRecipients(new String[] {
				"fabiofilippo7@gmail.com"
		});
		emailStructure.setSubject(SubjectConstants.REMINDER_FLIGHT);
		
		String body = 
				"Gentile " + customerDto.getName() + ",\n\n"
				+ "Le ricordiamo che il volo da Lei prenotato è fissato per domani "
				+ reservationDto.getDepartureDate().toLocalDate()
				+ " alle ore " + reservationDto.getDepartureDate().toLocalTime()
				+ ".\nAuguriamo buon viaggio ai passeggeri.\n\n";
		emailStructure.setBody(body);
		return emailStructure;
	}
}
