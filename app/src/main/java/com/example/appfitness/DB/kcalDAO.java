package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Bean.Utente;
import com.example.appfitness.Pagina3.Global;

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
                String timestamp = cursor.getString(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_calendario));
                Calendar data=Global.ConversioneStringCalendar(timestamp);
                kcal.setData(data);

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
            String timestamp = cursor.getString(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_calendario));
            Calendar data=Global.ConversioneStringCalendar(timestamp);
            kcal.setData(data);

            cursor.close();
        }

        db.close();
        return kcal;
    }

    public Kcal insertKcal(Kcal kcal) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.KcalDB.COLUMN_kcal, kcal.getKcal());
        values.put(SchemaDB.KcalDB.COLUMN_fase, kcal.getFase().toString());
        values.put(SchemaDB.KcalDB.COLUMN_carboidrati, kcal.getCarbo());
        values.put(SchemaDB.KcalDB.COLUMN_proteine, kcal.getProteine());
        values.put(SchemaDB.KcalDB.COLUMN_grassi, kcal.getGrassi());
        values.put(SchemaDB.KcalDB.COLUMN_sale, kcal.getSale());
        values.put(SchemaDB.KcalDB.COLUMN_acqua, kcal.getAcqua());
        values.put(SchemaDB.KcalDB.COLUMN_note, kcal.getNote());
        values.put(SchemaDB.KcalDB.COLUMN_calendario, Global.ConversioneCalendarString(kcal.getData()));

        // Esegui l'operazione di inserimento
        long newRowId = dbW.insert(SchemaDB.KcalDB.TABLE_NAME, null, values);
        kcal.setId(newRowId);

        db.close();

        return kcal; // Ritorna l'ID del nuovo record inserito
    }


    public boolean updateKcal(Kcal kcal) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.KcalDB.COLUMN_kcal, kcal.getKcal());
        values.put(SchemaDB.KcalDB.COLUMN_fase, kcal.getFase().toString());
        values.put(SchemaDB.KcalDB.COLUMN_carboidrati, kcal.getCarbo());
        values.put(SchemaDB.KcalDB.COLUMN_proteine, kcal.getProteine());
        values.put(SchemaDB.KcalDB.COLUMN_grassi, kcal.getGrassi());
        values.put(SchemaDB.KcalDB.COLUMN_sale, kcal.getSale());
        values.put(SchemaDB.KcalDB.COLUMN_acqua, kcal.getAcqua());
        values.put(SchemaDB.KcalDB.COLUMN_note, kcal.getNote());
        values.put(SchemaDB.KcalDB.COLUMN_calendario, Global.ConversioneCalendarString(kcal.getData()));

        String selection = SchemaDB.KcalDB._ID + " = ?";
        String[] selectionArgs = { String.valueOf(kcal.getId()) };

        int count = dbW.update(
                SchemaDB.KcalDB.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

        return count > 0;
    }

    public boolean deleteAllKcal() {
        SQLiteDatabase dbW = db.getWritableDatabase();
        // Rimuovi tutti i dati dalla tabella kcal
        int count = dbW.delete(SchemaDB.KcalDB.TABLE_NAME, null, null);
        db.close();
        // Se il count è maggiore di 0, significa che sono stati rimossi dei record
        return count > 0;
    }

    @SuppressLint("Range")
    public Kcal getKcalPerData(String timestamp) {
        SQLiteDatabase dbW = db.getReadableDatabase();
        Kcal kcal = null;

        Calendar calendar = Calendar.getInstance();

        String selectQuery = "SELECT * FROM " + SchemaDB.KcalDB.TABLE_NAME +
                " WHERE "+SchemaDB.KcalDB.COLUMN_calendario+" = ?";
        String[] selectionArgs = {String.valueOf(timestamp)};

        Cursor cursor = dbW.rawQuery(selectQuery, selectionArgs);

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
            String striData = cursor.getString(cursor.getColumnIndex(SchemaDB.KcalDB.COLUMN_calendario));
            Calendar data=Global.ConversioneStringCalendar(striData);
            kcal.setData(data);

            cursor.close();
        }
        dbW.close();
        return kcal;
    }
}
