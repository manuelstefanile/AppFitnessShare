package com.example.appfitness.Bean;

import com.google.gson.Gson;

public class Misure {
    private long id;
    private float braccioDx;
    private float braccioSx;
    private float gambaDx;
    private float gambaSx;
    private float petto;
    private float spalle;
    private float addome;

    public Misure(float braccioDx, float braccioSx, float gambaDx, float gambaSx, float petto, float spalle, float addome) {
        this.braccioDx = braccioDx;
        this.braccioSx = braccioSx;
        this.gambaDx = gambaDx;
        this.gambaSx = gambaSx;
        this.petto = petto;
        this.spalle = spalle;
        this.addome = addome;
    }

    public Misure() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getBraccioDx() {
        return braccioDx;
    }

    public void setBraccioDx(float braccioDx) {
        this.braccioDx = braccioDx;
    }

    public float getBraccioSx() {
        return braccioSx;
    }

    public void setBraccioSx(float braccioSx) {
        this.braccioSx = braccioSx;
    }

    public float getGambaDx() {
        return gambaDx;
    }

    public void setGambaDx(float gambaDx) {
        this.gambaDx = gambaDx;
    }

    public float getGambaSx() {
        return gambaSx;
    }

    public void setGambaSx(float gambaSx) {
        this.gambaSx = gambaSx;
    }

    public float getPetto() {
        return petto;
    }

    public void setPetto(float petto) {
        this.petto = petto;
    }

    public float getSpalle() {
        return spalle;
    }

    public void setSpalle(float spalle) {
        this.spalle = spalle;
    }

    public float getAddome() {
        return addome;
    }

    public void setAddome(float addome) {
        this.addome = addome;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Misure fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Misure.class);
    }

    @Override
    public String toString() {
        return "Misure{" +
                "braccioDx=" + braccioDx +
                ", braccioSx=" + braccioSx +
                ", gambaDx=" + gambaDx +
                ", gambaSx=" + gambaSx +
                ", petto=" + petto +
                ", spalle=" + spalle +
                ", addome=" + addome +
                '}';
    }
}
