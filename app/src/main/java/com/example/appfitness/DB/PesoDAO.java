package com.example.appfitness.DB;

import android.annotation.SuppressLint;
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
}
