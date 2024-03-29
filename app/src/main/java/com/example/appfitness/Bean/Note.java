package com.example.appfitness.Bean;

import com.google.gson.Gson;


public class Note {
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
