package com.example.appfitness.DB;

import android.annotation.SuppressLint;
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

    //ritorna un arrayList di id di Giorni per quella scheda
    @SuppressLint("Range")
    public ArrayList<Integer> getListaEserciziPerGiorno(String giornoDiRiferimento) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        ArrayList<Integer> result = new ArrayList<>();

        Cursor cursor = dbRead.query(
                SchemaDB.ListaEserciziDB.TABLE_NAME, // Nome della tua tabella
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.ListaEserciziDB.IDGiorno +" = ?", // Clausola WHERE
                new String[]{giornoDiRiferimento}, // Valori per la clausola WHERE
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

}
