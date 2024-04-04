package com.example.appfitness.Bean;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Giorno extends ListeClasseMarker {
    private String nomeGiorno;
    private ArrayList<Esercizio> listaEsercizi=new ArrayList<>();

    public Giorno(String nomeGiorno, ArrayList<Esercizio> listaEsercizi) {
        this.nomeGiorno = nomeGiorno;
        this.listaEsercizi = listaEsercizi;
    }

    public Giorno() {

    }

    public String getNomeGiorno() {
        return nomeGiorno;
    }

    public void setNomeGiorno(String nomeGiorno) {
        this.nomeGiorno = nomeGiorno;
    }

    public ArrayList<Esercizio> getListaEsercizi() {
        return listaEsercizi;
    }

    public void setListaEsercizi(ArrayList<Esercizio> listaEsercizi) {
        this.listaEsercizi = listaEsercizi;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Giorno fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Giorno.class);
    }

    @Override
    public String toString() {
        return "\nGiorno{" +
                "nomeGiorno='" + nomeGiorno + '\'' +
                ", listaEsercizi=" + listaEsercizi +
                '}';
    }
}
