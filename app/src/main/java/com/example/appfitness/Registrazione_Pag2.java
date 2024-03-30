package com.example.appfitness;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Utente;
import com.example.appfitness.Eccezioni.Eccezioni;

public class Registrazione_Pag2 extends Activity {

    Peso pesoSalvato;
    Misure misureSalvato;
    Kcal chiloK;
    Note noteSalvate;
    EditText nomeR,cognomeR,nomeUtenteR,etaR,altezzaR;
    Utente utente;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);

        nomeR=findViewById((int)R.id.nomeRegistrazione);
        cognomeR=findViewById((int)R.id.cognomeRegistrazione);
        nomeUtenteR=findViewById((int)R.id.nomeUtenteRegistrazione);
        etaR=findViewById((int)R.id.etaRegistrazione);
        altezzaR=findViewById((int)R.id.altezzaRegistrazione);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        pesoSalvato=new Peso();
        misureSalvato=new Misure();
        chiloK=new Kcal();
        noteSalvate= new Note();

        editor.putString("pesoPassato", pesoSalvato.toJson());
        editor.putString("misurePassate", misureSalvato.toJson());
        editor.putString("kcalPassate", chiloK.toJson());
        editor.putString("notePassate", noteSalvate.toJson());

        editor.apply();


    }
    public void SalvaAll(View v){
        //controlli e prendo i valori associati all edittext
        String nome=nomeR.getText().toString();
        String cognome=cognomeR.getText().toString();
        String nomeUtente=nomeUtenteR.getText().toString();
        int eta= Integer.parseInt(etaR.getText().toString().trim().length()!=0?etaR.getText().toString():"0");
        float altezza=Float.parseFloat(altezzaR.getText().toString().trim().length()!=0?altezzaR.getText().toString():"0");


        pesoSalvato=Peso.fromJson(sharedPreferences.getString("pesoPassato",null));
        noteSalvate = Note.fromJson(sharedPreferences.getString("notePassate", null));
        misureSalvato = Misure.fromJson(sharedPreferences.getString("misurePassate", null));
        chiloK = Kcal.fromJson(sharedPreferences.getString("kcalPassate", null));

        utente=new Utente(nome,cognome,nomeUtente,eta,altezza,pesoSalvato,misureSalvato,chiloK,noteSalvate);
        Toast.makeText(getApplicationContext(), "Salvato", Toast.LENGTH_SHORT).show();
    }
    public void RipristinaDati(View v){
        if(utente!=null)
            utente.RipristinaDatiUtente();
        pesoSalvato=new Peso();
        misureSalvato=new Misure();
        chiloK=new Kcal();
        noteSalvate= new Note();

        nomeR.setText("");
        cognomeR.setText("");
        nomeUtenteR.setText("");
        etaR.setText("");
        altezzaR.setText("");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pesoPassato", pesoSalvato.toJson());
        editor.putString("misurePassate", misureSalvato.toJson());
        editor.putString("kcalPassate", chiloK.toJson());
        editor.putString("notePassate", noteSalvate.toJson());
        editor.apply();

        Toast.makeText(getApplicationContext(), "Ripristinato con successo", Toast.LENGTH_SHORT).show();


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
                NotificheDialog.NotificaKcal(getLayoutInflater(),sharedPreferences);
                break;
            case "note":
                NotificheDialog.NotificaNote(getLayoutInflater(),sharedPreferences);
                break;
            default:
                break;
        }

    }
}
