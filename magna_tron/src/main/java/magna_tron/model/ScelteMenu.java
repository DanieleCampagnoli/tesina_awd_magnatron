package magna_tron.model;

import java.io.Serializable;
import javax.persistence.*;


/**
* <h1>ScelteMenu</h1>
* Questa classe rappresenta l'entità ScelteMenu.
* <br>
*<b>Named Queries</b>
* <br>
*<b>ScelteMenu.findAll</b>: seleziona tutte le entità di tipo ScelteMenu
* <br>
*<b>ScelteMenu.byMenuSettimanale</b>: seleziona tutte le entità di tipo ScelteMenu
* che di un certo MenuSettimanale, parametri:
*<ul>
*<li><b>idMenuSettimanale</b>: type Integer </li>
*</ul> 
* <br>
*<b>ScelteMenu.byMatricola</b>: seleziona tutte le entità di tipo ScelteMenu
* che di un certo MenuSettimanale e di una certa Persona, parametri:
*<ul>
*<li><b>idMenuSettimanale</b>: type integer </li>
*<li><b>matricola</b>: type integer </li>
*</ul>
* <br>
*<b>Named Native Queries</b>
* <br>
*<b>ScelteMenu.dataMenu</b>: seleziona le ScelteMenu che sono state effettuate nell'intervallo 
* temporale individuato da due date in input, parametri:
*<ul>
*<li><b>1</b>: estremo inferiore dell'intervallo, type Date </li>
*<li><b>2</b>: estremo superiore dell'intervallo, type Date </li>
*</ul>
* 
* @author  Daniele Campagnoli
* @version 1.0
*/

@Entity
@Table(name="scelte_menu")
@NamedQueries({
    @NamedQuery(name="ScelteMenu.findAll", query="SELECT s FROM ScelteMenu s"),
    @NamedQuery(name="ScelteMenu.byMenuSettimanale", query="SELECT s FROM ScelteMenu s where "
            + "s.id.idMenuSettimanale=:idMenuSettimanale"),
    @NamedQuery(name="ScelteMenu.byMatricola", query="SELECT s FROM ScelteMenu s where "
            + "s.id.idMenuSettimanale=:idMenuSettimanale and s.id.matricola=:matricola")
})

@NamedNativeQuery(name="ScelteMenu.dataMenu", 
query="select sm.*" 
+" from v_menu_giornaliero_data vmg join scelte_menu sm " 
+" on( "
+" vmg.ID_MENU_SETTIMANALE=sm.ID_MENU_SETTIMANALE and "
+" vmg.GIORNO=sm.GIORNO "
+" ) "  
+" where vmg.DATA_MENU>= ? and vmg.DATA_MENU<= ? ", 
resultClass=ScelteMenu.class)

public class ScelteMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ScelteMenuPK id;

	//bi-directional many-to-one association to MenuGiornaliero
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="GIORNO", referencedColumnName="GIORNO"),
		@JoinColumn(name="ID_MENU_SETTIMANALE", referencedColumnName="ID_MENU_SETTIMANALE")
		})
	private MenuGiornaliero menuGiornaliero;

	//bi-directional many-to-one association to Impiegato
	@ManyToOne
	@JoinColumn(name="MATRICOLA")
	private Persona persona;

	//uni-directional many-to-one association to Piatto
	@ManyToOne
	@JoinColumn(name="ID_PIATTO")
	private Piatto piatto;

	public ScelteMenu() {
	}

	public ScelteMenuPK getId() {
		return this.id;
	}

	public void setId(ScelteMenuPK id) {
		this.id = id;
	}

	public MenuGiornaliero getMenuGiornaliero() {
		return this.menuGiornaliero;
	}

	public void setMenuGiornaliero(MenuGiornaliero menuGiornaliero) {
		this.menuGiornaliero = menuGiornaliero;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Piatto getPiatto() {
		return this.piatto;
	}

	public void setPiatto(Piatto piatto) {
		this.piatto = piatto;
	}

}