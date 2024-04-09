package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Pagina3.PopupGiorno;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListaGiorniDAO {
    Context ct;
    DbHelper db;

    public ListaGiorniDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    //ritorna un arrayList di id di Giorni per quella scheda
    @SuppressLint("Range")
    public ArrayList<Integer> getListaGiorniPerScheda(Long idschedaDiRiferimento) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        ArrayList<Integer> result = new ArrayList<>();

        Cursor cursor = dbRead.query(
                "ListaGiorni", // Nome della tua tabella
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.ListaGiorniDB.COLUM_SCHEDARIFERIMENTO +" = ?", // Clausola WHERE
                new String[]{String.valueOf(idschedaDiRiferimento)}, // Valori per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        while (cursor.moveToNext()){
            result.add(cursor.getInt(cursor.getColumnIndex("IDGiorno")));
        }

        // Chiudi il database
        db.close();

        return result;
    }

    public void InserisciListaGiorni(Scheda schedaRiferimento){
        //scheda e id giorno int
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        ArrayList<Long> idSalvatiGiorni= PopupGiorno.idGiorniSalvati;
        for (Long id:idSalvatiGiorni) {
            //crea la query id/idSalvatiEsercizi
            ContentValues valuesListaGiorni = new ContentValues();
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUM_SCHEDARIFERIMENTO,schedaRiferimento.getId());
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUMN_IDGiorno,id);
            dbWritable.insert(SchemaDB.ListaGiorniDB.TABLE_NAME,null,valuesListaGiorni);
        }
    }

    public void DeleteListaPerIdGiorno(Integer idGiorno) {
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.ListaGiorniDB.TABLE_NAME,
                SchemaDB.ListaGiorniDB.COLUMN_IDGiorno + " = ?",
                new String[]{String.valueOf(idGiorno)});
        db.close();
    }
    public void DeleteListaPerScheda(String schedaName) {
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.ListaGiorniDB.TABLE_NAME,
                SchemaDB.ListaGiorniDB.COLUM_SCHEDARIFERIMENTO + " = ?",
                new String[]{String.valueOf(schedaName)});
        db.close();
    }
    public void Insert(Long idScheda, ArrayList<Long> idGiorni){
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        ContentValues valuesListaGiorni = new ContentValues();

        System.out.println("+++"+idGiorni);

        for(Long idG:idGiorni){
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUM_SCHEDARIFERIMENTO, idScheda);
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUMN_IDGiorno, idG);
            dbWritable.insert(SchemaDB.ListaGiorniDB.TABLE_NAME,null,valuesListaGiorni);
        }



    }

}
