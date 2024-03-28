package com.example.appfitness;

public class Misure {
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
}
