package com.example.appfitness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Fisico;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Bean.SerializzazioneFileDati;
import com.example.appfitness.Bean.SerializzazioneFileScheda;
import com.example.appfitness.Bean.Utente;
import com.example.appfitness.DB.FisicoDAO;
import com.example.appfitness.DB.ListaImgFisicoDAO;
import com.example.appfitness.DB.MisureDAO;
import com.example.appfitness.DB.PesoDAO;
import com.example.appfitness.DB.UtenteDAO;
import com.example.appfitness.DB.kcalDAO;
import com.example.appfitness.Pagina3.Global;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;
import com.example.appfitness.Pagina3.PopupSchede;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImportExport {

    public static final int CREATE_FILE_DATI_REQUEST_CODE=4;

    public ImportExport() {
    }

    public void UploadSchedaFile(Long idScheda, Activity act){
        System.out.println("mah"+idScheda);
        // Avvia l'intent per creare un nuovo file
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain"); // Tipo del file, puoi modificare in base al tipo di file che vuoi creare

        SerializzazioneFileScheda fileser=new SerializzazioneFileScheda();
        HashMap<Giorno, ArrayList<Esercizio>> mappa = new HashMap<>();
        //prendo la scheda
        Scheda schedatemp= Global.schedadao.getSchedaById(idScheda);
        fileser.setScheda(schedatemp);

        System.out.println("mah"+schedatemp.getNote());
        //popolo i giorni della lista
        ArrayList<Giorno> giorniAssociatiAScheda=new ArrayList<>();
        ArrayList<Integer> idGiorni=Global.listaGiornidao.getListaGiorniPerScheda(schedatemp.getId());
        for (Integer idg:idGiorni) {
            Giorno g=Global.giornoDao.getGiornoById(idg);
            giorniAssociatiAScheda.add(g);
            ArrayList<Esercizio> listaExpreGiorno=Global.ledao.getListaEserciziPerGiorno(Long.valueOf(idg));
            ArrayList<Esercizio> listaexCompleti=new ArrayList<>();

            for(Esercizio e:listaExpreGiorno){
                Esercizio estemp=Global.esercizioDao.getEsercizioById((int) e.getId());
                estemp.setOrdine(e.getOrdine());
                listaexCompleti.add(estemp);
            }
            mappa.put(g,listaexCompleti);
        }
        fileser.setMappa(mappa);
        System.out.println("proviamo ancora "+ fileser);
        PaginaScheda_Pag3.sf=fileser;

        act.startActivityForResult(intent, PopupSchede.CREATE_FILE_REQUEST_CODE);

    }

    public void UploadDatiFile(Activity act){

        // Avvia l'intent per creare un nuovo file
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain"); // Tipo del file, puoi modificare in base al tipo di file che vuoi creare

        act.startActivityForResult(intent, CREATE_FILE_DATI_REQUEST_CODE);

    }


    public void SalvaSchedaImportate(SerializzazioneFileScheda oggettoLetto, LayoutInflater cx){
        Scheda schedaSave=oggettoLetto.getScheda();
        System.out.println("scheda test "+ schedaSave);
        System.out.println("mah scheda test "+ schedaSave.getNote());
        for(Scheda sche:Global.schedadao.getAllSchede()){
            if(sche.getNomeScheda().equals(schedaSave.getNomeScheda())){
                //passa context, layoutInflate
                SovraschiviSchedaNotifica(true,schedaSave,oggettoLetto,cx);
                return;
            }

        }
        SovrascritturaSchedaImport(false,schedaSave,oggettoLetto,cx);
    }
    private void SovraschiviSchedaNotifica(boolean elimniaSchedaEsistente,Scheda schedaSave,SerializzazioneFileScheda oggettoLetto, LayoutInflater cx){
        AlertDialog.Builder builder = new AlertDialog.Builder(cx.getContext());
        // Imposta il layout personalizzato come vista del dialog box
        View customLayout = cx.inflate(R.layout.custom_dialog_layout, null);
        TextView testo= customLayout.findViewById((int)R.id.testoCustom);
        testo.setText("Scheda "+schedaSave.getNomeScheda()+" già presente. Vuoi sovrascrivere?");
        builder.setView(customLayout);
        // Ora puoi trovare i pulsanti all'interno del layout e aggiungere i listener di click
        Button buttonYes = customLayout.findViewById(R.id.button_yes);
        Button buttonNo = customLayout.findViewById(R.id.button_no);

        // Creazione dell'AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Aggiunta dei listener di click ai pulsanti
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SovrascritturaSchedaImport(elimniaSchedaEsistente,schedaSave,oggettoLetto,cx);
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Annulla l'eliminazione
                dialog.dismiss(); // Chiudi il dialog box
            }
        });
    }
    private void SovrascritturaSchedaImport(boolean eliminaSchedaEsistente,Scheda schedaSave,SerializzazioneFileScheda oggettoLetto,LayoutInflater cx){

        //elimina se esiste, la scheda presente
        if(eliminaSchedaEsistente){
            for(Scheda s:Global.schedadao.getAllSchede()){
                if(s.getNomeScheda().equals(schedaSave.getNomeScheda())){
                    Global.schedadao.DeleteScheda(s);
                }
            }
        }
        schedaSave.setId(Global.schedadao.InsertScheda(schedaSave));
        Global.adapterSchede.add(schedaSave);
        PaginaScheda_Pag3.avviaAnimazione=true;

        HashMap<Giorno,ArrayList<Esercizio>> mappa=oggettoLetto.getMappa();
        for(Map.Entry<Giorno,ArrayList<Esercizio>> oggChiaveVal:mappa.entrySet()){
            Giorno giornotemp=oggChiaveVal.getKey();
            giornotemp=Global.giornoDao.InsertGiorno(giornotemp);
            Global.listaGiornidao.InsertSingleDay(schedaSave.getId(),giornotemp.getId());

            //lista ex e ex
            for(Esercizio ex:oggChiaveVal.getValue()){
                ex=Global.esercizioDao.inserisciEsercizio(ex);
                Global.ledao.Insert(giornotemp.getId(),ex.getId(),0,ex.getOrdine());
            }

        }


        //se la scheda è gia presente, avvisa che verranno sovrascritti i dati
        SharedPreferences shp=cx.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(COSTANTI.NOTE_SCHEDA, new Note().toJson());
        edit.putString(COSTANTI.NOTE_GIORNO, new Note().toJson());
        edit.putString(COSTANTI.NOTE_ESERCIZIO, new Note().toJson());
        edit.commit();
        Intent intent = new Intent(cx.getContext(), PaginaScheda_Pag3.class);
        cx.getContext().startActivity(intent);
    }

    public static void SovrascritturaDatiImport(SerializzazioneFileDati sfd, LayoutInflater lf){
        UtenteDAO utentedao= new UtenteDAO(lf.getContext());
        kcalDAO kcaldao= new kcalDAO(lf.getContext());
        FisicoDAO fisicoDAO= new FisicoDAO(lf.getContext());
        MisureDAO misuradao= new MisureDAO(lf.getContext());
        PesoDAO pesodao = new PesoDAO(lf.getContext());
        ListaImgFisicoDAO listimgfisico= new ListaImgFisicoDAO(lf.getContext());

        kcaldao.deleteAllKcal();
        listimgfisico.deleteAllImgFisico();
        fisicoDAO.deleteAllFisico();
        misuradao.deleteAllMisure();
        pesodao.deleteAllPeso();
        utentedao.deleteAllUtente();

        System.out.println("UTENTE TEST "+ sfd.getUtente());
        utentedao.insertUtente(sfd.getUtente());

        for(Kcal kcal: sfd.getKcal()){
            kcaldao.insertKcal(kcal);
        }

        for(Peso peso: sfd.getPeso()){
            pesodao.insertPeso(peso);
        }

        for(Misure misura: sfd.getMisure()){
            misuradao.insertMisure(misura);
        }

        for(Peso peso: sfd.getPeso()){
            pesodao.insertPeso(peso);
        }

        for (Fisico fisico: sfd.getFisico()){
            fisicoDAO.inserisciFisico(fisico);
            listimgfisico.inserisciListaImg(fisico);

        }


    }
    
    
}
