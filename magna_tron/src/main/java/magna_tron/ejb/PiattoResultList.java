package magna_tron.ejb;

/*magna_tron*/

import magna_tron.model.Primo;
import magna_tron.model.Secondo;
import magna_tron.model.Contorno;
import magna_tron.model.Dessert;
import magna_tron.model.Piatto;

/*utils*/
import java.util.List;
import java.util.ArrayList;

/**
* contiene i dati ritornati da una interrogazione a PiattoService.getPiattoByNome . 
* In questo modo ci svincoliamo dalla gerachia di Piatto e si semplifica l'interfaccia 
* di PiattoService.
*/
public class PiattoResultList {
    private List<Primo> primi;
    private List<Secondo> secondi;
    private List<Contorno> contorni;
    private List<Dessert> dessert;
    
    PiattoResultList(){
       primi= new ArrayList<Primo>();
       secondi= new ArrayList<Secondo>();
       contorni = new ArrayList<Contorno>();
       dessert = new ArrayList<Dessert>();
    }
    public void setPrimi(List<Primo> inPrimi){
        primi= new ArrayList<Primo>(inPrimi); 

    }
    public void setSecondi(List<Secondo> inSecondi){
        secondi= new ArrayList<Secondo>(inSecondi); 

    }
   public void setContorni(List<Contorno> inContorni){
        contorni= new ArrayList<Contorno>(inContorni); 

   }
   public void setDessert(List<Dessert> inDessert){
        dessert= new ArrayList<Dessert>(inDessert); 

   }
   
    public List<Primo> getPrimi(){
        return primi;
    }
    
    public List<Secondo> getSecondi(){
        return secondi;
    }
    public List<Contorno> getContorni(){
        return contorni;
    }
    public List<Dessert> getDessert(){
        return dessert;
    }
    
}
