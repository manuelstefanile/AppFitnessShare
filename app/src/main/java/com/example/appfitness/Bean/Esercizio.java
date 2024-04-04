package com.example.appfitness.Bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class Esercizio extends ListeClasseMarker {
    private String nomeEsercizio,tecnica_intensita,esecuzione;
    private byte[] immagineMacchinario;
    private int numeroSerie,numeroRipetizioni;
    private float timer;

    public Esercizio(String nomeEsercizio, String tecnica_intensita, String esecuzione, Drawable immagineMacchinario, int numeroSerie, int numeroRipetizioni, float timer) {
        this.nomeEsercizio = nomeEsercizio;
        this.tecnica_intensita = tecnica_intensita;
        this.esecuzione = esecuzione;
        this.immagineMacchinario = drawableToByteArray(immagineMacchinario);;
        this.numeroSerie = numeroSerie;
        this.numeroRipetizioni = numeroRipetizioni;
        this.timer = timer;
    }

    public Esercizio() {

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
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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

    public int getNumeroRipetizioni() {
        return numeroRipetizioni;
    }

    public void setNumeroRipetizioni(int numeroRipetizioni) {
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

    @Override
    public String toString() {
        return "\nEsercizio{" +
                "nomeEsercizio='" + nomeEsercizio + '\'' +
                ", tecnica_intensita='" + tecnica_intensita + '\'' +
                ", esecuzione='" + esecuzione + '\'' +
                ", immagineMacchinario=" + Arrays.toString(immagineMacchinario) +
                ", numeroSerie=" + numeroSerie +
                ", numeroRipetizioni=" + numeroRipetizioni +
                ", timer=" + timer +
                '}';
    }
}
