package airport_01Jsf.view.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import airport_01Ejb.interfaces.AirplaneEjbInterface;
import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Model.dto.AirplaneDto;
import customUtils.constants.db.DbConstants;
import customUtils.constants.strings.GeneralConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import validatorUtils.ErrorsLogManager;

@SessionScoped
@Named
public class AirplaneBean implements Serializable {

	private static final long serialVersionUID = -8822495905976587563L;

	@EJB
	private AirplaneEjbInterface airplaneEjb;
	
	@EJB
	private CommonControllerEjbInterface commonEjb;
	
	@Inject
	private UtilsBean utilsBean;
	
	@Inject
	private MessagesBean messagesBean;
	
	private List<AirplaneDto> airplaneList;
	
	private AirplaneDto selectedAirplane;
	
	@PostConstruct
	public void init() {
		setSelectedAirplane(new AirplaneDto());
	}
	
	public void initAirplaneList() {
		try {
			airplaneList = airplaneEjb.findAllAirplane();
		} catch (DBQueryException e) {
			e.printStackTrace();
			messagesBean.unexpectedErrorMessage();
		} catch (UnforeseenException e) {
			e.printStackTrace();
			messagesBean.unexpectedErrorMessage();
		}
	}
	
	public void handleDeleteAirplane() {
		try {
			commonEjb.deleteAirplane(selectedAirplane);
			PrimeFaces.current().executeScript("PF('dialogAirplaneDelete').show()");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}
	
	public void handleUpdateAirplane() throws ValidatorException, DBQueryException, UnforeseenException {
		try {
			selectedAirplane = airplaneEjb.updateAirplane(selectedAirplane);
			utilsBean.hideAddEditMotherModelModal(DbConstants.AirplaneTable.MODEL_NAME);
			PrimeFaces.current().executeScript("PF('dialogAirplaneUpdate').show()");
		} catch (ValidatorException e) {
			PrimeFaces.current().executeScript("PF('dialogAirplaneEdit').show()");
			utilsBean.generateErrorsFromBackend(e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}
	
	public void handleInsertAirplane() {
		try {
			selectedAirplane.setId(null);
			airplaneEjb.insertAirplane(selectedAirplane);
			
			utilsBean.showModal("dialogAirplaneInsert");
		} catch (ValidatorException e) {
			utilsBean.showModal("dialogAirplaneEdit");
			String[] errorDetails = new ErrorsLogManager().errorMessageBreakUp(e.getMessage());
			for (String error : errorDetails) {
				messagesBean.newMessage(FacesMessage.SEVERITY_WARN, error.replace(GeneralConstants.NULL, "non valorizzato"), error);
			}
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		} finally {
			utilsBean.hideAddEditMotherModelModal(DbConstants.AirplaneTable.MODEL_NAME);
		}
	}
	
	public void valorizeSelectedAirplane(Long id) {
		try {
			List<AirplaneDto> list = airplaneEjb.findAllAirplane();
			for (AirplaneDto airplaneDto : list) {
				if (airplaneDto.getId() == id) {
					selectedAirplane = airplaneDto;
					break;
				}
			}
			PrimeFaces.current().executeScript("PF('dialogAirplaneEdit').show()");
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}
	
	public List<AirplaneDto> getAirplaneList() {
		return airplaneList;
	}

	public void setAirplaneList(List<AirplaneDto> airplaneList) {
		this.airplaneList = airplaneList;
	}

	public AirplaneDto getSelectedAirplane() {
		return selectedAirplane;
	}

	public void setSelectedAirplane(AirplaneDto selectedAirplane) {
		this.selectedAirplane = selectedAirplane;
	}

	public UtilsBean getUtilsBean() {
		return utilsBean;
	}

	public void setUtilsBean(UtilsBean utilsBean) {
		this.utilsBean = utilsBean;
	}
}
