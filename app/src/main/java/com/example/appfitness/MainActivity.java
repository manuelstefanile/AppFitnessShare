package com.example.appfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Inizia(View bottone){
        //porta in una nuova activity
        Intent i =new Intent();
        i.setClass(getApplicationContext(),Registrazione_Pag2.class);
        startActivity(i);
    }


}