package magna_tron.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
/**
* <h1>Contorno</h1>
* Questa classe rappresenta l'entità Contorno.
* <br>
*<b>Named Queries</b>
* <br>
*<b>Contorno.byNome</b>: seleziona tutte le entità di tipo Primo che contengono nel nome
* la stringa di input nome. parametri della query:
*<ul>
*<li><b>nome</b>: type String </li>
*</ul>
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@DiscriminatorValue("C")
@NamedQuery(name="Contorno.byNome", 
            query="SELECT c FROM Contorno c WHERE "
            +"(0 < LOCATE(:nome, c.nome))")
public class Contorno extends Piatto {
	private static final long serialVersionUID = 1L;
}