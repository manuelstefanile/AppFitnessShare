package com.example.appfitness.Pagina3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;
import com.example.appfitness.Registrazione_Pag2;
import com.example.appfitness.ToastPersonalizzato;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PopupGiorno {


    public static ArrayList<Long> idGiorniSalvati= new ArrayList<>();
    public static Long idGiornoAttuale=-1L;


    public static void CreaGiorno(LayoutInflater inflater, Scheda schedaRiferimento)
    {
        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shp.edit();
        edit.putString(COSTANTI.NOTE_GIORNO,new Note().toJson());
        edit.commit();

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_giorno, null);
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

        RelativeLayout ll = dialogView.findViewById((int)R.id.origineGiorno);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);

        alertDialog.getWindow().setLayout(size.x, size.y);

        EditText nomeGiorno=dialogView.findViewById((int)R.id.nomeGiorno);
        Button bottoneNote=dialogView.findViewById((int)R.id.bottoneNoteGiorno);
        Button creaEsercizio=dialogView.findViewById((int)R.id.CreaEsercizio);
        Button salvaGiorno=dialogView.findViewById((int)R.id.salvaGiorno);
        //prima devi mettere un nome per il giorno e poi puoi andare avanti
        Button back=dialogView.findViewById((int)R.id.backGiorno);
        creaEsercizio.setEnabled(false);
        creaEsercizio.setAlpha(0.5f);

        Giorno giornoNuovo=new Giorno();
        //schedaRiferimento.getListaGiorni().add(giornoNuovo);
        giornoNuovo.setListaEsercizi(new ArrayList<>());


        ListView listaEserciziView = (ListView)dialogView.findViewById(R.id.listaEserciziView);
        Global.adapterEsercizi = new AdapterListaScheda(dialogView.getContext(), R.layout.item_esercizi, new ArrayList<Esercizio>(),listaEserciziView);
        listaEserciziView.setAdapter(Global.adapterEsercizi);


        /*
        final View backgroundView = alertDialog.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                back.callOnClick();
                return true; // Indica che l'evento è stato consumato
            }
        });
        /*
         */

        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater, shp,COSTANTI.NOTE_GIORNO);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.listaGiornidao.Insert(schedaRiferimento.getId(),schedaRiferimento.getListaGiorni());
                //una volta inseriti i giorni, posso liberare la lista
                schedaRiferimento.setListaGiorni(new ArrayList<>());
                alertDialog.dismiss();
            }
        });

        creaEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    PopupEsercizio.CreaEsercizio(inflater,giornoNuovo);
            }
        });

        //disabilito la possibilita di modificare il nome.
        //abillito crea esercizio
        //inserisco il giorno
        //inserisco l id nella lista di riferimento in schede.
        //aggiorno l adapter di scheda con il nuovo giorno
        salvaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_GIORNO, null));

                String testoInserito = nomeGiorno.getText().toString().trim();
                if(testoInserito.length()>0) {
                    nomeGiorno.setEnabled(false);
                    bottoneNote.setEnabled(false);
                    salvaGiorno.setEnabled(false);
                    ToastPersonalizzato.ToastSuccesso("Giorno salvato con successo.",inflater);
                    creaEsercizio.setEnabled(true);
                    creaEsercizio.setAlpha(1f);
                    creaEsercizio.setBackgroundResource(R.drawable.drawable_scheda);
                    giornoNuovo.setNomeGiorno(testoInserito);
                    giornoNuovo.setNote(note.getNote());

                    /*************setto l idGlobalegiorno da passare a esercizi*******/
                    idGiornoAttuale=Global.giornoDao.InsertGiorno(giornoNuovo).getId();
                    /************************************************/

                    schedaRiferimento.getListaGiorni().add(giornoNuovo.getId());
                    Global.adapterGiorni.add(giornoNuovo);
                }

            }
        });

    }


    public static void ApriGiornoSelezionato(Giorno giorno,LayoutInflater inflater){

        /***********setto l idGlobalegiorno da passare a esercizi*******/
        idGiornoAttuale=giorno.getId();
        /***************************************/

        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shp.edit();
        edit.putString(COSTANTI.NOTE_GIORNO,new Note().toJson());
        edit.commit();


        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_giorno, null);
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

        RelativeLayout ll = dialogView.findViewById((int)R.id.origineGiorno);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);

        alertDialog.getWindow().setLayout(size.x, size.y);

        ListView listaEserciziiView = (ListView)dialogView.findViewById(R.id.listaEserciziView);
        Global.adapterEsercizi = new AdapterListaScheda(dialogView.getContext(), R.layout.item_esercizi, new ArrayList<Esercizio>(),listaEserciziiView);
        listaEserciziiView.setAdapter(Global.adapterEsercizi);

        Global.ledao= new ListaEserciziDAO(PopupSchede.act.getApplicationContext());
        Global.esercizioDao=new EsercizioDAO(PopupSchede.act.getApplicationContext());

        ArrayList<Esercizio> listaDiID=Global.ledao.getListaEserciziPerGiorno(giorno.getId());

        //per ogni id, ricercami l'elemento giorno e aggiungilo alla lista di giorni visibile
        for(Esercizio e:listaDiID){
            Esercizio extemp=Global.esercizioDao.getEsercizioById((int) e.getId());
            ArrayList<Esercizio>listaexgiorno=Global.ledao.getListaEserciziPerGiorno(giorno.getId());
            for(Esercizio exgio:listaexgiorno){
                if (exgio.getId()==extemp.getId())
                    extemp.setOrdine(exgio.getOrdine());
            }
            extemp.idGiornoAvviaRiferimento= giorno.getId();
            Global.adapterEsercizi.add(extemp);
        }

        EditText nomeGiorno=dialogView.findViewById((int)R.id.nomeGiorno);
        Button bottoneNote=dialogView.findViewById((int)R.id.bottoneNoteGiorno);
        Button creaEsercizio=dialogView.findViewById((int)R.id.CreaEsercizio);
        Button salvaGiorno=dialogView.findViewById((int)R.id.salvaGiorno);
        /*da controllare sto toast
        Toast.makeText(dialogView.getContext(), "Inserisci un nome per salvare la giornata.", Toast.LENGTH_LONG).show();*/
        Button back=dialogView.findViewById((int)R.id.backGiorno);
        bottoneNote.setText("Note");

        //modifico il weight di edit
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nomeGiorno.getLayoutParams();
        params.weight = 0.6f; // Impostare il layout_weight a 0.6
        nomeGiorno.setLayoutParams(params);

        nomeGiorno.setText(giorno.getNomeGiorno());
        //nomeGiorno.setInputType(InputType.TYPE_NULL);
        //4salvaGiorno.setVisibility(View.GONE);

        creaEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupEsercizio.CreaEsercizio(inflater,giorno);
            }
        });

        salvaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se la lista degli esercizzi è maggiore di 1 allora va inserito nel db e aggiornato
                if(giorno.getListaEsercizi().size()>0){
                    //Global.giornoDao.AggiornaGiorno(giorno.getNomeGiorno(),Global.schedaNuova.getNomeScheda());
                }

                Global.adapterGiorni.remove(giorno);
                giorno.setNomeGiorno(nomeGiorno.getText().toString());

                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_GIORNO, null));

                giorno.setNote(note.getNote());
                Global.giornoDao.updateGiorno(giorno);
                Global.adapterGiorni.add(giorno);


                ToastPersonalizzato.ToastSuccesso("Giorno salvato con successo",inflater);
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


                Note note=Note.fromJson(sh.getString(COSTANTI.NOTE_GIORNO, null));
                Note notaDaMostrare;
                if(note.getNote()!=null){
                    notaDaMostrare=note;
                }else
                    notaDaMostrare= new Note(giorno.getNote());

                edit.putString(COSTANTI.NOTE_GIORNO, notaDaMostrare.toJson());
                edit.apply();
                try {
                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater,sh,COSTANTI.NOTE_GIORNO);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });



    }

    public static void AvviaGiornoSelezionato(Giorno giorno,LayoutInflater inflater){
        /***********setto l idGlobalegiorno da passare a esercizi*******/
        idGiornoAttuale=giorno.getId();
        /****************************************/

        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shp.edit();

        edit.putString(COSTANTI.NOTE_GIORNO,new Note().toJson());
        edit.commit();


        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_giorno, null);
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

        RelativeLayout ll = dialogView.findViewById((int)R.id.origineGiorno);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);

        alertDialog.getWindow().setLayout(size.x, size.y);

        ListView listaEserciziiView = (ListView)dialogView.findViewById(R.id.listaEserciziView);
        Global.adapterEsercizi = new AdapterListaScheda(dialogView.getContext(), R.layout.item_esercizi_avvia, new ArrayList<Esercizio>(),true,listaEserciziiView);
        listaEserciziiView.setAdapter(Global.adapterEsercizi);

        Global.ledao= new ListaEserciziDAO(PopupSchede.act.getApplicationContext());
        Global.esercizioDao=new EsercizioDAO(PopupSchede.act.getApplicationContext());

        ArrayList<Esercizio> listaDiID=Global.ledao.getListaEserciziPerGiorno(giorno.getId());

        //per ogni id, ricercami l'elemento giorno e aggiungilo alla lista di giorni visibile
        for(Esercizio e:listaDiID){
            Esercizio extemp=Global.esercizioDao.getEsercizioById((int) e.getId());
            extemp.setCompletato(e.getCompletato()==true?1:0);
            extemp.idGiornoAvviaRiferimento=giorno.getId();

            ArrayList<Esercizio>listaexgiorno=Global.ledao.getListaEserciziPerGiorno(giorno.getId());
            for(Esercizio exgio:listaexgiorno){
                if (exgio.getId()==extemp.getId())
                    extemp.setOrdine(exgio.getOrdine());
            }
            extemp.idGiornoAvviaRiferimento=giorno.getId();
            Global.adapterEsercizi.add(extemp);
        }




        EditText nomeGiorno=dialogView.findViewById((int)R.id.nomeGiorno);
        Button bottoneNote=dialogView.findViewById((int)R.id.bottoneNoteGiorno);
        Button creaEsercizio=dialogView.findViewById((int)R.id.CreaEsercizio);
        Button salvaGiorno=dialogView.findViewById((int)R.id.salvaGiorno);
        /*da controllare sto toast
        Toast.makeText(dialogView.getContext(), "Inserisci un nome per salvare la giornata.", Toast.LENGTH_LONG).show();*/
        Button back=dialogView.findViewById((int)R.id.backGiorno);
        bottoneNote.setText("Note");

        //modifico il weight di edit
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nomeGiorno.getLayoutParams();
        params.weight = 0.6f; // Impostare il layout_weight a 0.6
        nomeGiorno.setLayoutParams(params);

        nomeGiorno.setText(giorno.getNomeGiorno());
        nomeGiorno.setInputType(InputType.TYPE_NULL);
        salvaGiorno.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        creaEsercizio.setText("Fine Allenamento");


        //quando premo il tasto indietro, resetta a 0
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //setta tutti gli esercizi di nuovo a non completati
                ArrayList<Esercizio> listaExtemp= (ArrayList<Esercizio>) Global.adapterEsercizi.getLista();
                List<Long> idex= listaExtemp.stream().map(item->item.getId()).collect(Collectors.toList());
                for(Long idtemp: idex){
                    Global.ledao.updateStato(giorno.getId(),idtemp,0);
                    Esercizio ex=Global.esercizioDao.getEsercizioById(idtemp.intValue());
                    Global.adapterEsercizi.UpdateEsercizio(ex);
                }
            }
        });
        //fine allenamento
        creaEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setta tutti gli esercizi di nuovo a non completati
                ArrayList<Esercizio> listaExtemp= (ArrayList<Esercizio>) Global.adapterEsercizi.getLista();
                List<Long> idex= listaExtemp.stream().map(item->item.getId()).collect(Collectors.toList());
                for(Long idtemp: idex){
                    Global.ledao.updateStato(giorno.getId(),idtemp,0);
                    Esercizio ex=Global.esercizioDao.getEsercizioById(idtemp.intValue());
                    System.out.println("cicc"+ex.getId());
                    Global.adapterEsercizi.UpdateEsercizio(ex);
                }

                alertDialog.dismiss();
            }
        });

        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit= sh.edit();


                Note note=Note.fromJson(sh.getString(COSTANTI.NOTE_GIORNO, null));
                Note notaDaMostrare;
                if(note.getNote()!=null){
                    notaDaMostrare=note;
                }else
                    notaDaMostrare= new Note(giorno.getNote());
                
                edit.putString(COSTANTI.NOTE_GIORNO, notaDaMostrare.toJson());
                edit.apply();
                try {
                    Registrazione_Pag2.editGlobal=false;
                    NotificheDialog.NotificaNote(inflater,sh,COSTANTI.NOTE_GIORNO);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });



    }


}
