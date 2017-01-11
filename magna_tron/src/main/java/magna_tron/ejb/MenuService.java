package magna_tron.ejb;
/*ejb*/
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/*jpa*/
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
/*magna_tron.model*/
import magna_tron.model.*;
import magna_tron.model.exception.ExceptionMenuSettimanale;
import magna_tron.ejb.exception.ExceptionMenuService;

/**
 * Fornisce le funzionalità per gestire i menu della mensa.
 * @author daniele
 */
 @Stateless
public class MenuService {
    	  @PersistenceContext(unitName = "magnaTronPersistenceUnit")
	  protected EntityManager em;

          /**
           * Restituisce i MenuSettimanali che hanno dataInizio maggiore o uguale a startDate
           * @param startDate data di inizio minima
           * @return MenuSettimanali che hanno dataInizio &ge; startDate
           */
          public List<MenuSettimanale> getMenuSettimanaleByStartDate(Date startDate){
                
                Query qMenuDate= em.createNamedQuery("MenuSettimanale.ByStartDate",MenuSettimanale.class)
				    .setParameter("dataInizio", startDate);
                
                List<MenuSettimanale> result;
                
                try{
                     result=qMenuDate.getResultList();
                
                }catch(javax.persistence.NoResultException e){
                     result= new ArrayList();
                }
                
                return result;
          }
          
          /**
           * 
           * @param id id del MenuSettimanale
           * @return MenuSettimanale ricercato
           * @throws javax.persistence.NoResultException se non esiste un MenuSettimanale che corrisponde
           * ad id
           */
          public MenuSettimanale getMenuSettimanaleById (int id) throws javax.persistence.NoResultException{

                return em.createNamedQuery("MenuSettimanale.ById",MenuSettimanale.class)
				    .setParameter("idMenuSettimanale",id).getSingleResult();
              
          }
          /**
           * Ritorna i MenuSettimanali che hanno la dataInizio massima rispetto a tutti i MenuSettimanali 
           * inseriti  e sono attivi.
           * @return lista dei MenuSettimanali ricercati
           */
          public List<MenuSettimanale> getMenuSettimanaleActive(){
                Query qMenuDate= em.createNamedQuery("MenuSettimanale.Attivo",MenuSettimanale.class);
                
                List<MenuSettimanale> result;
                try{
                     result=qMenuDate.getResultList();
                
                }catch(javax.persistence.NoResultException e){
                     result= new ArrayList();
                }
                
                return result;                  
          }
          
          
          /**crea un menu settimanale e restituisce la chiave del menuSettimanale
           * creato.  
          */ 
        /**
          * Crea un menuSettimanale  
          * @param cuoco Cuoco che crea il menu
          * @param dataInizio data di inizio del menu
          * @return id del nuovo MenuSettimanale creato
          */          
          public long createMenuSettimanale(Cuoco cuoco, Date dataInizio){

                  MenuSettimanale menu= new MenuSettimanale();
                  menu.setCuoco(cuoco);
                  menu.setDataInizio(dataInizio);
                  menu=em.merge(menu);
                  
                  return menu.getIdMenuSettimanale();
          }
          

          /**
           * Aggiunge un piatto al MenuSettimanale
           * @param <T> T fa parte della gerarchia Piatto
           * @param menuSettimanale MenuSettimanale 
           * @param idMenuGiornaliero MenuGiornaliero
           * @param piatto Piatto
           * @return MenuSettimanale aggiornato
           * @throws javax.persistence.NoResultException entità non trovate
           * @throws Exception non è possibile aggiungere un Piatto
           */
          
          public <T extends Piatto> MenuSettimanale addPiatto(MenuSettimanale menuSettimanale,
                  long idMenuGiornaliero,
                  T piatto) throws javax.persistence.NoResultException, Exception{
               /*aggiungo un piatto ad un certo MenuSettimanale in un certo MenuGiornaliero.
                *
                */
               
               /*
                 se il menu giornaliero richiesto non esiste viene creato 
                 e aggiunto al MenuSettimanale.
               */ 
               boolean found=false;
               for(MenuGiornaliero mg: menuSettimanale.getMenuGiornaliero()){
                       /*aggiunta del piatto al menuGiornaliero*/ 
                       if(mg.getId().getGiorno() ==idMenuGiornaliero){
                           mg.addPiatto(piatto);
                           found=true;
                           break;
                       }
               
               }
               
               /*creazione menu settimanle e aggiunta del piatto al menuGiornaliero*/
               if(!found){
                  MenuGiornaliero newMenuGiornaliero= new MenuGiornaliero();
                  newMenuGiornaliero.addPiatto(piatto);
                  MenuGiornalieroPK mgpk= new MenuGiornalieroPK();
                  mgpk.setGiorno((int)idMenuGiornaliero);
                  newMenuGiornaliero.setId(mgpk);
                  menuSettimanale.addMenuGiornaliero(newMenuGiornaliero);
               }

               /*salvataggio di MenuSettimanale*/
               menuSettimanale=em.merge(menuSettimanale);
               return menuSettimanale;
          }
          /**
           * rimuove un piatto dal MenuSettimanale
           * @param <T> T fa parte della gerarchia di Piatto
           * @param menuSettimanale MenuSettimanale da aggiornare
           * @param idMenuGiornaliero giorno
           * @param piatto Piatto da rimuovere
           * @return MenuSettimanale aggiornato
           * @throws javax.persistence.NoResultException le entità non sono state trovate
           * @throws Exception se non è possibile rimuovere il piatto
           */
    public <T extends Piatto> MenuSettimanale removePiatto(MenuSettimanale menuSettimanale
            ,long idMenuGiornaliero,T piatto) throws javax.persistence.NoResultException, Exception{
                 /*rimuove un piatto dal menu giornaliero*/   
                 for(MenuGiornaliero mg: menuSettimanale.getMenuGiornaliero()){   
                                
                   if(mg.getId().getGiorno()==idMenuGiornaliero){
                        mg.removePiatto(piatto);
                   }
                 }
                 
                 menuSettimanale=em.merge(menuSettimanale);
                 return menuSettimanale;
    }
    /**
     * attiva un MenuSettimanale
     * @param menuSettimanale MenuSettimanale da attivare
     * @return MenuSettimanale aggiornato
     * @throws ExceptionMenuSettimanale se non è possibile eseguire l'attivazione 
     */
    public MenuSettimanale activateMenuSettimanale(MenuSettimanale menuSettimanale) throws ExceptionMenuSettimanale{
          /*attiva il menu settimanale*/
          menuSettimanale.setAttivo(true);
          em.merge(menuSettimanale);
          return menuSettimanale;
    }
    /**
     * disattiva un MenuSettimanale
     * @param menuSettimanale MenuSettimanale da disattivare
     * @return MenuSettimanale aggiornato
     * @throws ExceptionMenuSettimanale se non è possibile disattivare il menu 
     */
    
    public MenuSettimanale deactivateMenuSettimanale(MenuSettimanale menuSettimanale) throws ExceptionMenuSettimanale{ 
       menuSettimanale.setAttivo(false);
       em.merge(menuSettimanale);
       return menuSettimanale;
    }
    /**
     * Controlla se è possbile attivare un MenuSettimanale o meno.
     * @param menuSettimanale MenuSettimanale da attivare
     * @return true se è possibile eseguire l'attivazione, false altrimenti
     */
    public boolean activateMenuSettimanaleValidation(MenuSettimanale menuSettimanale){
           try{
                menuSettimanale.attivoConstraint();
             }catch(ExceptionMenuSettimanale e){
             
             return false;
           }
           return true;
    }
   /**
    * con questo metodo gli Impiegati scelgono i Piatti dei MenuSettimanali 
    * @param menuGiornaliero menu scelto
    * @param piatto piatto scelto
    * @param impiegato soggetto che esegue la scelta
    * @return ScelteMenu creato
    * @throws ExceptionMenuService se il menu non può essere scelto
    */ 
    public ScelteMenu scegliMenu(MenuGiornaliero menuGiornaliero,Piatto piatto,Impiegato impiegato)throws ExceptionMenuService{
         
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             Date currentDate= new Date();
             Date menuDate= new Date();
                  try {
                      menuDate= sdf.parse(sdf.format(menuGiornaliero.getMenuSettimanale().getDataInizio()));
                      currentDate = sdf.parse(sdf.format(new Date()));
                  } catch (ParseException ex) {
                      Logger.getLogger(MenuService.class.getName()).log(Level.SEVERE, null, ex);
                  }
                   
             if(!menuDate.equals(currentDate)){
                  throw new ExceptionMenuService("il menu deve essere scelto nella data di inizio");
             }
            
         ScelteMenu scelta= new ScelteMenu();
         scelta.setMenuGiornaliero(menuGiornaliero);
         scelta.setPiatto(piatto);
         scelta.setPersona(impiegato);
          
         em.merge(scelta);
          
          return scelta;
    }
   /**
    * Restituisce le ScelteMenu di un MenuSettimanale
    * @param idMenuSettimanale id del menu settimanale da ricercare
    * @return ScelteMenu selezionate 
    */ 
    public List<ScelteMenu> getScelteMenu(int idMenuSettimanale){
                Query qScelteMenu= em.createNamedQuery("ScelteMenu.byMenuSettimanale",ScelteMenu.class)
				    .setParameter("idMenuSettimanale",idMenuSettimanale);
                
                List<ScelteMenu> result;
                
                try{
                     result=qScelteMenu.getResultList();
                
                }catch(javax.persistence.NoResultException e){
                     result= new ArrayList();
                }
                
                return result;
    
    }
    /**
     * @param idMenuSettimanale id del MenuSettianale
     * @param matricola matricola della Persona
     * @return scelte effettuate da un Impiegato 
     */
    public List<ScelteMenu> getScelteMenu(int idMenuSettimanale, int matricola){
            Query qScelteMenu= em.createNamedQuery("ScelteMenu.byMatricola",ScelteMenu.class)
				    .setParameter("idMenuSettimanale",idMenuSettimanale)
                                    .setParameter("matricola",matricola);
                
                List<ScelteMenu> result;
                
                try{
                     result=qScelteMenu.getResultList();
                
                }catch(javax.persistence.NoResultException e){
                     result= new ArrayList();
                }
                
                return result;
    }
   /**
    * 
    * @param idMenuSettimanale MenuSettimanale della scelta
    * @param idMenuGiornaliero giorno della scelta
    * @param idPiatto piatto della scelta
    * @param matricola matricola della Persona che ha effettuato la scelta 
    * @throws ExceptionMenuService le scelte si possono modificare solo nel primo giorno di attivazione del menu
    */      
    public void deleteScelteMenu(int idMenuSettimanale,int idMenuGiornaliero,int idPiatto, int matricola) throws ExceptionMenuService{
          ScelteMenuPK pk= new ScelteMenuPK(idMenuSettimanale,idMenuGiornaliero,idPiatto,matricola);
          ScelteMenu scelta= em.find(ScelteMenu.class,pk);
                
          if(scelta!=null){
             MenuGiornaliero menuGiornaliero= scelta.getMenuGiornaliero();
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             
             Date currentDate= new Date();
             Date menuDate= new Date();
            
              try {
                  currentDate = sdf.parse(sdf.format(new Date()));
                  menuDate= sdf.parse(sdf.format(menuGiornaliero.getMenuSettimanale().getDataInizio()));
              } catch (ParseException ex) {
                  Logger.getLogger(MenuService.class.getName()).log(Level.SEVERE, null, ex);
              }
                   
             
             if(!menuDate.equals(currentDate)){
                  throw new ExceptionMenuService("il menu deve essere scelto nella data di inizio");
             }
             em.remove(scelta);
          }
    }
    /**
     * Crea un nuovo MenuGiornaliero per un MenuSettimanale
     * @param ms MenuSettimanale di destinazione
     * @return MenuSettimanale
     */
    public MenuSettimanale createMenuGiornaliero(MenuSettimanale ms){
                int maxGiorno=0;                  
                MenuGiornalieroPK mgpk= new MenuGiornalieroPK();
                boolean found=false;
                for(MenuGiornaliero mg: ms.getMenuGiornaliero()){
                     if(mg.getId().getGiorno()>maxGiorno){
                         maxGiorno=mg.getId().getGiorno();
                         found=true;
                     }
                         
                }  
                
                
                mgpk.setGiorno(maxGiorno+1);
               
                
                MenuGiornaliero newMenuGiornaliero= new MenuGiornaliero();
                newMenuGiornaliero.setId(mgpk);
                ms.addMenuGiornaliero(newMenuGiornaliero);
                ms=em.merge(ms);
                return ms;
    }
   /**
    * 
    * @param startDate data minima
    * @param endDate data  massima
    * @return ScelteMenu dei MenuSettimanali con startDate &le; dataInizio &le; endDate 
    */
    public List<ScelteMenu> getScelte(Date startDate,Date endDate){
 
        
                Query qMenuDate= em.createNamedQuery("ScelteMenu.dataMenu");
                
                qMenuDate.setParameter(1, startDate,TemporalType.DATE);
                qMenuDate.setParameter(2, endDate,TemporalType.DATE)   ;
   
                
                List<ScelteMenu> result;
                try{
                     result=qMenuDate.getResultList();
                 //result= new ArrayList();
                }catch(javax.persistence.NoResultException e){
                     result= new ArrayList();
                } 
        return result;                
   }
   /**
    * 
    * @param startDate data minima
    * @param endDate data massima
    * @return per ogni piatto viene calcolato il numero di volte in cui è stato scelto nei MenuSettimanali 
    * con startDate &le; dataInizio &le; endDate
    */           
   public Map<Piatto,Integer> getSceltePiatti(Date startDate,Date endDate){
       List<ScelteMenu> scelte=getScelte(startDate,endDate);
       Map<Piatto,Integer> result= new HashMap<>();
       for(ScelteMenu sm:scelte){
           
           if(result.containsKey(sm.getPiatto())){
              int val=result.get(sm.getPiatto());
              result.put(sm.getPiatto(),val+1);
           }else{
              result.put(sm.getPiatto(),1);
           }
         
       }
       return result;
   }
   /**
    * 
    * @param startDate data minima
    * @param endDate data massima
    * @return per ogni Persona viene calcolata la spesa delle scelte effettuate 
    * sui MenuSettimanali con startDate &ge; dataInizio &ge; endDate
    */   
   public Map<Persona,Float> getSpesaImpiegato(Date startDate,Date endDate){
       List<ScelteMenu> scelte=getScelte(startDate,endDate);
       Map<Persona,Float> result= new HashMap<>();
       
       for(ScelteMenu sm:scelte){
           Piatto piatto= sm.getPiatto();
           if(result.containsKey(sm.getPersona())){
              float val=result.get(sm.getPersona());
              result.put(sm.getPersona(),val+piatto.getPrezzo());
           }else{
              result.put(sm.getPersona(),piatto.getPrezzo());
           }
         
       }
       return result;
   }   
   
    
}
