package com.example.appfitness;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Registrazione_Pag2 extends Activity {

    Peso pesoSalvato;
    Misure misureSalvato;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);


    }
    public void FunzioniInserisciRegistrazioni(View v) throws Eccezioni {
        String tag= (String) v.getTag();
        System.out.println("sono in funzione "+tag);
        switch (tag){
            case "peso":
                NotificheDialog.NotificaPeso(getLayoutInflater(),pesoSalvato);
                break;
            case "misure":
                NotificheDialog.NotificaMisure(getLayoutInflater());
                break;
            case "kcal":
                break;
            case "note":
                break;
            default:
                break;
        }

    }
}
