package com.example.appfitness.Pagina3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.DB.SchedaDAO;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import kotlin.Suppress;

public class PaginaScheda_Pag3 extends Activity {
    ListView lista;
    PopupSchede popS=new PopupSchede(this);

    @Override
    @SuppressLint({"Range", "WrongThread"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schede_pag3);

        Global.listaGiornidao= new ListaGiorniDAO(getApplicationContext());
        Global.schedadao=new SchedaDAO(getApplicationContext());
        Global.db=new DbHelper(getApplicationContext());
        Global.adapterSchede = new AdapterListaScheda(this, R.layout.item_scheda, new ArrayList<Scheda>());
        Global.giornoDao=new GiornoDAO(getApplicationContext());
        Global.esercizioDao=new EsercizioDAO(getApplicationContext());
        Global.ledao=new ListaEserciziDAO(getApplicationContext());

        //db.deleteDatabase();

        lista = (ListView)findViewById(R.id.listaSchedeView);
        lista.setAdapter(Global.adapterSchede);

        //chiama daoScheda
        ArrayList<Scheda> arrSchede= Global.schedadao.getAllSchede();
        for(Scheda schedaTemp:arrSchede){
            Global.adapterSchede.add(schedaTemp);
        }

        StampaTutto();
    }

    public static void StampaTutto(){
        System.out.println("*********************************************************");

        // Stampare tutti gli Utenti
        //printUserData(db);

        // Stampare tutti i Pesi
        //printPesoData(db);

        // Stampare tutte le Kcal
        //printKcalData(db);

        // Stampare tutte le Misure
        //printMisureData(db);

        // Stampare tutte le Schede
        printSchedaData(Global.db);

        // Stampare tutte le Liste Giorni
        printListaGiorniData(Global.db);

        // Stampare tutti i Giorni

        printGiornoData(Global.db);

        // Stampare tutte le Liste Esercizi
        printListaEserciziData(Global.db);

        // Stampare tutti gli Esercizi
        printEsercizioData(Global.db);
        System.out.println("*********************************************************");

    }
    // Metodo per stampare tutti gli Utenti
    private static void printUserData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.UtenteDB.TABLE_NAME);
        System.out.println("---- Tutti gli Utenti ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutti i Pesi
    private static void printPesoData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.PesoDB.TABLE_NAME);
        System.out.println("---- Tutti i Pesi ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Kcal
    private static void printKcalData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.KcalDB.TABLE_NAME);
        System.out.println("---- Tutte le Kcal ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Misure
    private static void printMisureData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.MisureDB.TABLE_NAME);
        System.out.println("---- Tutte le Misure ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Schede
    public static void printSchedaData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.SchedaDB.TABLE_NAME);
        System.out.println("---- Tutte le Schede ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Liste Giorni
    public static void printListaGiorniData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.ListaGiorniDB.TABLE_NAME);
        System.out.println("---- Tutte le Liste Giorni ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutti i Giorni
    public static void printGiornoData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.GiornoDB.TABLE_NAME);
        System.out.println("---- Tutti i Giorni ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Liste Esercizi
    public static void printListaEserciziData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.ListaEserciziDB.TABLE_NAME);
        System.out.println("---- Tutte le Liste Esercizi ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutti gli Esercizi
    public static void printEsercizioData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.EsercizioDB.TABLE_NAME);
        System.out.println("---- Tutti gli Esercizi ----");
        printCursor(cursor);
    }

    // Metodo per stampare un cursore
    public static void printCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    try{
                        System.out.print(cursor.getColumnName(i) + ": " + cursor.getString(i) + " | ");
                    }catch (Exception e){}

                }
                System.out.println(); // Nuova linea per ogni record

            } while (cursor.moveToNext());
        }
        System.out.println(); // Nuova linea per ogni record
        cursor.close();
    }


    public void OnSchedaClick(View v){
        int position = Integer.parseInt(v.getTag().toString());
        Scheda c = (Scheda) Global.adapterSchede.getItem(position);
        System.out.println(c);
        //apriSchedaSelezionata
        popS.ApriSchedaSelezionata(c,getLayoutInflater());
    }
    public void CreaScheda(View v){

        popS.CreaScheda(getLayoutInflater());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PopupSchede.onActivityResult(requestCode, resultCode, data);
    }

    public void ChiudTuttoNonSalva(View v){
        finish();
        Intent intent = new Intent(this, PaginaScheda_Pag3.class);
        startActivity(intent);
    }


}
