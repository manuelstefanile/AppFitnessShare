package com.example.appfitness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Fisico;
import com.example.appfitness.Bean.Fisico_Immagini;
import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.DB.FisicoDAO;
import com.example.appfitness.DB.ListaImgFisicoDAO;
import com.example.appfitness.DB.MisureDAO;
import com.example.appfitness.DB.PesoDAO;
import com.example.appfitness.DB.kcalDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.Pagina3.Global;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificheDialog {

    public static ArrayList<Fisico_Immagini> posa_immagine= new ArrayList<>();
    public static int immagineRiferimento=0;
    public static ImageButton immagineFisico, immagineFisico2,immagineFisico3,immagineFisico4,immagineFisico5,immagineFisico6,immagineFisico7;

    //vabbe ho iniziato quantomeno a scriverlo per vedere se parte
    public static void NotificaFisico(LayoutInflater inflater, SharedPreferences sh, String noteFisico, Activity act) throws Eccezioni {



        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.fisico_dettaglio, null);
        Button salvaButton=dialogView.findViewById((int)R.id.salvaButton);
        Button okButton=dialogView.findViewById((int)R.id.okButton);

        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);

        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText testoFisico1=dialogView.findViewById(R.id.posaFisico);
        EditText testoFisico2=dialogView.findViewById(R.id.posaFisico2);
        EditText testoFisico3=dialogView.findViewById(R.id.posaFisico3);
        EditText testoFisico4=dialogView.findViewById(R.id.posaFisico4);
        EditText testoFisico5=dialogView.findViewById(R.id.posaFisico5);
        EditText testoFisico6=dialogView.findViewById(R.id.posaFisico6);
        EditText testoFisico7=dialogView.findViewById(R.id.posaFisico7);
        EditText noteFisicoDialo=dialogView.findViewById(R.id.noteFisico);
        immagineFisico= dialogView.findViewById(R.id.immaginefisico);
        immagineFisico2= dialogView.findViewById(R.id.immaginefisico2);
        immagineFisico3= dialogView.findViewById(R.id.immaginefisico3);
        immagineFisico4= dialogView.findViewById(R.id.immaginefisico4);
        immagineFisico5= dialogView.findViewById(R.id.immaginefisico5);
        immagineFisico6= dialogView.findViewById(R.id.immaginefisico6);
        immagineFisico7= dialogView.findViewById(R.id.immaginefisico7);

        ArrayList<ImageButton> arrayImgB=new ArrayList<>();
        arrayImgB.add(immagineFisico);
        arrayImgB.add(immagineFisico2);
        arrayImgB.add(immagineFisico3);
        arrayImgB.add(immagineFisico4);
        arrayImgB.add(immagineFisico5);
        arrayImgB.add(immagineFisico6);
        arrayImgB.add(immagineFisico7);
        String[] nomi={"_","__","___","____","_____","______","_______"};
        /**********************************/
        // Carica l'immagine di default dalla cartella drawable

        for(int i=0;i<arrayImgB.size();i++){
            Fisico_Immagini fi=new Fisico_Immagini(
                    null,
                    nomi[i],
                    i,
                    arrayImgB.get(i));
            posa_immagine.add(fi);
        };
        /************************************/
        CalendarView calendario=dialogView.findViewById((int)R.id.calendarioFisico);
        Calendar dataSalvare=Calendar.getInstance();
        ImpostaCalendario(calendario,dialogView,dataSalvare,new Fisico());

        if(!Registrazione_Pag2.editGlobal){
            calendario.setFocusable(false);
            calendario.setClickable(false);

            testoFisico1.setFocusable(false);
            testoFisico1.setClickable(false);

            testoFisico2.setFocusable(false);
            testoFisico2.setClickable(false);

            testoFisico3.setFocusable(false);
            testoFisico3.setClickable(false);

            testoFisico4.setFocusable(false);
            testoFisico4.setClickable(false);

            testoFisico5.setFocusable(false);
            testoFisico5.setClickable(false);

            testoFisico6.setFocusable(false);
            testoFisico6.setClickable(false);

            testoFisico7.setFocusable(false);
            testoFisico7.setClickable(false);

            noteFisicoDialo.setFocusable(false);
            noteFisicoDialo.setClickable(false);

            salvaButton.setVisibility(View.GONE);
            okButton.setText("Back");
        }



        ListaImgFisicoDAO lifdao=new ListaImgFisicoDAO(dialogView.getContext());
        FisicoDAO fdao=new FisicoDAO(dialogView.getContext());

        /************ se la data corrente ha il set messo, allora mostra *********/
        Fisico fAttuale= fdao.getFisicoPerData(Global.ConversioneCalendarString(dataSalvare));
        if(fAttuale!=null){
            ArrayList<Fisico_Immagini> mappa=lifdao.getImmaginiPerIdFisico(fAttuale.getId());

            mappa.forEach((stringEntry -> {
                switch (stringEntry.getPosizione()){
                    case 0:
                        testoFisico1.setText(controlloStringaFisico(stringEntry.getNomePosa()));
                        immagineFisico.setImageDrawable(Global.byteArrayToDrawable(
                                stringEntry.getImmagine() != null ? stringEntry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 1:
                        testoFisico2.setText(controlloStringaFisico(stringEntry.getNomePosa()));
                        immagineFisico2.setImageDrawable(Global.byteArrayToDrawable(
                                stringEntry.getImmagine() != null ? stringEntry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 2:
                        testoFisico3.setText(controlloStringaFisico(stringEntry.getNomePosa()));
                        immagineFisico3.setImageDrawable(Global.byteArrayToDrawable(
                                stringEntry.getImmagine() != null ? stringEntry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 3:
                        testoFisico4.setText(controlloStringaFisico(stringEntry.getNomePosa()));
                        immagineFisico4.setImageDrawable(Global.byteArrayToDrawable(
                                stringEntry.getImmagine() != null ? stringEntry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 4:
                        testoFisico5.setText(controlloStringaFisico(stringEntry.getNomePosa()));
                        immagineFisico5.setImageDrawable(Global.byteArrayToDrawable(
                                stringEntry.getImmagine() != null ? stringEntry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 5:
                        testoFisico6.setText(controlloStringaFisico(stringEntry.getNomePosa()));
                        immagineFisico6.setImageDrawable(Global.byteArrayToDrawable(
                                stringEntry.getImmagine() != null ? stringEntry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 6:
                        testoFisico7.setText(controlloStringaFisico(stringEntry.getNomePosa()));
                        immagineFisico7.setImageDrawable(Global.byteArrayToDrawable(
                                stringEntry.getImmagine() != null ? stringEntry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                }

            }));
            noteFisicoDialo.setText(fAttuale.getNote());
        }
        /********************************/

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Controlla che il fisico inserito in quella data sia presente e fai
                String nome1 = testoFisico1.getText().toString().length() != 0 ? testoFisico1.getText().toString() : "_";
                String nome2 = testoFisico2.getText().toString().length() != 0 ? testoFisico2.getText().toString() : "__";
                String nome3 = testoFisico3.getText().toString().length() != 0 ? testoFisico3.getText().toString() : "___";
                String nome4 = testoFisico4.getText().toString().length() != 0 ? testoFisico4.getText().toString() : "____";
                String nome5 = testoFisico5.getText().toString().length() != 0 ? testoFisico5.getText().toString() : "_____";
                String nome6 = testoFisico6.getText().toString().length() != 0 ? testoFisico6.getText().toString() : "______";
                String nome7 = testoFisico7.getText().toString().length() != 0 ? testoFisico7.getText().toString() : "_______";



                if (!nome1.equals(nome2) && !nome1.equals(nome3) && !nome1.equals(nome4) && !nome1.equals(nome5) && !nome1.equals(nome6) && !nome1.equals(nome7) &&
                        !nome2.equals(nome3) && !nome2.equals(nome4) && !nome2.equals(nome5) && !nome2.equals(nome6) && !nome2.equals(nome7) &&
                        !nome3.equals(nome4) && !nome3.equals(nome5) && !nome3.equals(nome6) && !nome3.equals(nome7) &&
                        !nome4.equals(nome5) && !nome4.equals(nome6) && !nome4.equals(nome7) &&
                        !nome5.equals(nome6) && !nome5.equals(nome7) &&
                        !nome6.equals(nome7)) {

                    // Salvo prima il fisico. prendo l'id poi lista
                    Fisico f = new Fisico();

                    for (Fisico_Immagini bitm : posa_immagine) {
                        switch (bitm.getPosizione()) {
                            case 0:
                                bitm.setNomePosa(nome1);
                                break;
                            case 1:
                                bitm.setNomePosa(nome2);
                                break;
                            case 2:
                                bitm.setNomePosa(nome3);
                                break;
                            case 3:
                                bitm.setNomePosa(nome4);
                                break;
                            case 4:
                                bitm.setNomePosa(nome5);
                                break;
                            case 5:
                                bitm.setNomePosa(nome6);
                                break;
                            case 6:
                                bitm.setNomePosa(nome7);
                                break;
                        }
                    }


                    f.setPosa_immagine(posa_immagine);

                    f.setNote(noteFisicoDialo.getText().toString());
                    f.setCalendario(dataSalvare);

                    //fai update se gia presente
                    Fisico fSalvato = fdao.getFisicoPerData(Global.ConversioneCalendarString(dataSalvare));
                    if (fSalvato == null) {

                        f = fdao.inserisciFisico(f);
                        lifdao.inserisciListaImg(f);
                    } else {

                        f.setId(fSalvato.getId());
                        fdao.updateFisico(f);
                        lifdao.updateFis(f);
                    }
                    Registrazione_Pag2.StampaTutto();

                    immagineRiferimento = 0;


                    ToastPersonalizzato.ToastSuccessoCalendario(inflater);

                }else{
                    //metti l animazione nel toast
                    // Infla il layout personalizzato
                    ToastPersonalizzato.ToastErrore("Inserire tutti nomi diversi.",inflater);
                }



            }
        });

        NotificaImmaginePremutaIngrandisci(inflater, immagineFisico);
        immagineFisico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Registrazione_Pag2.editGlobal) {
                    NotificaImmagine(inflater, immagineFisico.getDrawable());
                } else {
                    selectImageFromGallery(act, 1);
                }
            }
        });

        NotificaImmaginePremutaIngrandisci(inflater, immagineFisico2);
        immagineFisico2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Registrazione_Pag2.editGlobal) {
                    NotificaImmagine(inflater, immagineFisico2.getDrawable());
                } else {
                    selectImageFromGallery(act, 2);
                }
            }
        });

        NotificaImmaginePremutaIngrandisci(inflater, immagineFisico3);
        immagineFisico3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Registrazione_Pag2.editGlobal) {
                    NotificaImmagine(inflater, immagineFisico3.getDrawable());
                } else {
                    selectImageFromGallery(act, 3);
                }
            }
        });

        NotificaImmaginePremutaIngrandisci(inflater, immagineFisico4);
        immagineFisico4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Registrazione_Pag2.editGlobal) {
                    NotificaImmagine(inflater, immagineFisico4.getDrawable());
                } else {
                    selectImageFromGallery(act, 4);
                }
            }
        });

        NotificaImmaginePremutaIngrandisci(inflater, immagineFisico5);
        immagineFisico5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Registrazione_Pag2.editGlobal) {
                    NotificaImmagine(inflater, immagineFisico5.getDrawable());
                } else {
                    selectImageFromGallery(act, 5);
                }
            }
        });

        NotificaImmaginePremutaIngrandisci(inflater, immagineFisico6);
        immagineFisico6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Registrazione_Pag2.editGlobal) {
                    NotificaImmagine(inflater, immagineFisico6.getDrawable());
                } else {
                    selectImageFromGallery(act, 6);
                }
            }
        });

        NotificaImmaginePremutaIngrandisci(inflater, immagineFisico7);
        immagineFisico7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Registrazione_Pag2.editGlobal) {
                    NotificaImmagine(inflater, immagineFisico7.getDrawable());
                } else {
                    selectImageFromGallery(act, 7);
                }
            }
        });


    }

    private static String controlloStringaFisico(String stringapassata) {
        switch (stringapassata) {
            case "_":
            case "__":
            case "___":
            case "____":
            case "_____":
            case "______":
            case "_______":
                return "";
            default:
                return stringapassata;
        }
    }

    private static void selectImageFromGallery(Activity act,int posizione){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        immagineRiferimento=posizione;
        act.startActivityForResult(intent,1); // Modificato qui

    }


    public static void NotificaPeso(LayoutInflater inflater, SharedPreferences sh) throws Eccezioni {

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.peso_dettaglio, null);
        Button salvaButton=dialogView.findViewById((int)R.id.salvaButton);
        Button okButton=dialogView.findViewById((int)R.id.okButton);

        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);
        builder.setCancelable(false);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        CalendarView calendario=dialogView.findViewById((int)R.id.calendarioPeso);
        EditText kiliEdit= dialogView.findViewById(R.id.pesoAttual);
        EditText noteDettaglio=dialogView.findViewById((int)R.id.notePeso);
        if(!Registrazione_Pag2.editGlobal){
            calendario.setFocusable(false);
            calendario.setClickable(false);
            kiliEdit.setFocusable(false);
            kiliEdit.setClickable(false);
            noteDettaglio.setFocusable(false);
            noteDettaglio.setClickable(false);

            salvaButton.setVisibility(View.GONE);
            okButton.setText("Back");
        }


        PesoDAO pdao=new PesoDAO(dialogView.getContext());
        Peso pesoStorage=new Peso();
        ArrayList<Peso> arr=pdao.getPesoInfo();
        if(arr.size()>0){
            pesoStorage=arr.get(0);
        }
        if(pesoStorage.getCalendario()==null)pesoStorage.setCalendario(Calendar.getInstance());
        if(pesoStorage.getPesoKg()!=0&&pesoStorage.getCalendario()!=null){
            calendario.setDate(pesoStorage.getCalendario().getTimeInMillis());
            kiliEdit.setText(String.valueOf(pesoStorage.getPesoKg()));
        }
        if(pesoStorage.getNote()!=null){
            noteDettaglio.setText(pesoStorage.getNote());
        }

        Calendar dataSalvare=pesoStorage.getCalendario();
        ImpostaCalendario(calendario,dialogView,dataSalvare,new Peso());

        Peso finalPesoStorage = pesoStorage;
        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //salva i dati che inserisci
                float pesoInserito = 0;
                String pesoStringa = kiliEdit.getText().toString().trim(); // Rimuovi spazi vuoti

                // Verifica se la stringa del peso è vuota
                if (pesoStringa.isEmpty()) {
                    ToastPersonalizzato.ToastErrore("Non stiamo pesando l'aria.",inflater);
                    return; // Esci dal metodo in caso di peso non valido
                }

                //gestisci eccezione
                try {
                    pesoInserito = Float.parseFloat(pesoStringa);
                } catch (NumberFormatException e) {
                    ToastPersonalizzato.ToastErrore("Formato non valido.", inflater);
                    return; // Esci dal metodo in caso di formato non valido
                }

                if (pesoInserito != 0) {
                   ToastPersonalizzato.ToastSuccessoCalendario(inflater);
                } else {
                    //metti l animazione nel toast
                    // Infla il layout personalizzato
                    ToastPersonalizzato.ToastErrore("Pesi 0 kg? Inserisci >0.",inflater);

                    return; // Esci dal metodo in caso di peso uguale a zero
                }

                finalPesoStorage.setPesoKg(pesoInserito);
                finalPesoStorage.setCalendario(dataSalvare);
                finalPesoStorage.setNote(noteDettaglio.getText().toString());


                Peso pOld=pdao.getPesoPerData(Global.ConversioneCalendarString(dataSalvare));

                if(pOld==null)
                    pdao.insertPeso(finalPesoStorage);
                else{
                    pOld.setNote(finalPesoStorage.getNote());
                    pOld.setPesoKg(finalPesoStorage.getPesoKg());
                    pOld.setCalendario(finalPesoStorage.getCalendario());
                    pdao.updatePeso(pOld);
                }



                //alertDialog.dismiss(); // Chiudi il dialog
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss(); // Chiudi il dialog

            }
        });




    }
    public static void NotificaMisure(LayoutInflater inflater,SharedPreferences sh) throws Eccezioni{

        View dialogView = inflater.inflate(R.layout.misure_dettaglio, null);
        Button salvaButton=dialogView.findViewById((int)R.id.SalvaMisure);
        Button okButton=dialogView.findViewById((int)R.id.OkMisure);
        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);
        builder.setCancelable(false);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



        EditText braccioDx=dialogView.findViewById(R.id.braccioDX);
        EditText braccioSx=dialogView.findViewById(R.id.braccioSX);
        EditText gambaDx=dialogView.findViewById(R.id.gambaDX);
        EditText gambaSx=dialogView.findViewById(R.id.gambaSX);
        EditText petto=dialogView.findViewById(R.id.petto);
        EditText spalle=dialogView.findViewById(R.id.spalle);
        EditText addome=dialogView.findViewById(R.id.addome);
        EditText fianchi=dialogView.findViewById((int)R.id.fianchi);
        EditText noteDettaglio=dialogView.findViewById((int)R.id.noteMisure);
        CalendarView calendario=dialogView.findViewById((int)R.id.calendarioMisure);

        HashMap<String,View> mappa=new HashMap<>();
        mappa.put("braccioDx",braccioDx);
        mappa.put("braccioSx",braccioSx);
        mappa.put("gambaDx",gambaDx);
        mappa.put("gambaSx",gambaSx);
        mappa.put("petto",petto);
        mappa.put("spalle",spalle);
        mappa.put("addome",addome);
        mappa.put("fianchi",fianchi);
        mappa.put("note",noteDettaglio);
        mappa.put("data",calendario);
        if(!Registrazione_Pag2.editGlobal){
            braccioDx.setFocusable(false);
            braccioDx.setClickable(false);
            braccioSx.setFocusable(false);
            braccioSx.setClickable(false);
            gambaDx.setFocusable(false);
            gambaDx.setClickable(false);
            gambaSx.setFocusable(false);
            gambaSx.setClickable(false);
            petto.setFocusable(false);
            petto.setClickable(false);
            addome.setFocusable(false);
            addome.setClickable(false);
            spalle.setFocusable(false);
            spalle.setClickable(false);
            fianchi.setFocusable(false);
            fianchi.setClickable(false);
            calendario.setFocusable(false);
            calendario.setClickable(false);
            noteDettaglio.setFocusable(false);
            noteDettaglio.setClickable(false);

            salvaButton.setVisibility(View.GONE);
            okButton.setText("Back");
        }


        MisureDAO mdao=new MisureDAO(dialogView.getContext());
        Misure misureStorage=new Misure();
        ArrayList<Misure> arr=mdao.getMisureInfo();
        if(arr.size()>0){
            misureStorage=arr.get(0);
        }



        //scorro tutte le proprietà del istanza
        //prendo le proprietà

        if(misureStorage.getId()!=0) {
            Field[] campi = misureStorage.getClass().getDeclaredFields();
            for (Field campo : campi) {
                campo.setAccessible(true); // Per accedere a campi privati
                try {
                    //prendo il valore del campo
                    Object valoreCampo = campo.get(misureStorage);
                    //se il campo è float e il valore è !=0 allora c è qualcosa
                    if ((campo.getType() == float.class && (float) valoreCampo != 0) ||
                            (campo.getType() == String.class && valoreCampo != null)) {
                        //prendo l'edit text da aggiornare e setto il testo
                        ((EditText) mappa.get(campo.getName())).setText(valoreCampo.toString());
                    } else if (campo.getType() == Calendar.class && valoreCampo != null) {
                        ((CalendarView) mappa.get(campo.getName())).setDate(((Calendar) valoreCampo).getTimeInMillis());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


        Calendar dataSalvare=misureStorage.getData();
        ImpostaCalendario(calendario,dialogView,dataSalvare,new Misure());


        Misure finalMisureStorage = misureStorage;
        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Ottenere i valori delle misure
                    float valoreBracicoDx = braccioDx.getText().toString().trim().length() != 0 ? Float.parseFloat(braccioDx.getText().toString()) : 0;
                    finalMisureStorage.setBraccioDx(valoreBracicoDx);


                    float valoreBracicoSx = braccioSx.getText().toString().trim().length() != 0 ? Float.parseFloat(braccioSx.getText().toString()) : 0;
                    finalMisureStorage.setBraccioSx(valoreBracicoSx);


                    float valoreGambaDx = gambaDx.getText().toString().trim().length() != 0 ? Float.parseFloat(gambaDx.getText().toString()) : 0;
                    finalMisureStorage.setGambaDx(valoreGambaDx);


                    float valoreGambaSx = gambaSx.getText().toString().trim().length() != 0 ? Float.parseFloat(gambaSx.getText().toString()) : 0;
                    finalMisureStorage.setGambaSx(valoreGambaSx);


                    float valorePetto = petto.getText().toString().trim().length() != 0 ? Float.parseFloat(petto.getText().toString()) : 0;
                    finalMisureStorage.setPetto(valorePetto);


                    float valoreSpalle = spalle.getText().toString().trim().length() != 0 ? Float.parseFloat(spalle.getText().toString()) : 0;
                    finalMisureStorage.setSpalle(valoreSpalle);


                    float valoreAddome = addome.getText().toString().trim().length() != 0 ? Float.parseFloat(addome.getText().toString()) : 0;
                    finalMisureStorage.setAddome(valoreAddome);


                    float valoreFianchi = fianchi.getText().toString().trim().length() != 0 ? Float.parseFloat(fianchi.getText().toString()) : 0;
                    finalMisureStorage.setFianchi(valoreFianchi);


                    // Controllare se almeno una misura è stata inserita correttamente e tutte le altre sono 0
                    boolean almenoUnaMisuraInserita = valoreBracicoDx != 0 || valoreBracicoSx != 0 || valoreGambaDx != 0 ||
                            valoreGambaSx != 0 || valorePetto != 0 || valoreSpalle != 0 || valoreAddome != 0 || valoreFianchi != 0;

                    if (!almenoUnaMisuraInserita) {
                        ToastPersonalizzato.ToastErrore("Prendi quel metro altrimenti le misure saranno 0 cm! Inserisci almeno una misura.",inflater);
                        return; // Esci dal metodo
                    }

                    finalMisureStorage.setNote(noteDettaglio.getText().toString());
                    finalMisureStorage.setData(dataSalvare);


                    // Mostra il Toast solo se tutte le misure sono state inserite correttamente
                    ToastPersonalizzato.ToastSuccessoCalendario(inflater);
                    Misure mOld=mdao.getMisureperData(Global.ConversioneCalendarString(dataSalvare));

                    if(mOld==null)
                        mdao.insertMisure(finalMisureStorage);
                    else{
                        mOld.setNote(finalMisureStorage.getNote());
                        mOld.setFianchi(finalMisureStorage.getFianchi());
                        mOld.setAddome(finalMisureStorage.getAddome());
                        mOld.setPetto(finalMisureStorage.getPetto());
                        mOld.setSpalle(finalMisureStorage.getSpalle());
                        mOld.setBraccioDx(finalMisureStorage.getBraccioDx());
                        mOld.setBraccioSx(finalMisureStorage.getBraccioSx());
                        mOld.setGambaDx(finalMisureStorage.getGambaDx());
                        mOld.setGambaSx(finalMisureStorage.getGambaSx());
                        mOld.setData(finalMisureStorage.getData());

                        mdao.updateMisure(mOld);
                    }





                } catch (NumberFormatException e) {
                    // Eccezione se il formato della misura non è valido
                    Toast.makeText(dialogView.getContext(), "Formato misura non valido.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    // Altro tipo di eccezione
                    e.printStackTrace();
                }
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss(); // Chiudi il dialog
            }
        });



    }
    public static void NotificaKcal(LayoutInflater inflater,SharedPreferences sh) throws Eccezioni{

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.kcal_dettaglio, null);
        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);
        builder.setCancelable(false);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        //prendo gli edit
        EditText kcalAttuali=dialogView.findViewById((int)R.id.kcalAttual);
        kcalAttuali.setText(null);
        RadioGroup radiogroup=dialogView.findViewById((int)R.id.radioGroup);
        RadioButton radioMassa=dialogView.findViewById((int)R.id.radioMassa);
        RadioButton radioNormo=dialogView.findViewById((int)R.id.radioNormo);
        RadioButton radioDeficit=dialogView.findViewById((int)R.id.radioDeficit);
        RadioButton radioRicomposizione=dialogView.findViewById((int)R.id.radioRicomposizione);
        EditText carbo=dialogView.findViewById((int)R.id.carbo);
        EditText proteine=dialogView.findViewById((int)R.id.proteine);
        EditText grassi=dialogView.findViewById((int)R.id.grassi);
        EditText sale=dialogView.findViewById((int)R.id.sale);
        EditText acqua=dialogView.findViewById((int)R.id.acqua);
        EditText noteDettaglio=dialogView.findViewById((int)R.id.noteDettaglio);
        CalendarView calendario=dialogView.findViewById((int)R.id.calendarioKcal);
        Button salvaButton=dialogView.findViewById((int)R.id.SalvaKcal);
        Button okButton=dialogView.findViewById((int)R.id.OkKcal);
        if(!Registrazione_Pag2.editGlobal){
            kcalAttuali.setFocusable(false);
            kcalAttuali.setClickable(false);

            radiogroup.setFocusable(false);
            radiogroup.setClickable(false);
            radioMassa.setFocusable(false);
            radioMassa.setClickable(false);
            radioDeficit.setFocusable(false);
            radioDeficit.setClickable(false);
            radioNormo.setFocusable(false);
            radioNormo.setClickable(false);
            radioRicomposizione.setFocusable(false);
            radioRicomposizione.setClickable(false);


            carbo.setClickable(false);
            carbo.setFocusable(false);
            proteine.setClickable(false);
            proteine.setFocusable(false);
            grassi.setClickable(false);
            grassi.setFocusable(false);
            sale.setClickable(false);
            sale.setFocusable(false);
            acqua.setFocusable(false);
            acqua.setClickable(false);

            calendario.setFocusable(false);
            calendario.setClickable(false);
            noteDettaglio.setFocusable(false);
            noteDettaglio.setClickable(false);
            salvaButton.setVisibility(View.GONE);
            okButton.setText("Back");
        }

        HashMap<String,View> mappa=new HashMap<>();

        mappa.put("kcal",kcalAttuali);
        //mappa.put("fase",);
        mappa.put("MASSA",radioMassa);
        mappa.put("NORMO",radioNormo);
        mappa.put("DEFINIZIONE",radioDeficit);
        mappa.put("RICOMPOSIZIONE",radioRicomposizione);

        mappa.put("carbo",carbo);
        mappa.put("proteine",proteine);
        mappa.put("grassi",grassi);
        mappa.put("sale",sale);
        mappa.put("acqua",acqua);
        mappa.put("note",noteDettaglio);
        mappa.put("data",calendario);


        kcalDAO kdao=new kcalDAO(dialogView.getContext());
        Kcal kcalStorage=new Kcal();
        ArrayList<Kcal> arrK=kdao.getKcalInfo();
        if(arrK.size()>0){
            kcalStorage=arrK.get(0);
        }





        Field[] campi = kcalStorage.getClass().getDeclaredFields();
        for (Field campo : campi) {
            campo.setAccessible(true); // Per accedere a campi privati
            try {
                //prendo il valore del campo
                Object valoreCampo = campo.get(kcalStorage);


                //se il campo è float allora carbo,proteine,grassi,sale,acqua
                if (campo.getType()==float.class&& (float)valoreCampo != 0) {
                    //prendo l'edit text da aggiornare e setto il testo
                    ((EditText) mappa.get(campo.getName())).setText(valoreCampo.toString());

                    //aggiorna i radio button chekkati
                }else if(campo.getType()== Kcal.Fase.class&&valoreCampo!=null){
                    ((RadioButton)mappa.get(valoreCampo.toString())).setChecked(true);
                }

                else if(campo.getType()==int.class){
                    ((EditText)mappa.get(campo.getName())).setText(valoreCampo.toString());
                }

                else if(campo.getType()==String.class&&valoreCampo!=null){
                    ((EditText)mappa.get(campo.getName())).setText(valoreCampo.toString());
                }

                else if(campo.getType()==Calendar.class&&valoreCampo!=null){
                    ((CalendarView)mappa.get(campo.getName())).setDate(((Calendar)valoreCampo).getTimeInMillis());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Calendar dataSalvare=kcalStorage.getData();
        ImpostaCalendario(calendario,dialogView,dataSalvare,new Kcal());


        Kcal finalKcalStorage = kcalStorage;
        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Imposta le kcal attuali
                    int kcalAttualiValue = Integer.parseInt(kcalAttuali.getText().toString().trim().length() != 0 ? kcalAttuali.getText().toString() : "0");
                    finalKcalStorage.setKcal(kcalAttualiValue);

                    // Imposta la fase delle kcal
                    if (radioMassa.isChecked()) {
                        finalKcalStorage.setFase(Kcal.Fase.MASSA);
                    } else if (radioNormo.isChecked()) {
                        finalKcalStorage.setFase(Kcal.Fase.NORMO);
                    } else if (radioDeficit.isChecked()) {
                        finalKcalStorage.setFase(Kcal.Fase.DEFINIZIONE);
                    } else {
                        finalKcalStorage.setFase(Kcal.Fase.RICOMPOSIZIONE);
                    }

                    // Imposta gli altri valori
                    finalKcalStorage.setCarbo(Float.parseFloat(carbo.getText().toString().trim().length() != 0 ? carbo.getText().toString() : "0"));
                    finalKcalStorage.setProteine(Float.parseFloat(proteine.getText().toString().trim().length() != 0 ? proteine.getText().toString() : "0"));
                    finalKcalStorage.setSale(Float.parseFloat(sale.getText().toString().trim().length() != 0 ? sale.getText().toString() : "0"));
                    finalKcalStorage.setGrassi(Float.parseFloat(grassi.getText().toString().trim().length() != 0 ? grassi.getText().toString() : "0"));
                    finalKcalStorage.setAcqua(Float.parseFloat(acqua.getText().toString().trim().length() != 0 ? acqua.getText().toString() : "0"));
                    finalKcalStorage.setNote(noteDettaglio.getText().toString());
                    finalKcalStorage.setData(dataSalvare);


                    Kcal kOld=kdao.getKcalPerData(Global.ConversioneCalendarString(dataSalvare));

                    if(kOld==null)
                        kdao.insertKcal(finalKcalStorage);
                    else{
                        finalKcalStorage.setId(kOld.getId());

                        kdao.updateKcal(finalKcalStorage);
                    }



                    // Controlla se le kcal attuali sono pari a 0

                    if (kcalAttualiValue == 0) {
                        ToastPersonalizzato.ToastErrore("Le kcal pari a 0? Digiuno estremo?",inflater);

                    } else {
                        // Mostra il Toast di successo se tutte le operazioni sono andate a buon fine
                        ToastPersonalizzato.ToastSuccessoCalendario(inflater);
                    }
                } catch (NumberFormatException e) {
                    // Gestisci l'eccezione se il formato non è valido
                    Toast.makeText(dialogView.getContext(), "Formato non valido.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    // Gestisci altre eccezioni
                    e.printStackTrace();
                }
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss(); // Chiudi il dialog
            }
        });

    }
    public static void NotificaImmagine(LayoutInflater inflater, Drawable immagine){
        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.immagine_big, null);
        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000"))); // Semi-trasparente nero

        ImageView immagineSet=dialogView.findViewById(R.id.immagineIngrandita);
        ImageButton closeButton=dialogView.findViewById(R.id.btnChiudi);

        immagineSet.setImageDrawable(immagine);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss(); // Chiudi il dialog

            }
        });

    }

    public static AlertDialog NotificaImmagineZoom(LayoutInflater inflater, Drawable immagine){
        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.immagine_big, null);
        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000"))); // Semi-trasparente nero

        ImageView immagineSet=dialogView.findViewById(R.id.immagineIngrandita);
        ImageButton closeButton=dialogView.findViewById(R.id.btnChiudi);
        closeButton.setVisibility(View.GONE);

        immagineSet.setImageDrawable(immagine);


        return alertDialog;

    }

    public static void NotificaImmaginePremutaIngrandisci(LayoutInflater inflater,ImageView immagineUtente){
        // TODO test
        /*
        System.out.println("sono in zoom");
        final AlertDialog[] alertNotifica = {null};
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                alertNotifica[0] = NotificaImmagineZoom(inflater, immagineUtente.getDrawable());
            }
        };

        immagineUtente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Inizia il conteggio dei 2 secondi
                        handler.postDelayed(runnable, 700);
                        break;
                    case MotionEvent.ACTION_UP:
                        // Annulla l'ingrandimento se il tempo di pressione è inferiore a 2 secondi
                        handler.removeCallbacks(runnable);
                        if (alertNotifica[0] != null) {
                            alertNotifica[0].dismiss();
                        }
                        break;
                }
                return true;  // Restituisci true per indicare che l'evento è stato gestito
            }
        });

         */

    }

    public static void NotificaNote(LayoutInflater inflater,SharedPreferences sh,String tipoNotifica) throws Eccezioni {

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.note_dettaglio, null);
        Button salvaButton = dialogView.findViewById((int) R.id.SalvaNote);
        Button okButton = dialogView.findViewById((int) R.id.OkNote);
        EditText noteDettaglio = dialogView.findViewById((int) R.id.noteDettaglio);
        if(!Registrazione_Pag2.editGlobal){
            //non far scrivere (QUESTO è L'ERRORE CHE NON FA VISUALIZZARE LE NOTE IN EDIT, PERò SE LEVI QUESTO POI VENGONO SALVATE LE STESSE NOTE
            // ANCHE IN SCHEDA, GIONI ED ESERCIZI PRESUMO)
            noteDettaglio.setInputType(InputType.TYPE_NULL);
            //rimuovi il tasto salva
            salvaButton.setVisibility(View.GONE);
        }

        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);
        // Impedisci la chiusura premendo al di fuori della finestra di dialogo
        builder.setCancelable(false);

        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        final View backgroundView = alertDialog.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**/
                return true; // Indica che l'evento è stato consumato
            }
        });

        Note noteStorage=new Note();
        switch (tipoNotifica){
            case COSTANTI.NOTE_REGISTRAZIONE:
                noteStorage=Note.fromJson(sh.getString(COSTANTI.NOTE_REGISTRAZIONE,null));
                break;
            case COSTANTI.NOTE_PESO:
                noteStorage=Note.fromJson(sh.getString(COSTANTI.NOTE_PESO,null));
                break;
            case COSTANTI.NOTE_MISURE:
                noteStorage=Note.fromJson(sh.getString(COSTANTI.NOTE_MISURE,null));
                break;
            case COSTANTI.NOTE_KCAL:
                noteStorage=Note.fromJson(sh.getString(COSTANTI.NOTE_KCAL,null));
                break;
            case COSTANTI.NOTE_SCHEDA:
                noteStorage=Note.fromJson(sh.getString(COSTANTI.NOTE_SCHEDA,null));
                break;
            case COSTANTI.NOTE_GIORNO:
                noteStorage=Note.fromJson(sh.getString(COSTANTI.NOTE_GIORNO,null));
                break;
            case COSTANTI.NOTE_ESERCIZIO:
                noteStorage=Note.fromJson(sh.getString(COSTANTI.NOTE_ESERCIZIO,null));
                break;

        }


        if(noteStorage.getNote()!=null){
            noteDettaglio.setText(noteStorage.getNote());
        }else noteDettaglio.setText("");

        if(Registrazione_Pag2.editGlobal){
            Note finalNoteStorage = noteStorage;
            salvaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalNoteStorage.setNote(noteDettaglio.getText().toString());
                    SharedPreferences.Editor edi = sh.edit();
                    edi.putString(tipoNotifica, finalNoteStorage.toJson());
                    edi.apply();

                    ToastPersonalizzato.ToastSuccesso("Note salvate...",inflater);
                }
            });
        }

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss(); // Chiudi il dialog
                }
            });
        // Interceptarre il tasto Indietro(Funziona se vuoi attivare il tasto back di sistema, al momento
        // è disattivato per il comando setCancelable
        /*alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // Chiudi il dialog solo se l'utente preme il tasto Indietro
                    alertDialog.dismiss();
                    return true;
                }
                return false;
            }
        });*/
    }

    private static void SettaVarDialogView(Object oggettoC,View dialogView,Object ogg){
        if (oggettoC.getClass() == Kcal.class) {
            Kcal oggettoKcal = (Kcal) ogg;

            // Prendi gli EditText e altri elementi dalla View del dialog
            EditText kcalAttuali = dialogView.findViewById(R.id.kcalAttual);
            RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
            RadioButton radioMassa = dialogView.findViewById(R.id.radioMassa);
            RadioButton radioNormo = dialogView.findViewById(R.id.radioNormo);
            RadioButton radioDeficit = dialogView.findViewById(R.id.radioDeficit);
            RadioButton radioRicomposizione = dialogView.findViewById(R.id.radioRicomposizione);
            EditText carbo = dialogView.findViewById(R.id.carbo);
            EditText proteine = dialogView.findViewById(R.id.proteine);
            EditText grassi = dialogView.findViewById(R.id.grassi);
            EditText sale = dialogView.findViewById(R.id.sale);
            EditText acqua = dialogView.findViewById(R.id.acqua);
            EditText noteDettaglio = dialogView.findViewById(R.id.noteDettaglio);
            CalendarView calendario = dialogView.findViewById(R.id.calendarioKcal);
            // Imposta i valori dell'oggetto Kcal nei rispettivi elementi della View
            if(oggettoKcal.getKcal()!=0)kcalAttuali.setText(String.valueOf(oggettoKcal.getKcal()));
                else kcalAttuali.setText("");

            switch (oggettoKcal.getFase()) {
                case MASSA:
                    radioMassa.setChecked(true);
                    break;
                case NORMO:
                    radioNormo.setChecked(true);
                    break;
                case DEFINIZIONE:
                    radioDeficit.setChecked(true);
                    break;
                case RICOMPOSIZIONE:
                    radioRicomposizione.setChecked(true);
                    break;
                default:
                    radioNormo.setChecked(true);
                    break;
            }

            if(oggettoKcal.getCarbo()!=0)carbo.setText(String.valueOf(oggettoKcal.getCarbo()));
                    else carbo.setText("");
            if(oggettoKcal.getProteine()!=0)proteine.setText(String.valueOf(oggettoKcal.getProteine()));
                else proteine.setText("");
            if(oggettoKcal.getGrassi()!=0)grassi.setText(String.valueOf(oggettoKcal.getGrassi()));
                else grassi.setText("");
            if(oggettoKcal.getSale()!=0)sale.setText(String.valueOf(oggettoKcal.getSale()));
                else sale.setText("");
            if(oggettoKcal.getAcqua()!=0)acqua.setText(String.valueOf(oggettoKcal.getAcqua()));
                else acqua.setText("");
            noteDettaglio.setText(oggettoKcal.getNote());

            // Imposta la data nel CalendarView
            if (calendario != null) {
                Calendar dataKcal = oggettoKcal.getData();
                if (dataKcal != null) {
                    calendario.setDate(dataKcal.getTimeInMillis());
                }
            }
        }else if(oggettoC.getClass()==Misure.class){
            Misure oggettoMisure = (Misure) ogg;
            EditText braccioDx=dialogView.findViewById(R.id.braccioDX);
            EditText braccioSx=dialogView.findViewById(R.id.braccioSX);
            EditText gambaDx=dialogView.findViewById(R.id.gambaDX);
            EditText gambaSx=dialogView.findViewById(R.id.gambaSX);
            EditText petto=dialogView.findViewById(R.id.petto);
            EditText spalle=dialogView.findViewById(R.id.spalle);
            EditText addome=dialogView.findViewById(R.id.addome);
            EditText fianchi=dialogView.findViewById((int)R.id.fianchi);
            EditText noteDettaglio=dialogView.findViewById((int)R.id.noteMisure);
            CalendarView calendario=dialogView.findViewById((int)R.id.calendarioMisure);

            // Impostare i valori negli EditText
            if(oggettoMisure.getBraccioDx()!=0)braccioDx.setText(String.valueOf(oggettoMisure.getBraccioDx()));
                else braccioDx.setText("");
            if(oggettoMisure.getBraccioSx()!=0)braccioSx.setText(String.valueOf(oggettoMisure.getBraccioSx()));
                else braccioSx.setText("");
            if(oggettoMisure.getGambaDx()!=0)gambaDx.setText(String.valueOf(oggettoMisure.getGambaDx()));
                else gambaDx.setText("");
            if(oggettoMisure.getBraccioSx()!=0)gambaSx.setText(String.valueOf(oggettoMisure.getGambaSx()));
                else gambaSx.setText("");
            if(oggettoMisure.getPetto()!=0)petto.setText(String.valueOf(oggettoMisure.getPetto()));
                else petto.setText("");
            if(oggettoMisure.getSpalle()!=0)spalle.setText(String.valueOf(oggettoMisure.getSpalle()));
                else spalle.setText("");
            if(oggettoMisure.getAddome()!=0)addome.setText(String.valueOf(oggettoMisure.getAddome()));
                else addome.setText("");
            if(oggettoMisure.getFianchi()!=0)fianchi.setText(String.valueOf(oggettoMisure.getFianchi()));
                else fianchi.setText("");
            noteDettaglio.setText(oggettoMisure.getNote());

            // Impostare la data nel CalendarView
            calendario.setDate(oggettoMisure.getData().getTimeInMillis());
        }else if(oggettoC.getClass()==Peso.class){
            Peso peso=(Peso)ogg;
            CalendarView calendario=dialogView.findViewById((int)R.id.calendarioPeso);
            EditText kiliEdit= dialogView.findViewById(R.id.pesoAttual);
            EditText noteDettaglio=dialogView.findViewById((int)R.id.notePeso);

            // Impostare il valore di peso e note nei rispettivi EditText
            if(peso.getPesoKg()!=0)kiliEdit.setText(String.valueOf(peso.getPesoKg()));
            else kiliEdit.setText("");
            noteDettaglio.setText(peso.getNote());
            calendario.setDate(peso.getCalendario().getTimeInMillis());

        }else if(oggettoC.getClass() == Fisico.class) {
            System.out.println("ci sono le img");
            Fisico fisico = (Fisico) ogg;
            CalendarView calendario = dialogView.findViewById(R.id.calendarioFisico);
            EditText posaText1 = dialogView.findViewById(R.id.posaFisico);
            EditText posaText2 = dialogView.findViewById(R.id.posaFisico2);
            EditText posaText3 = dialogView.findViewById(R.id.posaFisico3);
            EditText posaText4 = dialogView.findViewById(R.id.posaFisico4);
            EditText posaText5 = dialogView.findViewById(R.id.posaFisico5);
            EditText posaText6 = dialogView.findViewById(R.id.posaFisico6);
            EditText posaText7 = dialogView.findViewById(R.id.posaFisico7);
            EditText noteDettaglio = dialogView.findViewById(R.id.noteFisico);

            ImageButton immagineFisico = dialogView.findViewById(R.id.immaginefisico);
            ImageButton immagineFisico2 = dialogView.findViewById(R.id.immaginefisico2);
            ImageButton immagineFisico3 = dialogView.findViewById(R.id.immaginefisico3);
            ImageButton immagineFisico4 = dialogView.findViewById(R.id.immaginefisico4);
            ImageButton immagineFisico5 = dialogView.findViewById(R.id.immaginefisico5);
            ImageButton immagineFisico6 = dialogView.findViewById(R.id.immaginefisico6);
            ImageButton immagineFisico7 = dialogView.findViewById(R.id.immaginefisico7);

            // Impostare il valore di peso e note nei rispettivi EditText
            ArrayList<Fisico_Immagini> mappa = fisico.getPosa_immagine();
            mappa.get(0).setImmagineBRiferimento(immagineFisico);
            mappa.get(1).setImmagineBRiferimento(immagineFisico2);
            mappa.get(2).setImmagineBRiferimento(immagineFisico3);
            mappa.get(3).setImmagineBRiferimento(immagineFisico4);
            mappa.get(4).setImmagineBRiferimento(immagineFisico5);
            mappa.get(5).setImmagineBRiferimento(immagineFisico6);
            mappa.get(6).setImmagineBRiferimento(immagineFisico7);

            for (Fisico_Immagini entry : mappa) {
                switch (entry.getPosizione()) {
                    case 0:
                        posaText1.setText(controlloStringaFisico(entry.getNomePosa()));
                        mappa.get(0).getImmagineBRiferimento().setImageDrawable(Global.byteArrayToDrawable(
                                entry.getImmagine() != null ? entry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 1:
                        posaText2.setText(controlloStringaFisico(entry.getNomePosa()));
                        mappa.get(1).getImmagineBRiferimento().setImageDrawable(Global.byteArrayToDrawable(
                                entry.getImmagine() != null ? entry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 2:
                        posaText3.setText(controlloStringaFisico(entry.getNomePosa()));
                        mappa.get(2).getImmagineBRiferimento().setImageDrawable(Global.byteArrayToDrawable(
                                entry.getImmagine() != null ? entry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 3:
                        posaText4.setText(controlloStringaFisico(entry.getNomePosa()));
                        mappa.get(3).getImmagineBRiferimento().setImageDrawable(Global.byteArrayToDrawable(
                                entry.getImmagine() != null ? entry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 4:
                        posaText5.setText(controlloStringaFisico(entry.getNomePosa()));
                        mappa.get(4).getImmagineBRiferimento().setImageDrawable(Global.byteArrayToDrawable(
                                entry.getImmagine() != null ? entry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 5:
                        posaText6.setText(controlloStringaFisico(entry.getNomePosa()));
                        mappa.get(5).getImmagineBRiferimento().setImageDrawable(Global.byteArrayToDrawable(
                                entry.getImmagine() != null ? entry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                    case 6:
                        posaText7.setText(controlloStringaFisico(entry.getNomePosa()));
                        mappa.get(6).getImmagineBRiferimento().setImageDrawable(Global.byteArrayToDrawable(
                                entry.getImmagine() != null ? entry.getImmagine() : Global.drawableToByteArray(dialogView.getResources().getDrawable(R.drawable.noimg))));
                        break;
                }
            }

            noteDettaglio.setText(fisico.getNote());
            calendario.setDate(fisico.getCalendario().getTimeInMillis());
        }


    }

    private static void SettaTextAVuoto(Object oggettoC,View dialogView,Object ogg){
        if (oggettoC.getClass() == Kcal.class) {


            // Prendi gli EditText e altri elementi dalla View del dialog
            EditText kcalAttuali = dialogView.findViewById(R.id.kcalAttual);
            RadioButton radioNormo = dialogView.findViewById(R.id.radioNormo);
            EditText carbo = dialogView.findViewById(R.id.carbo);
            EditText proteine = dialogView.findViewById(R.id.proteine);
            EditText grassi = dialogView.findViewById(R.id.grassi);
            EditText sale = dialogView.findViewById(R.id.sale);
            EditText acqua = dialogView.findViewById(R.id.acqua);
            EditText noteDettaglio = dialogView.findViewById(R.id.noteDettaglio);

            // Imposta i valori dell'oggetto Kcal nei rispettivi elementi della View
            kcalAttuali.setText("");
            radioNormo.setChecked(true);
            carbo.setText("");
            proteine.setText("");
            grassi.setText("");
            sale.setText("");
            acqua.setText("");
            noteDettaglio.setText("");


        }else if(oggettoC.getClass()==Misure.class){

            EditText braccioDx=dialogView.findViewById(R.id.braccioDX);
            EditText braccioSx=dialogView.findViewById(R.id.braccioSX);
            EditText gambaDx=dialogView.findViewById(R.id.gambaDX);
            EditText gambaSx=dialogView.findViewById(R.id.gambaSX);
            EditText petto=dialogView.findViewById(R.id.petto);
            EditText spalle=dialogView.findViewById(R.id.spalle);
            EditText addome=dialogView.findViewById(R.id.addome);
            EditText fianchi=dialogView.findViewById((int)R.id.fianchi);
            EditText noteDettaglio=dialogView.findViewById((int)R.id.noteMisure);


            // Impostare i valori negli EditText
            braccioDx.setText("");
            braccioSx.setText("");
            gambaDx.setText("");
            gambaSx.setText("");
            petto.setText("");
            spalle.setText("");
            addome.setText("");
            fianchi.setText("");
            noteDettaglio.setText("");
        }else if(oggettoC.getClass()==Peso.class){

            EditText kiliEdit= dialogView.findViewById(R.id.pesoAttual);
            EditText noteDettaglio=dialogView.findViewById((int)R.id.notePeso);
            // Impostare il valore di peso e note nei rispettivi EditText
            kiliEdit.setText("");
            noteDettaglio.setText("");


        }else if(oggettoC.getClass() == Fisico.class) {

            EditText posaText1 = dialogView.findViewById(R.id.posaFisico);
            EditText posaText2 = dialogView.findViewById(R.id.posaFisico2);
            EditText posaText3 = dialogView.findViewById(R.id.posaFisico3);
            EditText posaText4 = dialogView.findViewById(R.id.posaFisico4);
            EditText posaText5 = dialogView.findViewById(R.id.posaFisico5);
            EditText posaText6 = dialogView.findViewById(R.id.posaFisico6);
            EditText posaText7 = dialogView.findViewById(R.id.posaFisico7);

            ImageButton immagineFisico11 = dialogView.findViewById(R.id.immaginefisico);
            ImageButton immagineFisico22 = dialogView.findViewById(R.id.immaginefisico2);
            ImageButton immagineFisico33 = dialogView.findViewById(R.id.immaginefisico3);
            ImageButton immagineFisico44 = dialogView.findViewById(R.id.immaginefisico4);
            ImageButton immagineFisico55 = dialogView.findViewById(R.id.immaginefisico5);
            ImageButton immagineFisico66 = dialogView.findViewById(R.id.immaginefisico6);
            ImageButton immagineFisico77 = dialogView.findViewById(R.id.immaginefisico7);

            EditText noteDettaglio = dialogView.findViewById(R.id.noteFisico);

            // Impostare il valore di peso e note nei rispettivi EditText
            posaText1.setText("");
            posaText2.setText("");
            posaText3.setText("");
            posaText4.setText("");
            posaText5.setText("");
            posaText6.setText("");
            posaText7.setText("");
            noteDettaglio.setText("");

            immagineFisico11.setImageResource(R.drawable.noimg);
            immagineFisico22.setImageResource(R.drawable.noimg);
            immagineFisico33.setImageResource(R.drawable.noimg);
            immagineFisico44.setImageResource(R.drawable.noimg);
            immagineFisico55.setImageResource(R.drawable.noimg);
            immagineFisico66.setImageResource(R.drawable.noimg);
            immagineFisico77.setImageResource(R.drawable.noimg);

            posa_immagine.get(0).setImmagine(null);
            posa_immagine.get(1).setImmagine(null);
            posa_immagine.get(2).setImmagine(null);
            posa_immagine.get(3).setImmagine(null);
            posa_immagine.get(4).setImmagine(null);
            posa_immagine.get(5).setImmagine(null);
            posa_immagine.get(6).setImmagine(null);
        }


    }

    private static void ImpostaCalendario(CalendarView calendario, View dialogView, Calendar dataSalvare,Object oggetto){
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            public void settaAll( int anno, int mese, int giorno){
                /*dammi *****************/
                Calendar calendarioMostra=Calendar.getInstance();
                calendarioMostra.set(anno,mese,giorno)
                ;
                if(oggetto.getClass()==Kcal.class) {

                    kcalDAO kcalDAO=new kcalDAO(dialogView.getContext());
                    Kcal oggKcal=kcalDAO.getKcalPerData(Global.ConversioneCalendarString(calendarioMostra));

                    if(oggKcal!=null){

                        SettaVarDialogView(oggetto,dialogView,oggKcal);
                    }else SettaTextAVuoto(oggetto,dialogView,oggKcal);

                }else if(oggetto.getClass()==Misure.class) {
                    MisureDAO misureDAO=new MisureDAO(dialogView.getContext());
                    Misure oggMisure=misureDAO.getMisureperData(Global.ConversioneCalendarString(calendarioMostra));
                    if(oggMisure!=null){

                        SettaVarDialogView(oggetto,dialogView,oggMisure);
                    }else SettaTextAVuoto(oggetto,dialogView,oggMisure);

                }else if(oggetto.getClass()==Peso.class) {
                    PesoDAO pesoDAO=new PesoDAO(dialogView.getContext());
                    Peso oggPeso=pesoDAO.getPesoPerData(Global.ConversioneCalendarString(calendarioMostra));
                    if(oggPeso!=null){
                        SettaVarDialogView(oggetto,dialogView,oggPeso);
                    }else SettaTextAVuoto(oggetto,dialogView,oggPeso);

                }else if(oggetto.getClass()==Fisico.class) {
                    FisicoDAO fdao=new FisicoDAO(dialogView.getContext());
                    Fisico oggFisico=fdao.getFisicoPerData(Global.ConversioneCalendarString(calendarioMostra));

                    /*********************chiama anche tutti di lista*********/
                    ListaImgFisicoDAO lfdao=new ListaImgFisicoDAO(dialogView.getContext());

                    if(oggFisico != null) {
                        ArrayList<Fisico_Immagini> listaim = lfdao.getImmaginiPerIdFisico(oggFisico.getId());

                        // Associo i bottoni alla lista
                        listaim.get(0).setImmagineBRiferimento(immagineFisico);
                        listaim.get(1).setImmagineBRiferimento(immagineFisico2);
                        listaim.get(2).setImmagineBRiferimento(immagineFisico3);
                        listaim.get(3).setImmagineBRiferimento(immagineFisico4);
                        listaim.get(4).setImmagineBRiferimento(immagineFisico5);
                        listaim.get(5).setImmagineBRiferimento(immagineFisico6);
                        listaim.get(6).setImmagineBRiferimento(immagineFisico7);

                        oggFisico.setPosa_immagine(listaim);

                        // Setto la var globale con le immagini
                        posa_immagine = oggFisico.getPosa_immagine();

                        SettaVarDialogView(oggetto, dialogView, oggFisico);
                    } else {
                        SettaTextAVuoto(oggetto, dialogView, oggFisico);
                    }


                }


            }
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int anno, int mese, int giorno) {
                settaAll(anno,mese,giorno);

                /****************************************************/
                Calendar calendarioAttuale = Calendar.getInstance();
                calendarioAttuale.set(Calendar.HOUR_OF_DAY, 0);
                calendarioAttuale.set(Calendar.MINUTE, 0);
                calendarioAttuale.set(Calendar.SECOND, 0);
                calendarioAttuale.set(Calendar.MILLISECOND, 0);

                Calendar calendarioSelezionato = Calendar.getInstance();
                calendarioSelezionato.set(anno, mese, giorno);
                calendarioSelezionato.set(Calendar.HOUR_OF_DAY, 0);
                calendarioSelezionato.set(Calendar.MINUTE, 0);
                calendarioSelezionato.set(Calendar.SECOND, 0);
                calendarioSelezionato.set(Calendar.MILLISECOND, 0);


                if (calendarioSelezionato.after(calendarioAttuale)) {
                    // Data selezionata è successiva alla data attuale
                    try {
                        calendario.setDate(calendarioAttuale.getTimeInMillis());
                        dataSalvare.setTimeInMillis(calendarioAttuale.getTimeInMillis());
                        //Calendar calendarioAttuale2 = Calendar.getInstance();

                        settaAll(calendarioAttuale.get(Calendar.YEAR),calendarioAttuale.get(Calendar.MONTH),calendarioAttuale.get(Calendar.DAY_OF_MONTH));
                        throw new Eccezioni(Eccezioni.tipiEccezioni.DATA_NON_VALIDA, dialogView);
                    } catch (Eccezioni e) {
                        e.printStackTrace();
                    }return;
                } else {
                    // Data selezionata è valida, puoi procedere con il salvataggio
                    dataSalvare.set(anno, mese, giorno);
                }

            }
        });

    }

    //popup Import
    public static void PopupImporta(LayoutInflater inflater, Activity act) {

        View dialogView = inflater.inflate(R.layout.importa_general_layout, null);
        Button salvaButton=dialogView.findViewById((int)R.id.SalvaMisure);
        Button okButton=dialogView.findViewById((int)R.id.OkMisure);
        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);
        builder.setCancelable(false);


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button dati = dialogView.findViewById(R.id.button_import_dati);
        Button scheda = dialogView.findViewById(R.id.button_import_scheda);
        Button chiudi = dialogView.findViewById((int)R.id.button_chiudi);

        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        scheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent per aprire il file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Tutti i tipi di file
                act.startActivityForResult(intent, 6);
                chiudi.callOnClick();
            }
        });

        dati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent per aprire il file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Tutti i tipi di file
                act.startActivityForResult(intent, 8);
                chiudi.callOnClick();
            }
        });






        

    }


}

