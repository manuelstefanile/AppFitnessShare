package com.example.appfitness.Bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scheda extends ListeClasseMarker {
    private String nomeScheda;
    private byte[] imgBytes;
    private ArrayList<Giorno> listaGiorni=new ArrayList<>() ;

    public Scheda(String nomeScheda, Drawable img) {
        this.nomeScheda = nomeScheda;
        this.imgBytes = drawableToByteArray(img);
    }
    public Scheda(String nomeScheda, byte[] img) {
        this.nomeScheda = nomeScheda;
        this.imgBytes = img;
    }

    public Scheda() {

    }

    public String getNomeScheda() {
        return nomeScheda;
    }

    public void setNomeScheda(String nomeScheda) {
        this.nomeScheda = nomeScheda;
    }





    public Drawable getImg() {
        return byteArrayToDrawable(imgBytes);
    }

    public void setImg(Drawable img) {
        this.imgBytes = drawableToByteArray(img);
    }
    private byte[] drawableToByteArray(Drawable drawable) {
        if (drawable == null) return null;

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Drawable byteArrayToDrawable(byte[] byteArray) {
        if (byteArray == null) return null;

        return new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }




    public ArrayList<Giorno> getListaGiorni() {
        return listaGiorni;
    }

    public void setListaGiorni(ArrayList<Giorno> listaGiorni) {
        this.listaGiorni = listaGiorni;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Scheda fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Scheda.class);
    }

    @Override
    public String toString() {
        return "\nScheda{" +
                "nomeScheda='" + nomeScheda + '\'' +
                ", imgBytes=" + Arrays.toString(imgBytes) +
                ", listaGiorni=" + listaGiorni +
                '}';
    }
}
