package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Bean.Utente;

import java.util.ArrayList;
import java.util.Calendar;

public class MisureDAO {
    Context ct;
    DbHelper db;

    public MisureDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    @SuppressLint("Range")
    // Metodo per ottenere le informazioni sulle misure dalla tabella
    public ArrayList<Misure> getMisureInfo() {
        ArrayList<Misure> misureList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + SchemaDB.MisureDB.TABLE_NAME;
        SQLiteDatabase dbW = db.getWritableDatabase();
        Cursor cursor = dbW.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Misure misure = new Misure();
                misure.setId(cursor.getLong(cursor.getColumnIndex(SchemaDB.MisureDB._ID)));
                misure.setBraccioDx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_braccioDX)));
                misure.setBraccioSx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_braccioSX)));
                misure.setGambaDx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_gambaDX)));
                misure.setGambaSx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_gambaSX)));
                misure.setPetto(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_petto)));
                misure.setSpalle(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_spalle)));
                misure.setAddome(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_addome)));
                misure.setFianchi(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_fianchi)));
                misure.setNote(cursor.getString(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_note)));

                // Converti il valore del timestamp in un oggetto Calendar
                long timestamp = cursor.getLong(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_calendario));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                misure.setData(calendar);

                misureList.add(misure);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return misureList;
    }

    @SuppressLint("Range")
    public Misure getMisureById(long id) {
        SQLiteDatabase dbW = db.getReadableDatabase();
        Misure misure = null;

        Cursor cursor = dbW.query(
                SchemaDB.MisureDB.TABLE_NAME,
                null,
                SchemaDB.MisureDB._ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            misure = new Misure();
            misure.setId(cursor.getLong(cursor.getColumnIndex(SchemaDB.MisureDB._ID)));
            misure.setBraccioDx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_braccioDX)));
            misure.setBraccioSx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_braccioSX)));
            misure.setGambaDx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_gambaDX)));
            misure.setGambaSx(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_gambaSX)));
            misure.setPetto(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_petto)));
            misure.setSpalle(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_spalle)));
            misure.setFianchi(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_fianchi)));
            misure.setAddome(cursor.getFloat(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_addome)));
            misure.setNote(cursor.getString(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_note)));

            // Converti il valore del timestamp in un oggetto Calendar
            long timestamp = cursor.getLong(cursor.getColumnIndex(SchemaDB.MisureDB.COLUMN_calendario));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            misure.setData(calendar);


            cursor.close();
        }

        db.close();
        return misure;
    }

    public Misure insertMisure(Misure misure) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.MisureDB.COLUMN_braccioDX, misure.getBraccioDx());
        values.put(SchemaDB.MisureDB.COLUMN_braccioSX, misure.getBraccioSx());
        values.put(SchemaDB.MisureDB.COLUMN_gambaDX, misure.getGambaDx());
        values.put(SchemaDB.MisureDB.COLUMN_gambaSX, misure.getGambaSx());
        values.put(SchemaDB.MisureDB.COLUMN_petto, misure.getPetto());
        values.put(SchemaDB.MisureDB.COLUMN_spalle, misure.getSpalle());
        values.put(SchemaDB.MisureDB.COLUMN_addome, misure.getAddome());
        values.put(SchemaDB.MisureDB.COLUMN_fianchi, misure.getFianchi());
        values.put(SchemaDB.MisureDB.COLUMN_note, misure.getNote());
        values.put(SchemaDB.MisureDB.COLUMN_calendario, misure.getData().getTimeInMillis());

        // Esegui l'operazione di inserimento
        long newRowId = dbW.insert(SchemaDB.MisureDB.TABLE_NAME, null, values);
        misure.setId(newRowId);
        db.close();

        return misure; // Ritorna l'ID del nuovo record inserito
    }

    public boolean updateMisure(Misure misure) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.MisureDB.COLUMN_braccioDX, misure.getBraccioDx());
        values.put(SchemaDB.MisureDB.COLUMN_braccioSX, misure.getBraccioSx());
        values.put(SchemaDB.MisureDB.COLUMN_gambaDX, misure.getGambaDx());
        values.put(SchemaDB.MisureDB.COLUMN_gambaSX, misure.getGambaSx());
        values.put(SchemaDB.MisureDB.COLUMN_petto, misure.getPetto());
        values.put(SchemaDB.MisureDB.COLUMN_spalle, misure.getSpalle());
        values.put(SchemaDB.MisureDB.COLUMN_addome, misure.getAddome());
        values.put(SchemaDB.MisureDB.COLUMN_fianchi, misure.getFianchi());
        values.put(SchemaDB.MisureDB.COLUMN_note, misure.getNote());
        values.put(SchemaDB.MisureDB.COLUMN_calendario, misure.getData().getTimeInMillis());

        String selection = SchemaDB.MisureDB._ID + " = ?";
        String[] selectionArgs = { String.valueOf(misure.getId()) };

        int count = dbW.update(
                SchemaDB.MisureDB.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

        return count > 0;
    }

}
