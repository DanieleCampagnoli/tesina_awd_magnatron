
package magna_tron.ejb;

/*ejb*/
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
/*jpa*/
import javax.persistence.*;
import javax.persistence.Query;

/*magna_tron*/
/*magna_tron.model*/
import magna_tron.model.Piatto;
import magna_tron.model.Primo;
import magna_tron.model.Secondo;
import magna_tron.model.Contorno;
import magna_tron.model.Dessert;
/*magna_tron.ejb*/
import magna_tron.ejb.exception.ExceptionPiattoService;
import magna_tron.ejb.PiattoServiceEnum;
import magna_tron.ejb.utils.ClassNameParser;
import magna_tron.ejb.PiattoResultList;
/**
 * gestione dei piatti erogati dalla mensa
 */
@Stateless
public class PiattoService {
	  @PersistenceContext(unitName = "magnaTronPersistenceUnit")
	  protected EntityManager em;
          /**
           * creazione di una entità della gerarchia Piatto
           * @param piatto
           * @return Piatto creato
           * @throws ExceptionPiattoService 
           */
          public <T extends Piatto>  T createPiatto(T piatto) throws ExceptionPiattoService{
               
               if(piatto.getDescrizione()==null){
                  throw new ExceptionPiattoService("descrizione piatto nulla");
               }
               if(piatto.getNome()==null){
                  throw new ExceptionPiattoService("nome piatto nullo");
               }
               if(piatto.getPrezzo()==0){
                throw new ExceptionPiattoService("prezzo del piatto è zero");
               }
               
               piatto=em.merge(piatto);
               
               return piatto;
          }
          /**
           * 
           * @param nome nome del piatto
           * @param descrizione descrizione del piatto
           * @param prezzo costo del piatto
           * @param portata portata di cui deve far parte il Piatto
           * @return Piatto creato
           * @throws ExceptionPiattoService errori di creazione del Piatto
           */
          public <T extends Piatto>  T createPiatto(String nome,
                                                       String descrizione,
                                                       float prezzo,
                                                       PiattoServiceEnum portata) throws ExceptionPiattoService{
              switch (portata) {
                  case PRIMO:
                    Primo piatto = new Primo();
                    piatto.setDescrizione(descrizione);
                    piatto.setNome(nome);
                    piatto.setPrezzo(prezzo);
                    return  (T) createPiatto(piatto);
                  case SECONDO:
                    Secondo secondo = new Secondo();
                    secondo.setDescrizione(descrizione);
                    secondo.setNome(nome);
                    secondo.setPrezzo(prezzo);
                    return  (T) createPiatto(secondo);
                  case CONTORNO:
                    Contorno contorno = new Contorno();
                    contorno.setDescrizione(descrizione);
                    contorno.setNome(nome);
                    contorno.setPrezzo(prezzo);
                    return  (T) createPiatto(contorno);
                  case DESSERT:
                    Dessert dessert = new Dessert();
                    dessert.setDescrizione(descrizione);
                    dessert.setNome(nome);
                    dessert.setPrezzo(prezzo);
                    return  (T) createPiatto(dessert);                  
                  default:
                        break;
              }
              throw new ExceptionPiattoService("il parametro portata ha un valore non supportato");
              
          }
          
          
          /**
           * query che restituisce tutti i piatti che contengono nel nome il parametro nome e sono di tipo classType
           * @param classType
           * @param nome nome da ricercare
           * @return lista dei piatti selezionati
           */
          protected Query getPiattoByNomeQuery(Class classType,String nome){
                 
                String name=classType.getName();
                
                String className=ClassNameParser.splitClassName(name);
                 
                return  em.createNamedQuery(className
                                             +".byNome",classType)
				    .setParameter("nome", nome);
          }
          /**
           * restituisce tutti i piatti che contengono nel nome il parametro nome
           * e sono di tipo classType
           * @param inClass
           * @param nome
           * @return 
           */
          protected <T extends Piatto> List<T> fetchPiattoByNome(Class<T> inClass,String nome){
               /*lista dei piatti di tipo T che ha un certo nome*/  
               try{
                     return getPiattoByNomeQuery(inClass,nome).getResultList();
                     
                }catch(javax.persistence.NoResultException e){
                    //ritorna una lista vuota
                     return new ArrayList<T>();
                }
          }
          /**
           * restituisce tutti i piatti che contengono nel nome il parametro nome 
           * @param nome
           * @return 
           */       
          public PiattoResultList getPiattoByNome(String nome){    
              
              PiattoResultList result = new PiattoResultList();
              
              result.setPrimi(fetchPiattoByNome(Primo.class,nome));
              result.setSecondi(fetchPiattoByNome(Secondo.class,nome));
              result.setContorni(fetchPiattoByNome(Contorno.class,nome));
              result.setDessert(fetchPiattoByNome(Dessert.class,nome));
              
              return result;
          
          }
          
          
          /**
           * restituisce tutti i piatti che contengono nel nome il parametro nome 
           * @param nome
           * @param portata
           * @return
           * @throws ExceptionPiattoService 
           */
          
          public  List getPiattoByNome(String nome,PiattoServiceEnum portata) throws ExceptionPiattoService{
              switch (portata) {
                  case PRIMO:     
                    return  fetchPiattoByNome(Primo.class,nome);
                  case SECONDO:
                    return  fetchPiattoByNome(Secondo.class,nome);
                  case CONTORNO:
                    return  fetchPiattoByNome(Contorno.class,nome);
                  case DESSERT:
                    return  fetchPiattoByNome(Dessert.class,nome);
                  default:
                        break;
              }
              throw new ExceptionPiattoService("il parametro portata ha un valore non supportato");
              
          }
          
}
