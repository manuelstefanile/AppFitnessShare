package com.example.appfitness.Pagina3;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.appfitness.Bean.Esercizio;
import com.example.appfitness.Bean.Giorno;
import com.example.appfitness.Bean.ListeClasseMarker;
import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.R;

import java.util.List;

public class AdapterListaScheda<T extends ListeClasseMarker> extends ArrayAdapter<T> {

    private LayoutInflater inflater;
    private SharedPreferences shp;

    public AdapterListaScheda(@NonNull Context context, int risorsaId, List<T> c, SharedPreferences sh) {
        super(context,risorsaId,c);
        inflater=LayoutInflater.from(context);
        shp=sh;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if(getItem(position).getClass()==Scheda.class){
            if (v == null) {
                v = inflater.inflate(R.layout.item_scheda, null);
                //v = inflater.inflate(resource, null);
            }
            Scheda c = (Scheda) getItem(position);
            ImageView im= v.findViewById(R.id.immagineScheda);
            Button bottone=v.findViewById(R.id.visualizzaScheda);
            Button bottoneElimina= v.findViewById(R.id.eliminaScheda);

            im.setImageDrawable(c.getImg());
            bottone.setText(c.getNomeScheda());

            im.setTag(position);
            bottone.setTag(position);
            bottoneElimina.setTag(position);

            bottone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupSchede.CreaScheda(inflater,shp,false);
                }
            });

        }else if(getItem(position).getClass()== Giorno.class){
            if (v == null) {
                v = inflater.inflate(R.layout.item_giorni, null);
                //v = inflater.inflate(resource, null);
            }
            Giorno c = (Giorno) getItem(position);

            Button bottone=v.findViewById(R.id.visualizzaGiorni);
            Button bottoneElimina= v.findViewById(R.id.eliminaGiorni);

            bottone.setText(c.getNomeGiorno());

            bottone.setTag(position);
            bottoneElimina.setTag(position);

        } else if(getItem(position).getClass()== Esercizio.class){
        if (v == null) {
            v = inflater.inflate(R.layout.item_esercizi, null);
            //v = inflater.inflate(resource, null);
        }
        Esercizio c = (Esercizio) getItem(position);

        Button bottone=v.findViewById(R.id.visualizzaEsercizi);
        Button bottoneElimina= v.findViewById(R.id.eliminaEsercizi);

        bottone.setText(c.getNomeEsercizio());

        bottone.setTag(position);
        bottoneElimina.setTag(position);

    }

        return v;
    }
}
