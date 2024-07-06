package com.example.appfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TextView;

import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.Pagina3.Global;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

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

                showNotificationPermissionDialog();

            }
        }



        //se l utente è gia registrato, vai a pagina 3
        DbHelper db=new DbHelper(getApplicationContext());
        db.deleteDatabase();
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


    private void showNotificationPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permesso di Notifica");
        builder.setMessage("Questa app richiede il permesso di inviare notifiche per mostrarti il timer di " +
                "recupero quando l'applicazione è in background. " +
                "Cosi puoi usare altre app mentre recuperi");

        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permesso concesso
                ToastPersonalizzato.ToastSuccesso("Permesso per le notifiche concesso",getLayoutInflater());
            } else {
                // Permesso negato
                ToastPersonalizzato.ToastErrore("Permesso per le notifiche negato", getLayoutInflater());

            }
        }
    }

    @Override
    public void onBackPressed() {
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

        TextView testo = dialog.findViewById(R.id.testoCustom);
        testo.setText("Sei sicuro di voler uscire?");
        // Aggiunta dei listener di click ai pulsanti
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss(); // Chiudi il dialog box

            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Chiudi il dialog box
            }
        });

    }
}