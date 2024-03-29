package com.example.appfitness;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class Registrazione_Pag2 extends Activity {

    Peso pesoSalvato;
    Misure misureSalvato;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        pesoSalvato=new Peso();
        misureSalvato=new Misure();

        editor.putString("pesoPassato", pesoSalvato.toJson());
        editor.putString("misurePassate", misureSalvato.toJson());

        editor.apply();


    }
    public void FunzioniInserisciRegistrazioni(View v) throws Eccezioni {
        String tag= (String) v.getTag();
        System.out.println("sono in funzione "+tag);
        switch (tag){
            case "peso":
                NotificheDialog.NotificaPeso(getLayoutInflater(),sharedPreferences);
                break;
            case "misure":
                NotificheDialog.NotificaMisure(getLayoutInflater(),sharedPreferences);
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
