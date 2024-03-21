package airport_01Jsf.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import airport_01Ejb.interfaces.AirportEjbInterface;
import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Model.dto.AirportDto;
import customUtils.constants.db.DbConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@SessionScoped
@Named
public class AirportBean implements Serializable {

	private static final long serialVersionUID = 7122393169617568932L;

	@EJB
	private AirportEjbInterface airportEjb;
	
	@EJB
	private CommonControllerEjbInterface commonEjb;
	
	@Inject
	private MessagesBean messagesBean;
	
	@Inject
	private UtilsBean utilsBean;
	
	private List<AirportDto> airportListDeparture;
	private List<AirportDto> airportListArrival;
	private AirportDto selectedAirportDeparture;
	private AirportDto selectedAirportArrival;
	private AirportDto selectedAirportToEdit;
	private String stringa;
	
	@PostConstruct
	public void init() {
		initAirportList();
		selectedAirportDeparture = new AirportDto();
		selectedAirportArrival = new AirportDto();
		//selectedAirportToEdit = new AirportDto();
	}
	
	public void initAirportList() {
		try {
			airportListDeparture = airportEjb.findAllAirport();
		} catch (DBQueryException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		} catch (Exception e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public void onAirportChange() {
		System.out.println(selectedAirportDeparture);
		if (null != selectedAirportDeparture.getId()) {
			airportListArrival = new ArrayList<>() {
				{
					airportListDeparture.forEach(airport -> {
						if (airport.getId() != selectedAirportDeparture.getId()) {
							add(airport);
						}
					});
				}
			};
		} else {
			airportListArrival = new ArrayList<>();
		}
	}
	
	public void handleDeleteAirport() {
		try {
			commonEjb.deleteAirport(selectedAirportToEdit);
			PrimeFaces.current().executeScript("PF('dialogAirportDelete').show()");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}
	
	public void handleUpdateAirport() {
		try {
			selectedAirportToEdit = airportEjb.updateAirport(selectedAirportToEdit);
			utilsBean.hideAddEditMotherModelModal(DbConstants.AirportTable.MODEL_NAME);
			PrimeFaces.current().executeScript("PF('dialogAirportUpdate').show()");
		} catch (ValidatorException e) {
			PrimeFaces.current().executeScript("PF('dialogAirportEdit').show()");
			
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}

	public void handleInsertAirport() {
		try {
			selectedAirportToEdit.setId(null);
			airportEjb.insertAirport(selectedAirportToEdit);
			
			PrimeFaces.current().executeScript("PF('dialogAirportInsert').show()");
		} catch (ValidatorException e) {
			PrimeFaces.current().executeScript("PF('dialogAirportEdit').show()");
			utilsBean.generateErrorsFromBackend(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		} finally {
			utilsBean.hideAddEditMotherModelModal(DbConstants.AirportTable.MODEL_NAME);
		}
	}
	
	public void valorizeSelectedAirport(Long id) {
		for (AirportDto airplaneDto : airportListDeparture) {
			if (id == airplaneDto.getId()) {
				selectedAirportToEdit = airplaneDto;
				break;
			}
		}
		PrimeFaces.current().executeScript("PF('dialogAirportEdit').show()");
	}
	
	public List<AirportDto> getAirportListDeparture() {
		return airportListDeparture;
	}

	public void setAirportListDeparture(List<AirportDto> airportListDeparture) {
		this.airportListDeparture = airportListDeparture;
	}

	public String getStringa() {
		return stringa;
	}

	public void setStringa(String stringa) {
		this.stringa = stringa;
	}

	public AirportDto getSelectedAirportDeparture() {
		return selectedAirportDeparture;
	}

	public void setSelectedAirportDeparture(AirportDto selectedAirportDeparture) {
		this.selectedAirportDeparture = selectedAirportDeparture;
	}

	public List<AirportDto> getAirportListArrival() {
		return airportListArrival;
	}

	public void setAirportListArrival(List<AirportDto> airportListArrival) {
		this.airportListArrival = airportListArrival;
	}

	public AirportDto getSelectedAirportArrival() {
		return selectedAirportArrival;
	}

	public void setSelectedAirportArrival(AirportDto selectedAirportArrival) {
		this.selectedAirportArrival = selectedAirportArrival;
	}

	public AirportDto getSelectedAirportToEdit() {
		return selectedAirportToEdit;
	}

	public void setSelectedAirportToEdit(AirportDto selectedAirportToEdit) {
		this.selectedAirportToEdit = selectedAirportToEdit;
	}

	public UtilsBean getUtilsBean() {
		return utilsBean;
	}

	public void setUtilsBean(UtilsBean utilsBean) {
		this.utilsBean = utilsBean;
	}
}
