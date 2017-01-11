package magna_tron.model;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

/**
* <h1>MenuGiornaliero</h1>
* Questa classe rappresenta l'entità MenuGiornaliero. Questa entità si occupa di
* mantenere le asscociazioni con i Piatti delle varie portate e il MenuSettimanale.
* <br>
*<b>Named Queries</b>
* <br>
*<b>MenuGiornaliero.findAll</b>: seleziona tutte le entità di tipo MenuGiornaliero
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@Table(name="menu_giornaliero")

@NamedQueries({
    @NamedQuery(name="MenuGiornaliero.findAll", query="SELECT m FROM MenuGiornaliero m")
})

public class MenuGiornaliero implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MenuGiornalieroPK id;

	//bi-directional many-to-one association to MenuSettimanale
	@ManyToOne
	@JoinColumn(name="ID_MENU_SETTIMANALE")
	private MenuSettimanale menuSettimanale;
           
	//uni-directional many-to-many association to Piatto
	@ManyToMany
	@JoinTable(
		name="menu_giornaliero_contorno"
		, joinColumns={
			@JoinColumn(name="GIORNO", referencedColumnName="GIORNO"),
			@JoinColumn(name="ID_MENU_SETTIMANALE", referencedColumnName="ID_MENU_SETTIMANALE")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_PIATTO")
			}
		)
	private List<Contorno> contorni;

	//uni-directional many-to-many association to Piatto
	@ManyToMany
	@JoinTable(
		name="menu_giornaliero_dessert"
		, joinColumns={
			@JoinColumn(name="GIORNO", referencedColumnName="GIORNO"),
			@JoinColumn(name="ID_MENU_SETTIMANALE", referencedColumnName="ID_MENU_SETTIMANALE")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_PIATTO")
			}
		)
	private List<Dessert> dessert;

	//uni-directional many-to-many association to Piatto
	@ManyToMany
	@JoinTable(
		name="menu_giornaliero_primo"
		, joinColumns={
			@JoinColumn(name="GIORNO", referencedColumnName="GIORNO"),
			@JoinColumn(name="ID_MENU_SETTIMANALE", referencedColumnName="ID_MENU_SETTIMANALE")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_PIATTO")
			}
		)
	private List<Primo> primi;

	//uni-directional many-to-many association to Piatto
	@ManyToMany
	@JoinTable(
		name="menu_giornaliero_secondo"
		, joinColumns={
			@JoinColumn(name="GIORNO", referencedColumnName="GIORNO"),
			@JoinColumn(name="ID_MENU_SETTIMANALE", referencedColumnName="ID_MENU_SETTIMANALE")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_PIATTO")
			}
		)
	private List<Secondo> secondi;

	//bi-directional many-to-one association to ScelteMenu
	@OneToMany(mappedBy="menuGiornaliero", cascade={CascadeType.ALL})
	private List<ScelteMenu> scelteMenu;

	public MenuGiornaliero() {
            primi= new ArrayList();
            secondi= new ArrayList();
            contorni= new ArrayList();
            dessert= new ArrayList();
	}

	public MenuGiornalieroPK getId() {
		return this.id;
	}

	public void setId(MenuGiornalieroPK id) {
		this.id = id;
	}

	public MenuSettimanale getMenuSettimanale() {
		return this.menuSettimanale;
	}

	public void setMenuSettimanale(MenuSettimanale menuSettimanale) {
		this.menuSettimanale = menuSettimanale;
	}

	public List<Contorno> getContorni() {
		return this.contorni;
	}

	public void setContorni(List<Contorno> contorni) {
		this.contorni = contorni;
	}

	public List<Dessert> getDessert() {
		return this.dessert;
	}

	public void setDessert(List<Dessert> dessert) {
		this.dessert = dessert;
	}

	public List<Primo> getPrimi() {
		return this.primi;
	}

	public void setPrimi(List<Primo> primi) {
		this.primi = primi;
	}

	public List<Secondo> getSecondi() {
		return this.secondi;
	}

	public void setSecondi(List<Secondo> secondi) {
		this.secondi = secondi;
	}

	public List<ScelteMenu> getScelteMenu() {
		return this.scelteMenu;
	}

	public void setScelteMenu(List<ScelteMenu> scelteMenu) {
		this.scelteMenu = scelteMenu;
	}

	public ScelteMenu addScelteMenu(ScelteMenu scelteMenu) {
		getScelteMenu().add(scelteMenu);
		scelteMenu.setMenuGiornaliero(this);

		return scelteMenu;
	}

	public ScelteMenu removeScelteMenu(ScelteMenu scelteMenu) {
		getScelteMenu().remove(scelteMenu);
		scelteMenu.setMenuGiornaliero(null);

		return scelteMenu;
	}

	/**
	 * indica se il menu giornaliero ha due piatti per ogni portata o meno
         * @return 1 se la condizione è vera, 0 altrimenti
	 */
	public byte getCompleteMenu(){
		if( 
			this.getPrimi().size()>=2 &&
			this.getSecondi().size()>=2 &&
			this.getDessert().size()>=2 &&
			this.getContorni().size()>=2
		   ){
			return 1;
		}
		return 0;
	}
        /**
        * aggiunge un piatto nella portata corrispondente
        * @param <T> il tipo di piatto deve far parte della gerarchia Piatto
        * @param piatto Piatto da aggiungere, può essere di una sottoclasse della
        * @throws java.lang.Exception
        * gerarchia Piatto
        */
       public <T extends Piatto> void addPiatto(T piatto) throws Exception{
             if(piatto instanceof Primo ){
                 this.addPiatto((Primo)piatto);
             }else if(piatto instanceof Secondo ){
                 this.addPiatto((Secondo)piatto);
             } else if(piatto instanceof Contorno ){
                 this.addPiatto((Contorno)piatto);
             } else if(piatto instanceof Dessert ){
                 this.addPiatto((Dessert)piatto);
             }else{
                    throw new Exception("la classe di piatto non è supportata");    
             }
       }
       /**
        * controlla se il piatto è presente all'interno di una lista.
        * Si utilizza per capire se un piatto è già presente all'interno di una lista
        * che rappresenta la portata del MenuGiornaliero.
        * @param <T>
        * @param piatti lista di piatti di una portata
        * @param piatto piatto su cui eseguire il controllo
        * @return true se il piatto è già presente nella lista, false altrimenti
        */
       private <T extends Piatto> boolean isPresent(List<T> piatti,T piatto){
                  for(T p: piatti){
                      if(p.equals(piatto)){
                         return true;
                      }
                  }
                  return false;
       }

       
       public void addPiatto(Primo piatto){
            if(isPresent(this.primi,piatto)==false) 
                 this.getPrimi().add(piatto);
       }
       public void addPiatto(Secondo piatto){
            if(isPresent(this.secondi,piatto)==false) 
                 this.getSecondi().add(piatto);
       }  
       public void addPiatto(Contorno piatto){
             if(isPresent(this.contorni,piatto)==false)
                 this.getContorni().add(piatto);
       }
       public void addPiatto(Dessert piatto){
             if(isPresent(this.dessert,piatto)==false)
                 this.getDessert().add(piatto);
       }
      /**
        * rimuove un piatto nella portata corrispondente
        * @param <T> il tipo di piatto deve far parte della gerachia Piatto
        * @param piatto Piatto da rimuovere 
        * @throws java.lang.Exception se non è possibile rimuovere il piatto
        */
       public <T extends Piatto> void removePiatto(T piatto) throws Exception{
             if(piatto instanceof Primo ){
                 this.removePiatto((Primo)piatto);
             }else if(piatto instanceof Secondo ){
                 this.removePiatto((Secondo)piatto);
             } else if(piatto instanceof Contorno ){
                 this.removePiatto((Contorno)piatto);
             } else if(piatto instanceof Dessert ){
                 this.removePiatto((Dessert)piatto);
             }else{
                    throw new Exception("la classe di piatto non è supportata");    
             }
       } 
        
       public void removePiatto(Primo piatto){
             
                 this.getPrimi().remove(piatto);
       }
       public void removePiatto(Secondo piatto){
             
                 this.getSecondi().remove(piatto);
       }  
       public void removePiatto(Contorno piatto){
             
                 this.getContorni().remove(piatto);
       }
       public void removePiatto(Dessert piatto){
             
                 this.getDessert().remove(piatto);
       }
       /**
        * @return tutti i Piatti di un MenuGiornaliero
        */
       public List<Piatto> getPiatti(){
           List<Piatto> piatti= new ArrayList();
           for(Primo p : primi)
               piatti.add((Piatto)p);
           for(Secondo p : secondi)
               piatti.add((Piatto)p);
           for(Contorno p : contorni )
               piatti.add((Piatto)p);
           for(Dessert p : dessert)
               piatti.add((Piatto)p);
           
           return piatti;
       }

       
}