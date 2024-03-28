package com.example.appfitness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class NotificheDialog {

    public static void NotificaPeso(LayoutInflater inflater, SharedPreferences sh) throws Eccezioni{

        Calendar dataSalvare=Calendar.getInstance();

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

        //prendo l oggetto
        Peso pesoStorage= Peso.fromJson(sh.getString("pesoPassato",null));

        //se il peso esiste ed è gia stato inserito allora mostralo
        if(pesoStorage.getPesoKg()!=0&&pesoStorage.getCalendario()!=null){
            calendario.setDate(pesoStorage.getCalendario().getTimeInMillis());
            kiliEdit.setText(String.valueOf(pesoStorage.getPesoKg()));
        }


        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int anno, int mese, int giorno) {
                Calendar calendarioAtutale= Calendar.getInstance();
                System.out.println("dat "+ giorno + " m " + mese +"anno "+ anno);
                try{
                    if(anno>calendarioAtutale.get(Calendar.YEAR)){
                        calendario.setDate(calendarioAtutale.getTimeInMillis());
                        throw new Eccezioni(Eccezioni.tipiEccezioni.DATA_NON_VALIDA,dialogView);
                    }
                    else if(mese>calendarioAtutale.get(Calendar.MONTH)){
                        calendario.setDate(calendarioAtutale.getTimeInMillis());
                        throw new Eccezioni(Eccezioni.tipiEccezioni.DATA_NON_VALIDA,dialogView);
                    }
                    else if(giorno>calendarioAtutale.get(Calendar.DAY_OF_MONTH)){
                        calendario.setDate(calendarioAtutale.getTimeInMillis());
                        throw new Eccezioni(Eccezioni.tipiEccezioni.DATA_NON_VALIDA,dialogView);
                    }else{
                        dataSalvare.set(anno,mese,giorno);
                    }
                }catch (Eccezioni e){

                }

            }
        });


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
                SharedPreferences.Editor edi=sh.edit();
                edi.putString("pesoPassato",pesoStorage.toJson());
                edi.apply();
                // Se desideri applicare i cambiamenti all'oggetto originale:
                //alertDialog.dismiss(); // Chiudi il dialog
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("sono in on ok");
                alertDialog.dismiss(); // Chiudi il dialog

            }
        });




    }
    public static Misure NotificaMisure(LayoutInflater inflater) throws Eccezioni{
        Misure[] misuraSalvare = new Misure[1];

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

        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText braccioDx=dialogView.findViewById(R.id.braccioDX);
                EditText braccioSx=dialogView.findViewById(R.id.braccioSX);
                EditText gambaDx=dialogView.findViewById(R.id.gambaDX);
                EditText gambaSx=dialogView.findViewById(R.id.gambaSX);
                EditText petto=dialogView.findViewById(R.id.petto);
                EditText spalle=dialogView.findViewById(R.id.spalle);
                EditText addome=dialogView.findViewById(R.id.addome);
                Misure misure=new Misure();
                try{
                    System.out.println("**" + braccioDx.getText().toString());
                    float valoreBracicoDx=braccioDx.getText().toString().trim().length()!=0?Float.parseFloat(braccioDx.getText().toString()):0;
                    misure.setBraccioDx(valoreBracicoDx);
                    braccioDx.setText(String.valueOf(misure.getBraccioDx()));

                    float valoreBracicoSx=braccioSx.getText().toString().trim().length()!=0?Float.parseFloat(braccioSx.getText().toString()):0;
                    misure.setBraccioSx(valoreBracicoSx);
                    braccioSx.setText(String.valueOf(misure.getBraccioSx()));

                    float valoreGambaDx=gambaDx.getText().toString().trim().length()!=0?Float.parseFloat(gambaDx.getText().toString()):0;
                    misure.setGambaDx(valoreGambaDx);
                    gambaDx.setText(String.valueOf(misure.getGambaDx()));

                    float valoreGambaSx=gambaSx.getText().toString().trim().length()!=0?Float.parseFloat(gambaSx.getText().toString()):0;
                    misure.setGambaSx(valoreGambaSx);
                    gambaSx.setText(String.valueOf(misure.getGambaSx()));

                    float valorePetto=petto.getText().toString().trim().length()!=0?Float.parseFloat(petto.getText().toString()):0;
                    misure.setPetto(valorePetto);
                    petto.setText(String.valueOf(misure.getPetto()));

                    float valoreSpalle=spalle.getText().toString().trim().length()!=0?Float.parseFloat(spalle.getText().toString()):0;
                    misure.setSpalle(valoreSpalle);
                    spalle.setText(String.valueOf(misure.getSpalle()));

                    float valoreAddome=addome.getText().toString().trim().length()!=0?Float.parseFloat(addome.getText().toString()):0;
                    misure.setAddome(valoreAddome);
                    addome.setText(String.valueOf(misure.getAddome()));


                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(dialogView.getContext(), "Salvato", Toast.LENGTH_SHORT).show();
                misuraSalvare[0] =misure;
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss(); // Chiudi il dialog
            }
        });

        return misuraSalvare[0];

    }

    public static class PesoWrapper {
        private Peso peso;

        public PesoWrapper(Peso peso) {
            this.peso = peso;
        }

        public Peso getPeso() {
            return peso;
        }

        public void setPeso(Peso peso) {
            this.peso = peso;
        }
    }
}

