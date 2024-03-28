package com.example.appfitness;

import android.view.View;
import android.widget.Toast;

public class Eccezioni extends Exception{
    public enum tipiEccezioni{
        FORMATO_NON_VALIDO,
        DATA_NON_VALIDA
    }

    public Eccezioni(tipiEccezioni tipoEccezione, View v) {
        switch (tipoEccezione){
            case FORMATO_NON_VALIDO:
                Toast.makeText(v.getContext(), "Formato don't valido", Toast.LENGTH_SHORT).show();
                break;
            case DATA_NON_VALIDA:
                Toast.makeText(v.getContext(), "Inserisci una data valida", Toast.LENGTH_SHORT).show();
                break;
        }
        //super("sdad");
    }
}
