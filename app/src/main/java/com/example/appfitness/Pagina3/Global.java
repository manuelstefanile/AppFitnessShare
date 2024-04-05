package com.example.appfitness.Pagina3;

import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaEserciziDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.DB.SchedaDAO;

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
}
