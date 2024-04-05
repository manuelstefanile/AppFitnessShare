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
                String nomeScheda=cursor.getString(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_nomeScheda));
                byte[] immagine=cursor.getBlob(cursor.getColumnIndex(SchemaDB.SchedaDB.COLUMN_immagineScheda));
                Scheda sch=new Scheda(nomeScheda,immagine);
                sch.setListaGiorni(new ArrayList<>());

                result.add(sch);

            } while (cursor.moveToNext());
        }
        // Chiudi il database
        dbRead.close();

        return result;
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

}