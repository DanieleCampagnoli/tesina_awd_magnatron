package magna_tron.controller;


import java.io.Serializable;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import magna_tron.ejb.LoginService;
import magna_tron.ejb.LoginServiceEnumRoles;
import magna_tron.model.*;
import magna_tron.controller.util.SessionUtil;
import magna_tron.ejb.exception.ExceptionLoginService;



/**
 * gestisce il login
 * @author daniele
 */
@Named
@SessionScoped
public class Login implements Serializable {
  
	private static final long serialVersionUID = 1094801825228386363L;
        
        @EJB
        LoginService loginService;
        
        
	private String pwd;
	private String user;
        private LoginServiceEnumRoles ruolo;
        

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
        private void setRuolo(LoginServiceEnumRoles r){
          ruolo=r;
        }
        public LoginServiceEnumRoles getRuolo(){
             return ruolo;
        }

	//validate login
        /**
         * valida i valori di user e password
         */
	public String validateUsernamePassword() {
	       //validazione user e pwd
               boolean invalid=false;
               if("".equals(user) || user==null){
                   FacesContext.getCurrentInstance().addMessage(
					"formLogin:username",
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Inserisci Username",
							"Inserisci Username"));
                  
                   invalid=true;
                }

               if("".equals(pwd) || pwd==null){
                   FacesContext.getCurrentInstance().addMessage(
					"formLogin:password",
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Inserisci Password",
							"Inserisci Password"));
                 
                   invalid=true;
                }
                
               if(invalid){
                  return "login";
               }
               
               try{
                //persona
                Persona persona =  loginService.getPersona(user, pwd);
		//salvataggio  persona
                HttpSession session = SessionUtil.getSession();
		session.setAttribute("persona", persona);
                //salvataggio ruolo
                ruolo= loginService.getPersonaType(persona);
                session.setAttribute("enumRuolo",ruolo);
                session.setAttribute("ruolo",loginService.getPersonaRole(ruolo, persona));

                setPwd("");
                
                switch (ruolo) {
                           case AMMINISTRATORE:
                                return "amministratore";
                           case CUOCO:
                                return "cuoco";
                           case IMPIEGATO:
                                return "impiegato";
                           default:
                                break;
                      }
                throw new ExceptionLoginService("");
        
                }catch(ExceptionLoginService e){
                 FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Username e Password non corrette",
							"Inserire Username e Password"));
			return "login"; 
                
                }
	}

	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtil.getSession();
		session.invalidate();
		return "login";
	}
}
