package com.example.appfitness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.KeyEvent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.DB.MisureDAO;
import com.example.appfitness.DB.PesoDAO;
import com.example.appfitness.DB.kcalDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.Pagina3.Global;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class NotificheDialog {

    //vabbe ho iniziato quantomeno a scriverlo per vedere se parte
    public static void NotificaFisico(LayoutInflater inflater, SharedPreferences sh) throws Eccezioni {
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
                System.out.println("sono in on salva");
                //salva i dati che inserisci
                float pesoInserito = 0;
                String pesoStringa = kiliEdit.getText().toString().trim(); // Rimuovi spazi vuoti

                // Verifica se la stringa del peso è vuota
                if (pesoStringa.isEmpty()) {
                    Toast.makeText(dialogView.getContext(), "Non stiamo pesando l'aria.", Toast.LENGTH_SHORT).show();
                    return; // Esci dal metodo in caso di peso non valido
                }

                //gestisci eccezione
                try {
                    pesoInserito = Float.parseFloat(pesoStringa);
                } catch (NumberFormatException e) {
                    Toast.makeText(dialogView.getContext(), "Formato peso non valido.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return; // Esci dal metodo in caso di formato non valido
                }

                if (pesoInserito != 0) {
                    Toast.makeText(dialogView.getContext(), "Peso salvato, Keep going Buddy!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(dialogView.getContext(), "Pesi 0 kg? Non avere paura della bilancia!", Toast.LENGTH_LONG).show();
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
        System.out.println("misureid"+misureStorage.getId());
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
                        Toast.makeText(dialogView.getContext(), "Prendi quel metro altrimenti le misure saranno 0 cm!", Toast.LENGTH_LONG).show();
                        return; // Esci dal metodo
                    }

                    // Controllare se alcune misure sono vuote mentre altre no
                    if ((valoreBracicoDx == 0 || valoreBracicoSx == 0 || valoreGambaDx == 0 || valoreGambaSx == 0 ||
                            valorePetto == 0 || valoreSpalle == 0 || valoreAddome == 0 || valoreFianchi == 0) &&
                            almenoUnaMisuraInserita) {
                        Toast.makeText(dialogView.getContext(), "Le misure non inserite saranno impostate a 0 cm.", Toast.LENGTH_LONG).show();
                    }

                    finalMisureStorage.setNote(noteDettaglio.getText().toString());
                    finalMisureStorage.setData(dataSalvare);


                    // Mostra il Toast solo se tutte le misure sono state inserite correttamente
                    Toast.makeText(dialogView.getContext(), "Misure salvate. Keep going Buddy!", Toast.LENGTH_LONG).show();

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
                        Toast.makeText(dialogView.getContext(), "Le kcal pari a 0? Digiuno estremo?", Toast.LENGTH_LONG).show();
                    } else if (kcalAttuali.getText().toString().trim().isEmpty()) {
                        // Controlla se le kcal attuali sono vuote
                        Toast.makeText(dialogView.getContext(), "Dovrai pur mangiare qualcosa.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Mostra il Toast di successo se tutte le operazioni sono andate a buon fine
                        Toast.makeText(dialogView.getContext(), "Kcal salvate, Keep going Buddy!", Toast.LENGTH_LONG).show();
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

                    Toast.makeText(dialogView.getContext(), "Note salvate.", Toast.LENGTH_SHORT).show();
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


        }

    }






    private static void ImpostaCalendario(CalendarView calendario, View dialogView, Calendar dataSalvare,Object oggetto){
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int anno, int mese, int giorno) {
                /*dammi *****************/
                Calendar calendarioMostra=Calendar.getInstance();
                calendarioMostra.set(anno,mese,giorno)
                ;
                if(oggetto.getClass()==Kcal.class) {
                    System.out.println("kcalkcal");
                    kcalDAO kcalDAO=new kcalDAO(dialogView.getContext());
                    Kcal oggKcal=kcalDAO.getKcalPerData(Global.ConversioneCalendarString(calendarioMostra));
                    System.out.println("datad"+calendarioMostra.getTimeInMillis());
                    if(oggKcal!=null){
                        System.out.println("datad"+oggKcal.getData().getTimeInMillis());
                        System.out.println("kcalkcalSetto");
                        SettaVarDialogView(oggetto,dialogView,oggKcal);
                    }else SettaTextAVuoto(oggetto,dialogView,oggKcal);

                }else if(oggetto.getClass()==Misure.class) {
                    MisureDAO misureDAO=new MisureDAO(dialogView.getContext());
                    Misure oggMisure=misureDAO.getMisureperData(Global.ConversioneCalendarString(calendarioMostra));
                    if(oggMisure!=null){
                        System.out.println("kcalkcalSetto");
                        SettaVarDialogView(oggetto,dialogView,oggMisure);
                    }else SettaTextAVuoto(oggetto,dialogView,oggMisure);

                }else if(oggetto.getClass()==Peso.class) {
                    PesoDAO pesoDAO=new PesoDAO(dialogView.getContext());
                    Peso oggPeso=pesoDAO.getPesoPerData(Global.ConversioneCalendarString(calendarioMostra));
                    if(oggPeso!=null){
                        SettaVarDialogView(oggetto,dialogView,oggPeso);
                    }else SettaTextAVuoto(oggetto,dialogView,oggPeso);


                }

                Calendar calendarioAtutale= Calendar.getInstance();
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

