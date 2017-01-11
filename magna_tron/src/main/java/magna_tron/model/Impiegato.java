package magna_tron.model;

import java.util.List;

import javax.persistence.*;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
*Utente con permessi da Impiegato.
* 
*<b> Named Queries</b>
* <br>
*<b>Impiegato.findByLogin</b> : restituisce l'entità Impiegato che corrisponde alla matricola.
*          <ul>
*              <li>matricola: type int </li>
*          </ul>
* 
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@DiscriminatorValue("I")
@NamedQueries({
    @NamedQuery(name="Impiegato.findByLogin", 
	            query="SELECT i FROM Impiegato i WHERE i.matricola= :matricola ")
})
public class Impiegato extends Persona {
	private static final long serialVersionUID = 1L;
	//bi-directional many-to-one association to ScelteMenu
	@OneToMany(mappedBy="persona")
	private List<ScelteMenu> scelteMenu;
	/**
         * restituisce tutte le scelte nei menu fatte dall'impiegato
         * @return scelte fatte dall'impiegato
         */
	public List<ScelteMenu> getScelteMenu() {
		return this.scelteMenu;
	}
        /**
         * 
         * @param scelteMenu rappresenta tutte le scelte nei menu fatte dall'impiegato 
         */
	public void setScelteMenu(List<ScelteMenu> scelteMenu) {
		this.scelteMenu = scelteMenu;
	}
        /**
         * 
         * @param scelteMenu nuova scelta da aggiungere
         * @return ritorna una versione di scelteMenu aggiornata con la persona che
         * ha effettuato la scelta
         */
	public ScelteMenu addScelteMenu(ScelteMenu scelteMenu) {
		getScelteMenu().add(scelteMenu);
		scelteMenu.setPersona(this);

		return scelteMenu;
	}
        /**
         * 
         * @param scelteMenu nuova scelta da aggiungere
         * @return ritorna una versione di scelteMenu senza persona
         */
	public ScelteMenu removeScelteMenu(ScelteMenu scelteMenu) {
		getScelteMenu().remove(scelteMenu);
		scelteMenu.setPersona(null);

		return scelteMenu;
	}

  
}
