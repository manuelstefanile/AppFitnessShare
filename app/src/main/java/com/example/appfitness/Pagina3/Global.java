package com.example.appfitness.Pagina3;

import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.DB.SchedaDAO;

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

    public static Scheda schedaNuova;

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

}
