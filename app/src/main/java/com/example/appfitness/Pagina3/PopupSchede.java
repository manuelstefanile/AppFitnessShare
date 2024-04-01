package com.example.appfitness.Pagina3;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PopupSchede {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static Activity act; // Aggiunto
    private static ImageButton imgScheda;
    ArrayList<Giorno> listaGiorni;
    static AdapterListaScheda<Giorno> adapGiorni;

    public PopupSchede(Activity activity) { // Costruttore aggiunto
        this.act = activity;
    }

    public static void CreaScheda(LayoutInflater inflater, SharedPreferences sh, boolean creazione){


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


        EditText nomeScheda= dialogView.findViewById((int)R.id.nomeScheda);
        imgScheda=dialogView.findViewById((int)R.id.aggiungiImmagineScheda);
        Button creaGiorno=dialogView.findViewById((int)R.id.CreaGiorno);
        Button salva=dialogView.findViewById((int)R.id.salvaScheda);
        Button back=dialogView.findViewById((int)R.id.backScheda);

        //ho premuto per visualizzare
        /*if(!creazione){
            Scheda schedaStore= Scheda.fromJson(sh.getString("scheda",null));
            nomeScheda.setText(schedaStore.getNomeScheda());

        }*/


        imgScheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupSchede.selectImageFromGallery();
            }
        });


        creaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreaGiorno(inflater,sh);
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Scheda scheda=new Scheda(nomeScheda.getText().toString(),imgScheda.getDrawable());

                //prendo la lista di giorni che ho convertito in json
                String listaGiorniString = sh.getString("giorni", null);
                ArrayList<Giorno> listaGiorni = new ArrayList<>();

                if (listaGiorniString != null) {
                    // Converti la stringa JSON in una lista di Giorno
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Giorno>>() {}.getType();
                    listaGiorni = gson.fromJson(listaGiorniString, type);
                }
                //aggiungo i giorni alla scheda.
                scheda.setListaGiorni(listaGiorni);

                SharedPreferences.Editor edi= sh.edit();
                edi.putString("scheda",scheda.toJson());
                edi.apply();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }




    public static void CreaGiorno(LayoutInflater inflater, SharedPreferences sh){

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
        Button salva=dialogView.findViewById((int)R.id.salvaGiorno);
        Button back=dialogView.findViewById((int)R.id.backGiorno);

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Giorno giorno=new Giorno(nomeGiorno.getText().toString(),null);

                //prendo la lista di giorni che ho convertito in json
                String listaEserciziString = sh.getString("esercizi", null);
                ArrayList<Esercizio> listaEsercizi = new ArrayList<>();

                if (listaEserciziString != null) {
                    // Converti la stringa JSON in una lista di Giorno
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Esercizio>>() {}.getType();
                    listaEsercizi = gson.fromJson(listaEserciziString, type);
                }
                //aggiungo i giorni alla scheda.
                giorno.setListaEsercizi(listaEsercizi);

                SharedPreferences.Editor edi= sh.edit();
                edi.putString("giorno",giorno.toJson());
                edi.apply();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        creaEsercizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreaEsercizio(inflater,sh);
            }
        });


    }


    public static void CreaEsercizio(LayoutInflater inflater, SharedPreferences sh){

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
        alertDialog.show();

        EditText nomeEsercizio = dialogView.findViewById((int) R.id.nomeEsercizio);
        ImageButton immagineEsercizio = dialogView.findViewById((int) R.id.immagineEsercizio);
        EditText numeroSerieEsercizio = dialogView.findViewById((int) R.id.numeroSerie);
        EditText numeroRipetEsercizio = dialogView.findViewById((int) R.id.numeroRipetizioni);
        EditText numeroTimetEsercizio = dialogView.findViewById((int) R.id.numeroTimer);
        EditText intensitaEsercizio = dialogView.findViewById((int) R.id.tecnicaIntensita);
        EditText esecuzioneEsercizio = dialogView.findViewById((int) R.id.esecuzione);
        Button salva=dialogView.findViewById((int)R.id.salvaEsercizio);
        Button ripristina=dialogView.findViewById((int)R.id.ripristinaEsercizio);
        Button back=dialogView.findViewById((int)R.id.backEsercizio);

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Esercizio esercizio=new Esercizio(nomeEsercizio.getText().toString(),intensitaEsercizio.getText().toString(),
                        esecuzioneEsercizio.getText().toString(),immagineEsercizio.getDrawable(),
                        Integer.parseInt(numeroSerieEsercizio.getText().toString().trim().length()!=0?numeroSerieEsercizio.getText().toString():"0"),
                        Integer.parseInt(numeroRipetEsercizio.getText().toString().trim().length()!=0?numeroRipetEsercizio.getText().toString():"0"),
                        Float.parseFloat(numeroTimetEsercizio.getText().toString().trim().length()!=0?numeroTimetEsercizio.getText().toString():"0")
                        );


                SharedPreferences.Editor edi= sh.edit();
                edi.putString("esercizio",esercizio.toJson());
                edi.apply();

                //devo aggiornare la lista dei giorni per quella lista

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



    }






    private static void selectImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        act.startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), PICK_IMAGE_REQUEST); // Modificato qui

    }





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
    }
}
