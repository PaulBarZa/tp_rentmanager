package com.epf.rentmanager.utils;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.VehiculeService;

import org.junit.Test;

public class VehiculeTest {

    @Test
    public void isVehiculeValid_should_return_true_with_valid_vehicule() throws Exception {
        // Given
        Vehicule vehicule = new Vehicule(5, "Peugeot", "207", 5);

        // Then
        assertTrue(VehiculeService.isVehiculeValid(vehicule));
    }

    @Test
    public void isVehiculeValid_should_return_exception_with_empty_vehicule_constructeur() throws Exception {
        // Given
        Vehicule vehicule = new Vehicule(5, " ", "207", 5);

        // Then
        assertThrows(ServiceException.class, () -> VehiculeService.isVehiculeValid(vehicule));
    }

    @Test
    public void isVehiculeValid_should_return_exception_with_empty_vehicule_modele() throws Exception {
        // Given
        Vehicule vehicule = new Vehicule(5, "Peugeot", " ", 5);

        // Then
        assertThrows(ServiceException.class, () -> VehiculeService.isVehiculeValid(vehicule));
    }

    @Test
    public void isVehiculeValid_should_return_exception_with_less_than_2_seats_vehicule() throws Exception {
        // Given
        Vehicule vehicule = new Vehicule(5, "Peugeot", "207", 1);

        // Then
        assertThrows(ServiceException.class, () -> VehiculeService.isVehiculeValid(vehicule));
    }

    @Test
    public void isVehiculeValid_should_return_exception_with_more_than_9_seats_vehicule() throws Exception {
        // Given
        Vehicule vehicule = new Vehicule(5, "Peugeot", "207", 10);

        // Then
        assertThrows(ServiceException.class, () -> VehiculeService.isVehiculeValid(vehicule));
    }

}
