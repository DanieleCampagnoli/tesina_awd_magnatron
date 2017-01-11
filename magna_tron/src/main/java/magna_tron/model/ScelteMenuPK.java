package magna_tron.model;

import java.io.Serializable;
import javax.persistence.*;

/**
* <h1>ScelteMenu</h1>
* Questa classe rappresenta la chiave primaria dell'entità ScelteMenu.
*/
@Embeddable
public class ScelteMenuPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_MENU_SETTIMANALE", insertable=false, updatable=false)
	private int idMenuSettimanale;

	@Column(name="GIORNO", insertable=false, updatable=false)
	private int giorno;

	@Column(name="ID_PIATTO", insertable=false, updatable=false)
	private int idPiatto;

	@Column(name="MATRICOLA", insertable=false, updatable=false)
	private int matricola;

	public ScelteMenuPK() {
	}
        
        
	public ScelteMenuPK(int idMenuSettimanale,int idMenuGiornaliero,int idPiatto, int matricola) {
           this.giorno=idMenuGiornaliero;
           this.idMenuSettimanale=idMenuSettimanale;
           this.idPiatto=idPiatto;
           this.matricola=matricola;
	}
        
	public int getIdMenuSettimanale() {
		return this.idMenuSettimanale;
	}
	public void setIdMenuSettimanale(int idMenuSettimanale) {
		this.idMenuSettimanale = idMenuSettimanale;
	}
	public int getGiorno() {
		return this.giorno;
	}
	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}
	public int getIdPiatto() {
		return this.idPiatto;
	}
	public void setIdPiatto(int idPiatto) {
		this.idPiatto = idPiatto;
	}
	public int getMatricola() {
		return this.matricola;
	}
	public void setMatricola(int matricola) {
		this.matricola = matricola;
	}
        /**
         * Funzione di uguaglianza basata su giorno, idPiatto e matricola.
         * @param other oggetto da confrontare
         * @return true se gli oggetti sono uguali, false altrimenti
         */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ScelteMenuPK)) {
			return false;
		}
		ScelteMenuPK castOther = (ScelteMenuPK)other;
		return 
			(this.idMenuSettimanale == castOther.idMenuSettimanale)
			&& (this.giorno == castOther.giorno)
			&& (this.idPiatto == castOther.idPiatto)
			&& (this.matricola == castOther.matricola);
	}
        /**
         * 
         * @return valore della funzione di hash 
         */ 
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idMenuSettimanale;
		hash = hash * prime + this.giorno;
		hash = hash * prime + this.idPiatto;
		hash = hash * prime + this.matricola;
		
		return hash;
	}
}