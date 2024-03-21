package airport_01Jsf.view.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import customUtils.constants.airport_01.ViewJsfConstants;

import java.io.Serializable;

@Named
@SessionScoped
public class MessagesBean implements Serializable {

	private static final long serialVersionUID = 8565729358016848751L;

//	private void info(String title, String details) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, details));
//    }
//     
//    private void warn(String title, String details) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, details));
//    }
//     
//    private void error(String title, String details) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, details));
//    }
//     
//    private void fatal(String title, String details) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, title, details));
//    }

    public void newMessage(Severity type, String title, String details) {
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, title, details));
    }
    
    public void unexpectedErrorMessage() {
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ViewJsfConstants.Errors.TITLE_UNEXPECTED_ERROR, ViewJsfConstants.Errors.DETAILS_UNEXPECTED_ERROR));
    }
}
