package com.example.appfitness.Pagina3;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AdapterListaScheda<T extends ListeClasseMarker> extends ArrayAdapter<T> {

    private LayoutInflater inflater;
    private List<T> itemList; // Riferimento alla lista
    //variabile per dire che voglio aprire la lista di ex da avviare
    private boolean apri;

    private ListView listView;
    private int dragStartPosition = -1;
    private int dragCurrentPosition = -1;


    public AdapterListaScheda(@NonNull Context context, int risorsaId, List<T> c, ListView listView) {
        super(context,risorsaId,c);
        inflater=LayoutInflater.from(context);
        itemList=c;
        this.listView=listView;

        String nomeList=getContext().getResources().getResourceEntryName(listView.getId());
        if(nomeList.equals("listaEserciziView")) {

            setupDragDrop();
        }


    }

    public AdapterListaScheda(@NonNull Context context, int risorsaId, List<T> c,boolean apri,ListView listView) {
        super(context,risorsaId,c);
        inflater=LayoutInflater.from(context);
        itemList=c;
        this.apri=apri;
        this.listView=listView;
        //+ per forza esercizio
        //setupDragDrop();

    }

    private void setupDragDrop() {
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dragStartPosition = listView.pointToPosition((int) event.getX(), (int) event.getY());
                        if (dragStartPosition != -1) {
                            // Imposta l'opacità dell'elemento selezionato
                            updateOpacity(dragStartPosition, 0.5f);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (dragStartPosition != -1) {
                            updateOpacity(dragStartPosition, 1.0f);
                            dragCurrentPosition = listView.pointToPosition((int) event.getX(), (int) event.getY());
                            if (dragCurrentPosition != -1) {
                                // Swap the items as the user moves them
                                Collections.swap(itemList, dragStartPosition, dragCurrentPosition);
                                notifyDataSetChanged();
                                updateOpacity(dragCurrentPosition, 0.5f);
                                dragStartPosition = dragCurrentPosition;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Ripristina l'opacità dell'elemento al rilascio
                        if (dragStartPosition != -1) {
                            /*******************************/
                            Esercizio e= (Esercizio) itemList.get(dragStartPosition);
                            for(int i=0;i<itemList.size();i++){
                                Esercizio extemp= (Esercizio) itemList.get(i);
                                Global.ledao.updateOrdine(e.idGiornoAvviaRiferimento,extemp.getId(),i);
                            }

                            /**************************/
                            updateOpacity(dragStartPosition, 1.0f);
                            dragStartPosition = -1;
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void updateOpacity(int position, float opacity) {
        View view = listView.getChildAt(position - listView.getFirstVisiblePosition());
        if (view != null) {
            view.setAlpha(opacity);
        }
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

        String nomeIdParent=null;
        if(parent!=null)nomeIdParent= getContext().getResources().getResourceEntryName(parent.getId());
        if(this.apri){
            if (v == null) {
                v = inflater.inflate(R.layout.item_esercizi_avvia, null);
            }
            Esercizio c = (Esercizio) getItem(position);
            //nonno devi aggiungere la parte per poter visualizzare le immagini
            //ImageView im= v.findViewById(R.id.immagineScheda);
            Button bottone = v.findViewById(R.id.visualizzaEsercizi);
            Button bottoneDelete = v.findViewById(R.id.deleteEsercizi);
            bottoneDelete.setVisibility(View.GONE);
            ImageView im = v.findViewById(R.id.immagineEsercizi);
            Bitmap bitmap = null;

            if (c.getImmagineMacchinario() != null)
                bitmap = ((BitmapDrawable) c.getImmagineMacchinario()).getBitmap();

            if (bitmap == null) {
                im.setImageResource(R.drawable.noimg);
            } else
                im.setImageDrawable(c.getImmagineMacchinario());

            bottone.setText(c.getNomeEsercizio());
            bottone.setTag(position);
            bottoneDelete.setTag(position);
            im.setTag(position);

            if(c.getCompletato()) bottone.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.drawable_botton_green));


            bottone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupEsercizio.AvviaEsercizioSelezionato(c, inflater);
                }
            });

            return v;

        }else {
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

            T item = getItem(position);
            if (item == null) return v;
            if (item.getClass() == Scheda.class) {
                if (v == null)
                    v = inflater.inflate(R.layout.item_scheda, null);
                //v = inflater.inflate(resource, null);
                Scheda c = (Scheda) getItem(position);
                ImageView im = v.findViewById(R.id.immagineScheda);
                Button bottone = v.findViewById(R.id.visualizzaScheda);
                Button bottoneElimina = v.findViewById(R.id.eliminaScheda);
                Bitmap bitmap = null;
                if (c.getImg() != null)
                    bitmap = ((BitmapDrawable) c.getImg()).getBitmap();

                if (bitmap == null) {
                    im.setImageResource(R.drawable.noimg);
                } else
                    im.setImageDrawable(c.getImg());
                bottone.setText(c.getNomeScheda());

                im.setTag(position);
                bottone.setTag(position);
                bottoneElimina.setTag(position);

            } else if (item.getClass() == Giorno.class) {
                if (v == null)
                    v = inflater.inflate(R.layout.item_giorni, null);
                //v = inflater.inflate(resource, null);

                Giorno c = (Giorno) getItem(position);

                Button bottone = v.findViewById(R.id.visualizzaGiorni);
                Button bottoneElimina = v.findViewById(R.id.eliminaGiorno);
                Button bottoneAvviaGiorno = v.findViewById(R.id.startAllenamento);


                bottone.setText(c.getNomeGiorno());

                bottone.setTag(position);
                bottoneAvviaGiorno.setTag(position);
                bottoneElimina.setTag(position);
                bottone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupGiorno.ApriGiornoSelezionato(c, inflater);
                    }
                });


            } else if (item.getClass() == Esercizio.class) {
                if (v == null)
                    v = inflater.inflate(R.layout.item_esercizi, null);


                Esercizio c = (Esercizio) getItem(position);
                //nonno devi aggiungere la parte per poter visualizzare le immagini
                //ImageView im= v.findViewById(R.id.immagineScheda);
                Button bottone = v.findViewById(R.id.visualizzaEsercizi);
                Button bottoneDelete = v.findViewById(R.id.deleteEsercizi);
                ImageView im = v.findViewById(R.id.immagineEsercizi);
                Bitmap bitmap = null;

                if (c.getImmagineMacchinario() != null)
                    bitmap = ((BitmapDrawable) c.getImmagineMacchinario()).getBitmap();

                if (bitmap == null) {
                    im.setImageResource(R.drawable.noimg);
                } else
                    im.setImageDrawable(c.getImmagineMacchinario());

                bottone.setText(c.getNomeEsercizio());
                bottone.setTag(position);
                bottoneDelete.setTag(position);
                im.setTag(position);


                bottone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupEsercizio.ApriEsercizioSelezionato(c, inflater);
                    }
                });

            }
        }

        return v;
    }


    public void UpdateEsercizio(@Nullable T object) {
        if (object instanceof Esercizio) {
            Iterator<T> iterator = itemList.iterator();
            while (iterator.hasNext()) {
                T e = iterator.next();
                if (((Esercizio)object).getId() == ((Esercizio)e).getId()) {
                    iterator.remove();
                    itemList.add(object);
                    notifyDataSetChanged(); // Aggiorna la vista dopo la rimozione
                    break; // Esci dal ciclo dopo aver aggiornato l'elemento
                }
            }
        }
    }


    public void AggiornaEsercizioCompletato(int pos,Esercizio exnew){
        itemList.remove(pos);
        exnew.setCompletato(1);
        itemList.add((T) exnew);
        notifyDataSetChanged();
    }

}
