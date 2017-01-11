
package test_magna_tron.ejb;

/*magan_tron*/
import java.io.File;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import magna_tron.ejb.LoginService;
import magna_tron.ejb.LoginServiceEnumRoles;
import magna_tron.model.Persona;
import magna_tron.model.Impiegato;
import magna_tron.model.Amministratore;
import magna_tron.model.Cuoco;
import magna_tron.ejb.exception.ExceptionLoginService;

/*utils*/

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.UserTransaction;
import javax.inject.Inject;


/*testing*/
/*junit*/

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
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.junit.After;
import org.junit.Before;

@RunWith(Arquillian.class)
public class TestLoginService {
    @EJB
    LoginService loginService;
    
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
	                .create(WebArchive.class, "TestLoginService.war")
                        .addPackages(true,"magna_tron.model")
                        .addPackages(true,"magna_tron.ejb.utils")
                        .addClass(LoginService.class)
                        .addClass(LoginServiceEnumRoles.class)
                        .addClass(ExceptionLoginService.class)
                        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
	                .addAsWebInfResource("META-INF/persistence.xml","classes/META-INF/persistence.xml");
                 
               // archive.as(ZipExporter.class).exportTo(new File("/home/daniele/Desktop/awd/magna_tron/dump/" + archive.getName()), true);
                
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
        
        
        
        
        @Test
        public void TestgetPersona(){
            /*test su loginService.getPersona*/
            String username=cuoco.getUsername();
            String password=cuoco.getPassword();
            
            
            Persona persona;
            boolean failureGetPersona=false;
            try {
                 persona = loginService.getPersona(username,password);
            } catch (ExceptionLoginService ex) {
               failureGetPersona=true;
           }
           assertFalse(failureGetPersona);
           
           boolean failureWrongCredential=false;
           try {
                 persona = loginService.getPersona("","");
            } catch (ExceptionLoginService ex) {
               failureWrongCredential=true;
           }
           assertTrue(failureWrongCredential);
        
        }
        
        @Test
        public void TestGetPersonaTypeCuoco(){
            /*test su loginService.getPersonaType con persona di tipo cuoco*/
            String username=cuoco.getUsername();
            String password=cuoco.getPassword();
            
            
            Persona persona=cuoco;
            
            boolean failurePersonaType=false;
            try {
                LoginServiceEnumRoles ptype;
                ptype = loginService.getPersonaType(persona);
                assertTrue(LoginServiceEnumRoles.CUOCO==ptype);
            } catch (ExceptionLoginService ex) {  
                failurePersonaType=true;
            }
            assertFalse(failurePersonaType); 
            
        }
         @Test
        public void TestGetPersonaTypeAmministratore(){
            /*test su loginService.getPersonaType con persona di tipo amministratore*/
            String username=amministratore.getUsername();
            String password=amministratore.getPassword();
            
            Persona persona= amministratore;
            boolean failure=false;
            
            
            
            boolean failurePersonaType=false;
            try {
                LoginServiceEnumRoles ptype;
                ptype = loginService.getPersonaType(persona);
                assertTrue(LoginServiceEnumRoles.AMMINISTRATORE==ptype);
            } catch (ExceptionLoginService ex) {  
                failurePersonaType=true;
            }
            assertFalse(failurePersonaType);
        }
        @Test
        public void TestGetPersonaTypeImpiegato(){
            /*test su loginService.getPersonaType con persona di tipo impiegato*/
            String username=impiegato.getUsername();
            String password=impiegato.getPassword();
            
            Persona persona= impiegato;
            
            boolean failurePersonaType=false;
            try {
                LoginServiceEnumRoles ptype;
                ptype = loginService.getPersonaType(persona);
                assertTrue(LoginServiceEnumRoles.IMPIEGATO==ptype);
            } catch (ExceptionLoginService ex) {  
                failurePersonaType=true;
            }
            assertFalse(failurePersonaType);
        }
       @Test 
        public void TestGetPersonaRoleCuoco(){
            boolean failureGetPersonaRole=false;
            try {
                
                Persona persona = loginService.getPersonaRole(LoginServiceEnumRoles.CUOCO, cuoco);
                assertTrue(persona instanceof Cuoco);
                
            } catch (ExceptionLoginService ex) {  
                failureGetPersonaRole=true;
            }
            assertFalse(failureGetPersonaRole);
        
        }
        @Test 
        public void TestGetPersonaRoleImpiegato(){
            boolean failureGetPersonaRole=false;
            try {
                
                Persona persona = loginService.getPersonaRole(LoginServiceEnumRoles.IMPIEGATO, impiegato);
                assertTrue(persona instanceof Impiegato);
                
            } catch (ExceptionLoginService ex) {  
                failureGetPersonaRole=true;
            }
            assertFalse(failureGetPersonaRole);
        
        }
        @Test 
        public void TestGetPersonaRoleAmministratore(){
            boolean failureGetPersonaRole=false;
            try {
                
                Persona persona = loginService.getPersonaRole(LoginServiceEnumRoles.AMMINISTRATORE, amministratore);
                assertTrue(persona instanceof Amministratore);
                
            } catch (ExceptionLoginService ex) {  
                failureGetPersonaRole=true;
            }
            assertFalse(failureGetPersonaRole);
        
        }
        @Test 
        public void TestGetPersonaRoleWrong(){
            boolean failureGetPersonaRole=false;
            try {
                
                Persona persona = loginService.getPersonaRole(LoginServiceEnumRoles.CUOCO, amministratore);
                assertTrue(persona instanceof Impiegato);
                
            } catch (ExceptionLoginService ex) {  
                failureGetPersonaRole=true;
            }
            assertTrue(failureGetPersonaRole);
        
        }        
        

}
