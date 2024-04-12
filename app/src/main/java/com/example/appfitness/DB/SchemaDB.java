package com.example.appfitness.DB;

import android.provider.BaseColumns;

public class SchemaDB {// To prevent someone from accidentally instantiating the
        // schema class, give it an empty constructor.
        public SchemaDB() {
        }

/* Inner class that defines the table contents */
public static abstract class UtenteDB implements BaseColumns {
    public static final String TABLE_NAME = "Utente";
    public static final String COLUMN_nome = "nome";
    public static final String COLUMN_cognome = "cognome";
    public static final String COLUMN_nomeUtente = "nomeUtente";
    public static final String COLUMN_eta = "eta";
    public static final String COLUMN_altezza = "altezza";

    public static final String COLUMN_IdPeso = "peso";
    public static final String COLUMN_IdKcal = "kcal";
    public static final String COLUMN_IdMisure = "misure";
    public static final String COLUMN_note = "note";
}
    public static abstract class PesoDB implements BaseColumns {
        public static final String TABLE_NAME = "Peso";
        public static final String COLUMN_pesoKg = "pesoKg";
        public static final String COLUMN_calendario = "calendario";

    }

    public static abstract class KcalDB implements BaseColumns {
        public static final String TABLE_NAME = "Kcal";
        public static final String COLUMN_kcal = "kcal";
        public static final String COLUMN_fase = "fase";
        public static final String COLUMN_carboidrati = "carbo";
        public static final String COLUMN_proteine = "proteine";
        public static final String COLUMN_grassi = "grassi";
        public static final String COLUMN_sale = "sale";
        public static final String COLUMN_acqua = "acqua";
        public static final String COLUMN_note = "note";
        public static final String COLUMN_calendario = "calendario";

    }
    public static abstract class MisureDB implements BaseColumns {
        public static final String TABLE_NAME = "Misure";
        public static final String COLUMN_braccioDX = "braccioDX";
        public static final String COLUMN_braccioSX = "braccioSX";
        public static final String COLUMN_gambaDX = "gambaDX";
        public static final String COLUMN_gambaSX = "gambaSX";
        public static final String COLUMN_petto = "petto";
        public static final String COLUMN_spalle = "spalle";
        public static final String COLUMN_addome = "addome";


    }



    public static abstract class SchedaDB implements BaseColumns {
        public static final String TABLE_NAME = "Scheda";
        public static final String COLUMN_nomeScheda = "nomeScheda";
        public static final String COLUMN_noteScheda = "noteScheda";
        public static final String COLUMN_immagineScheda = "imagineScheda";
    }
    public static abstract class ListaGiorniDB implements BaseColumns {
        public static final String TABLE_NAME = "ListaGiorni";
        public static final String COLUM_SCHEDARIFERIMENTO = "SchedaRiferimento";
        public static final String COLUMN_IDGiorno = "IDGiorno";
    }

    public static abstract class GiornoDB implements BaseColumns {
        public static final String TABLE_NAME = "Giorno";
        public static final String COLUMN_nomeGiorno = "nomeGiorno";
        public static final String COLUMN_noteGiorno = "noteGiorno";


    }
    public static abstract class ListaEserciziDB implements BaseColumns {
        public static final String TABLE_NAME = "ListaEsercizi";
        public static final String IDGiorno = "GiornoRiferimento";
        public static final String COLUMN_IDEsercizi = "IDEsercizi";
    }
    public static abstract class EsercizioDB implements BaseColumns {
        public static final String TABLE_NAME = "Esercizio";
        public static final String COLUMN_nomeEsercizio = "nomeEsercizio";
        public static final String COLUMN_tecnica_intensita = "tecnica_intensita";
        public static final String COLUMN_esecuzione = "esecuzione";
        public static final String COLUMN_immagineMacchinario = "immagineMacchinario";
        public static final String COLUMN_numeroSerie = "numeroSerie";
        public static final String COLUMN_numeroRipetizioni = "numeroRipetizioni";
        public static final String COLUMN_note = "note";
        public static final String COLUMN_timer = "timer";
    }





}
