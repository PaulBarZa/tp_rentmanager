package com.epf.rentmanager.model;

import java.time.LocalDate;

public class Reservation {

    private int id;
    private int client_id;
    private int vehicule_id;
    private LocalDate debut;
    private LocalDate fin;

    /**
     * Reservation class constructor
     */
    public Reservation() {
    }

    /**
     * Reservation class constructor
     * 
     * @param id
     * @param client_id
     * @param vehicule_id
     * @param debut
     * @param fin
     */
    public Reservation(int id, int client_id, int vehicule_id, LocalDate debut, LocalDate fin) {
        this.id = id;
        this.client_id = client_id;
        this.vehicule_id = vehicule_id;
        this.debut = debut;
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "Reservation info [ id : " + id + ", client_id : " + client_id + ", vehicule_id : " + vehicule_id
                + ", date de debut : " + debut + ", date de fin : " + fin + " ]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getVehicule_id() {
        return vehicule_id;
    }

    public void setVehicule_id(int vehicule_id) {
        this.vehicule_id = vehicule_id;
    }

    public LocalDate getDebut() {
        return debut;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }
}
