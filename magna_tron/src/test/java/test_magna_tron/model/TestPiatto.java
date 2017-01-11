package test_magna_tron.model;

/*testing*/
/*junit*/

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;


/*arquillian*/
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
/*shrinkwrap*/
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
/*jpa*/
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.inject.Inject;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class TestPiatto{
          @PersistenceContext(unitName = "magnaTronPersistenceUnit")
	  protected EntityManager em;
          @Inject
          UserTransaction utx;
          
        @Deployment
	public static WebArchive createDeployment() {                
	        final WebArchive archive= ShrinkWrap
	                .create(WebArchive.class, "PiattoTest.war")
                        .addPackages(true,"magna_tron.model")
                        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
	                .addAsWebInfResource("META-INF/persistence.xml","classes/META-INF/persistence.xml");

                 return archive;
                
	}
        
        @Before
        public void before() throws Exception{
              utx.begin();
              em.joinTransaction();
        }
        
        @After
        public void after() throws Exception{
              utx.rollback();
        }
	
	/*testiamo la creazione della gerarchia*/
	@Test
	public void PiattoPrimoHierarchy(){
		/*se non trova Primo genera una eccezione */
                boolean fail=false;
                try{
		String query="SELECT count(p.idPiatto) FROM Primo p";
	        em.createQuery(query, Integer.class).getSingleResult();
                }catch(Exception e){
                    fail=true;
                }
                assertFalse(fail);
	}
	@Test
	public void PiattoSecondoHierarchy(){
		/*se non trova Secondo genera un errore */
                 boolean fail=false;
               try{
		String query="SELECT count(p.idPiatto) FROM Secondo p";
	        em.createQuery(query, Integer.class).getSingleResult();
	       }catch(Exception e){
                    fail=true;
                }
                assertFalse(fail);
	}
	@Test
	public void PiattoContornoHierarchy(){
            /*se non trova Contorno genera un errore */
            boolean fail=false;
            try{
		String query="SELECT count(p.idPiatto) FROM Contorno p";
	        em.createQuery(query, Integer.class).getSingleResult();
	    }catch(Exception e){
                    fail=true;
                }
           assertFalse(fail);  
	}
	@Test
	public void PiattoDessertHierarchy(){
		/*se non trova Dessert genera un errore */
            boolean fail=false;
            try{
		String query="SELECT count(p.idPiatto) FROM Dessert p";
	        em.createQuery(query, Integer.class).getSingleResult();
            }catch(Exception e){
                    fail=true;
                }
           assertFalse(fail);
	    
	}

}