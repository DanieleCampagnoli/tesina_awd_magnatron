package magna_tron.controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import magna_tron.model.Amministratore;
import magna_tron.model.Cuoco;
import magna_tron.model.Impiegato;
import static magna_tron.ejb.LoginServiceEnumRoles.AMMINISTRATORE;
import static magna_tron.ejb.LoginServiceEnumRoles.CUOCO;
import static magna_tron.ejb.LoginServiceEnumRoles.IMPIEGATO;

/**
 * Filtro che gestisce l'accesso al sistema da parte degli utenti
 * @author daniele
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
        /**
         * 
         * @param request richiesta
         * @param response risposta
         * @param chain catena di filtri
         * @throws IOException errore di parsing della richiesta
         * @throws ServletException  errore di parsing della richiesta
         */  
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {

			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);

			String reqURI = reqt.getRequestURI();

                        //richieste che non necessitano permessi
                        
                        if(
                           reqURI.indexOf("/login.xhtml") >= 0
                           || reqURI.indexOf("/public/") >= 0
			   || reqURI.contains("javax.faces.resource")     
                          )
                             chain.doFilter(request, response);
                       
                        //richieste che necessitano permessi
                        
			else if (//validazione della sessione
                            ses != null  
                            && ses.getAttribute("persona") != null
                            && ses.getAttribute("enumRuolo") != null         
                            && ses.getAttribute("ruolo") != null
                            //valiazione dei permessi
                            &&( //amministratore
                                ( reqURI.indexOf("/amministratore/") >= 0
                                  && ses.getAttribute("enumRuolo")==AMMINISTRATORE)
                                ||
                                //cuoco
                                ( reqURI.indexOf("/cuoco/") >= 0
                                  && ses.getAttribute("enumRuolo")==CUOCO)
                                )
                                ||
                                //impiegato
			         ( reqURI.indexOf("/impiegato/") >= 0
                                  && ses.getAttribute("enumRuolo")==IMPIEGATO)
                            )

			     chain.doFilter(request, response);
                        
			else
				resp.sendRedirect(reqt.getContextPath() + "/faces/login.xhtml");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void destroy() {

	}
}
