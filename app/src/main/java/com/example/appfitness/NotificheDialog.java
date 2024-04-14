package com.example.appfitness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Eccezioni.Eccezioni;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;

public class NotificheDialog {

    public static void NotificaPeso(LayoutInflater inflater, SharedPreferences sh) throws Eccezioni {

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.peso_dettaglio, null);
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
        //prendo l oggetto
        Peso pesoStorage= Peso.fromJson(sh.getString("pesoPassato",null));

        if(pesoStorage.getCalendario()==null)pesoStorage.setCalendario(Calendar.getInstance());
        //se il peso esiste ed è gia stato inserito allora mostralo
        if(pesoStorage.getPesoKg()!=0&&pesoStorage.getCalendario()!=null){
            calendario.setDate(pesoStorage.getCalendario().getTimeInMillis());
            kiliEdit.setText(String.valueOf(pesoStorage.getPesoKg()));
        }
        if(pesoStorage.getNote()!=null){
            noteDettaglio.setText(pesoStorage.getNote());
        }

        Calendar dataSalvare=pesoStorage.getCalendario();
        ImpostaCalendario(calendario,dialogView,dataSalvare);


        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("sono in on salva");
                //salva i dati che inserisci
                float pesoInserito=0;
                //gestisci eccezione
                try{

                    pesoInserito=Float.parseFloat(kiliEdit.getText().toString());
                }catch (NumberFormatException e){
                    try {
                        throw new Eccezioni(Eccezioni.tipiEccezioni.FORMATO_NON_VALIDO,dialogView);
                    } catch (Eccezioni ex) {
                        ex.printStackTrace();
                    }

                }

                if(pesoInserito!=0){
                    Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();
                }
                pesoStorage.setPesoKg(pesoInserito);
                pesoStorage.setCalendario(dataSalvare);
                pesoStorage.setNote(noteDettaglio.getText().toString());
                SharedPreferences.Editor edi=sh.edit();
                edi.putString("pesoPassato",pesoStorage.toJson());
                edi.apply();
                System.out.println("___peso "+pesoStorage);

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

        Misure misureStorage= Misure.fromJson(sh.getString("misurePassate",null));
        //scorro tutte le proprietà del istanza
        //prendo le proprietà
        Field[] campi = misureStorage.getClass().getDeclaredFields();
        for (Field campo : campi) {
            campo.setAccessible(true); // Per accedere a campi privati
            try {
                //prendo il valore del campo
                Object valoreCampo = campo.get(misureStorage);
                //se il campo è float e il valore è !=0 allora c è qualcosa
                if ((campo.getType()==float.class&& (float)valoreCampo != 0)||
                        (campo.getType()==String.class&&valoreCampo!=null)) {
                    //prendo l'edit text da aggiornare e setto il testo
                    ((EditText) mappa.get(campo.getName())).setText(valoreCampo.toString());
                } else if(campo.getType()==Calendar.class&&valoreCampo!=null){
                    ((CalendarView)mappa.get(campo.getName())).setDate(((Calendar)valoreCampo).getTimeInMillis());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        Calendar dataSalvare=misureStorage.getData();
        ImpostaCalendario(calendario,dialogView,dataSalvare);


        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    float valoreBracicoDx=braccioDx.getText().toString().trim().length()!=0?Float.parseFloat(braccioDx.getText().toString()):0;
                    misureStorage.setBraccioDx(valoreBracicoDx);
                    braccioDx.setText(String.valueOf(misureStorage.getBraccioDx()));

                    float valoreBracicoSx=braccioSx.getText().toString().trim().length()!=0?Float.parseFloat(braccioSx.getText().toString()):0;
                    misureStorage.setBraccioSx(valoreBracicoSx);
                    braccioSx.setText(String.valueOf(misureStorage.getBraccioSx()));

                    float valoreGambaDx=gambaDx.getText().toString().trim().length()!=0?Float.parseFloat(gambaDx.getText().toString()):0;
                    misureStorage.setGambaDx(valoreGambaDx);
                    gambaDx.setText(String.valueOf(misureStorage.getGambaDx()));

                    float valoreGambaSx=gambaSx.getText().toString().trim().length()!=0?Float.parseFloat(gambaSx.getText().toString()):0;
                    misureStorage.setGambaSx(valoreGambaSx);
                    gambaSx.setText(String.valueOf(misureStorage.getGambaSx()));

                    float valorePetto=petto.getText().toString().trim().length()!=0?Float.parseFloat(petto.getText().toString()):0;
                    misureStorage.setPetto(valorePetto);
                    petto.setText(String.valueOf(misureStorage.getPetto()));

                    float valoreSpalle=spalle.getText().toString().trim().length()!=0?Float.parseFloat(spalle.getText().toString()):0;
                    misureStorage.setSpalle(valoreSpalle);
                    spalle.setText(String.valueOf(misureStorage.getSpalle()));

                    float valoreAddome=addome.getText().toString().trim().length()!=0?Float.parseFloat(addome.getText().toString()):0;
                    misureStorage.setAddome(valoreAddome);
                    addome.setText(String.valueOf(misureStorage.getAddome()));

                    float valoreFianchi=fianchi.getText().toString().trim().length()!=0?Float.parseFloat(fianchi.getText().toString()):0;
                    misureStorage.setFianchi(valoreFianchi);
                    fianchi.setText(String.valueOf(misureStorage.getFianchi()));

                    misureStorage.setNote(noteDettaglio.getText().toString());
                    misureStorage.setData(dataSalvare);

                    System.out.println("___+"+misureStorage);



                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor edi=sh.edit();
                edi.putString("misurePassate",misureStorage.toJson());
                edi.apply();
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


        builder.setPositiveButton(null,null);
        builder.setNegativeButton(null,null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //prendo l oggetto
        Kcal kcalStorage= Kcal.fromJson(sh.getString("kcalPassate",null));
        if(kcalStorage.getData()==null)kcalStorage.setData(Calendar.getInstance());

        //prendo gli edit
        EditText kcalAttuali=dialogView.findViewById((int)R.id.kcalAttual);
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

        Field[] campi = kcalStorage.getClass().getDeclaredFields();
        for (Field campo : campi) {
            campo.setAccessible(true); // Per accedere a campi privati
            try {
                //prendo il valore del campo
                Object valoreCampo = campo.get(kcalStorage);
                System.out.println(campo.getName());
                System.out.println(valoreCampo);

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
        ImpostaCalendario(calendario,dialogView,dataSalvare);



        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kcalStorage.setKcal(Integer.parseInt(kcalAttuali.getText().toString().trim().length()!=0?kcalAttuali.getText().toString():"0"));
                if(radioMassa.isChecked()){
                    kcalStorage.setFase(Kcal.Fase.MASSA);
                }else if(radioNormo.isChecked()){
                    kcalStorage.setFase(Kcal.Fase.NORMO);
                }else if(radioDeficit.isChecked()){
                    kcalStorage.setFase(Kcal.Fase.DEFINIZIONE);
                }else{
                    kcalStorage.setFase(Kcal.Fase.RICOMPOSIZIONE);
                }
                kcalStorage.setCarbo(Float.parseFloat(carbo.getText().toString().trim().length()!=0?carbo.getText().toString():"0"));
                kcalStorage.setProteine(Float.parseFloat(proteine.getText().toString().trim().length()!=0?proteine.getText().toString():"0"));
                kcalStorage.setSale(Float.parseFloat(sale.getText().toString().trim().length()!=0?sale.getText().toString():"0"));
                kcalStorage.setGrassi(Float.parseFloat(grassi.getText().toString().trim().length()!=0?grassi.getText().toString():"0"));
                kcalStorage.setAcqua(Float.parseFloat(acqua.getText().toString().trim().length()!=0?acqua.getText().toString():"0"));
                kcalStorage.setNote(noteDettaglio.getText().toString());
                kcalStorage.setData(dataSalvare);

                SharedPreferences.Editor edi=sh.edit();
                edi.putString("kcalPassate",kcalStorage.toJson());
                edi.apply();

                Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();
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

    public static void NotificaNote(LayoutInflater inflater,SharedPreferences sh) throws Eccezioni {

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.note_dettaglio, null);
        Button salvaButton = dialogView.findViewById((int) R.id.SalvaNote);
        Button okButton = dialogView.findViewById((int) R.id.OkNote);
        EditText noteDettaglio = dialogView.findViewById((int) R.id.noteDettaglio);
        if(!Registrazione_Pag2.editGlobal){
            //non far scrivere
            noteDettaglio.setInputType(InputType.TYPE_NULL);
            //rimuovi il tasto salva
            salvaButton.setVisibility(View.GONE);
        }

        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);

        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        //prendo l oggetto
        Note noteStorage = Note.fromJson(sh.getString("notePassate", null));
        //prendo gli edit


        if(noteStorage.getNote()!=null){
            noteDettaglio.setText(noteStorage.getNote());
        }else noteDettaglio.setText("");
        if(Registrazione_Pag2.editGlobal){
            salvaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noteStorage.setNote(noteDettaglio.getText().toString());
                    SharedPreferences.Editor edi = sh.edit();
                    edi.putString("notePassate", noteStorage.toJson());
                    edi.apply();

                    Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();
                }
            });
        }

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss(); // Chiudi il dialog
                }
            });
    }


    private static void ImpostaCalendario(CalendarView calendario, View dialogView, Calendar dataSalvare){
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int anno, int mese, int giorno) {
                Calendar calendarioAtutale= Calendar.getInstance();
                System.out.println("dat "+ giorno + " m " + mese +"anno "+ anno);
                try{
                    if(anno>calendarioAtutale.get(Calendar.YEAR)){
                        calendario.setDate(calendarioAtutale.getTimeInMillis());
                        dataSalvare.setTimeInMillis(calendarioAtutale.getTimeInMillis());
                        throw new Eccezioni(Eccezioni.tipiEccezioni.DATA_NON_VALIDA,dialogView);
                    }
                    else if(mese>calendarioAtutale.get(Calendar.MONTH)){
                        calendario.setDate(calendarioAtutale.getTimeInMillis());
                        dataSalvare.setTimeInMillis(calendarioAtutale.getTimeInMillis());
                        throw new Eccezioni(Eccezioni.tipiEccezioni.DATA_NON_VALIDA,dialogView);
                    }
                    else if(giorno>calendarioAtutale.get(Calendar.DAY_OF_MONTH)){
                        calendario.setDate(calendarioAtutale.getTimeInMillis());
                        dataSalvare.setTimeInMillis(calendarioAtutale.getTimeInMillis());
                        throw new Eccezioni(Eccezioni.tipiEccezioni.DATA_NON_VALIDA,dialogView);
                    }else{
                        dataSalvare.set(anno,mese,giorno);
                    }
                }catch (Eccezioni e){

                }

            }
        });

    }


}

