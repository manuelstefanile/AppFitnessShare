package com.example.appfitness.Pagina3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.DB.SchedaDAO;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;
import com.example.appfitness.Registrazione_Pag2;

import java.util.ArrayList;
import java.util.List;

public class PaginaScheda_Pag3 extends Activity {
    ListView lista;
    PopupSchede popS=new PopupSchede(this);
    TextView nomeutenteR;
    private Scheda schedaTemp;


    private static final String CHANNEL_ID = "TimerChannel";
    private boolean isInBackground = false;
    public static boolean avviaAnimazione=false;



    @Override
    @SuppressLint({"Range", "WrongThread"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schede_pag3);
        // Mostra il popup di benvenuto solo la prima volta
        PopupSchede.mostraPopupBenvenuto(this);




        /*************************************************/
        VisualizzaActivity();
        /*************************************************/
        Global.listaGiornidao= new ListaGiorniDAO(getApplicationContext());
        Global.schedadao=new SchedaDAO(getApplicationContext());
        Global.db=new DbHelper(getApplicationContext());

        Global.giornoDao=new GiornoDAO(getApplicationContext());
        Global.esercizioDao=new EsercizioDAO(getApplicationContext());
        Global.ledao=new ListaEserciziDAO(getApplicationContext());

        //db.deleteDatabase();

        lista = (ListView)findViewById(R.id.listaSchedeView);
        Global.adapterSchede = new AdapterListaScheda(this, R.layout.item_scheda, new ArrayList<Scheda>(),lista);
        lista.setAdapter(Global.adapterSchede);
        nomeutenteR=findViewById((int)R.id.nomeUtente);
        String nomeUtente=getIntent().getStringExtra("nomeUtente");
        if(nomeUtente!=null){
            nomeutenteR.setText("");
            nomeutenteR.setText(nomeUtente);
        }

        //chiama daoScheda
        ArrayList<Scheda> arrSchede= Global.schedadao.getAllSchede();
        for(Scheda schedaTemp:arrSchede){
            //se ho la scheda temp, allora eliminala

            if(schedaTemp.getNomeScheda().equals("temp")){

                Global.schedadao.DeleteScheda(schedaTemp);
            }else
                Global.adapterSchede.add(schedaTemp);
        }
        // Aggiungi un listener per ascoltare quando la vista è stata completamente caricata
        lista.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Rimuovi il listener per evitare chiamate ripetute
                lista.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Avvia l'animazione solo dopo che la ListView è stata completamente caricata
                if(avviaAnimazione) {
                    animateListViewItems();
                    avviaAnimazione=false;
                }
            }
        });

        StampaTutto();
    }

    public static void StampaTutto(){
        System.out.println("*********************************************************");

        // Stampare tutti gli Utenti
        //printUserData(db);

        // Stampare tutti i Pesi
        //printPesoData(db);

        // Stampare tutte le Kcal
        //printKcalData(db);

        // Stampare tutte le Misure
        //printMisureData(db);

        // Stampare tutte le Schede
        printSchedaData(Global.db);

        // Stampare tutte le Liste Giorni
        printListaGiorniData(Global.db);

        // Stampare tutti i Giorni

        printGiornoData(Global.db);

        // Stampare tutte le Liste Esercizi
        printListaEserciziData(Global.db);

        // Stampare tutti gli Esercizi
        printEsercizioData(Global.db);
        System.out.println("*********************************************************");

    }
    // Metodo per stampare tutti gli Utenti
    private static void printUserData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.UtenteDB.TABLE_NAME);
        System.out.println("---- Tutti gli Utenti ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutti i Pesi
    private static void printPesoData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.PesoDB.TABLE_NAME);
        System.out.println("---- Tutti i Pesi ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Kcal
    private static void printKcalData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.KcalDB.TABLE_NAME);
        System.out.println("---- Tutte le Kcal ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Misure
    private static void printMisureData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.MisureDB.TABLE_NAME);
        System.out.println("---- Tutte le Misure ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Schede
    public static void printSchedaData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.SchedaDB.TABLE_NAME);
        System.out.println("---- Tutte le Schede ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Liste Giorni
    public static void printListaGiorniData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.ListaGiorniDB.TABLE_NAME);
        System.out.println("---- Tutte le Liste Giorni ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutti i Giorni
    public static void printGiornoData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.GiornoDB.TABLE_NAME);
        System.out.println("---- Tutti i Giorni ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutte le Liste Esercizi
    public static void printListaEserciziData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.ListaEserciziDB.TABLE_NAME);
        System.out.println("---- Tutte le Liste Esercizi ----");
        printCursor(cursor);
    }

    // Metodo per stampare tutti gli Esercizi
    public static void printEsercizioData(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.EsercizioDB.TABLE_NAME);
        System.out.println("---- Tutti gli Esercizi ----");
        printCursor(cursor);
    }

    // Metodo per stampare un cursore
    public static void printCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    try{
                        System.out.print(cursor.getColumnName(i) + ": " + cursor.getString(i) + " | ");
                    }catch (Exception e){}

                }
                System.out.println(); // Nuova linea per ogni record

            } while (cursor.moveToNext());
        }
        System.out.println(); // Nuova linea per ogni record
        cursor.close();
    }


    public void OnSchedaClick(View v){
        int position = Integer.parseInt(v.getTag().toString());
        Scheda c = (Scheda) Global.adapterSchede.getItem(position);

        //apriSchedaSelezionata
        popS.ApriSchedaSelezionata(c,getLayoutInflater());
    }
    public void OnDeleteSchedaClick(View v){
        int position = Integer.parseInt(v.getTag().toString());
        Scheda c = (Scheda) Global.adapterSchede.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Imposta il layout personalizzato come vista del dialog box
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
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
                // Elimina la scheda
                ArrayList<Integer> listaGiorniId = Global.listaGiornidao.getIDListaGiorniPerScheda(c.getId());
                c.setListaGiorni(listaGiorniId);
                Global.schedadao.DeleteScheda(c);
                Global.adapterSchede.remove(c);
                dialog.dismiss(); // Chiudi il dialog box

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
    public void OnDeleteExPerGiornoClick(View v){

        int position = Integer.parseInt(v.getTag().toString());
        Esercizio c = (Esercizio) Global.adapterEsercizi.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Imposta il layout personalizzato come vista del dialog box
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
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
                // Elimina la lista di esercizi
                Global.ledao.DeleteListaPerNomeEsercizi(c.getId());
                Global.adapterEsercizi.remove(c);
                dialog.dismiss(); // Chiudi il dialog box
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

    public void OnDeleteGiornoClick(View v){
        int position = Integer.parseInt(v.getTag().toString());
        Giorno c = (Giorno) Global.adapterGiorni.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Imposta il layout personalizzato come vista del dialog box
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
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
                // Elimina il giorno
                Global.giornoDao.DeleteGiornoByGiorno(c);
                Global.adapterGiorni.remove(c);
                dialog.dismiss(); // Chiudi il dialog box
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
    public void CreaScheda(View v){

        popS.CreaScheda(getLayoutInflater());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PopupSchede.onActivityResult(requestCode, resultCode, data);
    }

    public void ChiudTuttoNonSalva(View v){
        SharedPreferences shp=this.getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(COSTANTI.NOTE_SCHEDA, new Note().toJson());
        edit.putString(COSTANTI.NOTE_GIORNO, new Note().toJson());
        edit.putString(COSTANTI.NOTE_ESERCIZIO, new Note().toJson());
        edit.commit();
        finish();
        Intent intent = new Intent(this, PaginaScheda_Pag3.class);
        intent.putExtra("nomeUtente",nomeutenteR.getText().toString());
        startActivity(intent);
    }

    public void EditUtente(View v){
        Intent i =new Intent();
        i.setClass(getApplicationContext(), Registrazione_Pag2.class);
        i.putExtra("mode","edit");
        startActivity(i);
    }
    public void SeeUtente(View v){
        Intent i =new Intent();
        i.setClass(getApplicationContext(), Registrazione_Pag2.class);
        i.putExtra("mode","see");
        startActivity(i);
    }



    public void VisualizzaActivity(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(10); // Numero massimo di attività da visualizzare
            for (ActivityManager.RunningTaskInfo task : tasks) {
                System.out.println("ActivityStack" + task.baseActivity.getClassName());

            }
        }

    }

    public void AvviaGiorno(View v){
        int position = Integer.parseInt(v.getTag().toString());
        Giorno c = (Giorno) Global.adapterGiorni.getItem(position);

        PopupGiorno.AvviaGiornoSelezionato(c,getLayoutInflater());

    }

    public void OnImmagineClick(View v){
        ImageView imag= (ImageView)v;
        NotificheDialog.NotificaImmagine(getLayoutInflater(),imag.getDrawable());
    }







    /***********************************/
    @Override
    protected void onResume() {
        super.onResume();
        String nomeUtente=getIntent().getStringExtra("nomeUtente");

        if(nomeUtente!=null){

            nomeutenteR.setText(nomeUtente);
        }
        isInBackground = false;
        stopTimerService();
    }
    @Override
    protected void onPause() {
        super.onPause();
        isInBackground = true;

        if(PopupEsercizio.tempotemp!=null &&PopupEsercizio.tempotemp[0]!=0) {
            TimerService.millisecondi = PopupEsercizio.tempotemp[0];
            startTimerService();
        }
    }


    private void startTimerService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        startService(serviceIntent);
    }

    private void stopTimerService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
    }

    private void animateListViewItems() {
        System.out.println("num el "+lista.getChildCount());
        // Per ogni elemento nella ListView, avvia l'animazione
        for (int i = 0; i < lista.getChildCount(); i++) {
            View view = lista.getChildAt(i);
            System.out.println("num el "+view);
            Animation animation =null;
            if(i%2==0) {
                animation=AnimationUtils.loadAnimation(this, R.anim.anim_move_item);
            }else animation=AnimationUtils.loadAnimation(this, R.anim.anim_move_item2);
            animation.setDuration(1500);
            view.startAnimation(animation);
        }
    }

}