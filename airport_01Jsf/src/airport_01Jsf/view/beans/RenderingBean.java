package airport_01Jsf.view.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import airport_01Jsf.utils.NavigationMapper;

@SessionScoped
@Named
public class RenderingBean implements Serializable {

	private static final long serialVersionUID = -7187265389237572455L;
	
	private NavigationMapper pageToRender;
	private NavigationMapper homePage 						= NavigationMapper.HOME_PAGE;
	private NavigationMapper loginSubpage 					= NavigationMapper.LOGIN_SUBPAGE;
	private NavigationMapper reservationSubpage 			= NavigationMapper.RESERVATION_SUBPAGE;
	private NavigationMapper userAreaReservationsSubpage 	= NavigationMapper.USER_AREA_RESERVATIONS_SUBPAGE;
	private NavigationMapper userAreaUnsubscribeSubpage 	= NavigationMapper.USER_AREA_UNSUBSCRIBE_SUBPAGE;
	private NavigationMapper registrationSubpage 			= NavigationMapper.REGISTRATION_SUBPAGE;
	private NavigationMapper adminHomePage					= NavigationMapper.ADMIN_HOME_PAGE;
	private NavigationMapper adminFleetSubpage				= NavigationMapper.ADMIN_FLEET_SUBPAGE;
	private NavigationMapper adminAirportsSubpage			= NavigationMapper.ADMIN_AIRPORTS_SUBPAGE;
	private NavigationMapper errorPage						= NavigationMapper.ERROR_PAGE;
	private NavigationMapper searchSubpage					= NavigationMapper.SEARCH_SUBPAGE;
	
	@PostConstruct
	public void init() {
		//pageToRender = homePage;
	}
	
	public void switchPage(NavigationMapper pageToRender) {
		this.pageToRender = pageToRender;
	}
	
	public void redirectToUserSide() {
		redirectToPage("http://localhost:8080/airport_01Jsf/airportProject/lufthansia.xhtml");
		pageToRender = NavigationMapper.HOME_PAGE;
	}
	
	public void redirectToAdminSide() {
		redirectToPage("http://localhost:8080/airport_01Jsf/airportProject/lufthansiaAdmin.xhtml");
		pageToRender = NavigationMapper.ADMIN_HOME_PAGE;
	}
	
	public void redirectToErrorPage() {
		redirectToPage("http://localhost:8080/airport_01Jsf/airportProject/error.xhtml");
		pageToRender = NavigationMapper.ERROR_PAGE;
	}
	
	private void redirectToPage(String url) {
        try {
        	FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public NavigationMapper getPageToRender() {
		return pageToRender;
	}

	public void setPageToRender(NavigationMapper pageToRender) {
		this.pageToRender = pageToRender;
	}

	public NavigationMapper getHomePage() {
		return homePage;
	}

	public void setHomePage(NavigationMapper homePage) {
		this.homePage = homePage;
	}

	public NavigationMapper getLoginSubpage() {
		return loginSubpage;
	}

	public void setLoginSubpage(NavigationMapper loginSubpage) {
		this.loginSubpage = loginSubpage;
	}

	public NavigationMapper getReservationSubpage() {
		return reservationSubpage;
	}

	public void setReservationSubpage(NavigationMapper reservationSubpage) {
		this.reservationSubpage = reservationSubpage;
	}

	public NavigationMapper getUserAreaReservationsSubpage() {
		return userAreaReservationsSubpage;
	}

	public void setUserAreaReservationsSubpage(NavigationMapper userAreaReservationsSubpage) {
		this.userAreaReservationsSubpage = userAreaReservationsSubpage;
	}

	public NavigationMapper getRegistrationSubpage() {
		return registrationSubpage;
	}

	public void setRegistrationSubpage(NavigationMapper registrationSubpage) {
		this.registrationSubpage = registrationSubpage;
	}

	public NavigationMapper getUserAreaUnsubscribeSubpage() {
		return userAreaUnsubscribeSubpage;
	}

	public void setUserAreaUnsubscribeSubpage(NavigationMapper userAreaUnsubscribeSubpage) {
		this.userAreaUnsubscribeSubpage = userAreaUnsubscribeSubpage;
	}

	public NavigationMapper getAdminHomePage() {
		return adminHomePage;
	}

	public void setAdminHomePage(NavigationMapper adminHomePage) {
		this.adminHomePage = adminHomePage;
	}

	public NavigationMapper getAdminFleetSubpage() {
		return adminFleetSubpage;
	}

	public void setAdminFleetSubpage(NavigationMapper adminFleetSubpage) {
		this.adminFleetSubpage = adminFleetSubpage;
	}

	public NavigationMapper getAdminAirportsSubpage() {
		return adminAirportsSubpage;
	}

	public void setAdminAirportsSubpage(NavigationMapper adminAirportsSubpage) {
		this.adminAirportsSubpage = adminAirportsSubpage;
	}

	public NavigationMapper getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(NavigationMapper errorPage) {
		this.errorPage = errorPage;
	}

	public NavigationMapper getSearchSubpage() {
		return searchSubpage;
	}

	public void setSearchSubpage(NavigationMapper searchSubpage) {
		this.searchSubpage = searchSubpage;
	}
	
}
