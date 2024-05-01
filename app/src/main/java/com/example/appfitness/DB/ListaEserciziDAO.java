package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListaEserciziDAO {
    Context ct;
    DbHelper db;

    public ListaEserciziDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    //ritorna un arrayList di id di esercizi per quella scheda
    @SuppressLint("Range")
    public ArrayList<Esercizio> getListaEserciziPerGiorno(Long idGiorno) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        ArrayList<Esercizio> result = new ArrayList<>();

        String[] selectionArgs = {String.valueOf(idGiorno)};
        Cursor cursor = dbRead.query(
                SchemaDB.ListaEserciziDB.TABLE_NAME, // Nome della tua tabella
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.ListaEserciziDB.IDGiorno +" = ?", // Clausola WHERE
                selectionArgs, // Valori per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        while (cursor.moveToNext()){
            Esercizio extemp=new Esercizio();
            extemp.setId(cursor.getInt(cursor.getColumnIndex(SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi)));
            extemp.setCompletato(cursor.getInt(cursor.getColumnIndex(SchemaDB.ListaEserciziDB.COLUMN_Stato)));
            result.add(extemp);
        }

        // Chiudi il database
        db.close();

        return result;
    }
    @SuppressLint("Range")
    public ArrayList<Integer> getIDListaEserciziPerGiorno(Long idGiorno) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        ArrayList<Integer> result = new ArrayList<>();

        String[] selectionArgs = {String.valueOf(idGiorno)};
        Cursor cursor = dbRead.query(
                SchemaDB.ListaEserciziDB.TABLE_NAME, // Nome della tua tabella
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.ListaEserciziDB.IDGiorno +" = ?", // Clausola WHERE
                selectionArgs, // Valori per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        while (cursor.moveToNext()){
            result.add(cursor.getInt(cursor.getColumnIndex(SchemaDB.ListaEserciziDB._ID)));
        }

        // Chiudi il database
        db.close();

        return result;
    }


    public void DeleteListaPerIdGiorno(Integer idGiorno) {
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.ListaEserciziDB.TABLE_NAME,
                SchemaDB.ListaEserciziDB.IDGiorno + " = ?",
                new String[]{String.valueOf(idGiorno)});
        db.close();
    }
    public void DeleteListaPerNomeEsercizi(Long idEx) {
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.ListaEserciziDB.TABLE_NAME,
                SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi + " = ?",
                new String[]{String.valueOf(idEx)});
        db.close();
    }

    public void Insert(Long idGiorno, Long idEx, int stato){
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        ContentValues valuesListaEx = new ContentValues();
        valuesListaEx.put(SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi, idEx);
        valuesListaEx.put(SchemaDB.ListaEserciziDB.IDGiorno, idGiorno);
        valuesListaEx.put(SchemaDB.ListaEserciziDB.COLUMN_Stato, stato);
        long idDBListaEx=dbWritable.insert(SchemaDB.ListaEserciziDB.TABLE_NAME,null,valuesListaEx);
    }

    public boolean updateStato(Long idGiorno,Long idEsercizio,int stato) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchemaDB.ListaEserciziDB.COLUMN_Stato, stato);


        String whereClause = SchemaDB.ListaEserciziDB.IDGiorno + " = ? AND " + SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi + " = ?";
        String[] whereArgs = {String.valueOf(idGiorno),String.valueOf(idEsercizio) };


        int rowsAffected = dbW.update(SchemaDB.ListaEserciziDB.TABLE_NAME, values, whereClause, whereArgs);

        dbW.close();

        PaginaScheda_Pag3.StampaTutto();


        return rowsAffected > 0;
    }
}
