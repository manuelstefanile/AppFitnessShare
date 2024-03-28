package com.example.appfitness;

import com.google.gson.Gson;

import java.util.Calendar;

public class Peso {
    private float pesoKg;
    private Calendar calendario;

    public Peso(float pesoKg, Calendar calendario) {
        this.pesoKg = pesoKg;
        this.calendario = calendario;
    }

    public Peso() {

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
}
