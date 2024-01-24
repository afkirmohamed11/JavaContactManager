package com.DL.bo;

public class Contact {
    private int id;
    private String nom;
    private String prenom;
    private String telephone1;
    private String telephone2;
    private String adresse;
    private String emailPersonnel;
    private String emailProfessionnel;
    private String genre;

    // Constructeurs
    public Contact(){};
    public Contact( String nom, String prenom, String telephone1, String telephone2, String adresse,
                   String emailPersonnel, String emailProfessionnel, String genre) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone1 = telephone1;
        this.telephone2 = telephone2;
        this.adresse = adresse;
        this.emailPersonnel = emailPersonnel;
        this.emailProfessionnel = emailProfessionnel;
        this.genre = genre;
    }
    // getters

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getEmailPersonnel() {
        return emailPersonnel;
    }

    public String getEmailProfessionnel() {
        return emailProfessionnel;
    }

    public String getGenre() {
        return genre;
    }

    // setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setEmailPersonnel(String emailPersonnel) {
        this.emailPersonnel = emailPersonnel;
    }

    public void setEmailProfessionnel(String emailProfessionnel) {
        this.emailProfessionnel = emailProfessionnel;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

