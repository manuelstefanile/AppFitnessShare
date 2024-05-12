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
import android.graphics.drawable.Drawable;
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

        final Esercizio[] esercizioAppenaSalvato = {null};
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendo le note
                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_ESERCIZIO, null));

                float minuti=Float.parseFloat(numeroTimetEsercizio.getText().toString().trim().length()!=0?numeroTimetEsercizio.getText().toString():"0");
                float secondi=Float.parseFloat(numeroTimet2Esercizio.getText().toString().trim().length()!=0?numeroTimet2Esercizio.getText().toString():"0");
                float seconditotali=(minuti*60)+ secondi;
                Drawable imgIns=null;
                if(!Global.areImagesEqual(immagineEsercizio.getDrawable(), dialogView.getResources().getDrawable(R.drawable.noimg))){
                    imgIns=immagineEsercizio.getDrawable();
                }
                Esercizio esercizio=new Esercizio(nomeEsercizio.getText().toString().trim().length()>0?nomeEsercizio.getText().toString().trim():"",
                        intensitaEsercizio.getText().toString(),
                        esecuzioneEsercizio.getText().toString(),

                        imgIns,

                        Integer.parseInt(numeroSerieEsercizio.getText().toString().trim().length()!=0
                                &&Integer.parseInt(numeroSerieEsercizio.getText().toString())>0
                                ?numeroSerieEsercizio.getText().toString():"1"),
                        numeroRipetEsercizio.getText().toString()
                        ,seconditotali,
                        note.getNote(),
                        pesoKgEsercizio.getText().toString()
                );

                if(esercizio.getNomeEsercizio()==""){
                    Toast.makeText(dialogView.getContext(), "Inserisci almeno il nome", Toast.LENGTH_LONG).show();
                    return;
                }

                /***** controlla che nel giorno non ci sia lo stesso ex *****/
                Esercizio esercizioNew=null;
                ArrayList<Esercizio> exlist=Global.ledao.getListaEserciziPerGiorno(PopupGiorno.idGiornoAttuale);
                for (Esercizio e:exlist) {
                    e=Global.esercizioDao.getEsercizioById((int) e.getId());
                    System.out.println("giorno attuale "+e.getNomeEsercizio());
                    if(e.getNomeEsercizio().equals(esercizio.getNomeEsercizio())){
                        esercizioNew=Global.esercizioDao.getEsercizioById((int) e.getId());
                        break;
                    }
                }
                //tutto ok
                if (esercizioNew==null){
                    AggiornaEsercizio(esercizio,note);
                }else {
                    if(esercizioAppenaSalvato[0]!=null&&esercizioAppenaSalvato[0].getId()==esercizioNew.getId()){
                        AggiornaEsercizio(esercizio,note);
                    }else
                        Toast.makeText(dialogView.getContext(), "Nome già presente", Toast.LENGTH_LONG).show();
                }
                /*************************************************************/


            }

            private void AggiornaEsercizio(Esercizio esercizio, Note note){
                //nella stessa schermata ho gia premuto salva, quindi elimino e risalvo
                if(esercizioAppenaSalvato[0]!=null){
                    Global.esercizioDao.DeleteEsercizioById(esercizioAppenaSalvato[0].getId());
                    Global.adapterEsercizi.remove(esercizioAppenaSalvato[0]);
                }
                esercizio.setNote(note.getNote());
                esercizio.setId(Global.esercizioDao.inserisciEsercizio(esercizio).getId());
                giornoNuovo.getListaEsercizi().add(esercizio.getId());
                int posizion= Global.adapterEsercizi.getLista().size()-1;
                esercizio.setOrdine(posizion);
                esercizio.idGiornoAvviaRiferimento= giornoNuovo.getId();
                Global.adapterEsercizi.add(esercizio);

                esercizioAppenaSalvato[0] =esercizio;
                Global.ledao.Insert(giornoNuovo.getId(),esercizio.getId(),0,posizion);
                Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();
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

        Esercizio esercizioNew=new Esercizio();
        System.out.println("giorno attuale "+PopupGiorno.idGiornoAttuale);
        System.out.println("giorno attuale "+esercizio.getNomeEsercizio());
        ArrayList<Esercizio> exlist=Global.ledao.getListaEserciziPerGiorno(PopupGiorno.idGiornoAttuale);
        for (Esercizio e:exlist) {
            e=Global.esercizioDao.getEsercizioById((int) e.getId());

            if(e.getNomeEsercizio().equals(esercizio.getNomeEsercizio())){
                esercizioNew=Global.esercizioDao.getEsercizioById((int) e.getId());
            }
        }
        esercizioNew.idGiornoAvviaRiferimento=PopupGiorno.idGiornoAttuale;
        System.out.println("esercizio aperto "+esercizioNew);

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
        immagineEsercizio.setImageDrawable(esercizioNew.getImmagineMacchinario()!=null?esercizioNew.getImmagineMacchinario():dialogView.getResources().getDrawable(R.drawable.noimg));
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

        Esercizio finalEsercizioNew = esercizioNew;
        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit= sh.edit();


                Note note=Note.fromJson(sh.getString(COSTANTI.NOTE_ESERCIZIO, null));
                Note notaDaMostrare;
                if(note.getNote()!=null){
                    notaDaMostrare=note;
                }else
                    notaDaMostrare= new Note(finalEsercizioNew.getNote());

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

        Esercizio finalEsercizioNew1 = esercizioNew;
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prendo le note
                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_ESERCIZIO, null));

                float minuti=Float.parseFloat(numeroTimetEsercizio.getText().toString().trim().length()!=0?numeroTimetEsercizio.getText().toString():"0");
                float secondi=Float.parseFloat(numeroTimet2Esercizio.getText().toString().trim().length()!=0?numeroTimet2Esercizio.getText().toString():"0");
                float seconditotali=(minuti*60)+ secondi;

                Drawable imgIns=null;
                if(!Global.areImagesEqual(immagineEsercizio.getDrawable(), dialogView.getResources().getDrawable(R.drawable.noimg))){
                    imgIns=immagineEsercizio.getDrawable();
                }
                Esercizio eser=new Esercizio(nomeEsercizio.getText().toString().trim().length()>0?nomeEsercizio.getText().toString().trim():"",
                        intensitaEsercizio.getText().toString(),
                        esecuzioneEsercizio.getText().toString(),
                        imgIns,
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
                    System.out.println(eser.getNomeEsercizio()+" "+ finalEsercizioNew1.getNomeEsercizio());
                    if(eser.getNomeEsercizio().equals(finalEsercizioNew1.getNomeEsercizio())){
                        eser.setId(finalEsercizioNew1.getId());

                        Global.adapterEsercizi.UpdateEsercizio(eser);
                        Global.esercizioDao.updateEsercizio(eser);
                        Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();


                        ;

                        //nomi diversi, allora controlla
                    }else{
                        Esercizio esercizioNew=null;
                        ArrayList<Esercizio> exlist=Global.ledao.getListaEserciziPerGiorno(PopupGiorno.idGiornoAttuale);
                        for (Esercizio e:exlist) {
                            e=Global.esercizioDao.getEsercizioById((int) e.getId());
                            System.out.println("nnn"+e.getNomeEsercizio()+"  sdfsdf "+eser.getNomeEsercizio());
                            if(e.getNomeEsercizio().equals(eser.getNomeEsercizio())){
                                esercizioNew=Global.esercizioDao.getEsercizioById((int) e.getId());
                            }
                        }
                        if(esercizioNew!=null){
                            Toast.makeText(dialogView.getContext(), "Nome esercizio già presente nel giorno", Toast.LENGTH_SHORT).show();
                        }else{
                            eser.setId(finalEsercizioNew1.getId());

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

        /** prendo l esercizio per quel giorno di riferimento */
        Esercizio esercizioNew=new Esercizio();
        ArrayList<Esercizio> exlist=Global.ledao.getListaEserciziPerGiorno(PopupGiorno.idGiornoAttuale);
        for (Esercizio e:exlist) {
            e=Global.esercizioDao.getEsercizioById((int) e.getId());
            if(e.getNomeEsercizio().equals(esercizio.getNomeEsercizio())){
                esercizioNew=Global.esercizioDao.getEsercizioById((int) e.getId());
            }
        }
        esercizioNew.idGiornoAvviaRiferimento=PopupGiorno.idGiornoAttuale;

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
        immagineEsercizio.setImageDrawable(esercizioNew.getImmagineMacchinario()!=null?esercizioNew.getImmagineMacchinario():dialogView.getResources().getDrawable(R.drawable.noimg));
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

        Esercizio finalEsercizioNew = esercizioNew;
        terminaEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //salva cio che hai scritto
                finalEsercizioNew.setPesoKG(pesoKgEsercizio.getText().toString());
                finalEsercizioNew.setNote(Note.fromJson(shp.getString(COSTANTI.NOTE_ESERCIZIO, null)).getNote());
                //esercizioNew.setCompletato(1);

                Global.esercizioDao.updateEsercizio(finalEsercizioNew);
                Global.ledao.updateStato(esercizio.idGiornoAvviaRiferimento, finalEsercizioNew.getId(),1);

                /************setta il background a verde*********/
                int posizioneitem=Global.adapterEsercizi.getPosition(esercizio);

                Global.adapterEsercizi.AggiornaEsercizioCompletato(posizioneitem, finalEsercizioNew);
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

        Esercizio finalEsercizioNew1 = esercizioNew;
        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit= sh.edit();


                Note note=Note.fromJson(sh.getString(COSTANTI.NOTE_ESERCIZIO, null));
                Note notaDaMostrare;
                if(note.getNote()!=null){
                    notaDaMostrare=note;
                }else
                    notaDaMostrare= new Note(finalEsercizioNew1.getNote());

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
