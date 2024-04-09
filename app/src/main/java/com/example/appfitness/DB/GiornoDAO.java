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


    public Giorno InsertGiorno(Giorno giorno){
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        /*
        //prendo gli id delle schede appena salvate
        ArrayList<Long> idSalvatiEx= PopupEsercizio.idEserciziSalvati;
        ArrayList<Long> idSalvatiListaEx= new ArrayList<>();
        //inserisco il giorno nel db

         */
        ContentValues valuesGiorno = new ContentValues();
        valuesGiorno.put(SchemaDB.GiornoDB.COLUMN_nomeGiorno, giorno.getNomeGiorno());
        long idDBGiorno=dbWritable.insert(SchemaDB.GiornoDB.TABLE_NAME,null,valuesGiorno);
        giorno.setId(idDBGiorno);
        //idSalvatiListaEx.add(idDBGiorno);

        /*
        //inserisco le liste associate
        for (Long id:idSalvatiEx) {
            System.out.println("_____ "+id);
            //crea la query id/idSalvatiEsercizi
            ContentValues valuesListaEx = new ContentValues();
            valuesListaEx.put(SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi,id);
            valuesListaEx.put(SchemaDB.ListaEserciziDB.IDGiorno,idDBGiorno);
            System.out.println(valuesListaEx.toString());
            long idW=dbWritable.insert(SchemaDB.ListaEserciziDB.TABLE_NAME,null,valuesListaEx);
            idSalvatiListaEx.add(idW);
        }
         */

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

            // Chiudi il cursore
            cursor.close();
        }
        // Chiudi il database
        dbRead.close();

        return result;
    }
    public ArrayList<Integer> getIDGiornoByName(String nomeGiorno) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        ArrayList<Integer> idTrovati=new ArrayList<>();

        Cursor cursor = dbRead.query(
                SchemaDB.GiornoDB.TABLE_NAME, // Nome della tua tabella Giorno
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.GiornoDB.COLUMN_nomeGiorno + " = ?", // Clausola WHERE per l'ID
                new String[]{nomeGiorno}, // Valore per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        // Assicurati che il cursore non sia nullo
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Aggiungi l'ID trovato alla lista
                idTrovati.add(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.GiornoDB._ID)));
            } while (cursor.moveToNext());

            // Chiudi il cursore qui, dopo aver finito di utilizzarlo
            cursor.close();
        }
        // Chiudi il database
        dbRead.close();

        return idTrovati;
    }
    /*
    public Integer getGiornoIDByNameAndScheda(String nomeGiorno, String nomeScheda) {
        System.out.println("*** "+ nomeGiorno +"  " + nomeScheda);
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Giorno result = null;
        Integer idRisultanti=null;

        //prendo il giorno con l'id in base al nome
        ArrayList<Integer> idTrovatiGiorno=getIDGiornoByName(nomeGiorno);
        System.out.println("***" + idTrovatiGiorno);

        //verifica se l id è presente in listaGiorni dove c è scheda di riferimento
        ListaGiorniDAO lgda=new ListaGiorniDAO(ct);
        ArrayList<Integer> idTrovatiListaGiorni=lgda.getListaGiorniPerScheda(nomeScheda);


        //se combacia, allora il giorno preso è quello giusto
        for(Integer idGiorno:idTrovatiGiorno){
            for(Integer idLista:idTrovatiListaGiorni){
                if(idGiorno==idLista) {
                    idRisultanti=idGiorno;
                }
            }
        }
        // Chiudi il database
        dbRead.close();

        return idRisultanti;
    }

     */

    /*
    public void AggiornaGiorno(String giornoName, String schedaName){

        Integer idGiorno=getGiornoIDByNameAndScheda(giornoName,schedaName);
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        //prendo gli id delle schede appena salvate
        ArrayList<Long> idSalvatiEx= PopupEsercizio.idEserciziSalvati;
        ArrayList<Long> idSalvatiListaEx= new ArrayList<>();
        for (Long id:idSalvatiEx) {
            System.out.println("_____ "+id);
            //crea la query id/idSalvatiEsercizi
            ContentValues valuesListaEx = new ContentValues();
            valuesListaEx.put(SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi,id);
            valuesListaEx.put(SchemaDB.ListaEserciziDB.IDGiorno,idGiorno);

            long idW=dbWritable.insert(SchemaDB.ListaEserciziDB.TABLE_NAME,null,valuesListaEx);
            idSalvatiListaEx.add(idW);
        }


        ContentValues valuesGiorno = new ContentValues();
        valuesGiorno.put(SchemaDB.GiornoDB.COLUMN_nomeGiorno, giornoName);
        long idDBGiorno=dbWritable.update(SchemaDB.GiornoDB.TABLE_NAME,valuesGiorno,
                SchemaDB.GiornoDB.COLUMN_nomeGiorno+" = ?",
                new String[]{giornoName});
        idSalvatiListaEx.add(idDBGiorno);

        dbWritable.close();

    }
    /
     */

    /*
    public void DeleteGiornoById(Long id){
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        dbWrite.delete(SchemaDB.GiornoDB.TABLE_NAME,
                SchemaDB.GiornoDB._ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();

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

    }

     */

}
