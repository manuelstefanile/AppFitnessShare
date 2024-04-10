package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Bean.Utente;

import java.util.ArrayList;
import java.util.Calendar;

public class PesoDAO {
    Context ct;
    DbHelper db;

    public PesoDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    @SuppressLint("Range")

    // Metodo per ottenere le informazioni sul peso dalla tabella
    public ArrayList<Peso> getPesoInfo() {
        ArrayList<Peso> pesoList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + SchemaDB.PesoDB.TABLE_NAME;
        SQLiteDatabase dbW = db.getWritableDatabase();
        Cursor cursor = dbW.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Peso peso = new Peso();
                peso.setId(cursor.getLong(cursor.getColumnIndex(SchemaDB.PesoDB._ID)));
                peso.setPesoKg(cursor.getFloat(cursor.getColumnIndex(SchemaDB.PesoDB.COLUMN_pesoKg)));

                // Converti il valore del timestamp in un oggetto Calendar
                long timestamp = cursor.getLong(cursor.getColumnIndex(SchemaDB.PesoDB.COLUMN_calendario));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                peso.setCalendario(calendar);

                pesoList.add(peso);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pesoList;
    }


    @SuppressLint("Range")
    public Peso getPesoById(long id) {
        SQLiteDatabase dbW = db.getReadableDatabase();
        Peso peso = null;

        Cursor cursor = dbW.query(
                SchemaDB.PesoDB.TABLE_NAME,
                null,
                SchemaDB.PesoDB._ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            peso = new Peso();
            peso.setId(cursor.getLong(cursor.getColumnIndex(SchemaDB.PesoDB._ID)));
            peso.setPesoKg(cursor.getFloat(cursor.getColumnIndex(SchemaDB.PesoDB.COLUMN_pesoKg)));

            Calendar c=Calendar.getInstance();
            long time=cursor.getLong(cursor.getColumnIndex(SchemaDB.PesoDB.COLUMN_calendario));
            c.setTimeInMillis(time);
            peso.setCalendario(c);
            // Se hai altre colonne nel modello, aggiungile qui
            cursor.close();
        }

        db.close();
        return peso;
    }

    public void updatePeso(Peso peso) {
        SQLiteDatabase dbW= db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.PesoDB.COLUMN_pesoKg, peso.getPesoKg());
        values.put(SchemaDB.PesoDB.COLUMN_calendario, peso.getCalendario().getTimeInMillis());

        // Esempio di clausola WHERE se vuoi aggiornare basandoti sull'ID
        String selection = SchemaDB.PesoDB._ID + "=?";
        String[] selectionArgs = { String.valueOf(peso.getId()) };

        // Esegui l'aggiornamento
        int rowsUpdated = dbW.update(
                SchemaDB.PesoDB.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        db.close();
    }
    public Peso insertPeso(Peso peso) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.PesoDB.COLUMN_pesoKg, peso.getPesoKg());
        values.put(SchemaDB.PesoDB.COLUMN_calendario, peso.getCalendario().getTimeInMillis());

        // Esegui l'operazione di inserimento
        long newRowId = dbW.insert(SchemaDB.PesoDB.TABLE_NAME, null, values);
        peso.setId(newRowId);
        db.close();
        return peso; // Ritorna l'ID del nuovo record inserito
    }
}
