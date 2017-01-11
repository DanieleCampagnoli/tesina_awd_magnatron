package magna_tron.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
* <h1>Amministratore</h1>
* Rappresenta l'amministratore del sito.
* <br>
*<b> Named Queries</b>
* <br>
*<b>Amministratore.findByLogin</b> : restituisce l'entità amministratore che corrisponde alla matricola.
*          <ul>
*              <li>matricola: type int </li>
*          </ul>
*  
* @author  Daniele Campagnoli
* @version 1.0
*/

@Entity
@DiscriminatorValue("A")
@NamedQueries({
    @NamedQuery(name="Amministratore.findByLogin", 
	            query="SELECT a FROM Amministratore a WHERE a.matricola= :matricola ")
})
public class Amministratore extends Persona {
	private static final long serialVersionUID = 1L;
	
}
