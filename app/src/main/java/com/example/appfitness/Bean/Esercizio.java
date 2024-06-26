package com.example.appfitness.Bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class Esercizio extends ListeClasseMarker implements Serializable {
    private long id;

    private String nomeEsercizio,tecnica_intensita,esecuzione,note;
    private byte[] immagineMacchinario;
    private int numeroSerie;
    private String numeroRipetizioni;
    private float timer;
    private String pesoKG;
    private boolean completato=false;
    private int ordine;

    public Long idGiornoAvviaRiferimento;

    public Esercizio(String nomeEsercizio, String tecnica_intensita, String esecuzione, Drawable immagineMacchinario,
                     int numeroSerie, String numeroRipetizioni, float timer,String note,String pesoKG) {
        this.nomeEsercizio = nomeEsercizio;
        this.tecnica_intensita = tecnica_intensita;
        this.esecuzione = esecuzione;
        this.immagineMacchinario = drawableToByteArray(immagineMacchinario);;
        this.numeroSerie = numeroSerie;
        this.numeroRipetizioni = numeroRipetizioni;
        this.timer = timer;
        this.note=note;
        this.pesoKG=pesoKG;
    }

    public Esercizio() {

    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    public void setCompletato(int valore){
        completato=valore==1?true:false;
    }
    public boolean getCompletato(){
        return completato;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeEsercizio() {
        return nomeEsercizio;
    }

    public void setNomeEsercizio(String nomeEsercizio) {
        this.nomeEsercizio = nomeEsercizio;
    }

    public String getTecnica_intensita() {
        return tecnica_intensita;
    }

    public void setTecnica_intensita(String tecnica_intensita) {
        this.tecnica_intensita = tecnica_intensita;
    }

    public String getEsecuzione() {
        return esecuzione;
    }

    public void setEsecuzione(String esecuzione) {
        this.esecuzione = esecuzione;
    }

    public String getPesoKG() {
        return pesoKG;
    }

    public void setPesoKG(String pesoKG) {
        this.pesoKG = pesoKG;
    }

    public Drawable getImmagineMacchinario() {
        return byteArrayToDrawable(immagineMacchinario);
    }

    public void setImmagineMacchinario(Drawable img) {
        this.immagineMacchinario = drawableToByteArray(img);
    }
    private byte[] drawableToByteArray(Drawable drawable) {
        if (drawable == null) return null;
        try{
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }catch (Exception e){
            return null;
        }


    }

    private Drawable byteArrayToDrawable(byte[] byteArray) {
        if (byteArray == null) return null;

        return new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }

    public int getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(int numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNumeroRipetizioni() {
        return numeroRipetizioni;
    }

    public void setNumeroRipetizioni(String numeroRipetizioni) {
        this.numeroRipetizioni = numeroRipetizioni;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Esercizio fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Esercizio.class);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Esercizio{" +
                "id=" + id +
                ", nomeEsercizio='" + nomeEsercizio + '\'' +
                ", tecnica_intensita='" + tecnica_intensita + '\'' +
                ", esecuzione='" + esecuzione + '\'' +
                ", note='" + note + '\'' +
                ", ordine='" + ordine + '\'' +
                ", numeroSerie=" + numeroSerie +
                ", numeroRipetizioni='" + numeroRipetizioni + '\'' +
                ", timer=" + timer +
                ", pesoKG=" + pesoKG +
                ", completato=" + completato +
                '}';
    }
}
