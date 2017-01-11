package test_magna_tron.model;

/*unit testing*/
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;




/*java utils*/
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
/*jpa*/
import javax.persistence.*;
import javax.transaction.UserTransaction;

/*magna_tron classes*/
import magna_tron.model.exception.ExceptionMenuSettimanale;
import magna_tron.model.*;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;


@RunWith(Arquillian.class)
public class TestMenuSettimanale{
    
        @PersistenceContext(unitName = "magnaTronPersistenceUnit")
	protected EntityManager em;
        @Inject
        UserTransaction utx;
        
	protected MenuSettimanale menu_settimanale;
	
	protected List<MenuGiornaliero> menu_giornaliero;
	
	protected List<Primo> primo;
	protected List<Secondo> secondo;
	protected List<Contorno> contorno;
	protected List<Dessert> dessert;
	
	
	  @Deployment
	    public static WebArchive createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "MenuSettimanale-test.war")
	                .addPackages(true,"magna_tron.model")
	                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
	                .addAsWebInfResource("META-INF/persistence.xml","classes/META-INF/persistence.xml");
	        
	    }
	
	
	@Before
	public void initializeTest(){
		/*inizializzazione dati per i test*/
        
		this.menu_settimanale= new MenuSettimanale();
		this.menu_settimanale.setIdMenuSettimanale(1);
		
	    this.menu_giornaliero= new ArrayList<MenuGiornaliero>();
		for(int i=1;i<=6;i++){
			/*creazione nuovo menu giornaliero*/
		    MenuGiornaliero mg= new MenuGiornaliero();
		    /*creazione id menu giornaliero*/
		    MenuGiornalieroPK mg_k=new MenuGiornalieroPK();
		    mg_k.setIdMenuSettimanale(this.menu_settimanale.getIdMenuSettimanale());
		    mg_k.setGiorno(i);
		    mg.setId(mg_k);
		    mg.setMenuSettimanale(menu_settimanale);
                    
		    this.menu_giornaliero.add(mg);
		    
		}  
	    /*creo piatti*/
		this.primo= new ArrayList<Primo>();
		this.secondo= new ArrayList<Secondo>();
		this.contorno= new ArrayList<Contorno>();
		this.dessert= new ArrayList<Dessert>();
		
		for(int i=0 ;i<2;i++){
			this.primo.add(new Primo());
			this.secondo.add(new Secondo());
			this.contorno.add(new Contorno());
			this.dessert.add(new Dessert());
		}
                for(Primo p: this.primo){
                     p.setNome("nome primo");
                     p.setDescrizione("descrizione primo");
                     p.setPrezzo(11);
                }
                for(Secondo s: this.secondo){
                     s.setNome("nome secondo");
                     s.setDescrizione("descrizione secondo");
                     s.setPrezzo(11);
                }
                for(Contorno c: this.contorno){
                     c.setNome("nome primo");
                     c.setDescrizione("descrizione contorno");
                     c.setPrezzo(11);
                }
                for(Dessert d: this.dessert){
                     d.setNome("nome dessert");
                     d.setDescrizione("descrizione dessert");
                     d.setPrezzo(11);
                }
                
                
	   
	}
	
	@Test
	public void TestAttivoCorrect (){
		
		for(MenuGiornaliero mg : this.menu_giornaliero){
			mg.setPrimi(this.primo);
			mg.setSecondi(this.secondo);
			mg.setContorni(this.contorno);
			mg.setDessert(this.dessert);
		}
	    
		this.menu_settimanale.setMenuGiornaliero(this.menu_giornaliero);
		
	    boolean thrown =false; 
	    try {
			this.menu_settimanale.setAttivo(true);
		} catch (ExceptionMenuSettimanale e) {
            
			e.printStackTrace();
		}
	    assertFalse(thrown);
	}
	@Test
	public void TestAttivoMenuGiornalieroLessSix (){
		
		/*rimuovo il primo menu giornaliero*/
		this.menu_giornaliero.remove(0);
		
		for(MenuGiornaliero mg : this.menu_giornaliero){
			mg.setPrimi(this.primo);
			mg.setSecondi(this.secondo);
			mg.setContorni(this.contorno);
			mg.setDessert(this.dessert);
		}
	    
		this.menu_settimanale.setMenuGiornaliero(this.menu_giornaliero);
		
	    boolean thrown =false; 
	    try {
			this.menu_settimanale.setAttivo(true);
		} catch (ExceptionMenuSettimanale e) {
			thrown =true;
		}
	    assertTrue(thrown);
	}
	@Test
	public void TestAttivoMenuGiornalieroNoPortata (){

		/*rimuovo una portata*/ 
		for(MenuGiornaliero mg : this.menu_giornaliero){
			mg.setPrimi(this.primo);
			mg.setSecondi(this.secondo);
			mg.setContorni(new ArrayList<Contorno>());
			mg.setDessert(this.dessert);
		}
	    
		this.menu_settimanale.setMenuGiornaliero(this.menu_giornaliero);
		
	    boolean thrown =false; 
	    try {
			this.menu_settimanale.setAttivo(true);
		} catch (ExceptionMenuSettimanale e) {
			thrown =true;
		}
	    assertTrue(thrown);
	}

}
