package com.example.appfitness.Pagina3;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.R;

import java.util.ArrayList;

public class PopupGiorno {

    static AdapterListaScheda adapterEsercizi;
    public static ArrayList<Long> idGiorniSalvati= new ArrayList<>();
    public static GiornoDAO giornoDao;
    private static EsercizioDAO esercizioDao;
    static ListaEserciziDAO ledao;

    public static void CreaGiorno(LayoutInflater inflater, Scheda schedaRiferimento, AdapterListaScheda<Giorno> adapGiorni){


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
        alertDialog.show();

        EditText nomeGiorno=dialogView.findViewById((int)R.id.nomeGiorno);
        Button creaEsercizio=dialogView.findViewById((int)R.id.CreaEsercizio);

        Button back=dialogView.findViewById((int)R.id.backGiorno);

        Giorno giornoNuovo=new Giorno();
        schedaRiferimento.getListaGiorni().add(giornoNuovo);
        giornoNuovo.setListaEsercizi(new ArrayList<>());


        ListView listaEserciziView = (ListView)dialogView.findViewById(R.id.listaEserciziView);
        adapterEsercizi = new AdapterListaScheda(dialogView.getContext(), R.layout.item_esercizi, new ArrayList<Esercizio>());
        listaEserciziView.setAdapter(adapterEsercizi);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                giornoNuovo.setNomeGiorno(nomeGiorno.getText().toString());
                adapGiorni.add(giornoNuovo);

                long idGiorno=giornoDao.InsertGiorno(giornoNuovo);
                idGiorniSalvati.add(idGiorno);
                PaginaScheda_Pag3.StampaTutto();
                PopupEsercizio.idEserciziSalvati=new ArrayList<>();
                alertDialog.dismiss();
            }
        });


        creaEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupEsercizio.CreaEsercizio(inflater,giornoNuovo,adapterEsercizi);
            }
        });


    }


    public static void ApriGiornoSelezionato(Giorno giorno,LayoutInflater inflater){

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
        alertDialog.show();

        ListView listaEserciziiView = (ListView)dialogView.findViewById(R.id.listaEserciziView);
        adapterEsercizi = new AdapterListaScheda(dialogView.getContext(), R.layout.item_esercizi, new ArrayList<Esercizio>());
        listaEserciziiView.setAdapter(adapterEsercizi);

        ledao= new ListaEserciziDAO(PopupSchede.act.getApplicationContext());
        esercizioDao=new EsercizioDAO(PopupSchede.act.getApplicationContext());
        ArrayList<Integer> listaDiID =ledao.getListaEserciziPerGiorno(giorno.getNomeGiorno());
        System.out.println("*****" + listaDiID.size());

        //per ogni id, ricercami l'elemento giorno e aggiungilo alla lista di giorni visibile
        for(Integer id:listaDiID){
            adapterEsercizi.add(esercizioDao.getEsercizioById(id));
            //adapterGiorni.notify();
        }

        EditText nomeGiorno=dialogView.findViewById((int)R.id.nomeGiorno);
        Button creaEsercizio=dialogView.findViewById((int)R.id.CreaEsercizio);
        Button back=dialogView.findViewById((int)R.id.backGiorno);
        nomeGiorno.setText(giorno.getNomeGiorno());
        nomeGiorno.setInputType(InputType.TYPE_NULL);

        creaEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupEsercizio.CreaEsercizio(inflater,giorno,adapterEsercizi);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se la lista degli esercizzi è maggiore di 1 allora va inserito nel db e aggiornato
                if(giorno.getListaEsercizi().size()>0){
                    giornoDao.AggiornaGiorno(giorno);
                }

                alertDialog.dismiss();
            }
        });



    }
}
