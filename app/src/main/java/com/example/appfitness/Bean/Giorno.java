package com.example.appfitness.Bean;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Giorno extends ListeClasseMarker {
    private long id;
    private String nomeGiorno,note;
    private ArrayList<Long> listaIdEsercizi=new ArrayList<>();

    public Giorno(String nomeGiorno, ArrayList<Long> listaEsercizi) {
        this.nomeGiorno = nomeGiorno;
        this.listaIdEsercizi = listaEsercizi;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Giorno() {

    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNomeGiorno() {
        return nomeGiorno;
    }

    public void setNomeGiorno(String nomeGiorno) {
        this.nomeGiorno = nomeGiorno;
    }

    public ArrayList<Long> getListaEsercizi() {
        return listaIdEsercizi;
    }

    public void setListaEsercizi(ArrayList<Long> listaEsercizi) {
        this.listaIdEsercizi = listaEsercizi;
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
        return "Giorno{" +
                "id=" + id +
                ", nomeGiorno='" + nomeGiorno + '\'' +
                ", note='" + note + '\'' +
                ", listaIdEsercizi=" + listaIdEsercizi +
                '}';
    }
}
