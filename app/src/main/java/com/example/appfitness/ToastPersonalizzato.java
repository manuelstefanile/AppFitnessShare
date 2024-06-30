package com.example.appfitness;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class ToastPersonalizzato {

    public static void ToastSuccesso(String testo, LayoutInflater li){
        View layout = li.inflate(R.layout.toast_calendario,null);
        TextView testoto=layout.findViewById(R.id.toast_text);
        testoto.setText(testo);
        LottieAnimationView la=layout.findViewById(R.id.animCalendario);
        la.setAnimation(R.raw.save);
        la.playAnimation();
        la.setColorFilter(R.color.lightGreen);
        Toast toast = new Toast(li.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    public static void ToastSuccessoCalendario(LayoutInflater li) {
        // Mostra il Toast solo se tutte le misure sono state inserite correttamente
        View layout = li.inflate(R.layout.toast_calendario, null);
        Toast toast = new Toast(li.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void ToastErrore(String text,LayoutInflater li) {
        View layout = li.inflate(R.layout.toast_erroresave, null);
        TextView testo = layout.findViewById((int) R.id.toast_text);
        testo.setText(text);
        Toast toast = new Toast(li.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
