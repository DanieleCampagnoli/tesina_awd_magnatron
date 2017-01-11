package magna_tron.model;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import magna_tron.model.exception.ExceptionMenuSettimanale;

/**
* <h1>MenuSettimanale</h1>
* Questa classe rappresenta l'entità MenuSettimanale. Questa entità si occupa di
* mantenere le associazioni con i MenuGiornalieri.
* <br>
*<b>Named Queries</b>
* <br>
*<b>MenuSettimanale.findAll</b>: seleziona tutte le entità di tipo MenuSettimanale
* <br>
* <b>MenuSettimanale.ByStartDate</b>: seleziona tutte le entità di tipo MenuSettimanale che hanno una dataInizio maggiore o uguale al parametro dataInizio di input.
* parametri:
* <ul>
*    <li><b>dataInizio</b>: data di inizio , type Date </li>
* </ul>
* <br>
* <b>MenuSettimanale.ById</b>: seleziona il MenuSettimanale identificato da idMenuSettimanale.
*parametri:
* <ul>
*    <li><b>idMenuSettimanale</b>: type int </li>
* </ul>
* <br>
* <b>MenuSettimanale.Attivo</b>: seleziona i MenuSettimanale che hanno la dataInzio massima e sono attivi.
* 
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@Table(name="menu_settimanale")
@NamedQueries({
@NamedQuery(name="MenuSettimanale.findAll", query="SELECT m FROM MenuSettimanale m"),
@NamedQuery(name="MenuSettimanale.ByStartDate", query="SELECT m FROM MenuSettimanale m WHERE m.dataInizio>= :dataInizio"),
@NamedQuery(name="MenuSettimanale.ById", query="SELECT m FROM MenuSettimanale m WHERE m.idMenuSettimanale= :idMenuSettimanale"),
@NamedQuery(name="MenuSettimanale.Attivo", query="SELECT m FROM MenuSettimanale m "
                                                 + "WHERE m.dataInizio=(SELECT MAX(ms.dataInizio) from MenuSettimanale ms where ms.attivo=true)"
                                                 + "and m.attivo=true")        
})
public class MenuSettimanale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_MENU_SETTIMANALE")
	private int idMenuSettimanale;

	@Column(name="ATTIVO")
	private boolean attivo;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO")
	private Date dataInizio;

	//bi-directional many-to-one association to MenuGiornaliero
	@OneToMany(mappedBy="menuSettimanale", cascade={CascadeType.ALL})
	private List<MenuGiornaliero> menuGiornaliero;

	//bi-directional many-to-one association to Cuoco
	@ManyToOne
	@JoinColumn(name="ID_CUOCO")
	private Cuoco cuoco;

	public MenuSettimanale() {
            menuGiornaliero= new ArrayList();
	}

	public int getIdMenuSettimanale() {
		return this.idMenuSettimanale;
	}

	public void setIdMenuSettimanale(int idMenuSettimanale) {
		this.idMenuSettimanale = idMenuSettimanale;
	}

	public boolean getAttivo() {
		return this.attivo;
	}
        /**
         * attivazione del MenuSettimanale. Questa operazione deve essere fatta rispettando
         * i vincoli imposti da MenuSettimanale.attivoConstraint().
         * @param attivo nuovo valore         
         * @throws magna_tron.model.exception.ExceptionMenuSettimanale se non è possibile aggiornare il valore        
        */
	public void setAttivo(boolean attivo) throws ExceptionMenuSettimanale {
		if(attivo==true){
			/*
			 *controlla che l'entità sia associata a 6 menu giornalieri
			 *aventi 2 piatti per ogni portata 
			 */
                     attivoConstraint();    
		}
		this.attivo = attivo;
	}
        /**
         * Controlla che i seguenti vincoli siano rispettati:
         * <ul>
         *    <li>il MenuSettimanale deve avere almeno 6 MenuGiornalieri </li>
         *    <li>Ogni MenuGiornaliero deve avere almeno 2 piatti per ogni portata </li>
         * </ul>
         * @throws ExceptionMenuSettimanale se i vincoli non sono rispettati
         */
        public void attivoConstraint() throws ExceptionMenuSettimanale{
            	if(this.getMenuGiornaliero().size()<6){
				throw new ExceptionMenuSettimanale("il menu settimanale deve avere 6 menu giornalieri");
	        }
			
		for(MenuGiornaliero mg : this.getMenuGiornaliero()){
				if(mg.getCompleteMenu()!=1){
					throw new ExceptionMenuSettimanale("il menu giornaliero del giorno numero: "+mg.getId().getGiorno()+
							                           "non ha due piatti per ogni portata");
				}
		}
        }  
	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public List<MenuGiornaliero> getMenuGiornaliero() {
		return this.menuGiornaliero;
	}

	public void setMenuGiornaliero(List<MenuGiornaliero> menuGiornaliero) {
		this.menuGiornaliero = menuGiornaliero;
	}

	public MenuGiornaliero addMenuGiornaliero(MenuGiornaliero menuGiornaliero) {
	      menuGiornaliero.setMenuSettimanale(this);	
              getMenuGiornaliero().add(menuGiornaliero);
              
		return menuGiornaliero;
	}

	public MenuGiornaliero removeMenuGiornaliero(MenuGiornaliero menuGiornaliero) {
		getMenuGiornaliero().remove(menuGiornaliero);
		menuGiornaliero.setMenuSettimanale(null);

		return menuGiornaliero;
	}

	public Cuoco getCuoco() {
		return this.cuoco;
	}

	public void setCuoco(Cuoco cuoco) {
		this.cuoco = cuoco;
	}

}