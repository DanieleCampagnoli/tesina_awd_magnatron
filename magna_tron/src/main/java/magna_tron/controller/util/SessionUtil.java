/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magna_tron.controller.util;


import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import magna_tron.model.*;

/**
 *classe di appoggio per gestire la sessione degli utenti.
 * @author daniele
 */
public class SessionUtil {
    /**
     * @return sessione corrente
     */
    public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}
    /**
     * @return richiesta corrente
     */         
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}
    /**
     * @return Persona della sessione corrente
     */
	public static Persona getPersona() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return (Persona) session.getAttribute("persona");
	}
    /**
     * @return Cuoco della sessione corrente
     */        
        public static Cuoco getCuoco(){
               HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return (Cuoco) session.getAttribute("ruolo");
        }
    /**
     * @return Amministratore della sessione corrente
     */                
        public static Amministratore getAmministratore(){
               HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return (Amministratore) session.getAttribute("ruolo");
        }
    /**
     * @return Impiegato della sessione corrente
     */                
        public static Impiegato getImpiegato(){
               HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return (Impiegato) session.getAttribute("ruolo");
        }
}
