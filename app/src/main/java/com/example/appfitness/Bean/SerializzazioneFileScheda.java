package com.example.appfitness.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SerializzazioneFileScheda implements Serializable {
    private Scheda scheda;
    private HashMap<Giorno,ArrayList<Esercizio>> mappa;

    public SerializzazioneFileScheda() {
        super();
    }


    public SerializzazioneFileScheda(Scheda scheda, HashMap<Giorno, ArrayList<Esercizio>> mappa) {
        this.scheda = scheda;
        this.mappa = mappa;
    }

    public Scheda getScheda() {
        return scheda;
    }

    public void setScheda(Scheda scheda) {
        this.scheda = scheda;
    }

    public HashMap<Giorno, ArrayList<Esercizio>> getMappa() {
        return mappa;
    }

    public void setMappa(HashMap<Giorno, ArrayList<Esercizio>> mappa) {
        this.mappa = mappa;
    }

    @Override
    public String toString() {
        return "SerializzazioneFileScheda{" +
                "scheda=" + scheda +
                ", mappa=" + mappa +
                '}';
    }
}
