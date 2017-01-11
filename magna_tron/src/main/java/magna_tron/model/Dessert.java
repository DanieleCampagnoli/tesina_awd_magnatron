package magna_tron.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
/**
* <h1>Dessert</h1>
* Questa classe rappresenta l'entità Dessert.
* <br>
*<b>Named Queries</b>
* <br>
*<b>Dessert.byNome</b>: seleziona tutte le entità di tipo Dessert che contengono nel nome
* la stringa di input nome. parametri della query:
*<ul>
*<li><b>nome</b>: type String </li>
*</ul>
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@DiscriminatorValue("D")
@NamedQuery(name="Dessert.byNome", 
            query="SELECT d FROM Dessert d WHERE "
            +"(0 < LOCATE(:nome, d.nome))")
public class Dessert extends Piatto {
	private static final long serialVersionUID = 1L;
}