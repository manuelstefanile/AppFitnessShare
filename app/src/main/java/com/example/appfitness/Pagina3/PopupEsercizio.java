package com.example.appfitness.Pagina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.database.SQLException;

import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.ExtendedSSLSession;

public class PopupEsercizio {

    public static ArrayList<Long> idEserciziSalvati= new ArrayList<>();

    public static ImageButton immagineEsercizio;




    public static void CreaEsercizio(LayoutInflater inflater, Giorno giornoNuovo){


        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //note all inizio della creazione dell ex è vuoto
        SharedPreferences.Editor edit=shp.edit();
        edit.putString("notePassate",new Note().toJson());
        edit.commit();

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_esercizio, null);
        Button salvaButton=dialogView.findViewById((int)R.id.salvaButton);
        Button okButton=dialogView.findViewById((int)R.id.okButton);

        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


        EditText nomeEsercizio = dialogView.findViewById((int) R.id.nomeEsercizio);
        immagineEsercizio = dialogView.findViewById((int) R.id.immagineEsercizio);
        EditText numeroSerieEsercizio = dialogView.findViewById((int) R.id.numeroSerie);
        EditText numeroRipetEsercizio = dialogView.findViewById((int) R.id.numeroRipetizioni);
        EditText numeroTimetEsercizio = dialogView.findViewById((int) R.id.numeroTimer);
        EditText intensitaEsercizio = dialogView.findViewById((int) R.id.tecnicaIntensita);
        EditText esecuzioneEsercizio = dialogView.findViewById((int) R.id.esecuzione);
        Button salva=dialogView.findViewById((int)R.id.salvaEsercizio);
        Button ripristina=dialogView.findViewById((int)R.id.ripristinaEsercizio);
        Button back=dialogView.findViewById((int)R.id.backEsercizio);
        Button bottoneNote = dialogView.findViewById(R.id.bottoneNote);


        bottoneNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("noteClick");
                try {
                    NotificheDialog.NotificaNote(inflater, shp,true);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });


        immagineEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("PREMON ");
                PopupEsercizio.selectImageFromGallery();
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendo le note
                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString("notePassate", null));

                Esercizio esercizio=new Esercizio(nomeEsercizio.getText().toString().trim().length()>0?nomeEsercizio.getText().toString().trim():"",
                        intensitaEsercizio.getText().toString(),
                        esecuzioneEsercizio.getText().toString(),immagineEsercizio.getDrawable(),
                        Integer.parseInt(numeroSerieEsercizio.getText().toString().trim().length()!=0?numeroSerieEsercizio.getText().toString():"0"),
                        Integer.parseInt(numeroRipetEsercizio.getText().toString().trim().length()!=0?numeroRipetEsercizio.getText().toString():"0"),
                        Float.parseFloat(numeroTimetEsercizio.getText().toString().trim().length()!=0?numeroTimetEsercizio.getText().toString():"0"),
                        note.getNote()

                );
                //inserisco il giorno nel db
                DbHelper db = new DbHelper(PopupSchede.act.getApplicationContext());
                SQLiteDatabase dbWritable = db.getWritableDatabase();

                ContentValues valuesEsercizio = new ContentValues();
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_nomeEsercizio, esercizio.getNomeEsercizio());
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_tecnica_intensita, esercizio.getTecnica_intensita());
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_esecuzione, esercizio.getEsecuzione());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if(esercizio.getImmagineMacchinario()!=null) {
                    Bitmap bitmap = ((BitmapDrawable) esercizio.getImmagineMacchinario()).getBitmap();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }

                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_immagineMacchinario, stream.toByteArray());
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_numeroSerie, esercizio.getNumeroSerie());
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_numeroRipetizioni, esercizio.getNumeroRipetizioni());
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_timer, esercizio.getTimer());
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_note,note.getNote());


                System.out.println("***_"+ esercizio.getNomeEsercizio());
                    long EsercizioId = dbWritable.insert(SchemaDB.EsercizioDB.TABLE_NAME, null, valuesEsercizio);
                if(EsercizioId==-1){
                    Toast.makeText(dialogView.getContext(), "Nome già presente", Toast.LENGTH_LONG).show();

                }else if(esercizio.getNomeEsercizio()==""){
                    Toast.makeText(dialogView.getContext(), "Inserisci almeno il nome", Toast.LENGTH_LONG).show();
                }
                else{
                    idEserciziSalvati.add(EsercizioId);
                    giornoNuovo.getListaEsercizi().add(esercizio);
                    Global.adapterEsercizi.add(esercizio);
                    Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();

                }




            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor edit=shp.edit();
                edit.putString("notePassate",new Note().toJson());
                edit.commit();
                alertDialog.dismiss();
            }
        });



    }

    public static void ApriEsercizioSelezionato(Esercizio esercizio,LayoutInflater inflater){
        System.out.println("_____-OLD EX " + esercizio);
        Esercizio esercizioNew=Global.esercizioDao.getEsercizioByNome(esercizio.getNomeEsercizio());
        System.out.println("_____-NEW EX " + esercizio);

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_esercizio, null);
        Button salvaButton=dialogView.findViewById((int)R.id.salvaButton);
        Button okButton=dialogView.findViewById((int)R.id.okButton);

        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        EditText nomeEsercizio = dialogView.findViewById((int) R.id.nomeEsercizio);
        immagineEsercizio = dialogView.findViewById((int) R.id.immagineEsercizio);
        EditText numeroSerieEsercizio = dialogView.findViewById((int) R.id.numeroSerie);
        EditText numeroRipetEsercizio = dialogView.findViewById((int) R.id.numeroRipetizioni);
        EditText numeroTimetEsercizio = dialogView.findViewById((int) R.id.numeroTimer);
        EditText intensitaEsercizio = dialogView.findViewById((int) R.id.tecnicaIntensita);
        EditText esecuzioneEsercizio = dialogView.findViewById((int) R.id.esecuzione);
        Button salva=dialogView.findViewById((int)R.id.salvaEsercizio);
        Button ripristina=dialogView.findViewById((int)R.id.ripristinaEsercizio);
        Button back=dialogView.findViewById((int)R.id.backEsercizio);
        Button bottoneNote = dialogView.findViewById(R.id.bottoneNote);
        bottoneNote.setText("Mostra");

        nomeEsercizio.setText(esercizioNew.getNomeEsercizio());
        immagineEsercizio.setImageDrawable(esercizioNew.getImmagineMacchinario());
        numeroSerieEsercizio.setText(String.valueOf(esercizioNew.getNumeroSerie()));
        numeroRipetEsercizio.setText(String.valueOf(esercizioNew.getNumeroRipetizioni()));
        numeroTimetEsercizio.setText(String.valueOf(esercizioNew.getTimer()));
        intensitaEsercizio.setText(esercizioNew.getTecnica_intensita());
        esecuzioneEsercizio.setText(esercizioNew.getEsecuzione());

        nomeEsercizio.setInputType(InputType.TYPE_NULL);
        numeroSerieEsercizio.setInputType(InputType.TYPE_NULL);
        numeroRipetEsercizio.setInputType(InputType.TYPE_NULL);
        numeroTimetEsercizio.setInputType(InputType.TYPE_NULL);
        intensitaEsercizio.setInputType(InputType.TYPE_NULL);
        esecuzioneEsercizio.setInputType(InputType.TYPE_NULL);
        salva.setVisibility(View.GONE);
        ripristina.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit= sh.edit();
                PaginaScheda_Pag3.StampaTutto();
                Note notaDaMostrare= new Note(esercizioNew.getNote());
                edit.putString("notePassate", notaDaMostrare.toJson());
                edit.apply();
                System.out.println("*****" + esercizioNew.getNote());
                try {
                    NotificheDialog.NotificaNote(inflater,sh,false);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });


    }



    private static void selectImageFromGallery(){
        System.out.println("selectIMGGG");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        PopupSchede.act.startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), 2); // Modificato qui

    }

}
