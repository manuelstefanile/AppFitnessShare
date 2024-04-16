package com.example.appfitness.Pagina3;

import static com.google.android.material.resources.MaterialResources.getDrawable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;
import com.example.appfitness.Registrazione_Pag2;

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

        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //note all inizio della creazione dell ex è vuoto
        SharedPreferences.Editor edit=shp.edit();
        edit.putString("notePassate",new Note().toJson());
        edit.commit();


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
        Button bottoneNote = dialogView.findViewById((int)R.id.bottoneNoteScheda);
        creaGiorno.setEnabled(false);
        //creaGiorno.setBackground((getDrawable((int) R.drawable.drawable_botton_grigio)));


        creaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupGiorno.CreaGiorno(inflater,schedaTemp);
            }
        });

        imgScheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupSchede.selectImageFromGallery();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nomeScheda.getText().toString().trim().length() == 0) {
                    Toast.makeText(dialogView.getContext(), "Inserisci un nome per salvare la scheda.", Toast.LENGTH_LONG).show();
                }
                else {
                        //per le note
                        SharedPreferences sharedPreferences = inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        Note note = Note.fromJson(sharedPreferences.getString("notePassate", null));

                        schedaTemp.setNomeScheda(nomeScheda.getText().toString());
                        schedaTemp.setImg(imgScheda.getDrawable());
                        schedaTemp.setNote(note.getNote());
                        Global.adapterSchede.add(schedaTemp);
                        Global.schedadao.ModificaSchedaTemp(schedaTemp);
                        Global.schedadao.ModificaSchedaTemp(schedaTemp);
                        PaginaScheda_Pag3.StampaTutto();

                        //ripristino le note
                        SharedPreferences.Editor edit = shp.edit();
                        edit.putString("notePassate", new Note().toJson());
                        edit.commit();

                        //alertDialog.dismiss();
                        ResettaVariabili();
                        Toast.makeText(dialogView.getContext(), "Scheda salvata, Keep going Buddy!", Toast.LENGTH_LONG).show();
                        creaGiorno.setEnabled(true);

                    }

            }
        });

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
    }

    public void ApriSchedaSelezionata(Scheda sched,LayoutInflater inflater){

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
        Button bottoneNote = dialogView.findViewById((int)R.id.bottoneNoteScheda);
        bottoneNote.setText("Note");
        nomeScheda.setText(sched.getNomeScheda());

        if(sched.getImg()!=null)
            imgScheda.setImageDrawable(sched.getImg());



        imgScheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupSchede.selectImageFromGallery();
            }
        });

        creaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupGiorno.CreaGiorno(inflater,sched);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sched.getListaGiorni().size() > 0)
                    Global.listaGiornidao.InserisciListaGiorni(sched);
                //rimuovo dalla lista giorni
                Global.adapterSchede.remove(sched);
                String nomeSchedaPrimaModifica = sched.getNomeScheda(); // Nome della scheda prima della modifica

                sched.setNomeScheda(nomeScheda.getText().toString());
                sched.setImg(imgScheda.getDrawable());

                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString("notePassate", null));
                sched.setNote(note.getNote());
                Global.schedadao.updateScheda(sched);

                Global.adapterSchede.add(sched);

                PaginaScheda_Pag3.StampaTutto();
                // Controllo se il nome della scheda è stato modificato
                if (!nomeSchedaPrimaModifica.equals(nomeScheda.getText().toString())) {
                    // Il nome della scheda è stato modificato, mostro il Toast
                    Toast.makeText(dialogView.getContext(), "Il nome della scheda è stato modificato", Toast.LENGTH_SHORT).show();
                } else if (sched.getImg() != null && !areImagesEqual(sched.getImg(), imgScheda.getDrawable())) {
                    // L'immagine è stata modificata, mostro il Toast
                    Toast.makeText(dialogView.getContext(), "Solo l'immagine è stata aggiornata.", Toast.LENGTH_SHORT).show();
                }

                //alertDialog.dismiss();
                //ResettaVariabili();
            }
        });
        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit= sh.edit();
                PaginaScheda_Pag3.StampaTutto();

                Note note=Note.fromJson(sh.getString("notePassate", null));
                Note notaDaMostrare;
                if(note.getNote()!=null){
                    notaDaMostrare=note;
                }else
                    notaDaMostrare= new Note(sched.getNote());

                edit.putString("notePassate", notaDaMostrare.toJson());
                edit.apply();
                try {
                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater,sh);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
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
            // Mostra il Toast per confermare l'aggiunta dell'immagine
            Toast.makeText(act, "Bella personalizzazione con l'immagine!", Toast.LENGTH_LONG).show();
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
    private boolean areImagesEqual(Drawable drawable1, Drawable drawable2) {
        Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
        Bitmap bitmap2 = ((BitmapDrawable) drawable2).getBitmap();

        if (bitmap1 == null || bitmap2 == null) {
            return false;
        }

        if (bitmap1.getWidth() != bitmap2.getWidth() || bitmap1.getHeight() != bitmap2.getHeight()) {
            return false;
        }

        for (int x = 0; x < bitmap1.getWidth(); x++) {
            for (int y = 0; y < bitmap1.getHeight(); y++) {
                if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

}
