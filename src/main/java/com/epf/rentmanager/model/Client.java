package com.epf.rentmanager.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Client {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate naissance;

    /**
     * Empty client class constructor
     * 
     */
    public Client() {
    }

    /**
     * Client class constructor
     * 
     * @param id
     * @param nom
     * @param prenom
     * @param email
     * @param naissance
     */
    public Client(int id, String nom, String prenom, String email, LocalDate naissance) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.naissance = naissance;
    }

    @Override
    public String toString() {
        return "Client info [ id : " + id + ", nom : " + nom + ", prenom : " + prenom + ", email : " + email
                + ", date de naissance : " + naissance + " ]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getNaissance() {
        return naissance;
    }

    public void setNaissance(LocalDate naissance) {
        this.naissance = naissance;
    }

    /**
     * Transform client birthday date to a string birthday which can be display
     * 
     * @return client birthday
     */
    public String transformNaissance() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(naissance);
    }
}
