package airport_01Jsf.view.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import airport_01Ejb.interfaces.CustomerEjbInterface;
import airport_01Jsf.utils.NavigationMapper;
import airport_01Model.dto.CustomerDto;
import customUtils.constants.airport_01.ViewJsfConstants;
import customUtils.constants.db.DbConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@SessionScoped
@Named
public class CustomerBean implements Serializable {

	private static final long serialVersionUID = 1470643407469724345L;
	private final long ADMIN_ROLE_ID = DbConstants.RoleTable.ID_ADMIN;
	private final long USER_ROLE_ID = DbConstants.RoleTable.ID_USER;
	
	@EJB
	private CustomerEjbInterface customerEjb;
	
	@Inject
	private FlightBean flightBean;
	
	@Inject
	private FlightRouteBean flightRouteBean;
	
	@Inject
	private RenderingBean renderingBean;
	
	@Inject
	private MessagesBean messagesBean;
	
	private CustomerDto customerDto;
	private boolean logged;
	
	@PostConstruct
	public void init() {
		customerDto = new CustomerDto();
	}
	
	public void handleLogin() {
		try {
			customerDto = customerEjb.findCustomerByEmailAndPassword(customerDto);
			logged = true;
			if (ADMIN_ROLE_ID == customerDto.getIdRole()) {
				renderingBean.redirectToAdminSide();
			} else {
				renderingBean.switchPage(null == flightBean.getFlightDto().getId()
											? NavigationMapper.HOME_PAGE
											: NavigationMapper.RESERVATION_SUBPAGE);
			}
			PrimeFaces.current().executeScript("PF('dialogLogin').show()");
		} catch (ValidatorException e) {
			messagesBean.newMessage(FacesMessage.SEVERITY_WARN, ViewJsfConstants.Errors.TITLE_VALIDATION_ERROR, e.getMessage());
			e.printStackTrace();
		} catch (DBQueryException e) {
			messagesBean.newMessage(FacesMessage.SEVERITY_ERROR, ViewJsfConstants.Errors.TITLE_WRONG_CREDENTIALS, "");
			e.printStackTrace();
		} catch (Exception e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		}
	}
	
	public void doLogout() {
		logged = false;
		init();
		flightRouteBean.init();
		renderingBean.redirectToUserSide();
	}
	
	public void handleRegister() {
		try {
			customerDto.setIdRole(USER_ROLE_ID);
			customerEjb.insertCustomer(customerDto);
			PrimeFaces.current().executeScript("PF('dialogRegistration').show()");
			handleLogin();
		} catch (ValidatorException e) {
			e.printStackTrace();
			messagesBean.newMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		} catch (DBQueryException e) {
			e.printStackTrace();
		} catch (UnforeseenException e) {
			e.printStackTrace();
		}
	}
	
	public void handleUnsubscribe() {
		try {
			customerEjb.deleteCustomer(customerDto);
			init();
			renderingBean.switchPage(NavigationMapper.HOME_PAGE);
			PrimeFaces.current().executeScript("PF('dialogUnsubscribe').show()");
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (DBQueryException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		} catch (UnforeseenException e) {
			messagesBean.unexpectedErrorMessage();
			e.printStackTrace();
		}
	}
	
	
	public CustomerDto findCustomerById() throws ValidatorException, DBQueryException, UnforeseenException {
		return customerEjb.findCustomerById(customerDto);
	}
	
	public void test() {
		System.out.println("test");
	}
	
	

	public CustomerDto getCustomerDto() {
		return customerDto;
	}

	public void setCustomerDto(CustomerDto customerDto) {
		this.customerDto = customerDto;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean isLogged) {
		this.logged = isLogged;
	}

	public RenderingBean getRenderingBean() {
		return renderingBean;
	}

	public void setRenderingBean(RenderingBean renderingBean) {
		this.renderingBean = renderingBean;
	}

	public MessagesBean getMessagesBean() {
		return messagesBean;
	}

	public void setMessagesBean(MessagesBean messagesBean) {
		this.messagesBean = messagesBean;
	}

	public long getADMIN_ROLE_ID() {
		return ADMIN_ROLE_ID;
	}

	public long getUSER_ROLE_ID() {
		return USER_ROLE_ID;
	}

	public FlightRouteBean getFlightRouteBean() {
		return flightRouteBean;
	}

	public void setFlightRouteBean(FlightRouteBean flightRouteBean) {
		this.flightRouteBean = flightRouteBean;
	}
	
}
