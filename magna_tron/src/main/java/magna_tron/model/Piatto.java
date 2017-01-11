package magna_tron.model;

import java.io.Serializable;
import javax.persistence.*;

/**
* <h1>Piatto</h1>
* Questa classe rappresenta l'entità Piatto.
* <br>
*<b>Named Queries</b>
* <br>
*<b>Piatto.findAll</b>: seleziona tutte le entità di tipo piatto   
* @author  Daniele Campagnoli
* @version 1.0
*/
@Entity
@Table(name="piatto")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="PORTATA")

@NamedQuery(name="Piatto.findAll", query="SELECT p FROM Piatto p")
public class Piatto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_PIATTO")
	private int idPiatto;

	@Lob
	@Column(name="DESCRIZIONE")
	private String descrizione;

	@Column(name="NOME")
	private String nome;

	@Column(name="PORTATA")
	private String portata;

	@Column(name="PREZZO")
	private float prezzo;

	public Piatto() {
	}
        /**
         * 
         * @return id del piatto 
         */  
	public int getIdPiatto() {
		return this.idPiatto;
	}
        /**
         * 
         * @param idPiatto nuovo id del piatto 
         */
	public void setIdPiatto(int idPiatto) {
		this.idPiatto = idPiatto;
	}
        /**
         * 
         * @return descrizone del piatto 
         */
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPortata() {
		return this.portata;
	}

	public void setPortata(String portata) {
		this.portata = portata;
	}

	public float getPrezzo() {
		return this.prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}
        /**
         * Uguaglianza per la classe di tipo piatto
         * @param obj oggetto su cui fare il confronto
         * @return true se gli oggetti sono uguali, false altrimenti
         */
        @Override
        public  boolean equals(Object obj) {
              if (obj == null) return false;
              if (obj == this) return true;
              if(obj instanceof Piatto){
                 Piatto p=(Piatto) obj;    
                 boolean eq=(p.getIdPiatto()==this.getIdPiatto());
                 return eq;
              }
              return false;
        }
        /**
         * funzione di hash su idPiatto
         * @return  valore della funzione di hash
         */
        @Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getIdPiatto();
		return hash;
	}

}