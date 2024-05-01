package com.example.appfitness.Pagina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

import android.database.SQLException;

import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;
import com.example.appfitness.Registrazione_Pag2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

import javax.net.ssl.ExtendedSSLSession;

public class PopupEsercizio {

    public static ArrayList<Long> idEserciziSalvati= new ArrayList<>();

    public static ImageButton immagineEsercizio;

    public static  long[] tempotemp;
    public static  boolean pausaTimer=false;





    public static void CreaEsercizio(LayoutInflater inflater, Giorno giornoNuovo){


        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //note all inizio della creazione dell ex è vuoto
        SharedPreferences.Editor edit=shp.edit();
        edit.putString(COSTANTI.NOTE_ESERCIZIO,new Note().toJson());
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

        WindowManager wm = (WindowManager) dialogView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        RelativeLayout ll = dialogView.findViewById((int)R.id.origineEsercizio);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);

        alertDialog.getWindow().setLayout(size.x, size.y);


        EditText nomeEsercizio = dialogView.findViewById((int) R.id.nomeEsercizio);
        immagineEsercizio = dialogView.findViewById((int) R.id.immagineEsercizio);
        EditText numeroSerieEsercizio = dialogView.findViewById((int) R.id.numberPicker);
        EditText numeroRipetEsercizio = dialogView.findViewById((int) R.id.numeroRipetizioni);
        EditText numeroTimetEsercizio = dialogView.findViewById((int) R.id.numeroTimer);
        EditText numeroTimet2Esercizio = dialogView.findViewById((int) R.id.numeroTimer2);
        EditText pesoKgEsercizio = dialogView.findViewById((int) R.id.pesoKG);
        EditText intensitaEsercizio = dialogView.findViewById((int) R.id.tecnicaIntensita);
        EditText esecuzioneEsercizio = dialogView.findViewById((int) R.id.esecuzione);
        Button salva=dialogView.findViewById((int)R.id.salvaEsercizio);
        Button back=dialogView.findViewById((int)R.id.backEsercizio);
        Button bottoneNote = dialogView.findViewById(R.id.bottoneNote);


        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("noteClick");
                try {
                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater, shp, COSTANTI.NOTE_ESERCIZIO);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });


        immagineEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupEsercizio.selectImageFromGallery();
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendo le note
                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_ESERCIZIO, null));

                float minuti=Float.parseFloat(numeroTimetEsercizio.getText().toString().trim().length()!=0?numeroTimetEsercizio.getText().toString():"0");
                float secondi=Float.parseFloat(numeroTimet2Esercizio.getText().toString().trim().length()!=0?numeroTimet2Esercizio.getText().toString():"0");
                float seconditotali=(minuti*60)+ secondi;
                Esercizio esercizio=new Esercizio(nomeEsercizio.getText().toString().trim().length()>0?nomeEsercizio.getText().toString().trim():"",
                        intensitaEsercizio.getText().toString(),
                        esecuzioneEsercizio.getText().toString(),immagineEsercizio.getDrawable(),
                        Integer.parseInt(numeroSerieEsercizio.getText().toString().trim().length()!=0
                                &&Integer.parseInt(numeroSerieEsercizio.getText().toString())>0
                                ?numeroSerieEsercizio.getText().toString():"1"),
                        numeroRipetEsercizio.getText().toString()
                        ,seconditotali,
                        note.getNote(),
                        pesoKgEsercizio.getText().toString()
                );
                //inserisco l ex nel db
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
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_pesoKG, esercizio.getPesoKG());
                valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_note,note.getNote());

                long EsercizioId = dbWritable.insert(SchemaDB.EsercizioDB.TABLE_NAME, null, valuesEsercizio);
                esercizio.setId(EsercizioId);
                if(EsercizioId==-1){
                    Toast.makeText(dialogView.getContext(), "Nome già presente", Toast.LENGTH_LONG).show();

                }else if(esercizio.getNomeEsercizio()==""){
                    Toast.makeText(dialogView.getContext(), "Inserisci almeno il nome", Toast.LENGTH_LONG).show();
                }
                else{
                    giornoNuovo.getListaEsercizi().add(EsercizioId);
                    Global.adapterEsercizi.add(esercizio);
                    Global.ledao.Insert(giornoNuovo.getId(),esercizio.getId(),0);
                    Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();

                }




            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit=shp.edit();

                edit.commit();
                alertDialog.dismiss();
            }
        });



    }

    public static void ApriEsercizioSelezionato(Esercizio esercizio,LayoutInflater inflater){

        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shp.edit();


        edit.putString(COSTANTI.NOTE_ESERCIZIO,new Note().toJson());
        edit.commit();

        Esercizio esercizioNew=Global.esercizioDao.getEsercizioByNome(esercizio.getNomeEsercizio());
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

        WindowManager wm = (WindowManager) dialogView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        RelativeLayout ll = dialogView.findViewById((int)R.id.origineEsercizio);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);

        alertDialog.getWindow().setLayout(size.x, size.y);

        EditText nomeEsercizio = dialogView.findViewById((int) R.id.nomeEsercizio);
        immagineEsercizio = dialogView.findViewById((int) R.id.immagineEsercizio);
        EditText numeroSerieEsercizio = dialogView.findViewById((int) R.id.numberPicker);
        EditText numeroRipetEsercizio = dialogView.findViewById((int) R.id.numeroRipetizioni);
        EditText numeroTimetEsercizio = dialogView.findViewById((int) R.id.numeroTimer);
        EditText numeroTimet2Esercizio = dialogView.findViewById((int) R.id.numeroTimer2);
        EditText pesoKgEsercizio = dialogView.findViewById((int) R.id.pesoKG);
        EditText intensitaEsercizio = dialogView.findViewById((int) R.id.tecnicaIntensita);
        EditText esecuzioneEsercizio = dialogView.findViewById((int) R.id.esecuzione);
        Button salva=dialogView.findViewById((int)R.id.salvaEsercizio);
        Button back=dialogView.findViewById((int)R.id.backEsercizio);
        Button bottoneNote = dialogView.findViewById(R.id.bottoneNote);
        bottoneNote.setText("Note");

        float totaleSecondi=esercizioNew.getTimer();
        int minuti = (int) (totaleSecondi / 60);
        float secondi = totaleSecondi % 60;


        nomeEsercizio.setText(esercizioNew.getNomeEsercizio());
        immagineEsercizio.setImageDrawable(esercizioNew.getImmagineMacchinario());
        numeroSerieEsercizio.setText(String.valueOf(esercizioNew.getNumeroSerie()));
        numeroRipetEsercizio.setText(String.valueOf(esercizioNew.getNumeroRipetizioni()));
        numeroTimetEsercizio.setText(String.valueOf(minuti));
        numeroTimet2Esercizio.setText(String.valueOf(secondi));
        pesoKgEsercizio.setText(esercizioNew.getPesoKG());
        intensitaEsercizio.setText(esercizioNew.getTecnica_intensita());
        esecuzioneEsercizio.setText(esercizioNew.getEsecuzione());

        /*
        nomeEsercizio.setInputType(InputType.TYPE_NULL);
        numeroSerieEsercizio.setInputType(InputType.TYPE_NULL);
        numeroRipetEsercizio.setInputType(InputType.TYPE_NULL);
        numeroTimetEsercizio.setInputType(InputType.TYPE_NULL);
        intensitaEsercizio.setInputType(InputType.TYPE_NULL);
        esecuzioneEsercizio.setInputType(InputType.TYPE_NULL);
        salva.setVisibility(View.GONE);


         */

        immagineEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupEsercizio.selectImageFromGallery();
            }
        });

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
                edit.putString(COSTANTI.NOTE_ESERCIZIO, notaDaMostrare.toJson());
                edit.apply();
                try {
                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater,sh,COSTANTI.NOTE_ESERCIZIO);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendo le note
                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_ESERCIZIO, null));

                float minuti=Float.parseFloat(numeroTimetEsercizio.getText().toString().trim().length()!=0?numeroTimetEsercizio.getText().toString():"0");
                float secondi=Float.parseFloat(numeroTimet2Esercizio.getText().toString().trim().length()!=0?numeroTimet2Esercizio.getText().toString():"0");
                float seconditotali=(minuti*60)+ secondi;

                Esercizio eser=new Esercizio(nomeEsercizio.getText().toString().trim().length()>0?nomeEsercizio.getText().toString().trim():"",
                        intensitaEsercizio.getText().toString(),
                        esecuzioneEsercizio.getText().toString(),immagineEsercizio.getDrawable(),
                        Integer.parseInt(numeroSerieEsercizio.getText().toString().trim().length()!=0
                                &&Integer.parseInt(numeroSerieEsercizio.getText().toString())>0
                                ?numeroSerieEsercizio.getText().toString():"1"),
                        numeroRipetEsercizio.getText().toString(),
                        seconditotali,
                        note.getNote(),
                        pesoKgEsercizio.getText().toString()

                );
                if(eser.getNomeEsercizio()==""){
                    Toast.makeText(dialogView.getContext(), "Inserisci almeno il nome", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println(eser.getNomeEsercizio()+" "+esercizioNew.getNomeEsercizio());
                    if(eser.getNomeEsercizio().equals(esercizioNew.getNomeEsercizio())){
                        eser.setId(esercizioNew.getId());

                        Global.adapterEsercizi.UpdateEsercizio(eser);
                        Global.esercizioDao.updateEsercizio(eser);
                        Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();


                        ;

                        //nomi diversi, allora controlla
                    }else{
                        Esercizio ex=Global.esercizioDao.getEsercizioByNome(eser.getNomeEsercizio());
                        if(ex!=null){
                            Toast.makeText(dialogView.getContext(), "Nome esercizio già presente", Toast.LENGTH_SHORT).show();
                        }else{
                            eser.setId(esercizioNew.getId());

                            Global.esercizioDao.updateEsercizio(eser);
                            Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();
                            Global.adapterEsercizi.UpdateEsercizio(eser);
                        }
                    }

                }
            }
        });



    }

    public static void AvviaEsercizioSelezionato(Esercizio esercizio,LayoutInflater inflater){

        //non posso tornare indietro se premo back



        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shp.edit();
        Note no=new Note(esercizio.getNote());
        edit.putString(COSTANTI.NOTE_ESERCIZIO, no.toJson());
        edit.apply();


        edit.putString(COSTANTI.NOTE_ESERCIZIO,new Note().toJson());
        edit.commit();

        Esercizio esercizioNew=Global.esercizioDao.getEsercizioByNome(esercizio.getNomeEsercizio());
        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.avvia_esercizo, null);
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

        WindowManager wm = (WindowManager) dialogView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        RelativeLayout ll = dialogView.findViewById((int)R.id.origineEsercizio);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);


        alertDialog.getWindow().setLayout(size.x, size.y);

        TextView nomeEsercizio = dialogView.findViewById((int) R.id.nomeEsercizio);
        immagineEsercizio = dialogView.findViewById((int) R.id.immagineEsercizio);
        TextView numeroSerieEsercizio = dialogView.findViewById((int) R.id.numberPicker);
        TextView numeroRipetEsercizio = dialogView.findViewById((int) R.id.numeroRipetizioni);

        Button bottoneTimer=dialogView.findViewById((int) R.id.bottoneTimer);
        Button bottoneCronometro=dialogView.findViewById((int) R.id.bottoneCronometro);

        EditText pesoKgEsercizio = dialogView.findViewById((int) R.id.pesoKG);
        TextView intensitaEsercizio = dialogView.findViewById((int) R.id.tecnicaIntensita);
        TextView esecuzioneEsercizio = dialogView.findViewById((int) R.id.esecuzione);

        Button terminaEx=dialogView.findViewById((int)R.id.TerminaEsercizio);
        terminaEx.setVisibility(View.GONE);

        Button bottoneNote = dialogView.findViewById(R.id.bottoneNote);
        bottoneNote.setText("Note");

        float totaleSecondi=esercizioNew.getTimer();

        nomeEsercizio.setText(esercizioNew.getNomeEsercizio());
        immagineEsercizio.setImageDrawable(esercizioNew.getImmagineMacchinario());
        numeroSerieEsercizio.setText(String.valueOf(esercizioNew.getNumeroSerie()));
        numeroRipetEsercizio.setText(String.valueOf(esercizioNew.getNumeroRipetizioni()));

        pesoKgEsercizio.setText((esercizioNew.getPesoKG()));
        intensitaEsercizio.setText(esercizioNew.getTecnica_intensita());
        esecuzioneEsercizio.setText(esercizioNew.getEsecuzione());

        numeroSerieEsercizio.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Metodo chiamato quando il testo è stato modificato
                String inputText = s.toString();
                if (inputText.equals("0")) {
                    bottoneTimer.setEnabled(false);
                    terminaEx.setVisibility(View.VISIBLE);

                    //quando premo il tasto indietro, non torna indietro
                    alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                                return true; // Consuma l'evento e non lo passa al sistema
                            }
                            return false; // L'evento non è stato gestito, passa al sistema
                        }});

                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        immagineEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificheDialog.NotificaImmagine(inflater,immagineEsercizio.getDrawable());
            }
        });

        terminaEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //salva cio che hai scritto
                esercizioNew.setPesoKG(pesoKgEsercizio.getText().toString());
                esercizioNew.setNote(Note.fromJson(shp.getString(COSTANTI.NOTE_ESERCIZIO, null)).getNote());
                //esercizioNew.setCompletato(1);

                Global.esercizioDao.updateEsercizio(esercizioNew);
                Global.ledao.updateStato(esercizio.idGiornoAvviaRiferimento,esercizioNew.getId(),1);

                /************setta il background a verde*********/
                int posizioneitem=Global.adapterEsercizi.getPosition(esercizio);

                Global.adapterEsercizi.AggiornaEsercizioCompletato(posizioneitem,esercizioNew);
                /************setta il background a verde*********/

                alertDialog.dismiss();
            }
        });

        bottoneTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvviaTimer(inflater,totaleSecondi,numeroSerieEsercizio);
            }
        });
        bottoneCronometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvviaCronometro(inflater);
            }
        });

        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit= sh.edit();

                Note notetemp = Note.fromJson(shp.getString(COSTANTI.NOTE_ESERCIZIO, null));
                if(notetemp.getNote()!=null){
                    esercizioNew.setNote(notetemp.getNote());
                }
                Note notaDaMostrare= new Note(esercizioNew.getNote());
                edit.putString(COSTANTI.NOTE_ESERCIZIO, notaDaMostrare.toJson());
                edit.apply();
                try {
                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater,sh,COSTANTI.NOTE_ESERCIZIO);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void AvviaTimer(LayoutInflater inflater,float totaleSecondi,TextView numeroSerie){
// Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.timer, null);
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

        TextView timerTesto=dialogView.findViewById(R.id.timer);

        final View backgroundView = alertDialog.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**/
                return true; // Indica che l'evento è stato consumato
            }
        });


        tempotemp = new long[1];
        long totalTimeInMillis =(long) (totaleSecondi * 1000);
        //setto la notifica;
        TimerService.millisecondi=totalTimeInMillis;
        final CountDownTimer[] countDownTimer = {new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Converti millisUntilFinished in minuti e secondi
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;


                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                timerTesto.setText(timeLeftFormatted);
                tempotemp[0] = millisUntilFinished;
                System.out.println("tempo onTick" + tempotemp[0]);
            }

            @Override
            public void onFinish() {
                numeroSerie.setText(String.valueOf(Integer.parseInt(numeroSerie.getText().toString()) - 1));
                // Azioni da intraprendere quando il cronometro è finito
                tempotemp=null;
                pausaTimer=false;
                alertDialog.dismiss();
            }


        }.start()};

        Button pauseButton = dialogView.findViewById(R.id.pauseButton);
        Button resumeButton = dialogView.findViewById(R.id.resumeButton);
        Button backButton = dialogView.findViewById(R.id.backButton);
        Button terminateButton = dialogView.findViewById(R.id.terminateButton);

        final boolean[] pausa = {false};
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Metti in pausa il timer
                pauseButton.setBackgroundResource(R.drawable.drawable_botton_grigio);
                countDownTimer[0].cancel();
                pausa[0] =true;
                pausaTimer=true;

                ;
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Riprendi il timer
                if(pausa[0]) {
                    countDownTimer[0] = new CountDownTimer(tempotemp[0], 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // Converti millisUntilFinished in minuti e secondi
                            int minutes = (int) (millisUntilFinished / 1000) / 60;
                            int seconds = (int) (millisUntilFinished / 1000) % 60;
                            pauseButton.setBackgroundResource(R.drawable.drawable_botton_grigio_chiaro);

                            String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                            timerTesto.setText(timeLeftFormatted);
                            tempotemp[0] = millisUntilFinished;

                        }

                        @Override
                        public void onFinish() {
                            numeroSerie.setText(String.valueOf(Integer.parseInt(numeroSerie.getText().toString()) - 1));
                            // Azioni da intraprendere quando il cronometro è finito
                            tempotemp=null;
                            alertDialog.dismiss();
                        }

                    }.start();
                    pausa[0]=false;
                    pausaTimer=false;
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Azioni per tornare indietro
                countDownTimer[0].cancel();
                tempotemp=null;
                alertDialog.dismiss();
                pausaTimer=false;
            }
        });

        terminateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer[0].onTick(0);
                countDownTimer[0].onFinish();
                countDownTimer[0].cancel();
                tempotemp=null;
                // Azioni per terminare direttamente

            }
        });


    }

    private static void AvviaCronometro(LayoutInflater inflater){
        Handler handler;
        final boolean[] isRunning = new boolean[1];
        final int[] seconds = {0};
         handler = new Handler();
         isRunning[0] = true;

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.cronometro, null);
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

        TextView timerTesto=dialogView.findViewById((int)R.id.cronometro);

        final View backgroundView = alertDialog.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**/
                return true; // Indica che l'evento è stato consumato
            }
        });


        //avvia cronometro
        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("isru "+isRunning[0]);
                if (isRunning[0]) {
                    seconds[0]++;
                    int minutes = (seconds[0] % 3600) / 60;
                    int secs = seconds[0] % 60;

                    String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                    timerTesto.setText(time);

                    System.out.println(" vado +"+seconds[0]);
                    // Esegui nuovamente il Runnable dopo 1 secondo
                    handler.postDelayed(this, 1000);
                } else handler.postDelayed(this, 1000);
            }
        });

        Button pausa= dialogView.findViewById(R.id.pauseButton);

        Button back= dialogView.findViewById(R.id.backButton);

        pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning[0] = !isRunning[0];
                if(isRunning[0])pausa.setText("PAUSA");else pausa.setText("RIPRENDI");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);
                alertDialog.dismiss();
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
