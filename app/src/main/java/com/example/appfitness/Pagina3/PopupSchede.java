package com.example.appfitness.Pagina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.R;

import java.io.IOException;
import java.util.ArrayList;

public class PopupSchede {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static Activity act; // Aggiunto
    private static ImageButton imgScheda;

    public PopupSchede(Activity activity) { // Costruttore aggiunto
        this.act = activity;
    }

    public  void CreaScheda(LayoutInflater inflater){


        // Creazione del layout della tua View
        View dialogView = inflater.inflate(R.layout.crea_scheda, null);
        Button salvaButton = dialogView.findViewById((int)R.id.salvaButton);
        Button okButton = dialogView.findViewById((int)R.id.okButton);
        // Creazione dell'AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        // Mostra l'AlertDialog
        AlertDialog alertDialog = builder.create();
        // Imposta lo sfondo trasparente
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        WindowManager wm = (WindowManager) dialogView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        RelativeLayout ll = dialogView.findViewById(R.id.origineScheda);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);

        alertDialog.getWindow().setLayout(size.x, size.y);

        //creo la scheda temp e l aggiungo al db
        Scheda schedaTemp=Global.schedadao.CreaSchedaTemp();

        //setto l adapter dei giorni a vuoto
        ListView listaGiorniView = (ListView)dialogView.findViewById(R.id.listaGiorniView);
        Global.adapterGiorni = new AdapterListaScheda(dialogView.getContext(), R.layout.item_giorni, new ArrayList<Giorno>());
        listaGiorniView.setAdapter(Global.adapterGiorni);


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

                PopupGiorno.CreaGiorno(inflater,schedaTemp);
            }
        });

        final View backgroundView = alertDialog.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                back.callOnClick();
                return true; // Indica che l'evento è stato consumato
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nomeScheda.getText().toString().trim().length()==0){
                    Toast.makeText(dialogView.getContext(), "Inserisci un nome", Toast.LENGTH_SHORT).show();
                }else {
                    schedaTemp.setNomeScheda(nomeScheda.getText().toString());
                    schedaTemp.setImg(imgScheda.getDrawable());
                    Global.adapterSchede.add(schedaTemp);
                    Global.schedadao.ModificaSchedaTemp(schedaTemp);
                    Global.schedadao.ModificaSchedaTemp(schedaTemp);
                    PaginaScheda_Pag3.StampaTutto();
                    alertDialog.dismiss();
                    ResettaVariabili();
                }
            }
        });
    }

    public void ApriSchedaSelezionata(Scheda sched,LayoutInflater inflater){
        //per rendere accessibile la scheda ai giorni con oclick dell adapter

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
        // Impostazione del background trasparente
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        WindowManager wm = (WindowManager) dialogView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        RelativeLayout ll = dialogView.findViewById(R.id.origineScheda);
        ViewGroup.LayoutParams llParams = ll.getLayoutParams();
        llParams.height = size.y; // Altezza dello schermo
        ll.setLayoutParams(llParams);

        alertDialog.getWindow().setLayout(size.x, size.y);

        ListView listaGiorniView = (ListView)dialogView.findViewById(R.id.listaGiorniView);
        Global.adapterGiorni = new AdapterListaScheda(dialogView.getContext(), R.layout.item_giorni, new ArrayList<Giorno>());
        listaGiorniView.setAdapter(Global.adapterGiorni);

        Global.listaGiornidao=new ListaGiorniDAO(act.getApplicationContext());

        ArrayList<Integer> listaDiID =Global.listaGiornidao.getListaGiorniPerScheda(sched.getId());

        //per ogni id, ricercami l'elemento giorno e aggiungilo alla lista di giorni visibile
        for(Integer id:listaDiID){
            Global.adapterGiorni.add(Global.giornoDao.getGiornoById(id));
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
                PopupGiorno.CreaGiorno(inflater,sched);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sched.getListaGiorni().size()>0)
                    Global.listaGiornidao.InserisciListaGiorni(sched);
                PaginaScheda_Pag3.StampaTutto();
                alertDialog.dismiss();
                ResettaVariabili();
            }
        });




    }

    private void ResettaVariabili(){
        Global.schedaNuova=null;
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
