package com.epf.rentmanager.ui.servlets.VehiculeServlets.utils;

import javax.servlet.http.HttpServletRequest;

import com.epf.rentmanager.model.Vehicule;

public class VehiculeServletsUtils {

    /**
     * Transform a request to a vehicule
     * 
     * @param request
     * @param isUpdate
     * @return vehicule
     */
    public static Vehicule requestToVehicule(HttpServletRequest request, boolean isUpdate) {

        Vehicule vehicule = new Vehicule();
        vehicule.setConstructeur(request.getParameter("manufacturer"));
        vehicule.setModele(request.getParameter("modele"));
        vehicule.setNb_places(Integer.parseInt(request.getParameter("seats")));

        if (isUpdate) {
            vehicule.setId(Integer.parseInt(request.getParameter("id")));
        }

        return vehicule;
    }
}
