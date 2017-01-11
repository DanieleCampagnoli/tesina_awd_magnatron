package test_magna_tron.model;

/*testing*/
/*junit*/

import java.util.Calendar;
import java.util.Date;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertTrue;

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

/*magna_trom*/
import magna_tron.model.Amministratore;
import magna_tron.model.Cuoco;
import magna_tron.model.Impiegato;

@RunWith(Arquillian.class)
public class TestPersona {
        @PersistenceContext(unitName = "magnaTronPersistenceUnit")
	protected EntityManager em;
        @Inject
        UserTransaction utx;
       
        /*dati del test*/
        Impiegato impiegato;
        Amministratore amministratore;
        Cuoco cuoco;
        
        
        @Deployment
	public static WebArchive createDeployment() {                
	        final WebArchive archive= ShrinkWrap
	                .create(WebArchive.class, "PersonaTest.war")
                        .addPackages(true,"magna_tron.model")
                        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
	                .addAsWebInfResource("META-INF/persistence.xml","classes/META-INF/persistence.xml");

                 return archive;
                
	    }
                
        @Before
        public void before() throws Exception{
              /*creazione dei dati del test*/
              //data di nascita             
              Calendar date = Calendar.getInstance();
              date.set(Calendar.YEAR, 1990);
              date.set(Calendar.MONTH,10);
              date.set(Calendar.DAY_OF_MONTH, 10);
              Date dataNascita= date.getTime();
              //impiegato
              impiegato= new Impiegato();
              impiegato.setNome("imp1");
              impiegato.setCognome("cimp1");
              impiegato.setDataNascita(dataNascita);
              impiegato.setPassword("secure123");
              impiegato.setUsername("secure_imp1_cimp1");
              
              //amministratore
              amministratore= new Amministratore();
              amministratore.setNome("amm1");
              amministratore.setCognome("camm1");
              amministratore.setDataNascita(dataNascita);
              amministratore.setPassword("secure123");
              amministratore.setUsername("secure_amm1_camm1");
              
              //Cuoco
              cuoco= new Cuoco();
              cuoco.setNome("cuoco1");
              cuoco.setCognome("ccuoco1");
              cuoco.setDataNascita(dataNascita);
              cuoco.setPassword("secure123");
              cuoco.setUsername("secure_cuoco1_ccuoco1");              
              
            //memorizzazione delle entità
              utx.begin();
                  
                   impiegato=em.merge(impiegato);
                   amministratore=em.merge(amministratore);
                   cuoco=em.merge(cuoco);
                   
              utx.commit();
        }
        
        @After
        public void after() throws Exception{
              /*cancello entità dal db*/
              utx.begin();
                  impiegato=em.merge(impiegato);
                  amministratore=em.merge(amministratore);
                  cuoco=em.merge(cuoco);
                  em.remove(impiegato);
                  em.remove(amministratore);
                  em.remove(cuoco);
              utx.commit();
        }
        
	/*testiamo la creazione della gerarchia*/
	@Test
	public void PersonaImpiegatoHierarchy(){
		
	        String query="SELECT COUNT(p.matricola) FROM Impiegato p";
	        long id_count= em.createQuery(query, Long.class).getSingleResult();
                assertTrue(id_count>0);
                
	}
	@Test
	public void PersonaCuocoHierarchy(){
		
		String query="SELECT count(p.matricola) FROM Cuoco p";
	        long id_count=em.createQuery(query, Long.class).getSingleResult();
                assertTrue(id_count>0);
	    
	}
	@Test
	public void PersonaAmministratoreHierarchy(){
		
		String query="SELECT count(p.matricola) FROM Amministratore p";
	        long id_count=em.createQuery(query, Long.class).getSingleResult();
                assertTrue(id_count>0);
	    
	}
	@Test
	public void PersonaHierarchy(){
	        
               String query="SELECT count(p.matricola) FROM Persona p";
	       long id_count=em.createQuery(query, Long.class).getSingleResult();
               assertTrue(id_count>0);
	    
	}

}
