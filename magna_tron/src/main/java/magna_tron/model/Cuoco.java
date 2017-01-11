package magna_tron.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
*Utente con permessi da Cuoco.
* 
*<b> Named Queries</b>
* <br>
*<b>Amministratore.findByLogin</b> : restituisce l'entità Cuoco che corrisponde alla matricola.
*          <ul>
*              <li>matricola: type int </li>
*          </ul>
* 
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@DiscriminatorValue("C")
@NamedQueries({
    @NamedQuery(name="Cuoco.findByLogin", 
	            query="SELECT c FROM Cuoco c WHERE c.matricola= :matricola ")
})
public class Cuoco extends Persona {
	private static final long serialVersionUID = 1L;
	//bi-directional many-to-one association to MenuSettimanale
	@OneToMany(mappedBy="cuoco")
	private List<MenuSettimanale> menuSettimanali;
	
	public List<MenuSettimanale> getMenuSettimanali() {
		return this.menuSettimanali;
	}

	public void setMenuSettimanali(List<MenuSettimanale> menuSettimanali) {
		this.menuSettimanali = menuSettimanali;
	}

	public MenuSettimanale addMenuSettimanali(MenuSettimanale menuSettimanali) {
		getMenuSettimanali().add(menuSettimanali);
		menuSettimanali.setCuoco(this);

		return menuSettimanali;
	}

	public MenuSettimanale removeMenuSettimanali(MenuSettimanale menuSettimanali) {
		getMenuSettimanali().remove(menuSettimanali);
		menuSettimanali.setCuoco(null);

		return menuSettimanali;
	}

}
