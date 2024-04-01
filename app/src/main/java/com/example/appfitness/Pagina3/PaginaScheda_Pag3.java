package com.example.appfitness.Pagina3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appfitness.Bean.Scheda;
import com.example.appfitness.R;

import java.util.ArrayList;

public class PaginaScheda_Pag3 extends Activity {
    ListView lista;
    AdapterListaScheda adapterSchede;
    AdapterListaScheda adapterGiorni;
    AdapterListaScheda adapterEsercizi;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schede_pag3);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        lista = (ListView)findViewById(R.id.listaSchedeView);
        adapterSchede = new AdapterListaScheda(this, R.layout.item_scheda, new ArrayList<Scheda>(),sharedPreferences);
        lista.setAdapter(adapterSchede);

        /*
        for (int i=0; i<3; i++) {
            Scheda c = new Scheda("pippo"+i,getResources().getDrawable(R.drawable.i1));
            ad.add(c);
        }*/


    }
    public void OnSchedaClick(View v){
        int position = Integer.parseInt(v.getTag().toString());
        //Scheda c = ad.getItem(position);
        Toast.makeText(getApplicationContext(),
                        "Click su Nome - posizione n."+position+": " , Toast.LENGTH_LONG)
                .show();
    }
    public void CreaScheda(View v){
        PopupSchede.act=this;
        PopupSchede.CreaScheda(getLayoutInflater(),sharedPreferences,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PopupSchede.onActivityResult(requestCode, resultCode, data);
    }
}
