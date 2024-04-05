package com.example.appfitness.Pagina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.DB.SchedaDAO;
import com.example.appfitness.R;

import java.io.IOException;
import java.util.ArrayList;

public class PopupSchede {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static Activity act; // Aggiunto
    private static ImageButton imgScheda;
    static SchedaDAO sDao;
    static ListaGiorniDAO listaGiorniDao;
    static GiornoDAO giornoDao;


    AdapterListaScheda adapterGiorni;
    public static Scheda schedaNuova;




    public PopupSchede(Activity activity) { // Costruttore aggiunto
        this.act = activity;
    }

    public  void CreaScheda(LayoutInflater inflater, AdapterListaScheda adapterSchede){

        sDao=new SchedaDAO(act);
        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_scheda, null);
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

        schedaNuova=new Scheda();
        schedaNuova.setListaGiorni(new ArrayList<>());


        ListView listaGiorniView = (ListView)dialogView.findViewById(R.id.listaGiorniView);
        adapterGiorni = new AdapterListaScheda(dialogView.getContext(), R.layout.item_giorni, new ArrayList<Giorno>());
        listaGiorniView.setAdapter(adapterGiorni);


        EditText nomeScheda= dialogView.findViewById((int)R.id.nomeScheda);
        imgScheda=dialogView.findViewById((int)R.id.aggiungiImmagineScheda);
        Button creaGiorno=dialogView.findViewById((int)R.id.CreaGiorno);
        Button back=dialogView.findViewById((int)R.id.backScheda);


        imgScheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupSchede.selectImageFromGallery();
            }
        });


        creaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupGiorno.CreaGiorno(inflater,schedaNuova,adapterGiorni);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schedaNuova.setNomeScheda(nomeScheda.getText().toString());
                schedaNuova.setImg(imgScheda.getDrawable());
                adapterSchede.add(schedaNuova);
                System.out.println("SchedaNuova= " +schedaNuova);
                sDao.InsertScheda(schedaNuova);
                PaginaScheda_Pag3.StampaTutto();
                alertDialog.dismiss();
                ResettaVariabili();
            }
        });
    }

    public void ApriSchedaSelezionata(Scheda sched,LayoutInflater inflater){
        //per rendere accessibile la scheda ai giorni con oclick dell adapter
        schedaNuova=sched;

        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_scheda, null);
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

        ListView listaGiorniView = (ListView)dialogView.findViewById(R.id.listaGiorniView);
        adapterGiorni = new AdapterListaScheda(dialogView.getContext(), R.layout.item_giorni, new ArrayList<Giorno>());
        listaGiorniView.setAdapter(adapterGiorni);

        listaGiorniDao=new ListaGiorniDAO(act.getApplicationContext());
        giornoDao=new GiornoDAO(act.getApplicationContext());
        ArrayList<Integer> listaDiID =listaGiorniDao.getListaGiorniPerScheda(sched.getNomeScheda());

        //per ogni id, ricercami l'elemento giorno e aggiungilo alla lista di giorni visibile
        for(Integer id:listaDiID){
            adapterGiorni.add(giornoDao.getGiornoById(id));
            //adapterGiorni.notify();
        }

        EditText nomeScheda= dialogView.findViewById((int)R.id.nomeScheda);
        imgScheda=dialogView.findViewById((int)R.id.aggiungiImmagineScheda);
        Button creaGiorno=dialogView.findViewById((int)R.id.CreaGiorno);
        Button back=dialogView.findViewById((int)R.id.backScheda);
        nomeScheda.setText(sched.getNomeScheda());
        nomeScheda.setInputType(InputType.TYPE_NULL);
        creaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupGiorno.CreaGiorno(inflater,sched,adapterGiorni);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sched.getListaGiorni().size()>0)
                    PaginaScheda_Pag3.listaGiornidao.InserisciListaGiorni(sched);
                alertDialog.dismiss();
                ResettaVariabili();
            }
        });




    }

    private void ResettaVariabili(){
        schedaNuova=null;
        PopupGiorno.idGiorniSalvati= new ArrayList<>();
        PopupEsercizio.idEserciziSalvati= new ArrayList<>();
        PopupEsercizio.immagineEsercizio=null;
    }

    private static void selectImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        act.startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), PICK_IMAGE_REQUEST); // Modificato qui

    }




//per le immagini e mostrarle a schermo quando pronte
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(act.getContentResolver(), imageUri);
                imgScheda.setImageBitmap(bitmap);
                imgScheda.setVisibility(View.VISIBLE); // Mostra l'ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(act, "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show();
            }
        }
        //sto nelle immagini di esercizio
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(act.getContentResolver(), imageUri);
                PopupEsercizio.immagineEsercizio.setImageBitmap(bitmap);
                PopupEsercizio.immagineEsercizio.setVisibility(View.VISIBLE); // Mostra l'ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(act, "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
