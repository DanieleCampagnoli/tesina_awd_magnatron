/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magna_tron.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import magna_tron.ejb.PiattoService;
import magna_tron.ejb.PiattoServiceEnum;

import magna_tron.ejb.exception.ExceptionPiattoService;
import magna_tron.model.Piatto;
/**
 * Controller per la gestione dei Piatti 
 * @author daniele
 */
@Named
@RequestScoped
public class Piatti {
    @EJB
    PiattoService piattoService;
    
    protected PiattoServiceEnum portataPiatto;
    protected String nomePiatto;
    protected List resultPiatto; 
   //dati nuovo piatto
    protected String newNomePiatto;
    protected float newPrezzo;
    protected String newDescrizione;
    protected PiattoServiceEnum newPortataPiatto;
    
    public void setNewNomePiatto(String newNomePiatto) {
        this.newNomePiatto = newNomePiatto;
    }

    public void setNewPrezzo(float newPrezzo) {
        this.newPrezzo = newPrezzo;
    }

    public void setNewDescrizione(String newDescrizione) {
        this.newDescrizione = newDescrizione;
    }

    public void setNewPortataPiatto(PiattoServiceEnum newPortataPiatto) {
        this.newPortataPiatto = newPortataPiatto;
    }

    public String getNewNomePiatto() {
        return newNomePiatto;
    }

    public float getNewPrezzo() {
        return newPrezzo;
    }

    public String getNewDescrizione() {
        return newDescrizione;
    }

    
    public PiattoServiceEnum getNewPortataPiatto() {
        return newPortataPiatto;
    }

    
   @PostConstruct
   public void before(){
       portataPiatto=PiattoServiceEnum.PRIMO;
       nomePiatto="";
    
    }
   
    
    public PiattoServiceEnum getPortataPiatto() {
        return portataPiatto;
    }

    public String getNomePiatto() {
        return nomePiatto;
    }

    public void setPortataPiatto(PiattoServiceEnum portataPiatto) throws ExceptionPiattoService {
        this.portataPiatto = portataPiatto;
        getListaPiatti();
    }

    public void setNomePiatto(String nomePiatto) {
        this.nomePiatto = nomePiatto;
    }
    
    public List /*<T extends Piatto> List<T>*/  getListaPiatti() throws ExceptionPiattoService{
          resultPiatto=piattoService.getPiattoByNome(nomePiatto,portataPiatto);
          return resultPiatto;
    }
    
    public PiattoServiceEnum[] getPortata(){
          return PiattoServiceEnum.values();
    }
    
    public void createPiatto() throws ExceptionPiattoService{
         piattoService.createPiatto(newNomePiatto,
                                    newDescrizione,
                                    newPrezzo,
                                    newPortataPiatto);
         this.setNomePiatto(newNomePiatto);
         this.setPortataPiatto(newPortataPiatto);
    }
    
}
