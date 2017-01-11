package magna_tron.model;

import java.io.Serializable;
import javax.persistence.*;

/**
* <h1>MenuGiornalieroPk</h1>
* Chiave primaria di MenuGiornaliero.
* @author  Daniele Campagnoli
* @version 1.0
*/
@Embeddable
public class MenuGiornalieroPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_MENU_SETTIMANALE", insertable=false, updatable=false)
	private int idMenuSettimanale;

	@Column(name="GIORNO")
	private int giorno;

	public MenuGiornalieroPK() {
	}
        /**
         * 
         * @return id del menu settimanale 
         */
	public int getIdMenuSettimanale() {
		return this.idMenuSettimanale;
	}
	public void setIdMenuSettimanale(int idMenuSettimanale) {
		this.idMenuSettimanale = idMenuSettimanale;
	}
        /**
         * 
         * @return giorno del MenuGiornaliero
         */
	public int getGiorno() {
		return this.giorno;
	}
        /**
         * @param giorno del MenuGiornaliero 
         */
	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}
        /**
         *calcola l'uguaglianza di due oggetti di tipo MenuGiornalieroPk in base a
         * idMenuSettimanale e giorno         
         * @param other oggetto da confrontare
         */    
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MenuGiornalieroPK)) {
			return false;
		}
		MenuGiornalieroPK castOther = (MenuGiornalieroPK)other;
		return 
			(this.idMenuSettimanale == castOther.idMenuSettimanale)
			&& (this.giorno == castOther.giorno);
	}
        /**
         * funzione di hash tra idMenuSettimanale e giorno 
         * @return valore della funzione di hash
         */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idMenuSettimanale;
		hash = hash * prime + this.giorno;
		
		return hash;
	}
}