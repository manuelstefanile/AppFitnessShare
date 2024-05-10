package com.example.appfitness.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appfitness.Bean.Scheda;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    private static final String DATABASE_NAME = "app_fitness.db";

    final private static String CREATE_CMD =
            "CREATE TABLE " + SchemaDB.UtenteDB.TABLE_NAME + " ("
                    + SchemaDB.UtenteDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.UtenteDB.COLUMN_nome + " TEXT NOT NULL, "
                    + SchemaDB.UtenteDB.COLUMN_cognome + " TEXT NOT NULL, "
                    + SchemaDB.UtenteDB.COLUMN_nomeUtente + " TEXT, "
                    + SchemaDB.UtenteDB.COLUMN_eta + " INTEGER, "
                    + SchemaDB.UtenteDB.COLUMN_altezza + " FLOAT, "
                    + SchemaDB.UtenteDB.COLUMN_IdPeso + " INTEGER, "
                    + SchemaDB.UtenteDB.COLUMN_IdKcal + " INTEGER, "
                    + SchemaDB.UtenteDB.COLUMN_IdMisure + " INTEGER, "
                    + SchemaDB.UtenteDB.COLUMN_immagine + " BLOB,"
                    + SchemaDB.UtenteDB.COLUMN_note + " TEXT"
                    + "); ";

    final private static String CREATE_PESO =
            "CREATE TABLE " + SchemaDB.PesoDB.TABLE_NAME + " ("
                    + SchemaDB.PesoDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.PesoDB.COLUMN_pesoKg + " FLOAT, "
                    + SchemaDB.PesoDB.COLUMN_calendario + " TEXT NOT NULL,"
                    + SchemaDB.PesoDB.COLUMN_note + " TEXT"
                    + "); ";

    final private static String CREATE_KCAL =
            "CREATE TABLE " + SchemaDB.KcalDB.TABLE_NAME + " ("
                    + SchemaDB.KcalDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.KcalDB.COLUMN_kcal + " INTEGER, "
                    + SchemaDB.KcalDB.COLUMN_fase + " TEXT, "
                    + SchemaDB.KcalDB.COLUMN_carboidrati + " FLOAT, "
                    + SchemaDB.KcalDB.COLUMN_proteine + " FLOAT, "
                    + SchemaDB.KcalDB.COLUMN_grassi + " FLOAT, "
                    + SchemaDB.KcalDB.COLUMN_sale + " FLOAT, "
                    + SchemaDB.KcalDB.COLUMN_acqua + " FLOAT, "
                    + SchemaDB.KcalDB.COLUMN_note + " TEXT, "
                    + SchemaDB.KcalDB.COLUMN_calendario + " TEXT NOT NULL"
                    + "); ";

    final private static String CREATE_MISURE =
            "CREATE TABLE " + SchemaDB.MisureDB.TABLE_NAME + " ("
                    + SchemaDB.MisureDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.MisureDB.COLUMN_braccioDX + " FLOAT, "
                    + SchemaDB.MisureDB.COLUMN_braccioSX + " FLOAT, "
                    + SchemaDB.MisureDB.COLUMN_gambaDX + " FLOAT, "
                    + SchemaDB.MisureDB.COLUMN_gambaSX + " FLOAT, "
                    + SchemaDB.MisureDB.COLUMN_petto + " FLOAT, "
                    + SchemaDB.MisureDB.COLUMN_spalle + " FLOAT, "
                    + SchemaDB.MisureDB.COLUMN_addome + " FLOAT,"
                    + SchemaDB.MisureDB.COLUMN_fianchi + " FLOAT, "
                    + SchemaDB.MisureDB.COLUMN_note + " TEXT, "
                    + SchemaDB.MisureDB.COLUMN_calendario + " TEXT NOT NULL"
                    + ");";

    final private static String CREATE_ESERCIZIO =
            "CREATE TABLE " + SchemaDB.EsercizioDB.TABLE_NAME + " ("
                    + SchemaDB.EsercizioDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.EsercizioDB.COLUMN_nomeEsercizio + " TEXT NOT NULL , "
                    + SchemaDB.EsercizioDB.COLUMN_tecnica_intensita + " TEXT, "
                    + SchemaDB.EsercizioDB.COLUMN_esecuzione + " TEXT, "
                    + SchemaDB.EsercizioDB.COLUMN_immagineMacchinario + " BLOB, "
                    + SchemaDB.EsercizioDB.COLUMN_numeroSerie + " INTEGER, "
                    + SchemaDB.EsercizioDB.COLUMN_numeroRipetizioni + " TEXT, "
                    + SchemaDB.EsercizioDB.COLUMN_pesoKG + " TEXT,"
                    + SchemaDB.EsercizioDB.COLUMN_note + " TEXT, "
                    + SchemaDB.EsercizioDB.COLUMN_timer + " FLOAT"
                    + ");";

    final private static String CREATE_SCHEDA =
            "CREATE TABLE " + SchemaDB.SchedaDB.TABLE_NAME + " ("
                    + SchemaDB.SchedaDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.SchedaDB.COLUMN_nomeScheda + " TEXT NOT NULL UNIQUE, "
                    + SchemaDB.SchedaDB.COLUMN_noteScheda + " TEXT, "
                    + SchemaDB.SchedaDB.COLUMN_immagineScheda + " BLOB "
                    + ");";

    final private static String CREATE_LISTAGIORNI =
            "CREATE TABLE " + SchemaDB.ListaGiorniDB.TABLE_NAME + " ("
                    + SchemaDB.ListaGiorniDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.ListaGiorniDB.COLUM_SCHEDARIFERIMENTO + " INTEGER , "
                    + SchemaDB.ListaGiorniDB.COLUMN_IDGiorno + " INTEGER REFERENCES "
                    + SchemaDB.GiornoDB.TABLE_NAME + "(" + SchemaDB.GiornoDB._ID + ") ON DELETE CASCADE"
                    + ");";

    final private static String CREATE_GIORNO =
            "CREATE TABLE " + SchemaDB.GiornoDB.TABLE_NAME + " ("
                    + SchemaDB.GiornoDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.GiornoDB.COLUMN_noteGiorno + " TEXT, "
                    + SchemaDB.GiornoDB.COLUMN_nomeGiorno + " TEXT NOT NULL"
                    + ");";


    final private static String CREATE_LISTAESERCIZI =
            "CREATE TABLE " + SchemaDB.ListaEserciziDB.TABLE_NAME + " ("
                    + SchemaDB.ListaEserciziDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.ListaEserciziDB.COLUMN_Stato + " INTEGER DEFAULT 0 , "
                    + SchemaDB.ListaEserciziDB.COLUMN_Ordine + " INTEGER, "
                    + SchemaDB.ListaEserciziDB.IDGiorno + " INTEGER REFERENCES "
                    + SchemaDB.GiornoDB.TABLE_NAME + "(" + SchemaDB.GiornoDB._ID + ") ON DELETE CASCADE, "
                    + SchemaDB.ListaEserciziDB.COLUMN_IDEsercizi + " INTEGER "
                    + ");";

    final private static String CREATE_FISICO =
            "CREATE TABLE " + SchemaDB.FisicoDB.TABLE_NAME + " ("
                    + SchemaDB.FisicoDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.FisicoDB.COLUMN_note + " TEXT, "
                    + SchemaDB.FisicoDB.COLUMN_calendario + " TEXT NOT NULL"
                    + ");";

    final private static String CREATE_LISTAIMGFISICO =
            "CREATE TABLE " + SchemaDB.ListaImmaginiFisicoDB.TABLE_NAME + " ("
                    + SchemaDB.ListaImmaginiFisicoDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.ListaImmaginiFisicoDB.COLUMN_IDFisico + " INTEGER, "
                    + SchemaDB.ListaImmaginiFisicoDB.COLUMN_Immagine + " BLOB, "
                    + SchemaDB.ListaImmaginiFisicoDB.COLUMN_NomePosa + " TEXT ,"
                    + SchemaDB.ListaImmaginiFisicoDB.COLUMN_PosizionePosa + " INTEGER "
                    + ");";


    final private static Integer VERSION = 1;
    final private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        System.out.println("DEBUG DatatabaseOpenHelper - constructor");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
        db.execSQL(CREATE_SCHEDA);
        db.execSQL(CREATE_PESO);
        db.execSQL(CREATE_MISURE);
        db.execSQL(CREATE_KCAL);
        db.execSQL(CREATE_LISTAGIORNI);
        db.execSQL(CREATE_LISTAESERCIZI);
        db.execSQL(CREATE_ESERCIZIO);
        db.execSQL(CREATE_GIORNO);
        db.execSQL(CREATE_FISICO);
        db.execSQL(CREATE_LISTAIMGFISICO);

        System.out.println("DEBUG DatatabaseOpenHelper - onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // non serve in questo esempio, ma deve esserci
    }

    //Questo metodo serve per cancellare il database
    //Non viene usato in questo esempio


    public void deleteDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.UtenteDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.PesoDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.KcalDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.MisureDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.SchedaDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.ListaGiorniDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.GiornoDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.ListaEserciziDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.EsercizioDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.FisicoDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SchemaDB.ListaImmaginiFisicoDB.TABLE_NAME);


        onCreate(db); // Ricrea le tabelle vuote
    }

    public Cursor getAllData(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        // Ottieni il nome delle colonne
        String[] columnNames = cursor.getColumnNames();

        // Stampa il nome della tabella
        System.out.println("Table: " + tableName);

        // Stampa i nomi delle colonne
        for (String columnName : columnNames) {
            System.out.print(columnName + " | ");
        }
        System.out.println(); // Nuova linea

        return cursor;
    }

}
