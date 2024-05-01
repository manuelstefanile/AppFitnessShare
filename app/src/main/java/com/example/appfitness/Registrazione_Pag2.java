package com.example.appfitness;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfitness.Bean.COSTANTI;
import com.example.appfitness.Bean.Kcal;
import com.example.appfitness.Bean.Misure;
import com.example.appfitness.Bean.Note;
import com.example.appfitness.Bean.Peso;
import com.example.appfitness.Bean.Utente;
import com.example.appfitness.DB.DbHelper;
import com.example.appfitness.DB.MisureDAO;
import com.example.appfitness.DB.PesoDAO;
import com.example.appfitness.DB.SchemaDB;
import com.example.appfitness.DB.UtenteDAO;
import com.example.appfitness.DB.kcalDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.Pagina3.Global;
import com.example.appfitness.Pagina3.PaginaScheda_Pag3;
import com.example.appfitness.Pagina3.PopupEsercizio;
import com.example.appfitness.Pagina3.PopupSchede;


import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class Registrazione_Pag2 extends Activity {


    Note noteSalvate;
    EditText nomeR,cognomeR,nomeUtenteR,etaR,altezzaR;
    Utente utente;
    SharedPreferences sharedPreferences;
    DbHelper db;
    Button bottoneNext,bottoneSalva,bottoneripristinaDati;
    ImageView immagineUtente;
    public static boolean editGlobal=true;

    public static PesoDAO pesodao;
    public static MisureDAO misuradao;
    public static UtenteDAO utentedao;
    public static com.example.appfitness.DB.kcalDAO kcalDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);

        pesodao=new PesoDAO(getApplicationContext());
        misuradao=new MisureDAO(getApplicationContext());
        kcalDAO=new kcalDAO(getApplicationContext());
        utentedao=new UtenteDAO(getApplicationContext());

        Button bottonePeso=findViewById((int)R.id.buttonPeso);
        Button bottoneKcal=findViewById((int)R.id.buttonKcal);
        Button bottoneMisure=findViewById((int)R.id.buttonMisure);
        Button bottoneNote=findViewById((int)R.id.buttonNote);
        bottoneripristinaDati=findViewById((int)R.id.ripristinaDatiButton);
        bottoneripristinaDati.setVisibility(View.INVISIBLE);
        bottoneSalva=findViewById((int)R.id.salvaButtonReg);
        bottoneNext=findViewById((int)R.id.bottoneNextRegistrazione);
        bottoneNext.setVisibility(View.INVISIBLE);
        immagineUtente=findViewById(R.id.immagineProfilo);
        //Toast.makeText(getApplicationContext(), "Devi prima salvare i dati per poter proseguire!", Toast.LENGTH_LONG).show();

        //apreo il db
        db=new DbHelper(getApplicationContext());
        Global.db=db;

        nomeR=findViewById((int)R.id.nomeRegistrazione);
        cognomeR=findViewById((int)R.id.cognomeRegistrazione);
        nomeUtenteR=findViewById((int)R.id.nomeUtenteRegistrazione);
        etaR=findViewById((int)R.id.etaRegistrazione);
        altezzaR=findViewById((int)R.id.altezzaRegistrazione);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();



        noteSalvate= new Note();


        editor.putString(COSTANTI.NOTE_REGISTRAZIONE, noteSalvate.toJson());
        editor.apply();

        bottoneNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassaPagina3(view);
            }
        });
        bottoneripristinaDati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RipristinaDati(view);
            }
        });
        bottoneSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvaAll(view);
            }
        });

        String modalita=getIntent().getStringExtra("mode");
        if (modalita!=null)
        {
            switch (modalita) {
                case "edit":
                    editGlobal=true;
                    bottoneNext.setEnabled(true);
                    bottoneNext.setVisibility(View.GONE);
                    bottoneripristinaDati.setText("Back");
                    bottoneripristinaDati.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PassaPagina3(view);
                        }
                    });
                    bottoneSalva.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //aggiorna
                            AggiornaDati();
                        }
                    });

                    immagineUtente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                    selectImageFromGallery();

                        }
                    });

                    ModalitaEdit(true);
                    break;
                case "see":
                    bottonePeso.setText("Peso");
                    bottoneNote.setText("Note");
                    bottoneKcal.setText("Kcal");
                    bottoneMisure.setText("Misure");
                    bottoneNext.setEnabled(true);
                    bottoneNext.setVisibility(View.GONE);
                    bottoneSalva.setVisibility(View.GONE);
                    bottoneripristinaDati.setText("Back");
                    bottoneripristinaDati.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PassaPagina3(view);
                        }
                    });

                    immagineUtente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NotificheDialog.NotificaImmagine(getLayoutInflater(),immagineUtente.getDrawable());
                        }
                    });


                    editGlobal=false;
                    ModalitaEdit(false);
                    break;
            }
            /**registrazione per la prima volta*/
        }else{
            immagineUtente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImageFromGallery();

                }
            });
        }


    }
    public void AggiornaDati(){
        int eta= Integer.parseInt(etaR.getText().toString().trim().length()!=0?etaR.getText().toString():"0");
        float altezza=Float.parseFloat(altezzaR.getText().toString().trim().length()!=0?altezzaR.getText().toString():"0");
        //controlli e prendo i valori associati all edittext
        String nome=nomeR.getText().toString();
        String cognome=cognomeR.getText().toString();
        String nomeUtente=nomeUtenteR.getText().toString();
        byte[] immagineByte =Global.drawableToByteArray(immagineUtente.getDrawable());

        /**
        if(eta<=0){
            Toast.makeText(getApplicationContext(), "Inserire un numero >0 in età", Toast.LENGTH_SHORT).show();
            return;
        }
        if(altezza<=0){
            Toast.makeText(getApplicationContext(), "Inserire un numero >0 in altezza", Toast.LENGTH_SHORT).show();
            return;
        }*/

        noteSalvate = Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_REGISTRAZIONE, null));

        long idUtente=utente.getId();

        /************************/
        Peso p=new Peso();
        ArrayList<Peso> arr=pesodao.getPesoInfo();
        if(arr.size()>0){
            p=arr.get(0);
        }

        /******************************/
        Misure m=new Misure();
        ArrayList<Misure> arrM=misuradao.getMisureInfo();
        if(arrM.size()>0){
            m=arrM.get(0);
        }

        /************************/
        Kcal k=new Kcal();
        ArrayList<Kcal> arrK=kcalDAO.getKcalInfo();
        if(arrK.size()>0){
            k=arrK.get(0);
        }


        utente=new Utente(nome,cognome,nomeUtente,eta,altezza,p,m,k,noteSalvate);
        utente.setId(idUtente);
        utente.setImmagine(immagineByte);

        utentedao.updateUtente(utente);

        Toast.makeText(getApplicationContext(), "Dati aggiornati con successo", Toast.LENGTH_SHORT).show();
        StampaTutto();
    }
    @SuppressLint("Range")
    public void SalvaAll(View v){
        // Controlli e prendo i valori associati all'edittext
        String nome = nomeR.getText().toString();
        String cognome = cognomeR.getText().toString();
        String nomeUtente = nomeUtenteR.getText().toString().trim();
        int eta = Integer.parseInt(etaR.getText().toString().trim().length() != 0 ? etaR.getText().toString() : "0");
        float altezza = Float.parseFloat(altezzaR.getText().toString().trim().length() != 0 ? altezzaR.getText().toString() : "0");
        byte[] immagineByte =Global.drawableToByteArray(immagineUtente.getDrawable());

        /**
        if(eta<=0){
            Toast.makeText(getApplicationContext(), "Inserire un numero >0 in età", Toast.LENGTH_SHORT).show();
            return;
        }
        if(altezza<=0){
            Toast.makeText(getApplicationContext(), "Inserire un numero >0 in altezza", Toast.LENGTH_SHORT).show();
            return;
        }*/

// Elimina il salvataggio precedente se presente.
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        dbWritable.delete(SchemaDB.MisureDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.UtenteDB.TABLE_NAME, null, null);



        //pesoSalvato = Peso.fromJson(sharedPreferences.getString("pesoPassato", null));
        noteSalvate = Note.fromJson(sharedPreferences.getString(COSTANTI.NOTE_REGISTRAZIONE, null));



        utente = new Utente(nome, cognome, nomeUtente, eta, altezza, new Peso(), new Misure(), new Kcal(), noteSalvate);
        utente.setImmagine(immagineByte);


        /************************/
        ArrayList<Peso> arr=pesodao.getPesoInfo();
        Long PesoID = null;
        if(arr.size()>0){
            PesoID=arr.get(0).getId();
        }

        /************************/
        ArrayList<Misure> arrM=misuradao.getMisureInfo();
        Long MisuraID = null;
        if(arrM.size()>0){
            MisuraID=arrM.get(0).getId();
        }

        /************************/
        ArrayList<Kcal> arrK=kcalDAO.getKcalInfo();
        Long KcalID = null;
        if(arrK.size()>0){
            KcalID=arrK.get(0).getId();
        }



        ContentValues valuesUtente = new ContentValues();
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_nome, utente.getNome());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_cognome, utente.getCognome());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_nomeUtente, utente.getNomeUtente());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_eta, utente.getEta());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_altezza, utente.getAltezza());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_IdPeso, PesoID);
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_IdKcal, KcalID);
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_IdMisure, MisuraID);
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_note, utente.getNote().getNote());
        valuesUtente.put(SchemaDB.UtenteDB.COLUMN_immagine, utente.getImmagine());
        long IdUtente = dbWritable.insert(SchemaDB.UtenteDB.TABLE_NAME, null, valuesUtente);

        Toast.makeText(getApplicationContext(), "Dati salvati. Potrai modificarli direttamente nella HomePage!", Toast.LENGTH_LONG).show();
        bottoneripristinaDati.setVisibility(View.VISIBLE);
        bottoneNext.setVisibility(View.VISIBLE);
        bottoneNext.setBackground((getDrawable((int) R.drawable.drawable_scheda)));
        dbWritable.close();
        StampaTutto();

    }
    public void RipristinaDati(View v){
        if(utente!=null)
            utente.RipristinaDatiUtente();

        noteSalvate= new Note();

        nomeR.setText("");
        cognomeR.setText("");
        nomeUtenteR.setText("");
        etaR.setText("");
        altezzaR.setText("");

        SharedPreferences.Editor editor = sharedPreferences.edit();



        editor.putString(COSTANTI.NOTE_REGISTRAZIONE, noteSalvate.toJson());
        editor.apply();

        SQLiteDatabase dbWritable = db.getWritableDatabase();
        dbWritable.delete(SchemaDB.PesoDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.KcalDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.MisureDB.TABLE_NAME, null, null);
        dbWritable.delete(SchemaDB.UtenteDB.TABLE_NAME, null, null);

        dbWritable.close();
        bottoneNext.setEnabled(false);

        Toast.makeText(getApplicationContext(), "Dati ripristinati", Toast.LENGTH_SHORT).show();


    }
    public void FunzioniInserisciRegistrazioni(View v) throws Eccezioni {
        String tag= (String) v.getTag();
        System.out.println("sono in funzione "+tag);
        switch (tag){
            case "peso":
                NotificheDialog.NotificaPeso(getLayoutInflater(),sharedPreferences);
                break;
            case "misure":
                NotificheDialog.NotificaMisure(getLayoutInflater(),sharedPreferences);
                break;
            case "kcal":
                NotificheDialog.NotificaKcal(getLayoutInflater(),sharedPreferences);
                break;
            case "note":
                NotificheDialog.NotificaNote(getLayoutInflater(),sharedPreferences, COSTANTI.NOTE_REGISTRAZIONE);
                break;
            case "fisico":
                NotificheDialog.NotificaFisico(getLayoutInflater(),sharedPreferences, COSTANTI.NOTE_FISICO,this);
                break;
            default:
                break;
        }

    }
    public void PassaPagina3(View v){
        db.close();
        Intent i =new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("nomeUtente",nomeUtenteR.getText().toString());
        i.setClass(getApplicationContext(), PaginaScheda_Pag3.class);
        startActivity(i);

    }
    private void ModalitaEdit(boolean edit){
        utente=utentedao.getUtenteInfo();
        nomeR.setText(utente.getNome());
        cognomeR.setText(utente.getCognome());
        nomeUtenteR.setText(utente.getNomeUtente());
        etaR.setText(utente.getEta()==0?"":String.valueOf(utente.getEta()));
        altezzaR.setText(utente.getAltezza()==0?"":String.valueOf(utente.getAltezza()));
        immagineUtente.setImageDrawable(Global.byteArrayToDrawable(utente.getImmagine()));

        noteSalvate=utente.getNote();


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();



        editor.putString(COSTANTI.NOTE_REGISTRAZIONE, noteSalvate.toJson());
        editor.apply();
        StampaTutto();
        //per far comparire il tasto nella pagina di Modifica dati e Visualizza dati
        bottoneripristinaDati.setVisibility(View.VISIBLE);

        TextView titolo=findViewById(R.id.titoloPaginaReg);
        //modalita see
        if(!edit){
            nomeR.setFocusable(false);
            nomeR.setClickable(false);
            cognomeR.setFocusable(false);
            cognomeR.setClickable(false);
            nomeUtenteR.setFocusable(false);
            nomeUtenteR.setClickable(false);
            etaR.setFocusable(false);
            etaR.setClickable(false);
            altezzaR.setFocusable(false);
            altezzaR.setClickable(false);

            //titolo= MODIFICA

            titolo.setText("DATI PERSONALI");
        }else
            titolo.setText("MODIFICA DATI");

    }
    public static void StampaTutto() {
        System.out.println("*********************************************************");

        printUtente(Global.db);

        printKcal(Global.db);

        printMisure(Global.db);

        printPeso(Global.db);
        printFisico(Global.db);
        printFisicoLista(Global.db);

        System.out.println("*********************************************************");

    }
    // Metodo per stampare tutte le Liste Esercizi
    public static void printUtente(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.UtenteDB.TABLE_NAME);
        System.out.println("----UTENTI----");
        PaginaScheda_Pag3.printCursor(cursor);
    }
    public static void printKcal(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.KcalDB.TABLE_NAME);
        System.out.println("----KCAL----");
        PaginaScheda_Pag3.printCursor(cursor);
    }
    public static void printMisure(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.MisureDB.TABLE_NAME);
        System.out.println("----MISURE----");
        PaginaScheda_Pag3.printCursor(cursor);
    }
    public static void printPeso(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.PesoDB.TABLE_NAME);
        System.out.println("----PESO----");
        PaginaScheda_Pag3.printCursor(cursor);
    }
    public static void printFisico(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.FisicoDB.TABLE_NAME);
        System.out.println("----FISICO----");
        PaginaScheda_Pag3.printCursor(cursor);
    }
    public static void printFisicoLista(DbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllData(SchemaDB.ListaImmaginiFisicoDB.TABLE_NAME);
        System.out.println("----ListaFisico----");
        PaginaScheda_Pag3.printCursor(cursor);
    }

    //per le immagini e mostrarle a schermo quando pronte


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //sto nelle immagini di esercizio
        System.out.println("img ig 1" );
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                System.out.println("img ig 2" );
                int immagineRif= NotificheDialog.immagineRiferimento;
                NotificheDialog.posa_immagine.add(immagineRif-1,bitmap);
                switch (immagineRif){
                    case 1:
                        NotificheDialog.immagineFisico.setImageBitmap(bitmap);
                        break;
                    case 2:
                        NotificheDialog.immagineFisico2.setImageBitmap(bitmap);
                        break;
                    case 3:
                        NotificheDialog.immagineFisico3.setImageBitmap(bitmap);
                        break;
                }



            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show();
            }

            /**mi trovo nell immagine profilo*/
        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            immagineUtente.setImageBitmap(bitmap);

        }
    }


    private  void selectImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), 2); // Modificato qui

    }
}
