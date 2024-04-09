package com.example.appfitness;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Utente;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.MisureDAO;
import com.example.appfitness.DB.PesoDAO;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.DB.UtenteDAO;
import com.example.appfitness.DB.kcalDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;

import java.sql.Date;

public class Registrazione_Pag2 extends Activity {

    Peso pesoSalvato;
    Misure misureSalvato;
    Kcal chiloK;
    Note noteSalvate;
    EditText nomeR,cognomeR,nomeUtenteR,etaR,altezzaR;
    Utente utente;
    SharedPreferences sharedPreferences;
    DbHelper db;
    Button bottoneNext;

    public static PesoDAO pesodao;
    public static MisureDAO misuradao;
    public static UtenteDAO utentedao;
    public static com.example.appfitness.DB.kcalDAO kcalDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);

        pesodao=new PesoDAO(getApplicationContext());
        misuradao=new MisureDAO(getApplicationContext());
        kcalDAO=new kcalDAO(getApplicationContext());
        utentedao=new UtenteDAO(getApplicationContext());

        bottoneNext=findViewById((int)R.id.bottoneNextRegistrazione);
        bottoneNext.setEnabled(false);
        //apreo il db
        db=new DbHelper(getApplicationContext());

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

        String modalita=getIntent().getStringExtra("mode");
        if (modalita!=null)
        {
            switch (modalita) {
                case "edit":
                    ModalitaEdit();
                    break;
            }
        }


    }
    @SuppressLint("Range")
    public void SalvaAll(View v){

        //elimina il salvataggio precendente se presente.
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        dbWritable.delete(SchemaDB.PesoDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.KcalDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.MisureDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.UtenteDB.TABLE_NAME, null, null);


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

        ContentValues valuesPeso = new ContentValues();
        valuesPeso.put(SchemaDB.PesoDB.COLUMN_pesoKg, pesoSalvato.getPesoKg());
        valuesPeso.put(SchemaDB.PesoDB.COLUMN_calendario, pesoSalvato.getCalendario().getTimeInMillis());
        long PesoID = dbWritable.insert(SchemaDB.PesoDB.TABLE_NAME, null, valuesPeso);

        ContentValues valuesMisura = new ContentValues();
        valuesMisura.put(SchemaDB.MisureDB.COLUMN_braccioDX, misureSalvato.getBraccioDx());
        valuesMisura.put(SchemaDB.MisureDB.COLUMN_braccioSX, misureSalvato.getBraccioSx());
        valuesMisura.put(SchemaDB.MisureDB.COLUMN_gambaDX, misureSalvato.getGambaDx());
        valuesMisura.put(SchemaDB.MisureDB.COLUMN_gambaSX, misureSalvato.getGambaSx());
        valuesMisura.put(SchemaDB.MisureDB.COLUMN_addome, misureSalvato.getAddome());
        valuesMisura.put(SchemaDB.MisureDB.COLUMN_petto, misureSalvato.getPetto());
        valuesMisura.put(SchemaDB.MisureDB.COLUMN_spalle, misureSalvato.getSpalle());
        System.out.println("___________________"+valuesMisura);
        long MisuraID = dbWritable.insert(SchemaDB.MisureDB.TABLE_NAME, null,valuesMisura);

        ContentValues valuesKcal = new ContentValues();
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_kcal, chiloK.getKcal());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_acqua, chiloK.getAcqua());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_carboidrati, chiloK.getCarbo());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_grassi, chiloK.getGrassi());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_note, chiloK.getNote());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_proteine, chiloK.getProteine());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_sale, chiloK.getSale());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_fase, chiloK.getFase().toString());
        valuesKcal.put(SchemaDB.KcalDB.COLUMN_calendario, chiloK.getData().getTimeInMillis());
        long KcalID = dbWritable.insert(SchemaDB.KcalDB.TABLE_NAME, null, valuesKcal);

        ContentValues valuesUtente = new ContentValues();
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_nome, utente.getNome());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_cognome, utente.getCognome());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_nomeUtente, utente.getNomeUtente());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_eta, utente.getEta());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_altezza, utente.getAltezza());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_IdPeso, PesoID);
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_IdKcal, KcalID);
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_IdMisure, MisuraID);
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_note, utente.getNote().getNote());
        long IdUtente = dbWritable.insert(SchemaDB.UtenteDB.TABLE_NAME, null, valuesUtente);

        Toast.makeText(getApplicationContext(), "Salvato", Toast.LENGTH_SHORT).show();
        bottoneNext.setEnabled(true);
        bottoneNext.setBackgroundColor((int)R.color.azzurrino);
        dbWritable.close();

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

        SQLiteDatabase dbWritable = db.getWritableDatabase();
        dbWritable.delete(SchemaDB.PesoDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.KcalDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.MisureDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.UtenteDB.TABLE_NAME, null, null);

        dbWritable.close();
        bottoneNext.setEnabled(false);
        bottoneNext.setBackgroundColor((int)R.color.grigio);

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
                NotificheDialog.NotificaNote(getLayoutInflater(),sharedPreferences,true);
                break;
            default:
                break;
        }

    }
    public void PassaPagina3(View v){
        db.close();
        Intent i =new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setClass(getApplicationContext(), PaginaScheda_Pag3.class);
        startActivity(i);
        finish();
    }
    private void ModalitaEdit(){
        Utente u=utentedao.getUtenteInfo();
        System.out.println("*** "+u);
        nomeR.setText(u.getNome());
        cognomeR.setText(u.getCognome());
        nomeUtenteR.setText(u.getNomeUtente());
        etaR.setText(String.valueOf(u.getEta()));
        altezzaR.setText(String.valueOf(u.getAltezza()));

        pesoSalvato=u.getPeso();
        misureSalvato=u.getMisure();
        chiloK=u.getKcal();
        noteSalvate=u.getNote();

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pesoPassato", pesoSalvato.toJson());
        editor.putString("misurePassate", misureSalvato.toJson());
        editor.putString("kcalPassate", chiloK.toJson());
        editor.putString("notePassate", noteSalvate.toJson());
        editor.apply();

    }
}
