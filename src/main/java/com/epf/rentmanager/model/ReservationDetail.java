package com.epf.rentmanager.model;

import java.time.LocalDate;

public class ReservationDetail {

    private int id;
    private String client_name;
    private String vehicule_name;
    private LocalDate debut;
    private LocalDate fin;

    /**
     * Reservation detail class constructor
     */
    public ReservationDetail() {
    }

    /**
     * Reservation class constructor
     * 
     * @param id
     * @param client_name
     * @param vehicule_name
     * @param debut
     * @param fin
     */
    public ReservationDetail(int id, String client_name, String vehicule_name, LocalDate debut, LocalDate fin) {
        this.id = id;
        this.client_name = client_name;
        this.vehicule_name = vehicule_name;
        this.debut = debut;
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "Reservation info [ id : " + id + "client_name : " + client_name + ", vehicule_name : " + vehicule_name
                + ", date de debut : " + debut + ", date de fin : " + fin + " ]";
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getVehicule_name() {
        return vehicule_name;
    }

    public void setVehicule_name(String vehicule_name) {
        this.vehicule_name = vehicule_name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
