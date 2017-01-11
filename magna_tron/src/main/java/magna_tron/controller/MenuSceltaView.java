
package magna_tron.controller;

import java.util.HashMap;
import java.util.Map;
import magna_tron.model.MenuGiornaliero;
import magna_tron.model.Piatto;
/**
 * 
 * utilizzato da MenuService.getCurrentMenuGiornalieriScelta()
 */
public class MenuSceltaView {
    private MenuGiornaliero menuGiornaliero;
    private Piatto piatto;
    private boolean scelto;
    
    public MenuSceltaView(MenuGiornaliero menuGiornaliero, Piatto piatto, boolean scelto) {
        this.menuGiornaliero = menuGiornaliero;
        this.piatto = piatto;
        this.scelto = scelto;
    } 
     
     
    public MenuGiornaliero getMenuGiornaliero() {
        return menuGiornaliero;
    }

    public Piatto getPiatto() {
        return piatto;
    }

    public boolean isScelto() {
        return scelto;
    }

    public void setMenuGiornaliero(MenuGiornaliero menuGiornaliero) {
        this.menuGiornaliero = menuGiornaliero;
    }

    public void setPiatto(Piatto piatto) {
        this.piatto = piatto;
    }

    public void setScelto(boolean scelto) {
        this.scelto = scelto;
    }
     
     
}

