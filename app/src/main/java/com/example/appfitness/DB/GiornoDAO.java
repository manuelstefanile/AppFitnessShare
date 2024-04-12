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
import com.example.appfitness.Pagina3.Global;
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


    public Giorno InsertGiorno(Giorno giorno){
        SQLiteDatabase dbWritable = db.getWritableDatabase();

        ContentValues valuesGiorno = new ContentValues();
        valuesGiorno.put(SchemaDB.GiornoDB.COLUMN_nomeGiorno, giorno.getNomeGiorno());
        valuesGiorno.put(SchemaDB.GiornoDB.COLUMN_noteGiorno, giorno.getNote());
        long idDBGiorno=dbWritable.insert(SchemaDB.GiornoDB.TABLE_NAME,null,valuesGiorno);
        giorno.setId(idDBGiorno);

        dbWritable.close();
        return giorno;

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
            result.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.GiornoDB._ID)));
            result.setNomeGiorno(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.GiornoDB.COLUMN_nomeGiorno)));
            result.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.GiornoDB.COLUMN_noteGiorno)));

            // Chiudi il cursore
            cursor.close();
        }
        // Chiudi il database
        dbRead.close();

        return result;
    }


    public void DeleteGiornoByGiorno(Giorno g){
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.GiornoDB.TABLE_NAME,
                SchemaDB.GiornoDB._ID + " = ?",
                new String[]{String.valueOf(g.getId())});
        db.close();

        //elimino la lista di giorni
        Global.listaGiornidao.DeleteListaPerIdGiorno(g.getId());
        //devo eliminare la lista di ex associata ai giorni
        Global.ledao.DeleteListaPerIdGiorno((int) g.getId());



        /*
        //elimina gli esercizi associati ai giorni
        //prendo lista giorni-esercizi
        ListaEserciziDAO leseDAO=new ListaEserciziDAO(ct);
        EsercizioDAO exDao=new EsercizioDAO(ct);
        ArrayList<Integer> idEsercizi=leseDAO.getListaEserciziPerGiorno(id);
        for(int idEx:idEsercizi){
            exDao.DeleteEsercizioById(idEx);
        }

        //elimino anche la lista di giorni e lista esercizi
        ListaGiorniDAO listaGiorniDAO=new ListaGiorniDAO(ct);
        listaGiorniDAO.DeleteListaPerIdGiorno(id);

        leseDAO.DeleteListaPerIdGiorno(id);
         */

    }



}
