package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Bean.Utente;

import java.util.ArrayList;
import java.util.Calendar;

public class kcalDAO {
    Context ct;
    DbHelper db;

    public kcalDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    @SuppressLint("Range")
    public ArrayList<Kcal> getKcalInfo() {
        ArrayList<Kcal> kcalList = new ArrayList<>();

        // Query per selezionare tutte le righe dalla tabella KcalDB
        String selectQuery = "SELECT * FROM " + SchemaDB.KcalDB.TABLE_NAME;

        SQLiteDatabase dbW = db.getWritableDatabase();
        Cursor cursor = dbW.rawQuery(selectQuery, null);

        // Itera attraverso tutte le righe e aggiungi i dati alla lista di KcalModel
        if (cursor.moveToFirst()) {
            do {
                Kcal kcal = new Kcal();
                kcal.setId(cursor.getInt(cursor.getColumnIndex(SchemaDB.KcalDB._ID)));
                kcal.setKcal(cursor.getInt(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_kcal)));

                String faseString = cursor.getString(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_fase));
                Kcal.Fase faseEnum = Kcal.Fase.fromString(faseString);
                kcal.setFase(faseEnum);

                kcal.setCarbo(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_carboidrati)));
                kcal.setProteine(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_proteine)));
                kcal.setGrassi(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_grassi)));
                kcal.setSale(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_sale)));
                kcal.setAcqua(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_acqua)));
                kcal.setNote(cursor.getString(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_note)));

                // Converti il valore del timestamp in un oggetto Calendar
                long timestamp = cursor.getLong(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_calendario));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                kcal.setData(calendar);

                kcalList.add(kcal);
            } while (cursor.moveToNext());
        }

        // Chiudi il cursore e il database
        cursor.close();
        db.close();

        return kcalList;
    }

    @SuppressLint("Range")
    public Kcal getKcalById(long id) {
        SQLiteDatabase dbW = db.getReadableDatabase();
        Kcal kcal = null;

        Cursor cursor = dbW.query(
                SchemaDB.KcalDB.TABLE_NAME,
                null,
                SchemaDB.KcalDB._ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            kcal = new Kcal();
            kcal.setId(cursor.getInt(cursor.getColumnIndex(SchemaDB.KcalDB._ID)));
            kcal.setKcal(cursor.getInt(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_kcal)));

            String faseString = cursor.getString(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_fase));
            Kcal.Fase faseEnum = Kcal.Fase.fromString(faseString);
            kcal.setFase(faseEnum);

            kcal.setCarbo(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_carboidrati)));
            kcal.setProteine(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_proteine)));
            kcal.setGrassi(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_grassi)));
            kcal.setSale(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_sale)));
            kcal.setAcqua(cursor.getFloat(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_acqua)));
            kcal.setNote(cursor.getString(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_note)));

            // Converti il valore del timestamp in un oggetto Calendar
            long timestamp = cursor.getLong(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_calendario));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            kcal.setData(calendar);

            cursor.close();
        }

        db.close();
        return kcal;
    }


}
