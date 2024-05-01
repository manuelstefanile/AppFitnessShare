package com.example.appfitness.Bean;

public class Utente {
    private long id;
    private String nome,cognome,nomeUtente;
    private int eta;
    private float altezza;
    private Peso peso;
    private Misure misure;
    private Kcal kcal;
    private Note note;
    private byte[] immagine;

    public Utente(String nome, String cognome, String nomeUtente, int eta, float altezza, Peso peso, Misure misure, Kcal kcal, Note note) {
        this.nome = nome;
        this.cognome = cognome;
        this.nomeUtente = nomeUtente;
        this.eta = eta;
        this.altezza = altezza;
        this.peso = peso;
        this.misure = misure;
        this.kcal = kcal;
        this.note = note;
    }

    public Utente() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public float getAltezza() {
        return altezza;
    }

    public void setAltezza(float altezza) {
        this.altezza = altezza;
    }

    public Peso getPeso() {
        return peso;
    }

    public void setPeso(Peso peso) {
        this.peso = peso;
    }

    public Misure getMisure() {
        return misure;
    }

    public void setMisure(Misure misure) {
        this.misure = misure;
    }

    public Kcal getKcal() {
        return kcal;
    }

    public void setKcal(Kcal kcal) {
        this.kcal = kcal;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void RipristinaDatiUtente(){
        nome=null;
        cognome=null;
        nomeUtente=null;
        eta=0;
        altezza=0;
        peso=null;
        misure=null;
        kcal=null;
        note=null;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", nomeUtente='" + nomeUtente + '\'' +
                ", eta=" + eta +
                ", altezza=" + altezza +
                ", peso=" + peso +
                ", misure=" + misure +
                ", kcal=" + kcal +
                ", note=" + note +
                '}';
    }
}
