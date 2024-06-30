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
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
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

import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Bean.SerializzazioneFileScheda;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.ImportExport;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;
import com.example.appfitness.Registrazione_Pag2;
import com.example.appfitness.ToastPersonalizzato;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class PopupSchede {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int CREATE_FILE_REQUEST_CODE = 5;
    public static Activity act; // Aggiunto
    private static ImageButton imgScheda;
    private static final String PREFS_NAME = "PopupPrefs";
    private static final String PREF_POPUP_SHOW = "popup_show";


    public PopupSchede(Activity activity) { // Costruttore aggiunto
        this.act = activity;
    }

    public static void mostraPopupBenvenuto(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean popupShow = prefs.getBoolean(PREF_POPUP_SHOW, false);

        if (!popupShow) {
            // Mostra il popup di benvenuto solo se non è stato mostrato in precedenza
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Benvenuto Buddy");
            String titleText = "Benvenuto Buddy";
            SpannableStringBuilder titleBuilder = new SpannableStringBuilder(titleText);
            titleBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setTitle(titleBuilder);
            SpannableStringBuilder message = new SpannableStringBuilder();
            message.append("In questa pagina potrai trovare:\n");

            // Aggiungi ciascun messaggio con stile italic
            message.append(Html.fromHtml("<br>"));
            message.append(Html.fromHtml("<i>- in alto a sinistra il tasto per modificare il tuo profilo.</i><br>"));
            message.append(Html.fromHtml("<br>"));
            message.append(Html.fromHtml("<i>- in alto a destra il tasto per poter visualizzare le tue informazioni.</i><br>"));
            message.append(Html.fromHtml("<br>"));
            message.append(Html.fromHtml("<i>- in basso al centro il tasto per poter creare una nuova scheda di allenamento.</i><br>"));


            // Imposta il messaggio nel builder
            builder.setMessage(message);

            builder.setPositiveButton("Capito!", (dialog, which) -> {
                // Salva lo stato del popup mostrato nella SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PREF_POPUP_SHOW, true);
                editor.apply();
            });

            builder.show();
        }
    }

    public  void CreaScheda(LayoutInflater inflater){
        PaginaScheda_Pag3.avviaAnimazione=true;

        SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //note all inizio della creazione dell ex è vuoto
        SharedPreferences.Editor edit=shp.edit();
        edit.putString(COSTANTI.NOTE_SCHEDA,new Note().toJson());
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
        Global.adapterGiorni = new AdapterListaScheda(dialogView.getContext(), R.layout.item_giorni, new ArrayList<Giorno>(),listaGiorniView);
        listaGiorniView.setAdapter(Global.adapterGiorni);


        EditText nomeScheda= dialogView.findViewById((int)R.id.nomeScheda);
        imgScheda=dialogView.findViewById((int)R.id.aggiungiImmagineScheda);
        Button creaGiorno=dialogView.findViewById((int)R.id.CreaGiorno);
        Button back=dialogView.findViewById((int)R.id.backScheda);
        Button bottoneNote = dialogView.findViewById((int)R.id.bottoneNoteScheda);
        Button uploadScheda=dialogView.findViewById((int)R.id.uploadScheda);
        creaGiorno.setEnabled(false);
        creaGiorno.setAlpha(0.5F);
        uploadScheda.setVisibility(View.INVISIBLE);
        //creaGiorno.setBackground((getDrawable((int) R.drawable.drawable_botton_grigio)));


        creaGiorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupGiorno.CreaGiorno(inflater,schedaTemp);
            }
        });

        NotificheDialog.NotificaImmaginePremutaIngrandisci(inflater,imgScheda);
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
                    ToastPersonalizzato.ToastErrore("Inserisci un nome per salvare la scheda.",inflater);
                }
                else {
                    for(Scheda s:Global.schedadao.getAllSchede()){
                        if(s.getNomeScheda().equals(nomeScheda.getText().toString())
                                &&!(schedaTemp.getNomeScheda().equals(nomeScheda.getText().toString()))){
                            ToastPersonalizzato.ToastErrore("Nome già presente.",inflater);
                            return;
                        }
                    }
                    //per le note
                    SharedPreferences sharedPreferences = inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    Note note = Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_SCHEDA, null));

                    schedaTemp.setNomeScheda(nomeScheda.getText().toString());
                    //se l immagine è quella di default allora non salvo nel db
                    Drawable imgIns=null;
                    if(!Global.areImagesEqual(imgScheda.getDrawable(), dialogView.getResources().getDrawable(R.drawable.noimg))){
                        imgIns=imgScheda.getDrawable();
                    }


                    schedaTemp.setImg(imgIns);


                    schedaTemp.setNote(note.getNote());
                    Global.adapterSchede.add(schedaTemp);
                    Global.schedadao.ModificaSchedaTemp(schedaTemp);


                    //alertDialog.dismiss();
                    //note all inizio della creazione dell ex è vuoto
                    SharedPreferences.Editor edit = shp.edit();
                    edit.putString(COSTANTI.NOTE_SCHEDA, new Note().toJson());
                    edit.commit();

                    ResettaVariabili();
                    ToastPersonalizzato.ToastSuccesso("Scheda salvata, Keep going Buddy!",inflater);
                    creaGiorno.setEnabled(true);
                    creaGiorno.setAlpha(1f);

                }

            }
        });

        bottoneNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                try {
                    SharedPreferences shp=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit= shp.edit();
                    Note note=Note.fromJson(shp.getString(COSTANTI.NOTE_SCHEDA, null));
                    Note notaDaMostrare;
                    if(note.getNote()!=null){
                        notaDaMostrare=note;
                    }else
                        notaDaMostrare= new Note(schedaTemp.getNote());
                    edit.putString(COSTANTI.NOTE_SCHEDA, notaDaMostrare.toJson());
                    edit.apply();

                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater, shp,COSTANTI.NOTE_SCHEDA);
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
        Global.adapterGiorni = new AdapterListaScheda(dialogView.getContext(), R.layout.item_giorni, new ArrayList<Giorno>(),listaGiorniView);
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
        Button uploadScheda=dialogView.findViewById((int)R.id.uploadScheda);
        bottoneNote.setText("Note");
        nomeScheda.setText(sched.getNomeScheda());




        if(sched.getImg()!=null)
            imgScheda.setImageDrawable(sched.getImg());



        uploadScheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadSchedaFile(sched.getId());
            }
        });

        NotificheDialog.NotificaImmaginePremutaIngrandisci(inflater,imgScheda);
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


                boolean uguale=false;
                for(Scheda s:Global.schedadao.getAllSchede()){
                    if(s.getNomeScheda().equals(nomeScheda.getText().toString())){
                        uguale=true;
                        break;
                    }
                }
                if(!uguale)
                    sched.setNomeScheda(nomeScheda.getText().toString());

                //se l immagine è quella di default allora non salvo nel db
                Drawable imgIns=null;
                if(!Global.areImagesEqual(imgScheda.getDrawable(), dialogView.getResources().getDrawable(R.drawable.noimg))){
                    imgIns=imgScheda.getDrawable();
                }
                sched.setImg(imgIns);

                SharedPreferences sharedPreferences=inflater.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                Note note=Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_SCHEDA, null));
                sched.setNote(note.getNote());
                Global.schedadao.updateScheda(sched);

                Global.adapterSchede.add(sched);


                // Controllo se il nome della scheda è stato modificato
                if (!nomeSchedaPrimaModifica.equals(nomeScheda.getText().toString())) {
                    // Il nome della scheda è stato modificato, mostro il Toast
                    ToastPersonalizzato.ToastSuccesso("Il nome della scheda è stato modificato",inflater);
                } else if (sched.getImg() != null && !Global.areImagesEqual(sched.getImg(), imgScheda.getDrawable())) {

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


                Note note=Note.fromJson(sh.getString(COSTANTI.NOTE_SCHEDA, null));
                Note notaDaMostrare;
                if(note.getNote()!=null){
                    notaDaMostrare=note;
                }else
                    notaDaMostrare= new Note(sched.getNote());

                edit.putString(COSTANTI.NOTE_SCHEDA, notaDaMostrare.toJson());
                edit.apply();
                try {
                    Registrazione_Pag2.editGlobal=true;
                    NotificheDialog.NotificaNote(inflater,sh,COSTANTI.NOTE_SCHEDA);
                } catch (Eccezioni e) {
                    e.printStackTrace();
                }
            }
        });







    }

    private void ResettaVariabili(){
        PopupGiorno.idGiorniSalvati= new ArrayList<>();
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

    private void UploadSchedaFile(Long idScheda){
        ImportExport impExp= new ImportExport();
        impExp.UploadSchedaFile(idScheda,act);
    }
}
