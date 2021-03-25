package com.epf.rentmanager.ui.servlets.ReservationServlets.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import com.epf.rentmanager.model.Reservation;

public class ReservationServletsUtils {

    /**
     * Transform a request to a reservation
     * 
     * @param request
     * @param isUpdate
     * @return reservation
     * @throws ParseException
     */
    public static Reservation requestToReservation(HttpServletRequest request, boolean isUpdate) throws ParseException {

        Reservation reservation = new Reservation();
        reservation.setVehicule_id(Integer.parseInt(request.getParameter("car")));
        reservation.setClient_id(Integer.parseInt(request.getParameter("client")));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        reservation.setDebut(LocalDate.parse(changeDateFormat(request.getParameter("begin")), formatter));
        reservation.setFin(LocalDate.parse(changeDateFormat(request.getParameter("end")), formatter));

        if (isUpdate) {
            reservation.setId(Integer.parseInt(request.getParameter("id")));
        }

        return reservation;
    }

    /**
     * Change date string format
     * 
     * @param oldFormat
     * @return Date string
     */
    private static String changeDateFormat(String oldFormat) {
        String[] array = oldFormat.split("");
        String year = array[0] + "" + array[1] + "" + array[2] + "" + array[3];
        String month = array[5] + "" + array[6];
        String day = array[8] + "" + array[9];

        return day + "/" + month + "/" + year;
    }
}
