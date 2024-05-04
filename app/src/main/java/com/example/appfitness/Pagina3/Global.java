package com.example.appfitness.Pagina3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.DB.SchedaDAO;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Global {
    public static AdapterListaScheda adapterSchede;


    public static ListaGiorniDAO listaGiornidao;
    public static SchedaDAO schedadao;
    public static GiornoDAO giornoDao;
    public static EsercizioDAO esercizioDao;
    public static ListaEserciziDAO ledao;



    public static AdapterListaScheda adapterGiorni;
    public static AdapterListaScheda adapterEsercizi;

    public static DbHelper db;

    public static boolean aperturaSchedaVisualizzazione=false;

    public static String ConversioneCalendarString(Calendar calendar){
        // Ottenere la data corrente
        Date currentDate = calendar.getTime();
        // Definire un formato per la data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Formattare la data corrente
        return sdf.format(currentDate);
    }
    public static Calendar ConversioneStringCalendar(String dateString){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try {
            // Convertire la stringa in un oggetto Date
            Date date = sdf.parse(dateString);
            calendar.setTime(date);

        } catch (  ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    public static byte[] drawableToByteArray(Drawable drawable) {
        if (drawable == null) return null;
        try{
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }catch (Exception e){
            return null;
        }


    }

    public static Drawable byteArrayToDrawable(byte[] byteArray) {
        if (byteArray == null) return null;

        return new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }

}
