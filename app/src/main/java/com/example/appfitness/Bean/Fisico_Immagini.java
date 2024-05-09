package com.example.appfitness.Bean;

import android.widget.ImageButton;

public class Fisico_Immagini {
    private byte[] immagine;
    private String nomePosa;
    private int posizione;
    private ImageButton immagineBRiferimento;

    public Fisico_Immagini(byte[] immagine, String nomePosa, int posizione, ImageButton ib) {
        this.immagine = immagine;
        this.nomePosa = nomePosa;
        this.posizione = posizione;
        this.immagineBRiferimento=ib;
    }
    public Fisico_Immagini(byte[] immagine, String nomePosa, int posizione) {
        this.immagine = immagine;
        this.nomePosa = nomePosa;
        this.posizione = posizione;

    }

    public ImageButton getImmagineBRiferimento() {
        return immagineBRiferimento;
    }

    public void setImmagineBRiferimento(ImageButton immagineBRiferimento) {
        this.immagineBRiferimento = immagineBRiferimento;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public String getNomePosa() {
        return nomePosa;
    }

    public void setNomePosa(String nomePosa) {
        this.nomePosa = nomePosa;
    }

    public int getPosizione() {
        return posizione;
    }

    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }
}
