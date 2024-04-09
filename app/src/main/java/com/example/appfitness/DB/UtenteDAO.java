package com.example.appfitness.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.Bean.Utente;
import com.example.appfitness.Pagina3.Global;
import com.example.appfitness.Pagina3.PopupGiorno;
import com.example.appfitness.Registrazione_Pag2;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UtenteDAO {
    Context ct;
    DbHelper db;

    public UtenteDAO(Context ct) {
        this.ct = ct;
        db=new DbHelper(ct);
    }

    @SuppressLint("Range")
    public Utente getUtenteInfo() {
        Utente utente = new Utente();

        String selectQuery = "SELECT * FROM " + SchemaDB.UtenteDB.TABLE_NAME;
        SQLiteDatabase dbW = db.getWritableDatabase();
        Cursor cursor = dbW.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            utente.setId(cursor.getLong(cursor.getColumnIndex(SchemaDB.UtenteDB._ID)));
            utente.setNome(cursor.getString(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_nome)));
            utente.setCognome(cursor.getString(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_cognome)));
            utente.setNomeUtente(cursor.getString(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_nomeUtente)));
            utente.setEta(cursor.getInt(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_eta)));
            utente.setAltezza(cursor.getFloat(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_altezza)));

            //ricerca il peso
            int idPeso=cursor.getInt(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_IdPeso));
            Peso p=Registrazione_Pag2.pesodao.getPesoById(idPeso);

            //ricerca misura
            int idMisura=cursor.getInt(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_IdMisure));
            Misure m= Registrazione_Pag2.misuradao.getMisureById(idPeso);

            //ricerca kcal
            int idkcal=cursor.getInt(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_IdKcal));
            Kcal k= Registrazione_Pag2.kcalDAO.getKcalById(idPeso);

            utente.setPeso(p);
            utente.setMisure(m);
            utente.setKcal(k);
            Note n=new Note();
            n.setNote(cursor.getString(cursor.getColumnIndex(SchemaDB.UtenteDB.COLUMN_note)));
            utente.setNote(n);
        }

        cursor.close();
        db.close();

        return utente;
    }
}
