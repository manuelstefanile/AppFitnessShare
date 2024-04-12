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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;
import com.example.appfitness.Registrazione_Pag2;

import java.util.ArrayList;

public class PopupGiorno {


    public static ArrayList<Long> idGiorniSalvati= new ArrayList<>();
    private static Note noteScheda;

    public static void CreaGiorno(LayoutInflater inflater, Scheda schedaRiferimento)
    {
        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shp.edit();
        noteScheda = Note.fromJson(shp.getString("notePassate", null));
        edit.putString("notePassate",new Note().toJson());
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
        creaEsercizio.setBackgroundResource(R.drawable.drawable_botton_grey);

        Giorno giornoNuovo=new Giorno();
        //schedaRiferimento.getListaGiorni().add(giornoNuovo);
        giornoNuovo.setListaEsercizi(new ArrayList<>());


        ListView listaEserciziView = (ListView)dialogView.findViewById(R.id.listaEserciziView);
        Global.adapterEsercizi = new AdapterListaScheda(dialogView.getContext(), R.layout.item_esercizi, new ArrayList<Esercizio>());
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
                    NotificheDialog.NotificaNote(inflater, shp);
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

                //ripristina notifiche di scheda
                SharedPreferences.Editor edit=shp.edit();
                edit.putString("notePassate",noteScheda.toJson());
                edit.commit();
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
                Note note=Note.fromJson(sharedPreferences.getString("notePassate", null));

                String testoInserito = nomeGiorno.getText().toString().trim();
                if(testoInserito.length()>0) {
                    nomeGiorno.setEnabled(false);
                    bottoneNote.setEnabled(false);
                    salvaGiorno.setEnabled(false);
                    creaEsercizio.setEnabled(true);
                    creaEsercizio.setBackgroundResource(R.drawable.drawable_scheda);
                    giornoNuovo.setNomeGiorno(testoInserito);
                    giornoNuovo.setNote(note.getNote());
                    Global.giornoDao.InsertGiorno(giornoNuovo);
                    schedaRiferimento.getListaGiorni().add(giornoNuovo.getId());
                    Global.adapterGiorni.add(giornoNuovo);
                }

            }
        });

    }


    public static void ApriGiornoSelezionato(Giorno giorno,LayoutInflater inflater){

        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shp.edit();
        noteScheda = Note.fromJson(shp.getString("notePassate", null));
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
        Global.adapterEsercizi = new AdapterListaScheda(dialogView.getContext(), R.layout.item_esercizi, new ArrayList<Esercizio>());
        listaEserciziiView.setAdapter(Global.adapterEsercizi);

        Global.ledao= new ListaEserciziDAO(PopupSchede.act.getApplicationContext());
        Global.esercizioDao=new EsercizioDAO(PopupSchede.act.getApplicationContext());

        ArrayList<Integer> listaDiID=Global.ledao.getListaEserciziPerGiorno(giorno.getId());

        //per ogni id, ricercami l'elemento giorno e aggiungilo alla lista di giorni visibile
        for(Integer id:listaDiID){
            Global.adapterEsercizi.add(Global.esercizioDao.getEsercizioById(id));
        }

        EditText nomeGiorno=dialogView.findViewById((int)R.id.nomeGiorno);
        Button bottoneNote=dialogView.findViewById((int)R.id.bottoneNoteGiorno);
        Button creaEsercizio=dialogView.findViewById((int)R.id.CreaEsercizio);
        Button salvaGiorno=dialogView.findViewById((int)R.id.salvaGiorno);
        Button back=dialogView.findViewById((int)R.id.backGiorno);
        bottoneNote.setText("Mostra");

        //modifico il weight di edit
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nomeGiorno.getLayoutParams();
        params.weight = 0.6f; // Impostare il layout_weight a 0.6
        nomeGiorno.setLayoutParams(params);

        nomeGiorno.setText(giorno.getNomeGiorno());
        nomeGiorno.setInputType(InputType.TYPE_NULL);
        salvaGiorno.setVisibility(View.GONE);

        creaEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupEsercizio.CreaEsercizio(inflater,giorno);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se la lista degli esercizzi è maggiore di 1 allora va inserito nel db e aggiornato
                if(giorno.getListaEsercizi().size()>0){
                    //Global.giornoDao.AggiornaGiorno(giorno.getNomeGiorno(),Global.schedaNuova.getNomeScheda());
                }

                //ripristina notifiche di scheda
                SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=shp.edit();
                edit.putString("notePassate",noteScheda.toJson());
                edit.commit();

                alertDialog.dismiss();
            }
        });

        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit= sh.edit();
                PaginaScheda_Pag3.StampaTutto();
                Note notaDaMostrare= new Note(giorno.getNote());
                edit.putString("notePassate", notaDaMostrare.toJson());
                edit.apply();
                try {
                    Registrazione_Pag2.editGlobal=false;
                    NotificheDialog.NotificaNote(inflater,sh);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });



    }

}
