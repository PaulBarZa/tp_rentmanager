package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.utils.DaoOutils;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

    private DaoOutils daoOutils;

    @Autowired
    public ReservationDao(DaoOutils daoOutils) {
        this.daoOutils = daoOutils;
    }

    private static final String COUNT_RESERVATIONS = "SELECT COUNT(id) AS count FROM Reservation;";
    private static final String FIND_RESERVATIONS_BY_ID = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation WHERE id=?;";
    private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation WHERE client_id=?;";
    private static final String FIND_RESERVATIONS_BY_VEHICULE_QUERY = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation WHERE vehicule_id=?;";
    private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicule_id, debut, fin FROM Reservation;";
    private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicule_id, debut, fin) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET client_id = ?, vehicule_id = ?, debut = ?, fin = ? WHERE id = ?;";
    private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";

    /**
     * Count reservations in Reservation table
     * 
     * @return number of reservations
     * @throws DaoException
     */
    public int count() throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(COUNT_RESERVATIONS, false);
                ResultSet resultSet = preparedStatement.executeQuery();) {

            if (resultSet.next()) {
                return resultSet.getInt("count");

            } else {
                throw new DaoException("Failed to count reservations");
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Find a reservation in Reservation table think his id
     * 
     * @param id
     * @return reservation corresponding to the id
     * @throws DaoException
     */
    public Optional<Reservation> findById(int id) throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_RESERVATIONS_BY_ID, false);) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Reservation reservation = resultToReservation(resultSet);
                resultSet.close();
                return Optional.of(reservation);

            } else {
                resultSet.close();
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Find a reservation in Reservation table think his client id
     * 
     * @param clientId
     * @return reservation corresponding to a client id
     * @throws DaoException
     */
    public List<Reservation> findResaByClientId(int clientId) throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY,
                false);) {

            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservations.add(resultToReservation(resultSet));
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return reservations;
    }

    /**
     * Find a reservation in Reservation table think his vehicule id
     * 
     * @param vehiculeId
     * @return reservation corresponding to a vehicule id
     * @throws DaoException
     */
    public List<Reservation> findResaByVehiculeId(int vehiculeId) throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_RESERVATIONS_BY_VEHICULE_QUERY,
                false);) {
            preparedStatement.setInt(1, vehiculeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservations.add(resultToReservation(resultSet));
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return reservations;
    }

    /**
     * Return all reservations from Reservation table
     * 
     * @return list of reservations
     * @throws DaoException
     */
    public List<Reservation> findAll() throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_RESERVATIONS_QUERY, false);
                ResultSet resultSet = preparedStatement.executeQuery();) {

            while (resultSet.next()) {
                reservations.add(resultToReservation(resultSet));
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return reservations;
    }

    /**
     * Create a reservation in Reservation table
     * 
     * @param reservation
     * @return reservation key
     * @throws DaoException
     */
    public int create(Reservation reservation) throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(CREATE_RESERVATION_QUERY, true);) {

            PreparedStatement complePreparedStatement = completePreparedStatement(preparedStatement, reservation);
            complePreparedStatement.executeUpdate();
            ResultSet resultSet = complePreparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException("Failed to get reservation keys during creation");
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Update a reservation in Reservation table
     * 
     * @param reservation
     * @return reservation key
     * @throws DaoException
     */
    public int update(Reservation reservation) throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(UPDATE_RESERVATION_QUERY, true);) {

            PreparedStatement complePreparedStatement = completePreparedStatement(preparedStatement, reservation);
            complePreparedStatement.setLong(5, reservation.getId());
            int statementStatus = complePreparedStatement.executeUpdate();

            if (statementStatus != 1) {
                throw new DaoException("This reservation doesn't exists");
            }
            return statementStatus;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Delete a reservation in Reservation table
     * 
     * @param reservation
     * @return statement status
     * @throws DaoException
     */
    public int delete(Reservation reservation) throws DaoException {
        try (Connection connection = new ConnectionManager().getConnection();
                PreparedStatement preparedStatement = daoOutils.initPreparedStatement(DELETE_RESERVATION_QUERY,
                        false);) {

            preparedStatement.setInt(1, reservation.getId());
            int statementStatus = preparedStatement.executeUpdate();

            if (statementStatus != 1) {
                throw new DaoException("This reservation doesn't exists");
            }
            return statementStatus;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Delete all reservations which contain a given client id
     * 
     * @param clientId
     * @throws DaoException
     */
    public void clientDeleted(int clientId) throws DaoException {
        List<Reservation> reservations = findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getClient_id() == clientId) {
                delete(reservation);
            }
        }
    }

    /**
     * Delete all reservations which contain a given vehicule id
     * 
     * @param vehiculeId
     * @throws DaoException
     */
    public void vehiculeDeleted(int vehiculeId) throws DaoException {
        List<Reservation> reservations = findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getVehicule_id() == vehiculeId) {
                delete(reservation);
            }
        }
    }

    /**
     * Complete a PreparedStatement with client data
     * 
     * @param preparedStatement
     * @param client
     * @return PreparedStatement corresponding with client data
     * @throws SQLException
     */
    public PreparedStatement completePreparedStatement(PreparedStatement preparedStatement, Reservation reservation)
            throws SQLException {
        preparedStatement.setInt(1, reservation.getClient_id());
        preparedStatement.setInt(2, reservation.getVehicule_id());
        preparedStatement.setDate(3, Date.valueOf(reservation.getDebut()));
        preparedStatement.setDate(4, Date.valueOf(reservation.getFin()));
        return preparedStatement;
    }

    /**
     * Return a Reservation from a resultSet
     * 
     * @param resultSet
     * @return reservation corresponding to the result
     * @throws SQLException
     */
    public Reservation resultToReservation(ResultSet resultSet) throws SQLException {
        return new Reservation(resultSet.getInt("id"), resultSet.getInt("client_id"), resultSet.getInt("vehicule_id"),
                resultSet.getDate("debut").toLocalDate(), resultSet.getDate("fin").toLocalDate());
    }
}
