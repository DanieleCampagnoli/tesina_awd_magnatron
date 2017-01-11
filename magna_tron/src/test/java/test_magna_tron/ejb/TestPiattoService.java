package test_magna_tron.ejb;



/*testing*/
/*junit*/

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import magna_tron.ejb.LoginService;
import magna_tron.ejb.LoginServiceEnumRoles;
import magna_tron.ejb.exception.ExceptionLoginService;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/*arquillian*/
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
/*shrinkwrap*/
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.After;
import org.junit.Before;

/*magna_tron*/
/*magna_tron.ejb*/
import magna_tron.ejb.PiattoService;
import magna_tron.ejb.exception.ExceptionPiattoService;
import magna_tron.ejb.PiattoResultList;

        
        
import magna_tron.model.Primo;
import magna_tron.model.Secondo;
import magna_tron.model.Contorno;
import magna_tron.model.Dessert;

/*test di PiattoService*/
@RunWith(Arquillian.class)
public class TestPiattoService {

    @EJB
    PiattoService piattoService;
    
    @PersistenceContext(unitName = "magnaTronPersistenceUnit")
     protected EntityManager em;
    
    @Inject
    UserTransaction utx;    
    
    /*dati del test*/
    Primo primo;
    Secondo secondo;
    Contorno contorno;
    Dessert dessert;
    
    
        @Deployment
	public static WebArchive createDeployment() {                
	        final WebArchive archive= ShrinkWrap
	                .create(WebArchive.class, "TestPiattoService.war")
                        .addPackages(true,"magna_tron.model")
                        .addPackages(true,"magna_tron.ejb.utils")
                        .addClass(PiattoService.class)
                        .addClass(ExceptionPiattoService.class)
                        .addClass(PiattoResultList.class)
                        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
	                .addAsWebInfResource("META-INF/persistence.xml","classes/META-INF/persistence.xml");

                 return archive;
	    }
        
        @Before
        public void before() throws Exception{
               primo= new Primo();
               primo.setDescrizione("descrizione primo test");
               primo.setNome("nome primo test");
               primo.setPrezzo(12);
               
               secondo= new Secondo();
               secondo.setDescrizione("descrizione secondo test");
               secondo.setNome("nome secondo test");
               secondo.setPrezzo(12);
               
               contorno= new Contorno();
               contorno.setDescrizione("descrizione secondo test");
               contorno.setNome("nome secondo test");
               contorno.setPrezzo(12);
               
               dessert= new Dessert();
               dessert.setDescrizione("descrizione dessert test");
               dessert.setNome("nome dessert test");
               dessert.setPrezzo(12);
               
               
        //memorizzo i piatti sul database   
        utx.begin();
            primo=em.merge(primo);
            secondo=em.merge(secondo);
            contorno=em.merge(contorno);
            dessert=em.merge(dessert);
        utx.commit();
               
        }
        @After
        public void after() throws Exception{
        //rimuovo i piatti dal database
        
        utx.begin();
            primo=em.merge(primo);
            secondo=em.merge(secondo);
            contorno=em.merge(contorno);
            dessert=em.merge(dessert);
            
            em.remove(primo);
            em.remove(secondo);
            em.remove(contorno);
            em.remove(dessert);
        utx.commit();
        
        }
        
        
        @Test
        public void TestCreatePiattoPrimo() throws Exception{
         boolean failure=false;    
        try {
            primo=piattoService.createPiatto(primo);
        } catch (ExceptionPiattoService ex) {
           failure=true;
        }
        assertFalse(failure);
        assertFalse(primo.getIdPiatto()==0);
        
        //rimuovo il piatto dal database
        
        utx.begin();
            primo=em.merge(primo);
            em.remove(primo);
        utx.commit();
        
        
        }
        
        @Test
        public void TestGetPiattoByNome() throws Exception{
        /*controllo che il metodo GetPiattoByNome restituisca tutti i piatti che contengono nel nome
        * la stringa di input
        */ 
            
       
            
        
        PiattoResultList result =piattoService.getPiattoByNome(primo.getNome().substring(0,4));
        
        //ogni elemento di result deve avere almeno un elemento
        
        assertTrue(result.getPrimi().size()>0);
        assertTrue(result.getSecondi().size()>0);
        assertTrue(result.getContorni().size()>0);
        assertTrue(result.getDessert().size()>0);

        }
        
}
