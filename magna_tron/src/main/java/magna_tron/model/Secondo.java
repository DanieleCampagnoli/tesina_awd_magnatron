package magna_tron.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;


/**
* <h1>Secondo</h1>
* Questa classe rappresenta l'entità Secondo.
* <br>
*<b>Named Queries</b>
* <br>
*<b>Secondo.byNome</b>: seleziona tutte le entità di tipo Primo che contengono nel nome
* la stringa di input nome. parametri della query:
*<ul>
*<li><b>nome</b>: type String </li>
*</ul>
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@DiscriminatorValue("S")
@NamedQuery(name="Secondo.byNome", 
            query="SELECT s FROM Secondo s WHERE "
            +"(0 < LOCATE(:nome, s.nome))")
public class Secondo extends Piatto {
	private static final long serialVersionUID = 1L;
}
