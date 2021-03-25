package com.epf.rentmanager.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.utils.DaoOutils;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Vehicule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VehiculeDao {

    private DaoOutils daoOutils;

    @Autowired
    public VehiculeDao(DaoOutils daoOutils) {
        this.daoOutils = daoOutils;
    }

    private static final String COUNT_VEHICULES = "SELECT COUNT(id) AS count FROM Vehicule;";
    private static final String FIND_VEHICULE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicule WHERE id=?;";
    private static final String FIND_VEHICULES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicule;";
    private static final String CREATE_VEHICULE_QUERY = "INSERT INTO Vehicule(constructeur, modele, nb_places) VALUES(?, ?, ?);";
    private static final String UPDATE_VEHICULE_QUERY = "UPDATE Vehicule SET constructeur = ?, modele = ?, nb_places = ? WHERE id = ?;";
    private static final String DELETE_VEHICULE_QUERY = "DELETE FROM Vehicule WHERE id=?;";

    /**
     * Count vehicules in Vehicule table
     * 
     * @return number of vehicules
     * @throws DaoException
     */
    public int count() throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(COUNT_VEHICULES, false);
                ResultSet resultSet = preparedStatement.executeQuery();) {

            if (resultSet.next()) {
                return resultSet.getInt("count");
            } else {
                throw new DaoException("Failed to count vehicules");
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Find a vehicule in Vehicule table think his id
     * 
     * @param id
     * @return vehicule corresponding to the id
     * @throws DaoException
     */
    public Optional<Vehicule> findById(long id) throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_VEHICULE_QUERY, false);) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Vehicule vehicule = resultToVehicule(resultSet);
                resultSet.close();
                return Optional.of(vehicule);
            } else {
                resultSet.close();
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Return all vehicules from Vehicule table
     * 
     * @return list of vehicules
     * @throws DaoException
     */
    public List<Vehicule> findAll() throws DaoException {
        List<Vehicule> vehicules = new ArrayList<>();
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_VEHICULES_QUERY, true);
                ResultSet resultSet = preparedStatement.executeQuery();) {

            while (resultSet.next()) {
                vehicules.add(resultToVehicule(resultSet));
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return vehicules;
    }

    /**
     * Create a vehicule in Vehicule table
     * 
     * @param vehicule
     * @return vehicule key
     * @throws DaoException
     */
    public int create(Vehicule vehicule) throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(CREATE_VEHICULE_QUERY, true);) {

            PreparedStatement completedPreparedStatement = completePreparedStatement(preparedStatement, vehicule);
            completedPreparedStatement.executeUpdate();
            ResultSet resultSet = completedPreparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException("Failed to get vehicule keys during creation");
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Update a vehicule in Vehicule table
     * 
     * @param vehicule
     * @return vehicule key
     * @throws DaoException
     */
    public int update(Vehicule vehicule) throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(UPDATE_VEHICULE_QUERY, false);) {

            PreparedStatement completedPreparedStatement = completePreparedStatement(preparedStatement, vehicule);
            completedPreparedStatement.setInt(4, vehicule.getId());
            int statementStatus = completedPreparedStatement.executeUpdate();

            if (statementStatus != 1) {
                throw new DaoException("This vehicule doesn't exists");
            }
            return statementStatus;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Delete a vehicule in Vehicule table
     * 
     * @param vehicule
     * @return statement status (1 for ok, other value for error/empty)
     * @throws DaoException
     */
    public int delete(Vehicule vehicule) throws DaoException {
        try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(DELETE_VEHICULE_QUERY, false);) {

            preparedStatement.setInt(1, vehicule.getId());
            int statementStatus = preparedStatement.executeUpdate();

            if (statementStatus != 1) {
                throw new DaoException("This vehicule doesn't exists");
            }
            return statementStatus;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    /**
     * Complete a PreparedStatement with vehicule data
     * 
     * @param preparedStatement
     * @param vehicule
     * @return PreparedStatement corresponding with vehicule data
     * @throws SQLException
     */
    public PreparedStatement completePreparedStatement(PreparedStatement preparedStatement, Vehicule vehicule)
            throws SQLException {
        preparedStatement.setString(1, vehicule.getConstructeur());
        preparedStatement.setString(2, vehicule.getModele());
        preparedStatement.setInt(3, vehicule.getNb_places());
        return preparedStatement;
    }

    /**
     * Return a vehicule from a resultSet
     * 
     * @param resultSet
     * @return vehicule corresponding to the result
     * @throws SQLException
     */
    public Vehicule resultToVehicule(ResultSet resultSet) throws SQLException {
        return new Vehicule(resultSet.getInt("id"), resultSet.getString("constructeur"), resultSet.getString("modele"),
                resultSet.getInt("nb_places"));
    }
}
