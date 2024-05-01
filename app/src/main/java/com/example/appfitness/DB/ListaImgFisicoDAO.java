package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Fisico;
import com.example.appfitness.Pagina3.Global;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ListaImgFisicoDAO {
    Context ct;
    DbHelper db;

    public ListaImgFisicoDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }


    public void inserisciListaImg(Fisico f) {
        //inserisco l ex nel db
        SQLiteDatabase dbWritable = db.getWritableDatabase();

        HashMap<String, byte[]> listaImg=f.getPosa_immagine();
        for (Map.Entry<String, byte[]> entry : listaImg.entrySet()) {
            String nomeImmagine = entry.getKey();
            byte[] byteArray = entry.getValue();

            ContentValues valueslista = new ContentValues();
            valueslista.put(SchemaDB.ListaImmaginiFisicoDB.COLUMN_NomePosa, nomeImmagine);
            valueslista.put(SchemaDB.ListaImmaginiFisicoDB.COLUMN_Immagine, byteArray);
            valueslista.put(SchemaDB.ListaImmaginiFisicoDB.COLUMN_IDFisico, f.getId());

            dbWritable.insert(SchemaDB.ListaImmaginiFisicoDB.TABLE_NAME, null, valueslista);
        }

        dbWritable.close();

    }

    @SuppressLint("Range")
    public HashMap<String, byte[]> getImmaginiPerIdFisico(long idFisico) {
        SQLiteDatabase dbReadable = db.getReadableDatabase();
        HashMap<String, byte[]> immagini = new HashMap<>();

        String selectQuery = "SELECT * FROM " + SchemaDB.ListaImmaginiFisicoDB.TABLE_NAME +
                " WHERE " + SchemaDB.ListaImmaginiFisicoDB.COLUMN_IDFisico + " = ?";
        String[] selectionArgs = {String.valueOf(idFisico)};

        Cursor cursor = dbReadable.rawQuery(selectQuery, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                 String nomeImmagine = cursor.getString(cursor.getColumnIndex(SchemaDB.ListaImmaginiFisicoDB.COLUMN_NomePosa));
                byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(SchemaDB.ListaImmaginiFisicoDB.COLUMN_Immagine));
                immagini.put(nomeImmagine, byteArray);
            } while (cursor.moveToNext());

            cursor.close();
        }

        dbReadable.close();
        return immagini;
    }

    //update dove c è l id di riferimento al fisico
    public boolean updateFis(Fisico f) {




        /*************** SOVRASCRIVE SEMPRE LA STESSA IMG metti anche il secondo controllo sul nome, quindi prendi i nomi precedentementi salvati ******************************/
        deleteImmaginiPerIdFisico(f.getId());

        SQLiteDatabase dbWritable = db.getWritableDatabase();
        HashMap<String, byte[]> listaImg=f.getPosa_immagine();
        int totalRowsAffected = 0;
        for (Map.Entry<String, byte[]> entry : listaImg.entrySet()) {
            String nomeImmagine = entry.getKey();
            byte[] byteArray = entry.getValue();

            ContentValues values = new ContentValues();
            values.put(SchemaDB.ListaImmaginiFisicoDB.COLUMN_NomePosa, nomeImmagine);
            values.put(SchemaDB.ListaImmaginiFisicoDB.COLUMN_IDFisico, f.getId());
            values.put(SchemaDB.ListaImmaginiFisicoDB.COLUMN_Immagine, byteArray);
            dbWritable.insert(SchemaDB.ListaImmaginiFisicoDB.TABLE_NAME,
                    null, values);
            // Aggiungi il numero di righe aggiornate al totale

        }
        dbWritable.close();
        System.out.println("___"+totalRowsAffected);
        // Controlla se l'aggiornamento ha avuto successo
        return true;


    }

    public boolean deleteImmaginiPerIdFisico(long idFisico) {
        SQLiteDatabase dbWritable = db.getWritableDatabase();

        int rowsDeleted = dbWritable.delete(
                SchemaDB.ListaImmaginiFisicoDB.TABLE_NAME,
                SchemaDB.ListaImmaginiFisicoDB.COLUMN_IDFisico + " = ?",
                new String[]{String.valueOf(idFisico)}
        );

        dbWritable.close();

        // Controlla se almeno una riga è stata eliminata con successo
        return rowsDeleted > 0;
    }





}