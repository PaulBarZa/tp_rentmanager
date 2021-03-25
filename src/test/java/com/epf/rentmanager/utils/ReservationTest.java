package com.epf.rentmanager.utils;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;

import org.junit.Test;

public class ReservationTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Reservation initValidReservation() {
        LocalDate startDate = LocalDate.parse("11/10/2020", this.formatter);
        LocalDate endDate = LocalDate.parse("16/10/2020", this.formatter);
        return new Reservation(5, 4, 3, startDate, endDate);
    }

    @Test
    public void isReservationValid_should_return_true_with_valid_reservation() throws Exception {
        // Given
        Reservation reservation = initValidReservation();
        List<Reservation> reservationList = new ArrayList<Reservation>();

        // Then
        assertTrue(ReservationService.isReservationValid(reservationList, reservation));
    }

    @Test
    public void isReservationValid_should_return_exception_with_invalid_reservation_date_duration() throws Exception {
        // Given
        Reservation reservation = initValidReservation();
        List<Reservation> reservationList = new ArrayList<Reservation>();

        LocalDate endDate = LocalDate.parse("19/10/2020", this.formatter);
        reservation.setFin(endDate);

        // Then
        assertThrows(ServiceException.class, () -> ReservationService.isReservationValid(reservationList, reservation));
    }

    @Test
    public void isReservationValid_should_return_exception_with_invalid_reservation_vehicule_date_duration()
            throws Exception {
        // Given
        Reservation reservation = initValidReservation();
        List<Reservation> reservationList = new ArrayList<Reservation>();

        Reservation reservation1 = initValidReservation();
        reservation1.setDebut(LocalDate.parse("04/10/2020", this.formatter));
        reservation1.setFin(LocalDate.parse("10/10/2020", this.formatter));
        reservationList.add(reservation1);

        Reservation reservation2 = initValidReservation();
        reservation2.setDebut(LocalDate.parse("27/09/2020", this.formatter));
        reservation2.setFin(LocalDate.parse("03/10/2020", this.formatter));
        reservationList.add(reservation2);

        Reservation reservation3 = initValidReservation();
        reservation3.setDebut(LocalDate.parse("20/09/2020", this.formatter));
        reservation3.setFin(LocalDate.parse("26/09/2020", this.formatter));
        reservationList.add(reservation3);

        Reservation reservation4 = initValidReservation();
        reservation4.setDebut(LocalDate.parse("12/09/2020", this.formatter));
        reservation4.setFin(LocalDate.parse("19/09/2020", this.formatter));
        reservationList.add(reservation4);

        // Then
        assertThrows(ServiceException.class, () -> ReservationService.isReservationValid(reservationList, reservation));
    }

}
