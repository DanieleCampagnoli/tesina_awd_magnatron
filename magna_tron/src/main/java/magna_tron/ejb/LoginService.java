package magna_tron.ejb;
/*magna_tron*/

import magna_tron.model.Cuoco;
import magna_tron.model.Impiegato;
import magna_tron.model.Amministratore;
import magna_tron.model.Persona;
import magna_tron.ejb.exception.ExceptionLoginService;
import magna_tron.ejb.utils.ClassNameParser;
/*jpa*/
import javax.ejb.*;
import javax.persistence.*;
import javax.persistence.Query;
/*utils*/

/**
 * Fornisce le funzionalità per effettuare il login e recuperare i permessi di un utente.
 * @author daniele
 */
@Stateless
public class LoginService {
          
	  @PersistenceContext(unitName = "magnaTronPersistenceUnit")
	  protected EntityManager em;
	  /**
           * Resituisce la persona corrispondente ad una coppia di username e password.
           * @param username username inserita
           * @param password password inserita
           * @return la Persona corrispondente
           * @throws ExceptionLoginService se username e password non sono corrette
           */
	  public Persona getPersona(String username,String password) throws ExceptionLoginService{
		  try{
                     Persona persona = em.createNamedQuery("Persona.findByLogin",Persona.class)
				    .setParameter("username", username)
				    .setParameter("password",password)
		 		    .getSingleResult();
                    if(persona==null){
                         throw new ExceptionLoginService("credenziali non valide");
                      }
                     
                    return persona;
                   
                  }catch(ExceptionLoginService 
                         | javax.persistence.NoResultException e ){

                     throw new ExceptionLoginService("credenziali non valide");
                  
                  }
	  }
          
          /**
           * processa il nome della classe senza il package e ritorna una variabile di LoginServiceEnumRoles
           * @param ptype nome della classe
           * @return elemento di LoginServiceEnumRoles corrispondente
           * @throws ExceptionLoginService se ptype non corrisponde a nessun elemento di LoginServiceEnumRoles
           */
          private LoginServiceEnumRoles ClassPersonaType( String ptype) throws ExceptionLoginService{
              
                
              String className= ClassNameParser.splitClassName(ptype);
         
              switch (className) {
                  case "Cuoco":
                      return LoginServiceEnumRoles.CUOCO;
                  case "Amministratore":
                      return LoginServiceEnumRoles.AMMINISTRATORE;
                  case "Impiegato":
                      return LoginServiceEnumRoles.IMPIEGATO;
                  default:
                      break;
              }
              throw new ExceptionLoginService("ptype non corrisponde a nessuna classe supportata");
          }
          /**
           * Stabilisce se una Persona ha un certo ruolo
           * @param classType ruolo della persona
           * @param persona Persona su cui eseguire il controllo
           * @return true se la persona ha un certo ruolo, false altrimenti
           */
          private boolean isRole(Class classType,Persona persona ){
                   
               Long role = em.createNamedQuery("Persona.findType",Long.class)
				    .setParameter("matricola", persona.getMatricola())
				    .setParameter("tipo",classType)
				    .getSingleResult();
		  
	       return (role==1);
		  
          }
         /**
           * ruolo di una persona
           * @param persona Persona su cui eseguire il controllo
           * @return l'elemento di LoginServiceEnumRoles corrispondente
           * @throws magna_tron.ejb.exception.ExceptionLoginService se la persona non ha un ruolo
           */            
	  public LoginServiceEnumRoles getPersonaType(Persona persona) throws ExceptionLoginService{
                 /*tipo di ruolo ricoperto dalla persona*/    
           
	         if(isRole(Cuoco.class,persona)){
                    return ClassPersonaType(Cuoco.class.getName());
                 }
                 else if(isRole(Amministratore.class,persona)){
                    return ClassPersonaType(Amministratore.class.getName());
                 } 
                 else if(isRole(Impiegato.class,persona)){
                    return ClassPersonaType(Impiegato.class.getName());
                 } 
              
	      throw new ExceptionLoginService("la persona non ha un ruolo");
	  }
          
         /**
           * Query che ritira il ruolo di una persona
           * @param persona Persona su cui eseguire il controllo
           * @param classType Classe che si vuole ricevere in output, deve far parte della gerarchia di Persona
           * @return query per ritirare il ruolo di una Persona
           */                      
          private Query  createPersonaRoleQuery(Class classType,Persona persona) throws ExceptionLoginService{
                 /*ritira i dati relativi al ruolo ricoperto dalla persona*/ 
                String className=ClassNameParser.splitClassName(classType.getName());
                 
                return  em.createNamedQuery(className
                                             +".findByLogin",classType)
				    .setParameter("matricola", persona.getMatricola());
          }
         /**
           * Restituisce il ruolo di una persona
           * @param <T> T fa parte della gerarchia di Persona
           * @param role ruolo della Persona
           * @param persona persona di input
           * @return ruolo della persona
           *  @throws ExceptionLoginService se la persona non ha il ruolo ricercato
           */
          public <T extends Persona> T getPersonaRole(LoginServiceEnumRoles role,Persona persona) throws ExceptionLoginService{
               try{       
                    if(null!=role)switch (role) {
                           case AMMINISTRATORE:
                                return (T) createPersonaRoleQuery(Amministratore.class,persona).getSingleResult();
                           case CUOCO:
                                return (T) createPersonaRoleQuery(Cuoco.class,persona).getSingleResult();
                           case IMPIEGATO:
                                return (T) createPersonaRoleQuery(Impiegato.class,persona).getSingleResult();
                           default:
                                break;
                      }
             }catch(javax.persistence.NoResultException e){
	      throw new ExceptionLoginService("la persona non ha il ruolo selezionato");    
             }
               
             throw new ExceptionLoginService("la persona non ha il ruolo selezionato");  
	  }

	  
}
