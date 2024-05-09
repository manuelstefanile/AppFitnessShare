package com.example.appfitness.Bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Fisico {
    private long id;
    private ArrayList<Fisico_Immagini> posa_immagine= new ArrayList<>();
    private String note;
    private Calendar calendario=Calendar.getInstance();


    public ArrayList<Fisico_Immagini> getPosa_immagine() {
        return posa_immagine;
    }

    public void setPosa_immagine(ArrayList<Fisico_Immagini> posa_immagine) {
        this.posa_immagine = posa_immagine;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Calendar getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendar calendario) {
        this.calendario = calendario;
    }
}
