package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appfitness.Bean.Fisico;
import com.example.appfitness.Pagina3.Global;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FisicoDAO {
    Context ct;
    DbHelper db;

    public FisicoDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }


    public Fisico inserisciFisico(Fisico fisico) {
        //inserisco l ex nel db
        SQLiteDatabase dbWritable = db.getWritableDatabase();

            ContentValues valueslista = new ContentValues();
            valueslista.put(SchemaDB.FisicoDB.COLUMN_note, fisico.getNote());
            valueslista.put(SchemaDB.FisicoDB.COLUMN_calendario, Global.ConversioneCalendarString(fisico.getCalendario()));
            long id=dbWritable.insert(SchemaDB.FisicoDB.TABLE_NAME, null, valueslista);



        dbWritable.close();
        fisico.setId(id);
        return fisico;

    }

    @SuppressLint("Range")
    public Fisico getFisicoPerData(String dataString) {
        SQLiteDatabase dbW = db.getReadableDatabase();
        // Creare un oggetto Calendar
        Calendar calendar = Calendar.getInstance();

        String selectQuery = "SELECT * FROM " + SchemaDB.FisicoDB.TABLE_NAME +
                " WHERE "+SchemaDB.FisicoDB.COLUMN_calendario+" = ?";
        String[] selectionArgs = {String.valueOf(dataString)};


        Cursor cursor = dbW.rawQuery(selectQuery, selectionArgs);

        Fisico fisico = null;
        if (cursor != null && cursor.moveToFirst()) {
            fisico = new Fisico();
            fisico.setId(cursor.getLong(cursor.getColumnIndex(SchemaDB.FisicoDB._ID)));
            fisico.setNote(cursor.getString(cursor.getColumnIndex(SchemaDB.FisicoDB.COLUMN_note)));

            // Converti il valore del timestamp in un oggetto Calendar
            String striData = cursor.getString(cursor.getColumnIndex(SchemaDB.FisicoDB.COLUMN_calendario));
            Calendar data = Global.ConversioneStringCalendar(striData);
            fisico.setCalendario(data);
            // Se hai altre colonne nel modello, aggiungile qui
            cursor.close();
        }

        dbW.close();
        return fisico;
    }

    public boolean updateFisico(Fisico fisico) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.FisicoDB.COLUMN_note, fisico.getNote());

        String selection = SchemaDB.FisicoDB._ID + " = ?";
        String[] selectionArgs = { String.valueOf(fisico.getId()) };

        int count = dbW.update(
                SchemaDB.FisicoDB.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

        return count > 0;
    }



}
