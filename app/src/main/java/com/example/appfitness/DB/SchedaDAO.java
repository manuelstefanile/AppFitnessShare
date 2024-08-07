package com.example.appfitness.DB;

import android.annotation.SuppressLint;
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
import com.example.appfitness.Pagina3.PopupGiorno;
import com.example.appfitness.Pagina3.PopupSchede;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SchedaDAO {
    Context ct;
    DbHelper db;

    public SchedaDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    @SuppressLint("Range")
    public ArrayList<Scheda> getAllSchede() {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        ArrayList<Scheda> result = new ArrayList<>();
        Cursor cursor= null;
        cursor = dbRead.query(
                SchemaDB.SchedaDB.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id=cursor.getInt(cursor.getColumnIndex(SchemaDB.SchedaDB._ID));
                String nomeScheda=cursor.getString(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_nomeScheda));
                String noteScheda=cursor.getString(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_noteScheda));
                byte[] immagine=cursor.getBlob(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_immagineScheda));
                Scheda sch=new Scheda(nomeScheda,immagine);
                sch.setNote(noteScheda);
                sch.setId(id);
                sch.setListaGiorni(new ArrayList<>());
                result.add(sch);

            } while (cursor.moveToNext());
        }
        // Chiudi il database
        dbRead.close();

        return result;
    }

    @SuppressLint("Range")
    public Scheda getSchedaById(Long id) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Scheda result = null;
        Cursor cursor = dbRead.query(
                SchemaDB.SchedaDB.TABLE_NAME, // Nome della tua tabella Giorno
                null, // Array di colonne; null seleziona tutte le colonne
                SchemaDB.SchedaDB._ID + " = ?", // Clausola WHERE per l'ID
                new String[]{String.valueOf(id)}, // Valore per la clausola WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );
        if (cursor != null && cursor.moveToFirst()) {

                int idt=cursor.getInt(cursor.getColumnIndex(SchemaDB.SchedaDB._ID));
                String nomeScheda=cursor.getString(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_nomeScheda));
                String noteScheda=cursor.getString(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_noteScheda));
                byte[] immagine=cursor.getBlob(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_immagineScheda));
                result=new Scheda(nomeScheda,immagine);
                result.setNote(noteScheda);
                result.setId(id);
                result.setListaGiorni(new ArrayList<>());


        }
        // Chiudi il database
        dbRead.close();

        return result;
    }

    public Scheda CreaSchedaTemp(){
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        ContentValues valuesScheda = new ContentValues();
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_nomeScheda, "temp");
        long id=dbWritable.insert(SchemaDB.SchedaDB.TABLE_NAME,null,valuesScheda);
        Scheda schedaTemp=new Scheda("temp");
        schedaTemp.setListaGiorni(new ArrayList<>());
        schedaTemp.setId(id);
        return schedaTemp;
    }

    public void ModificaSchedaTemp(Scheda scheda){
        System.out.println("nnnnn"+scheda.getImg());
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        //prendo gli id delle schede appena salvate


        ContentValues valuesScheda = new ContentValues();
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_nomeScheda, scheda.getNomeScheda());
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_noteScheda, scheda.getNote());


        if(scheda.getImg()!=null)
            valuesScheda.put(SchemaDB.SchedaDB.COLUMN_immagineScheda, Global.drawableToByteArray(scheda.getImg()));

        long idDBScheda=dbWritable.update(SchemaDB.SchedaDB.TABLE_NAME,valuesScheda,
                SchemaDB.SchedaDB._ID+" = ?",
                new String[]{String.valueOf(scheda.getId())});

        dbWritable.close();

    }


    public Long InsertScheda(Scheda schedaNuova){

        System.out.println("mah 2 "+ schedaNuova.getNote());
        SQLiteDatabase dbWritable = db.getWritableDatabase();

        ContentValues valuesScheda = new ContentValues();

        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_nomeScheda, schedaNuova.getNomeScheda());
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_noteScheda, schedaNuova.getNote());
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_immagineScheda, Global.drawableToByteArray(schedaNuova.getImg()));
        Long idScheda=dbWritable.insert(SchemaDB.SchedaDB.TABLE_NAME,null,valuesScheda);

        //salva anche una lista di giorni

        ArrayList<Long> idSalvatiGiorni= PopupGiorno.idGiorniSalvati;
        for (Long id:idSalvatiGiorni) {
            //crea la query id/idSalvatiEsercizi
            ContentValues valuesListaGiorni = new ContentValues();
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUM_SCHEDARIFERIMENTO,schedaNuova.getNomeScheda());
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUMN_IDGiorno,id);
            dbWritable.insert(SchemaDB.ListaGiorniDB.TABLE_NAME,null,valuesListaGiorni);
        }

        return idScheda;
    }

    public void DeleteScheda(Scheda scheda){
        SQLiteDatabase dbWrite = db.getWritableDatabase();

        ArrayList<Integer> idGiorni=Global.listaGiornidao.getListaGiorniPerScheda(scheda.getId());
        //elimina lista giorni dove c è la scheda id di riferimento
        //qui ho l id ListaGiorni
        for(Number idLG:scheda.getListaGiorni()){
            dbWrite.delete(SchemaDB.ListaGiorniDB.TABLE_NAME,
                    SchemaDB.ListaGiorniDB._ID + " = ?",
                    new String[]{String.valueOf(idLG)});

        }

        //prendo i giorni id da lista giorni ed elmino anche i giorni
        for (Integer idG: idGiorni){
            dbWrite.delete(SchemaDB.GiornoDB.TABLE_NAME,
                    SchemaDB.GiornoDB._ID + " = ?",
                    new String[]{String.valueOf(idG)});

            dbWrite.delete(SchemaDB.GiornoDB.TABLE_NAME,
                    SchemaDB.GiornoDB._ID + " = ?",
                    new String[]{String.valueOf(idG)});

        }

        for(Integer i: idGiorni) {

            //elimino gli id esercizi..... da vedere
            /*
            ArrayList<Integer> arr = Global.ledao.getListaEserciziPerGiorno(Long.valueOf(i));
            for(Integer idex:arr){
                dbWrite.delete(SchemaDB.EsercizioDB.TABLE_NAME,
                        SchemaDB.EsercizioDB._ID + " = ?",
                        new String[]{String.valueOf(idex)});
            }

             */

            //prendo gli id esercizi in listaEsercizi associati ai giorni ed elimino
            dbWrite.delete(SchemaDB.ListaEserciziDB.TABLE_NAME,
                    SchemaDB.ListaEserciziDB.IDGiorno + " = ?",
                    new String[]{String.valueOf(i)});
        }

        dbWrite.delete(SchemaDB.SchedaDB.TABLE_NAME,
                SchemaDB.SchedaDB._ID + " = ?",
                new String[]{String.valueOf(scheda.getId())});

        db.close();
        /*

        dbWrite.delete(SchemaDB.SchedaDB.TABLE_NAME,
                SchemaDB.SchedaDB.COLUMN_nomeScheda + " = ?",
                new String[]{nome});
        db.close();

        //elimina i giorni associati alla scheda
        ListaGiorniDAO ldao=new ListaGiorniDAO(ct);
        GiornoDAO gdao=new GiornoDAO(ct);
        ArrayList<Integer> idGiorni=ldao.getListaGiorniPerScheda(nome);
        for(Integer idD:idGiorni){
            //elimina giorno
            gdao.DeleteGiornoById(idD);
        }
        ldao.DeleteListaPerScheda(nome);

         */
    }

    public void updateScheda(Scheda scheda) {
        SQLiteDatabase dbW = db.getWritableDatabase();

        ContentValues valuesScheda = new ContentValues();
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_nomeScheda, scheda.getNomeScheda());
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_noteScheda, scheda.getNote());

        // Converto l'immagine in un byte array
        byte[] byteArray = null;
        if (scheda.getImg() != null) {
            byteArray=Global.drawableToByteArray(scheda.getImg());
        }
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_immagineScheda, byteArray);

        int rowsUpdated = dbW.update(
                SchemaDB.SchedaDB.TABLE_NAME,
                valuesScheda,
                SchemaDB.SchedaDB._ID + " = ?",
                new String[]{String.valueOf(scheda.getId())});

        db.close();
    }


}
