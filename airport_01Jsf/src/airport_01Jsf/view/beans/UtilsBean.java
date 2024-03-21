package airport_01Jsf.view.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.GeneralConstants;
import validatorUtils.ErrorsLogManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Named
@SessionScoped
public class UtilsBean implements Serializable {

	private static final long serialVersionUID = 4426805178914974816L;
	
	private boolean disablePassengersNumberInput;
	private int passengerAccordionIndex;
	
	@Inject
	private CustomerBean customerBean;
	
	@Inject
	private FlightBean flightBean;
	
	@Inject
	private RenderingBean renderingBean;
	
	@Inject
	private MessagesBean messagesBean;
	
//	metodo per stampare l'hashcode di un componente con id autogenerato, salvatore
//	public void findComponentByGeneratedId() {
//        System.out.println("COMPONENTE: " + FacesContext.getCurrentInstance().getViewRoot().findComponent("adminDom:j_idt4"));
//    }
	
	public void checkUserRole() {
		if (DbConstants.RoleTable.ID_ADMIN != customerBean.getCustomerDto().getIdRole()) {
			renderingBean.redirectToErrorPage();
		}
	}
	
	public String formatDateFromLocalDate(LocalDate localDate) {
		if (null != localDate) {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String formattedDate = localDate.format(dateFormatter);
			return formattedDate;
		}
		return null;
	}

	public String formatDateFromLocalDateTime(LocalDateTime localDateTime) {
		if (null != localDateTime) {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String formattedDate = localDateTime.format(dateFormatter);
			return formattedDate;
		}
		return null;
	}
	
	public String formatTimeFromLocalDateTime(LocalDateTime localDateTime) {
		if (null != localDateTime) {
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			String formattedTime = localDateTime.format(timeFormatter);
			return formattedTime;
		}
		return null;
	}
	
	public LocalDate minDateForRegister() {
		return LocalDate.now().minusYears(118);
	}
	
	public LocalDate maxDateForRegister() {
		return LocalDate.now().minusYears(18);
	}
	
	public LocalDate minDateForEditFlight() {
		return LocalDate.now().plusMonths(1);
	}
	
	public String yearRangeForRegister() {
		return minDateForRegister().getYear() + ":" + LocalDate.now().getYear();
	}
	
	public String yearRangeForDepartureDate() {
		return LocalDate.now().plusMonths(1).getYear() + ":" + minDateForEditFlight().getYear();
	}
	
	public String yearRangeForArrivalDate() {
		return (null == flightBean.getFlightDto().getDepartureDate() 
					? LocalDate.now().getYear()
					: flightBean.getFlightDto().getDepartureDate())
				+ ":" + minDateForEditFlight().getYear();
	}
	
	public void insertFlightRequest() {
		showModal("dialogInsertFlight");
	}
	
	public void updateFlightRequest(Long id) {
		flightBean.getFlightDto().setId(id);
		//flightBean.valorizeFlightBasedOnSelected();
		showModal("dialogUpdateFlight");
	}
	
	public void deleteRequest(String model, Long id) {
		if ("Flight".equals(model)) {
			flightBean.getFlightDto().setId(id);
		}
		showModal("dialogDelete" + model);
	}
	
	public void showEditMotherModelModal(String model) {
		hideModal("dialog" + model + "Edit");
		showModal("dialog" + model + "AddEdit");
	}
	
	public void hideAddEditMotherModelModal(String model) {
		hideModal("dialog" + model + "AddEdit");
	}
	
	public void hideModal(String modalName) {
		PrimeFaces.current().executeScript("PF('" + modalName + "').hide()");
	}
	
	public void showModal(String modalName) {
		PrimeFaces.current().executeScript("PF('" + modalName + "').show()");
	}
	
	public void generateErrorsFromBackend(String errorMessage) {
		String[] errorDetails = new ErrorsLogManager().errorMessageBreakUp(errorMessage);
		for (String error : errorDetails) {
			messagesBean.newMessage(FacesMessage.SEVERITY_WARN, error.replace(GeneralConstants.NULL, "non valorizzato"), error);
		}
	}

	public boolean isDisablePassengersNumberInput() {
		return disablePassengersNumberInput;
	}

	public void setDisablePassengersNumberInput(boolean disablePassengersNumberInput) {
		this.disablePassengersNumberInput = disablePassengersNumberInput;
	}

	public int getPassengerAccordionIndex() {
		return passengerAccordionIndex;
	}

	public void setPassengerAccordionIndex(int passengerAccordionIndex) {
		this.passengerAccordionIndex = passengerAccordionIndex;
	}
	
	public void incrementAccordionIndex() {
		passengerAccordionIndex++;
	}

	public FlightBean getFlightBean() {
		return flightBean;
	}

	public void setFlightBean(FlightBean flightBean) {
		this.flightBean = flightBean;
	}

}
