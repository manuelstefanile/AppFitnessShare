package com.example.appfitness;

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
}
