package com.example.appfitness.Bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Calendar;

public class Peso implements Serializable {
    private long id;
    private float pesoKg;
    private String note;
    private Calendar calendario=Calendar.getInstance();

    public Peso(float pesoKg, Calendar calendario) {
        this.pesoKg = pesoKg;
        this.calendario = calendario;
    }

    public Peso() {

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

    public float getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(float pesoKg) {
        this.pesoKg = pesoKg;
    }

    public Calendar getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendar calendario) {
        this.calendario = calendario;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Peso fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Peso.class);
    }

    @Override
    public String toString() {
        return "Peso{" +
                "id=" + id +
                ", pesoKg=" + pesoKg +
                ", note='" + note + '\'' +
                ", calendario=" + calendario +
                '}';
    }
}
