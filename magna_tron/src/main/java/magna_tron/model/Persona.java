package magna_tron.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
* <h1>Persona</h1>
* Questa classe rappresenta l'entità persona. Una persona è un utente del sistema
* e ha diversi ruoli specificati dal campo ruolo, che viene utilizzato come 
* discriminante per creare le sottoclassi che rappresentano i ruoli delle persone.
* In questo sistema una persona può avere un solo ruolo.
* <br>
*<b> Named Queries</b>
* <br>
*<b>Persona.findAll</b> : restituisce tutte le entità di tipo persona
* <br>
*<b>Persona.findByLogin</b> :restituisce la persona in base ai parametri:
*          <ul>
*              <li>username: type String </li>
*              <li>password: type String </li>
*          </ul>
* <br>
* <b>Persona.findType</b>: restituisce 1 se la persona ha un certo ruolo, 0 altrimenti. Il parametro tipo
* specifica il tipo di ruolo che la persona dovrebbe avere, il parametro matricola specifica la matricola
* della persona
*          <ul>
*              <li>tipo: type Class </li>
*              <li>matricola: type Integer </li>
*          </ul> 
* @author  Daniele Campagnoli
* @version 1.0
*/

@Entity
@Table(name="persona")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="RUOLO")
@NamedQueries({
	@NamedQuery(name="Persona.findAll", query="SELECT p FROM Persona p"),
	@NamedQuery(name="Persona.findByLogin", 
	            query="SELECT p FROM Persona p WHERE p.username= :username and p.password= :password"),
	@NamedQuery(name="Persona.findType", 
    query="SELECT COUNT(p) FROM Persona p WHERE p.matricola= :matricola and TYPE(p) = :tipo")
	
})
public class Persona implements Serializable {
	private static final long serialVersionUID = 1L;
        
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MATRICOLA")
	private int matricola;

	@Column(name="COGNOME")
	private String cognome;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_NASCITA")
	private Date dataNascita;

	@Column(name="NOME")
	private String nome;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="RUOLO")
	private String ruolo;

	@Column(name="USERNAME")
	private String username;





	public Persona() {
	}
        /**
         * matricola della persona
         * @return int, matricola della persona 
         */ 
	public int getMatricola() {
		return this.matricola;
	}
        /**
         * Questo metodo assegna una matricola ad una persona. In alternativa si
         * deve memorizzare la persona tramite un EntityManager per poter reperire
         * l'identificatore della persona.
         * @param matricola, matricola della persona 
         */
	public void setMatricola(int matricola) {
		this.matricola = matricola;
	}
        /**
         * 
         * @return cognome della persona 
         */  
	public String getCognome() {
		return this.cognome;
	}
        /**
         * @param cognome: cognome della persona 
         */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
        /**
         *@return data di nascita
         */
	public Date getDataNascita() {
		return this.dataNascita;
	}
        /**
         * 
         * @param dataNascita data di nascita della persona 
         */
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
        /**
         * 
         * @return nome della persona 
         */ 
	public String getNome() {
		return this.nome;
	}
        /**
         * 
         * @param nome nome della persona 
         */
	public void setNome(String nome) {
		this.nome = nome;
	}
        /**
         * 
         * @return password della persona 
         */
	public String getPassword() {
		return this.password;
	}
        /**
         * 
         * @param password nuova password 
         */
	public void setPassword(String password) {
		this.password = password;
	}
        /**
         * 
         * @return ruolo della persona, questo valore viene utilizzato come discriminante per 
         * determinare i permessi associati alla persona.
         */
	public String getRuolo() {
		return this.ruolo;
	}
        /**
         * Imposta il ruolo della persona.
         * @param ruolo ruolo della persona
         */  
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
        /**
         * 
         * @return username della persona 
         */
	public String getUsername() {
		return this.username;
	}
        /**
         * 
         * @param username nuovo username della persona
         */
	public void setUsername(String username) {
		this.username = username;
	}
        /**
         * Metodo che determina il comportamento di equals. Questo metodo viene utilizzato
         * per usufruire dei metodi definiti dalle strutture dati di tipo Collection. L'uguaglianza
         * è basta sulla matricola della persona.
         * @param obj oggetto su cui effettuare il confronto
         * @return true se obj è uguale a this, false altrimenti 
         */
        @Override
        public  boolean equals(Object obj) {
              if (obj == null) return false;
              if (obj == this) return true;
              if(obj instanceof Persona){
                 Persona p=(Persona) obj;    
                 boolean eq=(p.getMatricola()==this.getMatricola());
                 return eq;
              }
              return false;
        }
        /**
         * Funzione di Hash su persona basata su alcune costanti e la matricola.
         * Permette di sfruttare i metodi definiti dalle strutture dati di tipo Collection.
         * @return int, valore della funzione di hash
         */
        @Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getMatricola();
		return hash;
	}
	
}