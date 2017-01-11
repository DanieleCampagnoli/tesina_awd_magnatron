
package magna_tron.controller;


import java.time.* ;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import magna_tron.controller.util.SessionUtil;
import magna_tron.ejb.MenuService;
import magna_tron.ejb.exception.ExceptionMenuService;
import magna_tron.model.Impiegato;
import magna_tron.model.MenuGiornaliero;
import magna_tron.model.MenuSettimanale;
import magna_tron.model.Persona;
import magna_tron.model.Piatto;
import magna_tron.model.ScelteMenu;
import magna_tron.model.exception.ExceptionMenuSettimanale;


@Named
@SessionScoped
public class Menu implements Serializable{
     private static final long serialVersionUID = 1094801825228386363L;    
     
     @EJB
     MenuService menuService;

     
    //parametro di ricerca dei menu e degli ordini
    protected Date dataInizio;
    //parametro di ricerca degli ordini
    protected Date dataFine;
    //id del menu settimanale selezionato
    protected int idMenuSettimanale;
    //menu settimanale selezionato
    protected MenuSettimanale currentMenuSettimanale;
    //id del menu giornaliero selezionato
    protected Long idMenuGiornaliero;
    //lista dei menu settimanali;
    protected List<MenuSettimanale> resultMenuSettimanale;
    //data inizio nuovo menu settimanale
    protected Date newDataInizio;
    //scelte del menu
    protected List<ScelteMenu> resultScelteMenu;
    //ordini degli impiegati
    protected Map<Piatto,Integer> ordini;
    // ordini KeyList
    protected List<Piatto> ordiniKeyList;
    //spese degli impiegati
    protected Map<Persona,Float> spese;
    //spese KeyList
    protected List<Persona> speseKeyList;
    
    public Long getIdMenuGiornaliero() {
        return idMenuGiornaliero;
    }
    /**
     * Quando si richiama questo metodo di viene visualizzata la view menuModificaAdd
     * @param idMenuGiornaliero
     * @return 
     */     
    public String setIdMenuGiornaliero(Long idMenuGiornaliero) {
        this.idMenuGiornaliero = idMenuGiornaliero;
        return "menuModificaAdd";
    }
    
    /**
     * imposta la dataInizio del nuovo menu
     * @param newDataInizio 
     */
    public void setNewDataInizio(Date newDataInizio) {
        this.newDataInizio = newDataInizio;
    }

    /**
     * restituisce la dataInizio del nuovo menu 
     */
    
    public Date getNewDataInizio() {
        return newDataInizio;
    }
    
    /**
     * inizializzazione del bean
     */
    @PostConstruct
    public void before(){
       resultMenuSettimanale = new ArrayList();
    }
    /**
     * restituisce la dataInizio per la ricerca dei MenuSettimanali 
     */
    public Date getDataInizio() {
        return dataInizio;
    }
    /**
     * restituisce idMenuSettimanale del MenuSettimanale corrente 
     */
    public int getIdMenuSettimanale() {
        return idMenuSettimanale;
    }
    /**
     * 
     * imposta la dataInizio per la ricerca dei MenuSettimanali 
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }
    /**
     * 
     * imposta idMenuSettimanale corrente 
     */
    public void setIdMenuSettimanale(int idMenuSettimanale) {
        this.idMenuSettimanale = idMenuSettimanale;
    }    
    /**
     * ricerca i menuSettimanali che hanno una certa data dataInizio
     */
    public void setResultMenuSettimanale(){
        if(dataInizio==null){
          dataInizio= new Date();
        } 
        resultMenuSettimanale = menuService.getMenuSettimanaleByStartDate(dataInizio);
    }
    
   /**
    *  restituisce i MenuSettimanali attivi  
    */
   public List<MenuSettimanale>  getResultMenuSettimanaleActive(){
       /*restituisce i menu attivi*/
        return menuService.getMenuSettimanaleActive();
    }
    
   
   /**
     * restituisce i menuSettimanali che hanno una certa data dataInizio
     */     
    public List<MenuSettimanale> getResultMenuSettimanale(){
            setResultMenuSettimanale();
            return resultMenuSettimanale; 
    }
   /**
     * crea un nuovo MenuSettimanale con gli attributi: newDataInizio 
     */    
    public void createMenuSettimanale() throws Exception{
        if(newDataInizio==null){
           throw new Exception("dataInizio null"); 
        }
        menuService.createMenuSettimanale(SessionUtil.getCuoco(),newDataInizio);
        setDataInizio(newDataInizio);
        setResultMenuSettimanale();
    }
    
    /**
      *apre la view necessaria a modificare il MenuSettimanale selezionato
      */
    public String updateMenuSettimanale(int idMenuSettimanale) throws Exception{
          this.idMenuSettimanale=idMenuSettimanale;
          
          for(MenuSettimanale ms: resultMenuSettimanale){
               if(ms.getIdMenuSettimanale()==idMenuSettimanale){
                  currentMenuSettimanale=ms;
                  break;
               }
          
          }
          
          
          return "menuModifica";
      
    }
    /**
      *apre la view necessaria a modificare il MenuSettimanale selezionato
      */    
    public List<MenuGiornaliero> getCurrentMenuGiornalieri(){
           MenuSettimanale currentMenu=
                   menuService.getMenuSettimanaleById(idMenuSettimanale);
           
           return currentMenu.getMenuGiornaliero();
    }
    /**
      * ritorna le scelte effettuate dall'utente sul MenuGiornaliero selezionato
      */
    public List<MenuSceltaView> getCurrentMenuGiornalieriScelta(){
           MenuSettimanale currentMenu=
                   menuService.getMenuSettimanaleById(idMenuSettimanale);
           getScelteMenu();
           
           List<MenuSceltaView> scelte= new ArrayList();
           
           for(MenuGiornaliero mg:currentMenu.getMenuGiornaliero()){
                for(Piatto p: mg.getPiatti()){
                     boolean found=false;
                     for(ScelteMenu sm:resultScelteMenu){
                         Piatto piattoSm=sm.getPiatto();
                         if(piattoSm.equals(p) && 
                            mg.getId().getGiorno()==sm.getMenuGiornaliero().getId().getGiorno()){
                           found=true;
                           break;
                         }
                     
                     }
                     scelte.add(new MenuSceltaView(mg,p,found));
                     
                }
           
           }
           
           return scelte;
    }
   
    /**
     *  giorni del menu giornaliero selezionato 
     */
    public List<Integer> getGiorni(){
    
         List<Integer> giorni= new ArrayList();
         for(MenuGiornaliero mg : getCurrentMenuGiornalieri()){
            giorni.add(mg.getId().getGiorno());
         }
         return giorni;
    }
    /**
     * rimuove il Piatto dal MenuGiornaliero
     * @param idMenuGiornaliero MenuGiornaliero selezionato
     * @param piatto Piatto selezionato
     */
    public  <T extends Piatto> void removePiatto(long idMenuGiornaliero,T piatto) throws Exception{
            MenuSettimanale currentMenu=
                   menuService.getMenuSettimanaleById(idMenuSettimanale);          
          menuService.removePiatto(currentMenu, idMenuGiornaliero, piatto);
    
    }
    
    /**
     * aggiunge il Piatto dal MenuGiornaliero
     * @param piatto Piatto selezionato
     */    
    public <T extends Piatto> void addPiatto(T piatto) throws Exception{
                    MenuSettimanale currentMenu=
                   menuService.getMenuSettimanaleById(idMenuSettimanale);  
                   menuService.addPiatto(currentMenu, idMenuGiornaliero, piatto);
    }
    /**
      * crea un MenuGiornaliero nuovo  
      */    
    public void addMenuGiornaliero(){
             MenuSettimanale currentMenu=
                   menuService.getMenuSettimanaleById(idMenuSettimanale);
             menuService.createMenuGiornaliero(currentMenu);
    }
    /**
     * controlla se è possibile attivare il MenuSettimanale
     * @param ms MenuSettimanale da attivare
     */
    public boolean canActivate(MenuSettimanale ms){
       
          return  menuService.activateMenuSettimanaleValidation(ms);
    }

    /**
     * attiva il MenuSettimanale
     * @param ms MenuSettimanale da attivare
     */    
    public void activate(MenuSettimanale ms) {
        try{
            menuService.activateMenuSettimanale(ms);
        }catch(ExceptionMenuSettimanale e){
           FacesContext facesContext = FacesContext.getCurrentInstance();
           facesContext.addMessage("activateForm", new FacesMessage(e.getMessage()));   
        }
    }
    /**
     * disattiva il MenuSettimanale
     * @param ms MenuSettimanale da attivare
     */    
    public void deactivate(MenuSettimanale ms) throws ExceptionMenuSettimanale{
        menuService.deactivateMenuSettimanale(ms);
    }
    /**
     * recupera ScelteMenu dell'utente e del MenuSettimanale corrente
    */    
    public void getScelteMenu(){
        
        Persona user= SessionUtil.getPersona();
        resultScelteMenu=menuService.getScelteMenu(idMenuSettimanale, user.getMatricola());     
    }
    /**
     * stabilisce se un piatto è stato scelto o meno dall'utente corrente
     * @param p
     * @return true se il piatto è stato scelto
     */
    public boolean piattoScelto(Piatto p){
         for(ScelteMenu sm : resultScelteMenu){
             if(sm.getId().getIdPiatto()==p.getIdPiatto()){
                  return true;
             }
         
         }
         return false;
    }
    /**
     * sceglie un Piatto nel MenuSettimanale selezionato 
     * @param piatto
     * @param menuGiornaliero
     * @throws ExceptionMenuService
     * @throws ParseException 
     */
    public <T extends Piatto> void scegliMenu(T piatto, MenuGiornaliero menuGiornaliero) throws ExceptionMenuService, ParseException{
        Impiegato user= SessionUtil.getImpiegato();
        menuService.scegliMenu(menuGiornaliero,piatto, user);
        
    }
    /**
     * sceglie un Piatto nel MenuSettimanale selezionato 
     * @param p Piatto di input
     * @param idMenuGiornaliero id del MenuGiornaliero da eliminare 
     * @throws ExceptionMenuService
     * @throws ParseException 
     */    
    public <T extends Piatto> void deleteScelteMenu(T p, int idMenuGiornaliero) throws ExceptionMenuService, ParseException{
        Persona user= SessionUtil.getPersona();
        menuService.deleteScelteMenu(idMenuSettimanale, idMenuGiornaliero,p.getIdPiatto(), user.getMatricola());
        
    }    
    /**
     * questa funzione stabilisce se un MenuSettimanale può essere modificato o meno
     * 
     * @return true il MenuSettimanale può essere modificato
     * @throws ParseException 
     */
    public boolean canUpdate() throws ParseException{
       MenuSettimanale currentMenu=
                   menuService.getMenuSettimanaleById(idMenuSettimanale);
       
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Date currentDate = sdf.parse(sdf.format(new Date()));
       Date menuDate= sdf.parse(sdf.format(currentMenu.getDataInizio()));
       
       boolean scelto=(menuDate.equals(currentDate));
       return scelto;
    }
    
    /**
     * reperisce gli ordini corrispondenti a dataInizio. Se dataInizio è null ritorna gli ordini
     * di oggi.
     */
    public void setOrdini(){
        // di default ritorna gli ordini di oggi
         if(dataInizio==null || dataFine==null){
               dataInizio= new Date();
               dataFine=dataInizio;
         }
         ordini=menuService.getSceltePiatti(dataInizio, dataFine);
         ordiniKeyList= new ArrayList(ordini.keySet());
    }
    /**
     * 
     * setta il valore di ordini   
     */
    public Map<Piatto,Integer> getOrdini(){
        if(ordini==null){
           ordini= new HashMap<>();
        }
        return ordini;
    }
    /**
     * 
     * chiavi di ordini 
     */
    public List<Piatto> getOrdiniKeyList(){
        //defualt: ordini del giorno corrente
        if(ordiniKeyList==null){
            setOrdini();
        }
        return ordiniKeyList;
    }
    /**
     * 
     * chiavi degli ordini del giorno corrente 
     */
    public List<Piatto> getCurrentOrdiniKeyList(){
        //ordini del giorno corrente
        dataInizio=null;
        dataFine=null;
        setOrdini();
        
        return ordiniKeyList;
    }
    
     
    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }
    /** 
          ritira le spese effettuate dagli impiegati nell'intervallo temporale
          specificato. Gli impiegati che non hanno effettuato acquisti nell'intervallo temporale
          non compaiono nella lista.
    */
    public void setImpiegatoSpesa(){

        //inizializzo le date se sono null
         if(dataInizio==null || dataFine==null){
               dataInizio= new Date();
               
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               Calendar c = Calendar.getInstance();
             try {
                    c.setTime(sdf.parse( sdf.format(dataInizio)));
                 
             } catch (ParseException ex) {}
               c.add(Calendar.DATE, 1);  // number of days to add
               dataFine = c.getTime();
         }
         // inizializzazione spese e speseKey
         spese=menuService.getSpesaImpiegato(dataInizio, dataFine);
         speseKeyList= new ArrayList(spese.keySet());    
    }

    public Map<Persona, Float> getSpese() {
        return spese;
    }

    public List<Persona> getSpeseKeyList() {
        return speseKeyList;
    }
    
    
    
}
 