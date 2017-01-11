package magna_tron.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

/**
* <h1>Primo</h1>
* Questa classe rappresenta l'entità Primo.
* <br>
*<b>Named Queries</b>
* <br>
*<b>Primo.byNome</b>: seleziona tutte le entità di tipo Primo che contengono nel nome
* la stringa di input nome. parametri della query:
*<ul>
*<li><b>nome</b>: type String </li>
*</ul>
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@DiscriminatorValue("P")
@NamedQuery(name="Primo.byNome", 
            query="SELECT p FROM Primo p WHERE "
            +"(0 < LOCATE(:nome, p.nome))")
public class Primo extends Piatto {
	private static final long serialVersionUID = 1L;
}
