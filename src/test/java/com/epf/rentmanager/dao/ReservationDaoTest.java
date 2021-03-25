package com.epf.rentmanager.dao;

import com.epf.rentmanager.dao.utils.DaoOutils;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Reservation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Date;

/**
 * Unit test for reservation dao.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReservationDaoTest {

    private static final String COUNT_RESERVATIONS = "SELECT COUNT(id) AS count FROM Reservation;";
    private static final String FIND_RESERVATIONS_BY_ID = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation WHERE id=?;";
    private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation WHERE client_id=?;";
    private static final String FIND_RESERVATIONS_BY_VEHICULE_QUERY = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation WHERE vehicule_id=?;";
    private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation;";
    private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicule_id, debut, fin) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET client_id = ?, vehicule_id = ?, debut = ?, fin = ? WHERE id = ?;";
    private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";

    @InjectMocks
    private ReservationDao reservationDao;

    @Mock
    private DaoOutils daoOutils;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet result;

    @Test
    public void count_should_return_integer() throws DaoException, SQLException {
        // When
        initValidConnection(COUNT_RESERVATIONS, null, null, false);
        when(this.result.getInt("count")).thenReturn(10);

        // Then
        assertEquals(10, reservationDao.count());
    }

    @Test
    public void count_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(COUNT_RESERVATIONS, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.count());
    }

    @Test
    public void count_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(COUNT_RESERVATIONS, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.count());
    }

    @Test
    public void findById_should_return_client() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        initValidConnection(FIND_RESERVATIONS_BY_ID, startDate, endDate, false);

        // Then
        assertTrue(Optional.of(initReservation(startDate, endDate)).toString()
                .equals(reservationDao.findById(1).toString()));
    }

    @Test
    public void findById_should_return_empty_client() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_RESERVATIONS_BY_ID, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertEquals(Optional.empty(), reservationDao.findById(1));
    }

    @Test
    public void findById_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_RESERVATIONS_BY_ID, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.findById(1));
    }

    @Test
    public void findResaByClientId_should_return_reservation_list() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        initValidConnection(FIND_RESERVATIONS_BY_CLIENT_QUERY, startDate, endDate, false);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(initReservation(startDate, endDate));

        // Then
        assertTrue(reservations.toString().equals(reservationDao.findResaByClientId(1).toString()));
    }

    @Test
    public void findResaByClientId_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.findResaByClientId(1));
    }

    @Test
    public void findResaByVehiculeId_should_return_reservation_list() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        initValidConnection(FIND_RESERVATIONS_BY_VEHICULE_QUERY, startDate, endDate, false);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(initReservation(startDate, endDate));

        // Then
        assertTrue(reservations.toString().equals(reservationDao.findResaByVehiculeId(1).toString()));
    }

    @Test
    public void findResaByVehiculeId_should_return_dao_exception_when_sql_exception()
            throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_RESERVATIONS_BY_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.findResaByVehiculeId(1));
    }

    @Test
    public void findAll_should_return_client_list() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        initValidConnection(FIND_RESERVATIONS_QUERY, startDate, endDate, false);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(initReservation(startDate, endDate));

        // Then
        assertTrue(reservations.toString().equals(reservationDao.findAll().toString()));
    }

    @Test
    public void findAll_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_RESERVATIONS_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.findAll());
    }

    @Test
    public void create_should_return_integer() throws DaoException, SQLException {
        // When
        initValidConnection(CREATE_RESERVATION_QUERY, null, null, true);
        when(this.stmt.getGeneratedKeys()).thenReturn(result);
        when(this.result.getInt(1)).thenReturn(1);

        // Then
        assertEquals(1, reservationDao.create(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void create_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(CREATE_RESERVATION_QUERY, true)).thenReturn(stmt);
        when(this.stmt.getGeneratedKeys()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.create(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void create_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(CREATE_RESERVATION_QUERY, true)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.create(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void update_should_return_integer() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_RESERVATION_QUERY, true)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(1);

        // Then
        assertEquals(1, reservationDao.update(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void update_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_RESERVATION_QUERY, true)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(2);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.update(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void update_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_RESERVATION_QUERY, true)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.update(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void delete_should_return_integer() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_RESERVATION_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(1);

        // Then
        assertEquals(1, reservationDao.delete(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void deletee_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_RESERVATION_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(2);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.delete(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void delete_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_RESERVATION_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> reservationDao.delete(initReservation(new Date(0), new Date(10))));
    }

    @Test
    public void clientDeleted_should_call_delete_method() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        Reservation reservation = initReservation(startDate, endDate);
        ReservationDao reservationDaoSpy = initMockedDaoSetUp(reservation);

        // Then
        reservationDaoSpy.clientDeleted(2);
        verify(reservationDaoSpy, times(1)).delete(reservation);
    }

    @Test
    public void clientDeleted_should_not_call_delete_method() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        Reservation reservation = initReservation(startDate, endDate);
        ReservationDao reservationDaoSpy = initMockedDaoSetUp(reservation);

        // Then
        reservationDaoSpy.clientDeleted(1);
        verify(reservationDaoSpy, times(0)).delete(reservation);
    }

    @Test
    public void vehiculeDeleted_should_call_delete_method() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        Reservation reservation = initReservation(startDate, endDate);
        ReservationDao reservationDaoSpy = initMockedDaoSetUp(reservation);

        // Then
        reservationDaoSpy.vehiculeDeleted(2);
        verify(reservationDaoSpy, times(1)).delete(reservation);
    }

    @Test
    public void vehiculeDeleted_should_not_call_delete_method() throws DaoException, SQLException {
        // When
        Date startDate = new Date(0);
        Date endDate = new Date(10);
        Reservation reservation = initReservation(startDate, endDate);
        ReservationDao reservationDaoSpy = initMockedDaoSetUp(reservation);

        // Then
        reservationDaoSpy.vehiculeDeleted(1);
        verify(reservationDaoSpy, times(0)).delete(reservation);
    }

    public void initValidConnection(String query, Date startDate, Date endDate, boolean isGeneratedKeys)
            throws SQLException {
        when(this.daoOutils.initPreparedStatement(query, isGeneratedKeys)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(true).thenReturn(false);
        when(this.result.getInt("id")).thenReturn(1);
        when(this.result.getInt("client_id")).thenReturn(2);
        when(this.result.getInt("vehicule_id")).thenReturn(2);
        when(this.result.getDate("debut")).thenReturn(startDate);
        when(this.result.getDate("fin")).thenReturn(endDate);
    }

    public ReservationDao initMockedDaoSetUp(Reservation reservation) throws DaoException {

        List<Reservation> reservations = new ArrayList<Reservation>();
        reservations.add(reservation);

        ReservationDao reservationDaoSpy = spy(this.reservationDao);
        doReturn(reservations).when(reservationDaoSpy).findAll();
        doReturn(1).when(reservationDaoSpy).delete(reservation);

        return reservationDaoSpy;
    }

    public Reservation initReservation(Date startDate, Date endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Reservation reservation = new Reservation(1, 2, 2, LocalDate.parse(startDate.toString(), formatter),
                LocalDate.parse(endDate.toString(), formatter));
        return reservation;
    }
}
