package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    public ArrayList<Integer> getListaEserciziPerGiorno(Long idGiorno) {
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
            result.add(cursor.getInt(cursor.getColumnIndex("IDEsercizi")));
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

    public void Insert(Long idGiorno, Long idEx){
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        ContentValues valuesListaEx = new ContentValues();
        valuesListaEx.put(SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi, idEx);
        valuesListaEx.put(SchemaDB.ListaEserciziDB.IDGiorno, idGiorno);
        long idDBListaEx=dbWritable.insert(SchemaDB.ListaEserciziDB.TABLE_NAME,null,valuesListaEx);
    }
}
