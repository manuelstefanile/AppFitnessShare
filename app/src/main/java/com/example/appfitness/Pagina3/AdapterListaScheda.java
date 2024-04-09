package com.example.appfitness.Pagina3;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.ListeClasseMarker;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.DB.EsercizioDAO;
import com.example.appfitness.DB.GiornoDAO;
import com.example.appfitness.DB.ListaGiorniDAO;
import com.example.appfitness.DB.SchedaDAO;
import com.example.appfitness.Eccezioni.Eccezioni;
import com.example.appfitness.NotificheDialog;
import com.example.appfitness.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdapterListaScheda<T extends ListeClasseMarker> extends ArrayAdapter<T> {

    private LayoutInflater inflater;
    private List<T> itemList; // Riferimento alla lista

    public AdapterListaScheda(@NonNull Context context, int risorsaId, List<T> c) {
        super(context,risorsaId,c);
        inflater=LayoutInflater.from(context);
        itemList=c;

    }
    @Override
    public int getCount() {
        return itemList.size();
    }

    public List<T> getLista(){
            return itemList;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        String nomeIdParent = getContext().getResources().getResourceEntryName(parent.getId());

        switch (nomeIdParent) {
            case "listaSchedeView":
                if (v == null) {
                    v = inflater.inflate(R.layout.item_scheda, null);


                }
                break;
            case "listaGiorniView":
                if (v == null) {
                    v = inflater.inflate(R.layout.item_giorni, null);


                }
                break;
            case "listaEserciziView":
                if (v == null) {
                    v = inflater.inflate(R.layout.item_esercizi, null);

                }
                break;

        }

        T item=getItem(position);
        if(item==null)return v;
        if(item.getClass()==Scheda.class){
            if(v==null)
                v = inflater.inflate(R.layout.item_scheda, null);
            //v = inflater.inflate(resource, null);
            Scheda c = (Scheda) getItem(position);
            ImageView im= v.findViewById(R.id.immagineScheda);
            Button bottone=v.findViewById(R.id.visualizzaScheda);
            Bitmap bitmap =null;
            if(c.getImg()!=null)
                 bitmap=((BitmapDrawable) c.getImg()).getBitmap();

            if (bitmap==null){
                im.setImageResource(R.drawable.noimg);
            }else
                im.setImageDrawable(c.getImg());
            bottone.setText(c.getNomeScheda());

            im.setTag(position);
            bottone.setTag(position);

        }else if(item.getClass()== Giorno.class){
            if(v==null)
                v = inflater.inflate(R.layout.item_giorni, null);
            //v = inflater.inflate(resource, null);

            Giorno c = (Giorno) getItem(position);

            Button bottone=v.findViewById(R.id.visualizzaGiorni);


            bottone.setText(c.getNomeGiorno());

            bottone.setTag(position);
            bottone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupGiorno.ApriGiornoSelezionato(c,inflater);
                }
            });



        } else if(item.getClass()== Esercizio.class){
            if(v==null)
                v = inflater.inflate(R.layout.item_esercizi, null);


            Esercizio c = (Esercizio) getItem(position);

            Button bottone=v.findViewById(R.id.visualizzaEsercizi);

            bottone.setText(c.getNomeEsercizio());
            bottone.setTag(position);

            bottone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupEsercizio.ApriEsercizioSelezionato(c,inflater);
                }
            });

        }

        return v;
    }

}
