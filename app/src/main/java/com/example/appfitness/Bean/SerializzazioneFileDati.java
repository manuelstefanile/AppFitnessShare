package com.example.appfitness.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SerializzazioneFileDati implements Serializable {
    private Utente utente;
    private ArrayList<Fisico> fisico;
    private ArrayList<Kcal> kcal;
    private ArrayList<Misure> misure;
    private ArrayList<Peso> peso;


    public SerializzazioneFileDati() {
        super();
    }

    public SerializzazioneFileDati(Utente utente, ArrayList<Fisico> fisico, ArrayList<Kcal> kcal, ArrayList<Misure> misure, ArrayList<Peso> peso) {
        this.utente = utente;
        this.fisico = fisico;
        this.kcal = kcal;
        this.misure = misure;
        this.peso = peso;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public ArrayList<Fisico> getFisico() {
        return fisico;
    }

    public void setFisico(ArrayList<Fisico> fisico) {
        this.fisico = fisico;
    }

    public ArrayList<Kcal> getKcal() {
        return kcal;
    }

    public void setKcal(ArrayList<Kcal> kcal) {
        this.kcal = kcal;
    }

    public ArrayList<Misure> getMisure() {
        return misure;
    }

    public void setMisure(ArrayList<Misure> misure) {
        this.misure = misure;
    }

    public ArrayList<Peso> getPeso() {
        return peso;
    }

    public void setPeso(ArrayList<Peso> peso) {
        this.peso = peso;
    }
}
