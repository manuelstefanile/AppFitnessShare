package com.example.appfitness.Bean;

import com.google.gson.Gson;

import java.util.Calendar;

public class Kcal {
    private int kcal;
    private Fase fase;
    private float carbo,proteine,grassi,sale,acqua;
    private String note;
    private Calendar data;

    public Kcal() {

    }


    public enum Fase{
        MASSA,
        NORMO,
        DEFINIZIONE,
        RICOMPOSIZIONE
    }

    public Kcal(int kcal, Fase fase, float carbo, float proteine, float grassi, float sale, float acqua, String note, Calendar data) {
        this.kcal = kcal;
        this.fase = fase;
        this.carbo = carbo;
        this.proteine = proteine;
        this.grassi = grassi;
        this.sale = sale;
        this.acqua = acqua;
        this.note = note;
        this.data = data;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public float getCarbo() {
        return carbo;
    }

    public void setCarbo(float carbo) {
        this.carbo = carbo;
    }

    public float getProteine() {
        return proteine;
    }

    public void setProteine(float proteine) {
        this.proteine = proteine;
    }

    public float getGrassi() {
        return grassi;
    }

    public void setGrassi(float grassi) {
        this.grassi = grassi;
    }

    public float getSale() {
        return sale;
    }

    public void setSale(float sale) {
        this.sale = sale;
    }

    public float getAcqua() {
        return acqua;
    }

    public void setAcqua(float acqua) {
        this.acqua = acqua;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Kcal fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Kcal.class);
    }

    @Override
    public String toString() {
        return "Kcal{" +
                "kcal=" + kcal +
                ", fase=" + fase +
                ", carbo=" + carbo +
                ", proteine=" + proteine +
                ", grassi=" + grassi +
                ", sale=" + sale +
                ", acqua=" + acqua +
                ", note='" + note + '\'' +
                ", data=" + data +
                '}';
    }
}
