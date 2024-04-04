package com.example.appfitness.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Pagina3.PopupEsercizio;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class GiornoDAO {
    Context ct;
    DbHelper db;

    public GiornoDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }


    public long InsertGiorno(Giorno giorno){
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        //prendo gli id delle schede appena salvate
        ArrayList<Long> idSalvatiEx= PopupEsercizio.idEserciziSalvati;
        ArrayList<Long> idSalvatiListaEx= new ArrayList<>();
        for (Long id:idSalvatiEx) {
            System.out.println("_____ "+id);
            //crea la query id/idSalvatiEsercizi
            ContentValues valuesListaEx = new ContentValues();
            valuesListaEx.put(SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi,id);
            valuesListaEx.put(SchemaDB.ListaEserciziDB.IDGiorno,giorno.getNomeGiorno());

            System.out.println(valuesListaEx.toString());
            long idW=dbWritable.insert(SchemaDB.ListaEserciziDB.TABLE_NAME,null,valuesListaEx);
            idSalvatiListaEx.add(idW);
        }
        //inserisco il giorno nel db
        ContentValues valuesGiorno = new ContentValues();
        valuesGiorno.put(SchemaDB.GiornoDB.COLUMN_nomeGiorno, giorno.getNomeGiorno());
        long idDBGiorno=dbWritable.insert(SchemaDB.GiornoDB.TABLE_NAME,null,valuesGiorno);
        idSalvatiListaEx.add(idDBGiorno);

        dbWritable.close();
        return idDBGiorno;

    }

    public Giorno getGiornoById(int id) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Giorno result = null;

        Cursor cursor = dbRead.query(
                SchemaDB.GiornoDB.TABLE_NAME, // Nome della tua tabella Giorno
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.GiornoDB._ID + " = ?", // Clausola WHERE per l'ID
                new String[]{String.valueOf(id)}, // Valore per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        if (cursor.moveToFirst()) {
            // Se la riga è stata trovata, puoi creare il tuo oggetto Giorno
            result = new Giorno();
            result.setNomeGiorno(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.GiornoDB.COLUMN_nomeGiorno)));


            // Chiudi il cursore
            cursor.close();
        }
        // Chiudi il database
        dbRead.close();

        return result;
    }

    public void AggiornaGiorno(Giorno giorno){
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        //prendo gli id delle schede appena salvate
        ArrayList<Long> idSalvatiEx= PopupEsercizio.idEserciziSalvati;
        ArrayList<Long> idSalvatiListaEx= new ArrayList<>();
        for (Long id:idSalvatiEx) {
            System.out.println("_____ "+id);
            //crea la query id/idSalvatiEsercizi
            ContentValues valuesListaEx = new ContentValues();
            valuesListaEx.put(SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi,id);
            valuesListaEx.put(SchemaDB.ListaEserciziDB.IDGiorno,giorno.getNomeGiorno());

            long idW=dbWritable.insert(SchemaDB.ListaEserciziDB.TABLE_NAME,null,valuesListaEx);
            idSalvatiListaEx.add(idW);
        }

        ContentValues valuesGiorno = new ContentValues();
        valuesGiorno.put(SchemaDB.GiornoDB.COLUMN_nomeGiorno, giorno.getNomeGiorno());
        long idDBGiorno=dbWritable.update(SchemaDB.GiornoDB.TABLE_NAME,valuesGiorno,
                SchemaDB.GiornoDB.COLUMN_nomeGiorno+" = ?",
                new String[]{giorno.getNomeGiorno()});
        idSalvatiListaEx.add(idDBGiorno);

        dbWritable.close();

    }

}
