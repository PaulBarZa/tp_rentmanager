package com.epf.rentmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit test for reservation service.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationDao reservationDao;

    public Reservation initValidReservation() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse("10/10/2010", formatter);
        LocalDate endDate = LocalDate.parse("15/10/2010", formatter);

        return new Reservation(1, 1, 1, startDate, endDate);
    }

    @Test
    public void count_should_return_integer_when_dao_return_integer() throws ServiceException, DaoException {
        // When
        when(this.reservationDao.count()).thenReturn(1);

        // Then
        assertEquals(1, reservationService.count());
    }

    @Test
    public void count_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.reservationDao.count()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.count());
    }

    @Test
    public void findById_should_return_integer_when_dao_return_integer() throws ServiceException, DaoException {
        // When
        Reservation reservation = initValidReservation();
        when(this.reservationDao.findById(1)).thenReturn(Optional.of(reservation));

        // Then
        assertEquals(reservation, reservationService.findById(1));
    }

    @Test
    public void findById_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.reservationDao.findById(1)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.findById(1));
    }

    @Test
    public void findResaByClientId_should_return_reservation_list() throws ServiceException, DaoException {
        // When
        List<Reservation> reservations = new ArrayList<Reservation>();
        reservations.add(initValidReservation());
        when(this.reservationDao.findResaByClientId(1)).thenReturn(reservations);

        // Then
        assertEquals(reservations, reservationService.findResaByClientId(1));
    }

    @Test
    public void findResaByClientId_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.reservationDao.findResaByClientId(1)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.findResaByClientId(1));
    }

    @Test
    public void findResaByVehiculeId_should_return_reservation_list() throws ServiceException, DaoException {
        // When
        List<Reservation> reservations = new ArrayList<Reservation>();
        reservations.add(initValidReservation());
        when(this.reservationDao.findResaByVehiculeId(1)).thenReturn(reservations);

        // Then
        assertEquals(reservations, reservationService.findResaByVehicleId(1));
    }

    @Test
    public void findResaByVehiculeId_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.reservationDao.findResaByVehiculeId(1)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.findResaByVehicleId(1));
    }

    @Test
    public void findAll_should_return_reservations_list_when_dao_return_reservations_list()
            throws DaoException, ServiceException {
        // Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse("10/10/2009", formatter);
        LocalDate endDate = LocalDate.parse("15/10/2009", formatter);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(initValidReservation());
        reservations.add(new Reservation(2, 2, 2, startDate, endDate));

        // When
        when(this.reservationDao.findAll()).thenReturn(reservations);

        // Then
        assertEquals(reservations, reservationService.findAll());
    }

    @Test
    public void findAll_should_fail_when_dao_throws_exception() throws DaoException {
        // When
        when(this.reservationDao.findAll()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.findAll());
    }

    @Test
    public void create_reservation_should_return_integer() throws ServiceException, DaoException {
        // When
        Reservation reservation = initValidReservation();
        when(this.reservationDao.create(reservation)).thenReturn(1);

        // Then
        assertEquals(1, reservationService.create(reservation));
    }

    @Test
    public void create_reservation_should_throws_exception() throws DaoException {
        // When
        Reservation reservation = initValidReservation();
        when(this.reservationDao.create(reservation)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.create(reservation));
    }

    @Test
    public void update_reservation_should_return_integer() throws ServiceException, DaoException {
        // When
        Reservation reservation = initValidReservation();
        when(this.reservationDao.update(reservation)).thenReturn(1);

        // Then
        assertEquals(1, reservationService.update(reservation));
    }

    @Test
    public void update_reservation_should_throws_exception() throws DaoException {
        // When
        Reservation reservation = initValidReservation();
        when(this.reservationDao.update(reservation)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.update(reservation));
    }

    @Test
    public void delete_reservation_should_return_integer() throws ServiceException, DaoException {
        // When
        Reservation reservation = initValidReservation();
        when(this.reservationDao.delete(reservation)).thenReturn(1);

        // Then
        assertEquals(1, reservationService.delete(reservation));
    }

    @Test
    public void delete_reservation_should_throws_exception() throws DaoException {
        // When
        Reservation reservation = initValidReservation();
        when(this.reservationDao.delete(reservation)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.delete(reservation));
    }

}
