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
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Pagina3.PopupEsercizio;

import java.util.ArrayList;

public class EsercizioDAO {
    Context ct;
    DbHelper db;

    public EsercizioDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    public Esercizio getEsercizioById(int id) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Esercizio result = null;

        Cursor cursor = dbRead.query(
                SchemaDB.EsercizioDB.TABLE_NAME, // Nome della tua tabella Giorno
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.EsercizioDB._ID + " = ?", // Clausola WHERE per l'ID
                new String[]{String.valueOf(id)}, // Valore per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        if (cursor.moveToFirst()) {
            // Se la riga è stata trovata, puoi creare il tuo oggetto Giorno
            result = new Esercizio();
            result.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB._ID)));
            result.setNomeEsercizio(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_nomeEsercizio)));
            // Chiudi il cursore
            cursor.close();
        }
        // Chiudi il database
        dbRead.close();

        return result;
    }

    public Esercizio getEsercizioByNome(String nome) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Esercizio result = null;

        Cursor cursor = dbRead.query(
                SchemaDB.EsercizioDB.TABLE_NAME, // Nome della tua tabella Giorno
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.EsercizioDB.COLUMN_nomeEsercizio + " = ?", // Clausola WHERE per l'ID
                new String[]{nome}, // Valore per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        if (cursor.moveToFirst()) {
            // Se la riga è stata trovata, puoi creare il tuo oggetto Giorno
            result = new Esercizio();
            result.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB._ID)));
            result.setNomeEsercizio(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_nomeEsercizio)));
            result.setEsecuzione(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_esecuzione)));
            result.setNumeroRipetizioni(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_numeroRipetizioni)));
            result.setNumeroSerie(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_numeroSerie)));
            result.setTecnica_intensita(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_tecnica_intensita)));
            result.setTimer(cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_timer)));
            @SuppressLint("Range") byte[] immagine=cursor.getBlob(cursor.getColumnIndex(SchemaDB.EsercizioDB.COLUMN_immagineMacchinario));
            Bitmap bitmap = BitmapFactory.decodeByteArray(immagine, 0, immagine.length);
            Drawable immDraw= new BitmapDrawable(Resources.getSystem(), bitmap);
            result.setImmagineMacchinario(immDraw);
            result.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_note)));

            // Chiudi il cursore
            cursor.close();
        }
        // Chiudi il database
        dbRead.close();

        return result;
    }

    public void DeleteEsercizioById(int id){
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.EsercizioDB.TABLE_NAME,
                SchemaDB.EsercizioDB._ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();

    }
    public void DeleteEsercizioByName(String id){
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.EsercizioDB.TABLE_NAME,
                SchemaDB.EsercizioDB.COLUMN_nomeEsercizio + " = ?",
                new String[]{String.valueOf(id)});
        db.close();

        //prendo l id dell ex

        ListaEserciziDAO listaEserciziDAO=new ListaEserciziDAO(ct);
        //listaEserciziDAO.DeleteListaPerNomeEsercizi(id);



    }



}
