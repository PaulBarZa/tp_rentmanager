package com.epf.rentmanager.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    @Autowired
    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    /**
     * Count reservations in Reservation table
     * 
     * @return number of reservations
     * @throws ServiceException
     */
    public int count() throws ServiceException {
        try {
            return reservationDao.count();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Find a reservation in Reservation table think his id
     * 
     * @param id
     * @return reservation corresponding to the id
     * @throws ServiceException
     */
    public Reservation findById(int id) throws ServiceException {
        try {
            return reservationDao.findById(id).get();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Find a reservation in Reservation table think his client id
     * 
     * @param clientId
     * @return reservation corresponding to a client id
     * @throws ServiceException
     */
    public List<Reservation> findResaByClientId(int id) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Find a reservation in Reservation table think his vehicule id
     * 
     * @param vehiculeId
     * @return reservation corresponding to a vehicule id
     * @throws ServiceException
     */
    public List<Reservation> findResaByVehicleId(int id) throws ServiceException {
        try {
            return reservationDao.findResaByVehiculeId(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Return all reservations from Reservation table
     * 
     * @return list of reservations
     * @throws ServiceException
     */
    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Create a reservation in Reservation table
     * 
     * @param reservation
     * @return reservation key
     * @throws ServiceException
     */
    public int create(Reservation reservation) throws ServiceException {
        try {
            List<Reservation> reservationList = findAll();
            if (!isReservationAvalaible(reservationList, reservation)) {
                throw new ServiceException("This vehicule isn't available this day");
            }
            isReservationValid(reservationList, reservation);
            return reservationDao.create(reservation);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Update a reservation in Reservation table
     * 
     * @param reservation
     * @return reservation key
     * @throws ServiceException
     */
    public int update(Reservation reservation) throws ServiceException {
        try {
            isReservationValid(findAll(), reservation);
            return reservationDao.update(reservation);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Delete a reservation in Reservation table
     * 
     * @param reservation
     * @return statement status (1 for ok, other value for error/empty)
     * @throws ServiceException
     */
    public int delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Check if reservation is valid
     * 
     * @param reservationList
     * @param reservation
     * @return boolean depends on reservation validity
     * @throws ServiceException
     */
    public static boolean isReservationValid(List<Reservation> reservationList, Reservation reservation)
            throws ServiceException {

        if (!isReservationDurationValid(reservation)) {
            throw new ServiceException("Invalid reservation duration, reservation must be 7 days maximum");
        }

        if (!isReservationVehiculeDurationValid(reservationList, reservation)) {
            throw new ServiceException(
                    "Invalid vehicule reservation duration, a vehicule can't be booked more than 30 days");
        }

        return true;
    }

    /**
     * Check if reservation is available today
     * 
     * @param reservationList
     * @param reservation
     * @return boolean depends on reservation avalaiblity
     */
    public static boolean isReservationAvalaible(List<Reservation> reservationList, Reservation reservation) {

        for (Reservation reserva : reservationList) {
            if (reserva.getVehicule_id() == reservation.getVehicule_id()) {

                Date begin = Date.valueOf(reserva.getDebut());
                Date end = Date.valueOf(reserva.getFin());
                Date myDate_begin = Date.valueOf(reservation.getDebut());
                Date myDate_end = Date.valueOf(reservation.getFin());

                if (isBetween(begin, end, myDate_begin, myDate_end)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if a date is between an other date
     * 
     * @param begin
     * @param end
     * @param myDate_begin
     * @param myDate_end
     * @return boolean depends of date is between two others ot not
     */
    public static boolean isBetween(Date begin, Date end, Date myDate_begin, Date myDate_end) {

        if ((myDate_begin.equals(begin) || myDate_begin.after(begin))
                && (myDate_begin.equals(end) || myDate_begin.before(end))) {
            return true;
        }
        if ((myDate_end.equals(begin) || myDate_end.after(begin))
                && (myDate_end.equals(end) || myDate_end.before(end))) {
            return true;
        }

        return false;
    }

    /**
     * Check if reservation duration is valid
     * 
     * @param reservationList
     * @param reservation
     * @return boolean depends on reservation duration validity
     */
    public static boolean isReservationDurationValid(Reservation reservation) {

        long timeInMillis = Date.valueOf(reservation.getFin()).getTime()
                - Date.valueOf(reservation.getDebut()).getTime();

        if (timeInMillis > 604800001) { // Milliseconds in a week = 604800000
            return false;
        }

        return true;
    }

    /**
     * Check if vehicule reservation duration is ok
     * 
     * @param reservationList
     * @param reservation
     * @return boolean depends on reservation vehicule duration validity
     */
    public static boolean isReservationVehiculeDurationValid(List<Reservation> reservationList,
            Reservation reservation) {

        ArrayList<List<Date>> dates = ReservationService.getSortedDateLists(reservationList, reservation);
        List<Date> sortedStartDateList = dates.get(0);
        List<Date> sortedEndDateList = dates.get(1);

        long globalTimeInMillis = getDateDiff(sortedEndDateList.get(0), sortedStartDateList.get(0));
        for (int i = 0; i < sortedStartDateList.size() - 1; i++) {

            long delayInMillis = getDateDiff(sortedStartDateList.get(i + 1), sortedEndDateList.get(i));

            if (delayInMillis > 86400000) { // Time in a day : 86 400 000 milliseconds
                globalTimeInMillis = getDateDiff(sortedEndDateList.get(i + 1), sortedStartDateList.get(i + 1));
                continue;
            }

            globalTimeInMillis += getDateDiff(sortedEndDateList.get(i + 1), sortedStartDateList.get(i + 1));

            if ((globalTimeInMillis / 10) + 1 > 259200000) { // Time in 30 days : 2 592 000 000 milliseconds
                return false;
            }
        }
        return true;
    }

    /**
     * Get a sorted date start and end lists from reservation list
     * 
     * @param reservationList
     * @return Array of the two date list
     */
    public static ArrayList<List<Date>> getSortedDateLists(List<Reservation> reservationList, Reservation reservation) {

        List<Date> startDateList = new ArrayList<Date>();
        List<Date> endDateList = new ArrayList<Date>();

        for (Reservation reserva : reservationList) {
            if (reserva.getVehicule_id() == reservation.getVehicule_id()) {
                startDateList.add(Date.valueOf(reserva.getDebut()));
                endDateList.add(Date.valueOf(reserva.getFin()));
            }
        }

        startDateList.add(Date.valueOf(reservation.getDebut()));
        endDateList.add(Date.valueOf(reservation.getFin()));
        Collections.sort(startDateList);
        Collections.sort(endDateList);

        ArrayList<List<Date>> dates = new ArrayList<>();
        dates.add(startDateList);
        dates.add(endDateList);
        return dates;
    }

    /**
     * Get the difference between two dates
     * 
     * @param date1
     * @param date2
     * @return long corresponding to the difference in milliseconds
     */
    public static long getDateDiff(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

}
