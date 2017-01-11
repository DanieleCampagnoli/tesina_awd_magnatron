package test_magna_tron.ejb;

/*testing*/
/*junit*/

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Map;
/*arquillian*/
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
/*shrinkwrap*/
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.After;
import org.junit.Before;

/*jpa*/

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/*ejb*/
import javax.ejb.EJB;
import javax.inject.Inject;


/*magna_tron.model*/
import magna_tron.model.*;

/*magna_tron.ejb*/
import magna_tron.ejb.MenuService;
import magna_tron.ejb.exception.ExceptionMenuService;
import magna_tron.model.exception.ExceptionMenuSettimanale;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;



@RunWith(Arquillian.class)
public class TestMenuService {
    @EJB
    MenuService menuService;
    
    @PersistenceContext(unitName = "magnaTronPersistenceUnit")
     protected EntityManager em;
    
    @Inject
    UserTransaction utx;    
    /*dati del test*/
    Cuoco cuoco;
    MenuSettimanale menuSettimanaleEmpty;
    MenuSettimanale menuSettimanale;
    List<Primo> primi;
    List<Secondo> secondi;
    List<Contorno> contorni;
    List<Dessert> dessert;
    List<MenuGiornaliero> menuGiornalieri;
    Impiegato impiegato;
    List<ScelteMenu> scelte;
    
            @Deployment
	public static WebArchive createDeployment() {                
	        final WebArchive archive= ShrinkWrap
	                .create(WebArchive.class, "TestMenuService.war")
                        .addPackages(true,"magna_tron.model")
                        .addClass(MenuService.class)
                        .addClass(ExceptionMenuService.class)
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
              
              //data del menu settimanale
              LocalDate LocalDateDataInizioMenu = LocalDate.of(2014, Month.DECEMBER, 12);
              Date dataInizioMenu= java.sql.Date.valueOf(LocalDateDataInizioMenu);
              
              //impiegato
              impiegato= new Impiegato();
              impiegato.setNome("impiegato_Test");
              impiegato.setCognome("impiegato_Test_cognome");
              impiegato.setDataNascita(dataNascita);
              impiegato.setPassword("secure_impiegato");
              impiegato.setUsername("secure_impiegato");  
              
              utx.begin();
                  impiegato=em.merge(impiegato);
              utx.commit();
              
              //Cuoco
              cuoco= new Cuoco();
              cuoco.setNome("cuoco1");
              cuoco.setCognome("ccuoco1");
              cuoco.setDataNascita(dataNascita);
              cuoco.setPassword("secure123");
              cuoco.setUsername("secure_cuoco1_ccuoco1");    
              
              utx.begin();
                  cuoco=em.merge(cuoco);
              utx.commit();
              
              //MenuSettimanale
              menuSettimanaleEmpty= new MenuSettimanale();
              menuSettimanaleEmpty.setDataInizio(dataInizioMenu);
              menuSettimanaleEmpty.setCuoco(cuoco);
              
              
              //MenuSettimanale
              menuSettimanale= new MenuSettimanale();
              menuSettimanale.setDataInizio(dataInizioMenu);
              menuSettimanale.setCuoco(cuoco);
              
              //piatti
     
		this.primi= new ArrayList();
		this.secondi= new ArrayList();
		this.contorni= new ArrayList();
		this.dessert= new ArrayList();
		
		for(int i=0 ;i<2;i++){
			this.primi.add(new Primo());
			this.secondi.add(new Secondo());
			this.contorni.add(new Contorno());
			this.dessert.add(new Dessert());
		}
                for(Primo p: this.primi){
                     p.setNome("nome primo");
                     p.setDescrizione("descrizione primo");
                     p.setPrezzo(11);
                }
                for(Secondo s: this.secondi){
                     s.setNome("nome secondo");
                     s.setDescrizione("descrizione secondo");
                     s.setPrezzo(11);
                }
                for(Contorno c: this.contorni){
                     c.setNome("nome primo");
                     c.setDescrizione("descrizione contorno");
                     c.setPrezzo(11);
                }
                for(Dessert d: this.dessert){
                     d.setNome("nome dessert");
                     d.setDescrizione("descrizione dessert");
                     d.setPrezzo(11);
                }
           //memorizzazione delle entità Piatto
              utx.begin();
              
              for(int i=0;i<primi.size();i++){
                     primi.set(i,em.merge(primi.get(i)));
                     
                   }
              for(int i=0;i<secondi.size();i++){
                     secondi.set(i,em.merge(secondi.get(i)));
                     
               }
              for(int i=0;i<contorni.size();i++){
                     contorni.set(i,em.merge(contorni.get(i)));
               }
              for(int i=0;i<dessert.size();i++){
                    dessert.set(i,em.merge(dessert.get(i)));
               }              
              
              utx.commit();   
              
             //menu giornalieri
              		
	    this.menuGiornalieri= new ArrayList();
		for(int i=1;i<=6;i++){
	            /*creazione nuovo menu giornaliero*/
		    MenuGiornaliero mg= new MenuGiornaliero();
                    MenuGiornalieroPK mg_k=new MenuGiornalieroPK();
		    mg_k.setGiorno(i);
		    mg.setId(mg_k);
                    mg.setMenuSettimanale(menuSettimanale);
		    /*creazione id menu giornaliero*/
		    this.menuGiornalieri.add(mg);
		}
            
            //aggiungo piatti ai menu giornalieri
            for(MenuGiornaliero mg: this.menuGiornalieri){
                mg.setPrimi(this.primi);
                mg.setSecondi(this.secondi);
                mg.setContorni(this.contorni);
                mg.setDessert(this.dessert);
            }
            //aggiungo i MenuGiornaliero al MenuSettimanale     
            this.menuSettimanale.setMenuGiornaliero(menuGiornalieri);
            
              //memorizzazione delle entità MenuSettimanale e Cuco
              utx.begin();
                   menuSettimanaleEmpty=em.merge(menuSettimanaleEmpty);
                   menuSettimanale=em.merge(menuSettimanale);
              utx.commit();
              
              //scelte menu
         this.scelte= new ArrayList();
         for(MenuGiornaliero mg : menuSettimanale.getMenuGiornaliero()){     
            
            for(Piatto piatto: mg.getPrimi()){ 
               ScelteMenu scelta= new ScelteMenu();
               scelta.setMenuGiornaliero(mg);
               scelta.setPiatto(piatto);
               scelta.setPersona(impiegato);
               scelte.add(scelta);
            }
              
         }
         utx.begin();
              for(int i=0;i<scelte.size();i++){
                    scelte.set(i,em.merge(scelte.get(i)));
               }
         utx.commit();
         
    }
        
    @After
    public void after() throws Exception{
            /*cancello entità dal db*/
              utx.begin();
                  menuSettimanaleEmpty=em.merge(menuSettimanaleEmpty);
                  menuSettimanale=em.merge(menuSettimanale);
                  cuoco=em.merge(cuoco);
                  impiegato=em.merge(impiegato);
                  
                  em.remove(menuSettimanaleEmpty);
                  em.remove(menuSettimanale);
                  em.remove(cuoco);
                  em.remove(impiegato);
                  
             for(int i=0;i<primi.size();i++){
                     primi.set(i,em.merge(primi.get(i)));
                     em.remove(primi.get(i));
              }
              for(int i=0;i<secondi.size();i++){
                     secondi.set(i,em.merge(secondi.get(i)));
                     em.remove(secondi.get(i));
               }
              for(int i=0;i<contorni.size();i++){
                     contorni.set(i,em.merge(contorni.get(i)));
                     em.remove(contorni.get(i));
               }
              for(int i=0;i<dessert.size();i++){
                    dessert.set(i,em.merge(dessert.get(i)));
                    em.remove(dessert.get(i));
               }
              /* scelte vengono eliminate da menu giornaliero
              for(int i=0;i<scelte.size();i++){
                  scelte.set(i, em.merge(scelte.get(i)));
                  em.remove(scelte.get(i));
              }
              */  
                  
              utx.commit();
    }
    
   //@Test
    public void TestGetMenuSettimanale(){
       Date dataInizio=menuSettimanale.getDataInizio();
       List<MenuSettimanale> result=menuService.getMenuSettimanaleByStartDate(dataInizio);
       
       assertTrue(result.size()>0);
       
    }
    
   //@Test 
    public void TestAddPiatto() throws Exception{ 
            for(int i=0;i<menuGiornalieri.size();i++){
               //aggiungo i primi
                for(Primo p : primi){
                 menuSettimanaleEmpty=menuService.addPiatto(menuSettimanaleEmpty,i, p);    
                }
               //aggiungo i secondi
                for(Secondo p : secondi){
                 menuSettimanaleEmpty=menuService.addPiatto(menuSettimanaleEmpty,i, p);    
                }
               //aggiungo i contorni
                for(Contorno p : contorni){
                menuSettimanaleEmpty=menuService.addPiatto(menuSettimanaleEmpty,i, p);    
                }
                //aggiungo i dessert
                for(Dessert p : dessert){
                 menuSettimanaleEmpty=menuService.addPiatto(menuSettimanaleEmpty,i, p);    
                }
            }
      
  
       
       utx.begin();
        MenuSettimanale dbnewMenuSettimanaleEmpty=em.find(MenuSettimanale.class,menuSettimanaleEmpty.getIdMenuSettimanale());        
       utx.commit();
       List<MenuGiornaliero> listMenuGiornaliero=dbnewMenuSettimanaleEmpty.getMenuGiornaliero();
       MenuGiornaliero dbnewMenuGiornaliero=listMenuGiornaliero.get(0);
       List<Primo> listPrimo=dbnewMenuGiornaliero.getPrimi();

        assertTrue(listPrimo.size()==primi.size());
        assertTrue(listMenuGiornaliero.size()==
                menuGiornalieri.size());
        
        
    }
    
   //@Test 
    public void TestRemovePiattoPrimo() throws Exception{
        for(MenuGiornaliero mg :menuSettimanale.getMenuGiornaliero()){
              for(Primo p : primi){
                menuSettimanale=menuService.removePiatto(menuSettimanale,
                                          mg.getId().getGiorno(), p);
                 
                 
              }
        }      
              utx.begin();
                  MenuSettimanale dbnewMenuSettimanale=em.find(MenuSettimanale.class,menuSettimanale.getIdMenuSettimanale());
              utx.commit();
              
        for(MenuGiornaliero mg : dbnewMenuSettimanale.getMenuGiornaliero()){      
             List<Primo> listPrimo=mg.getPrimi();
             if(listPrimo.isEmpty()){
             assertTrue(listPrimo.isEmpty());
             }
        }
        
    }
    
    //@Test
    public void TestActivateMenuSettimanale(){
       boolean failure=false;
       try{ 
       menuSettimanale=menuService.activateMenuSettimanale(this.menuSettimanale);
       assertTrue(menuSettimanale.getAttivo());
       }catch(ExceptionMenuSettimanale e){
            failure=true;
       }
       assertFalse(failure);
       
    }
    
    //@Test
    public void TestRemovePiattoGetMenuSettimanale() throws Exception{
          for(MenuGiornaliero mg :menuSettimanale.getMenuGiornaliero()){
              for(Primo p : primi){
                  MenuSettimanale delMenuSettimanale= menuService
                  .getMenuSettimanaleById(menuSettimanale.getIdMenuSettimanale());  
                  
                menuService.removePiatto(delMenuSettimanale,
                                          mg.getId().getGiorno(), p);       
              }
          }
          
          MenuSettimanale newMenuSettimanale= menuService
                  .getMenuSettimanaleById(menuSettimanale.getIdMenuSettimanale());
          
          
          for(MenuGiornaliero mg : newMenuSettimanale.getMenuGiornaliero()){      
             List<Primo> listPrimo=mg.getPrimi();
             assertTrue(listPrimo.isEmpty());
          }
          
    
    }
    //@Test
    public void TestScegliMenu() throws ParseException, ExceptionMenuService{
       List<MenuGiornaliero> mg= menuSettimanale.getMenuGiornaliero();
       menuService.scegliMenu(mg.get(0), primi.get(0), impiegato);
    
    
    }
    @Test 
    public void TestScelteMenu() throws ParseException{ 
       Date startDataInizio= menuSettimanale.getDataInizio();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Calendar c = Calendar.getInstance();
       c.setTime(sdf.parse( sdf.format(startDataInizio)));
       c.add(Calendar.DATE, 1);  // number of days to add
       Date endDataInizio = c.getTime(); 
       
      List<ScelteMenu> result= menuService.getScelte(startDataInizio,endDataInizio);
      assertTrue(result.size()>0);
      
    }
    
    
    @Test 
    public void TestScelteMenuPiatti() throws ParseException{ 
       Date startDataInizio= menuSettimanale.getDataInizio();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Calendar c = Calendar.getInstance();
       c.setTime(sdf.parse( sdf.format(startDataInizio)));
       c.add(Calendar.DATE, 1);  // number of days to add
       Date endDataInizio = c.getTime(); 
       
      Map<Piatto,Integer> result= menuService.getSceltePiatti(startDataInizio,endDataInizio);
      assertTrue(result.size()>0);
      
    }
    
    @Test 
    public void TestSpesaImpiegato() throws ParseException{ 
       Date startDataInizio= menuSettimanale.getDataInizio();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Calendar c = Calendar.getInstance();
       c.setTime(sdf.parse( sdf.format(startDataInizio)));
       c.add(Calendar.DATE, 1);  // number of days to add
       Date endDataInizio = c.getTime(); 
       
      Map<Persona,Float> result= menuService.getSpesaImpiegato(startDataInizio,endDataInizio);
      assertTrue(result.size()>0);
      
    }
    
    

}
