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
                byte[] immagine=cursor.getBlob(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_immagineScheda));
                Scheda sch=new Scheda(nomeScheda,immagine);
                sch.setId(id);
                sch.setListaGiorni(new ArrayList<>());
                result.add(sch);

            } while (cursor.moveToNext());
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

        SQLiteDatabase dbWritable = db.getWritableDatabase();
        //prendo gli id delle schede appena salvate


        ContentValues valuesScheda = new ContentValues();
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_nomeScheda, scheda.getNomeScheda());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(scheda.getImg()!=null) {
            Bitmap bitmap = ((BitmapDrawable) scheda.getImg()).getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_immagineScheda, stream.toByteArray());

        long idDBScheda=dbWritable.update(SchemaDB.SchedaDB.TABLE_NAME,valuesScheda,
                SchemaDB.SchedaDB._ID+" = ?",
                new String[]{String.valueOf(scheda.getId())});

        dbWritable.close();

    }


    public void InsertScheda(Scheda schedaNuova){

        SQLiteDatabase dbWritable = db.getWritableDatabase();

        ContentValues valuesScheda = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(schedaNuova.getImg()!=null) {
            Bitmap bitmap = ((BitmapDrawable) schedaNuova.getImg()).getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_nomeScheda, schedaNuova.getNomeScheda());
        valuesScheda.put(SchemaDB.SchedaDB.COLUMN_immagineScheda, stream.toByteArray());
       dbWritable.insert(SchemaDB.SchedaDB.TABLE_NAME,null,valuesScheda);


        //salva anche una lista di giorni
        ArrayList<Long> idSalvatiGiorni= PopupGiorno.idGiorniSalvati;
        for (Long id:idSalvatiGiorni) {
            //crea la query id/idSalvatiEsercizi
            ContentValues valuesListaGiorni = new ContentValues();
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUM_SCHEDARIFERIMENTO,schedaNuova.getNomeScheda());
            valuesListaGiorni.put(SchemaDB.ListaGiorniDB.COLUMN_IDGiorno,id);
            dbWritable.insert(SchemaDB.ListaGiorniDB.TABLE_NAME,null,valuesListaGiorni);
        }

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

}
