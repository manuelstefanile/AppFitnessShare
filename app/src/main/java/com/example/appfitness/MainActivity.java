package com.example.appfitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("creo canale");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.default_notification_channel_id),
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},101);

            }
        }



        //se l utente è gia registrato, vai a pagina 3
        DbHelper db=new DbHelper(getApplicationContext());
        //db.deleteDatabase();
        SQLiteDatabase dbRead=db.getReadableDatabase();
        int count=0;

        TextView te=findViewById((int)R.id.testoUtenteInizia);
        // Query per ottenere tutti gli utenti
        Cursor cursor = dbRead.query(
                SchemaDB.UtenteDB.TABLE_NAME,   // Nome della tabella
                null,       // Tutte le colonne (null = tutte le colonne)
                null,       // Colonne per la clausola WHERE (null = tutte le righe)
                null,       // Valori per la clausola WHERE (null = tutte le righe)
                null,       // GroupBy
                null,       // Having
                null        // OrderBy
        );

        // Ora puoi iterare attraverso il cursore per ottenere i risultati
        String nomeUtente=null;
        while (cursor.moveToNext()) {
            count++;
            nomeUtente=cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.UtenteDB.COLUMN_nomeUtente));
        }
        // Chiudi il cursore quando hai finito di utilizzarlo
        cursor.close();

        //porta in una nuova activity

        if(count==0){


        }else {
            te.setText("Bentornato, " +nomeUtente);
            te.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            te.setTypeface(null, Typeface.BOLD);
            te.setGravity(Gravity.CENTER);

        }



    }
    public void Inizia(View bottone){


        //se l utente è gia registrato, vai a pagina 3
        DbHelper db=new DbHelper(getApplicationContext());
        //db.deleteDatabase();
        SQLiteDatabase dbRead=db.getReadableDatabase();
        int count=0;

        // Query per ottenere tutti gli utenti
        Cursor cursor = dbRead.query(
                SchemaDB.UtenteDB.TABLE_NAME,   // Nome della tabella
                null,       // Tutte le colonne (null = tutte le colonne)
                null,       // Colonne per la clausola WHERE (null = tutte le righe)
                null,       // Valori per la clausola WHERE (null = tutte le righe)
                null,       // GroupBy
                null,       // Having
                null        // OrderBy
        );

        // Ora puoi iterare attraverso il cursore per ottenere i risultati
        String nomeUtente=null;
        while (cursor.moveToNext()) {
            count++;
            nomeUtente=cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.UtenteDB.COLUMN_nomeUtente));
        }
        // Chiudi il cursore quando hai finito di utilizzarlo
        cursor.close();

        //porta in una nuova activity
        Intent i =new Intent();
        if(count==0){
            i.setClass(getApplicationContext(),Registrazione_Pag2.class);
        }else {
            i.setClass(getApplicationContext(), PaginaScheda_Pag3.class);
            i.putExtra("nomeUtente",nomeUtente);
        }

        startActivity(i);
    }


}