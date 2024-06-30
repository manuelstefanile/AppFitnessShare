package com.example.appfitness.Bean;

import com.google.gson.Gson;

import java.io.Serializable;


public class Note implements Serializable {
    private String note;


    public Note() {

    }


    public Note( String note) {
        this.note = note;

    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Note fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Note.class);
    }
}
