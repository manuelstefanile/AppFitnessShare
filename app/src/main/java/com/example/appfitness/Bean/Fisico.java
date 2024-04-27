package com.example.appfitness.Bean;

import java.util.Calendar;
import java.util.HashMap;

public class Fisico {
    private long id;
    private HashMap<String, byte[]> posa_immagine= new HashMap<>();
    private String note;
    private Calendar calendario=Calendar.getInstance();


    public void setPosa_immagine(HashMap<String, byte[]> posa_immagine) {
        this.posa_immagine = posa_immagine;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HashMap<String, byte[]> getPosa_immagine() {
        return posa_immagine;
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
