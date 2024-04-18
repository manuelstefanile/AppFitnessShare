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
import com.example.appfitness.Pagina3.Global;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;
import com.example.appfitness.Pagina3.PopupEsercizio;

import java.io.ByteArrayOutputStream;
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
            result.setNumeroRipetizioni(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_numeroRipetizioni)));
            result.setNumeroSerie(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_numeroSerie)));
            result.setTecnica_intensita(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_tecnica_intensita)));
            result.setTimer(cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_timer)));
            result.setPesoKG(cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.EsercizioDB.COLUMN_pesoKG)));
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

    public void DeleteEsercizioById(long id){
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.EsercizioDB.TABLE_NAME,
                SchemaDB.EsercizioDB._ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();

        //elimino dalla lista di ex quelli associati all esercizio
        Global.ledao.DeleteListaPerNomeEsercizi(id);

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

    public Esercizio inserisciEsercizio(Esercizio esercizio) {
        //inserisco l ex nel db
        SQLiteDatabase dbWritable = db.getWritableDatabase();

        ContentValues valuesEsercizio = new ContentValues();
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_nomeEsercizio, esercizio.getNomeEsercizio());
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_tecnica_intensita, esercizio.getTecnica_intensita());
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_esecuzione, esercizio.getEsecuzione());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(esercizio.getImmagineMacchinario()!=null) {
            Bitmap bitmap = ((BitmapDrawable) esercizio.getImmagineMacchinario()).getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }

        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_immagineMacchinario, stream.toByteArray());
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_numeroSerie, esercizio.getNumeroSerie());
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_numeroRipetizioni, esercizio.getNumeroRipetizioni());
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_timer, esercizio.getTimer());
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_pesoKG, esercizio.getPesoKG());
        valuesEsercizio.put(SchemaDB.EsercizioDB.COLUMN_note,esercizio.getNote());

        long EsercizioId = dbWritable.insert(SchemaDB.EsercizioDB.TABLE_NAME, null, valuesEsercizio);
        esercizio.setId(EsercizioId);

        return esercizio;
    }

    public boolean updateEsercizio(Esercizio esercizio) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.EsercizioDB.COLUMN_nomeEsercizio, esercizio.getNomeEsercizio());
        values.put(SchemaDB.EsercizioDB.COLUMN_esecuzione, esercizio.getEsecuzione());
        values.put(SchemaDB.EsercizioDB.COLUMN_numeroRipetizioni, esercizio.getNumeroRipetizioni());
        values.put(SchemaDB.EsercizioDB.COLUMN_numeroSerie, esercizio.getNumeroSerie());
        values.put(SchemaDB.EsercizioDB.COLUMN_tecnica_intensita, esercizio.getTecnica_intensita());
        values.put(SchemaDB.EsercizioDB.COLUMN_pesoKG, esercizio.getPesoKG());
        values.put(SchemaDB.EsercizioDB.COLUMN_timer, esercizio.getTimer());

        // Converti l'immagine in un byte array
        byte[] immagineByteArray = null;
        Drawable immagineDrawable = esercizio.getImmagineMacchinario();
        if (immagineDrawable != null) {
            Bitmap bitmap = ((BitmapDrawable) immagineDrawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            immagineByteArray = stream.toByteArray();
        }
        values.put(SchemaDB.EsercizioDB.COLUMN_immagineMacchinario, immagineByteArray);

        values.put(SchemaDB.EsercizioDB.COLUMN_note, esercizio.getNote());

        String whereClause = SchemaDB.EsercizioDB._ID + " = ?";
        String[] whereArgs = {String.valueOf(esercizio.getId())};

        int rowsAffected = dbW.update(SchemaDB.EsercizioDB.TABLE_NAME, values, whereClause, whereArgs);

        dbW.close();

        PaginaScheda_Pag3.StampaTutto();
        System.out.println("______"+esercizio);

        return rowsAffected > 0;
    }

}
