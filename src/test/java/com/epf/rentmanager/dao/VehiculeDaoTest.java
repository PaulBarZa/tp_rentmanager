package com.epf.rentmanager.dao;

import com.epf.rentmanager.dao.utils.DaoOutils;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Vehicule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Unit test for client dao.
 */
@RunWith(MockitoJUnitRunner.class)
public class VehiculeDaoTest {

    private static final String COUNT_VEHICULES = "SELECT COUNT(id) AS count FROM Vehicule;";
    private static final String FIND_VEHICULE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicule WHERE id=?;";
    private static final String FIND_VEHICULES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicule;";
    private static final String CREATE_VEHICULE_QUERY = "INSERT INTO Vehicule(constructeur, modele, nb_places) VALUES(?, ?, ?);";
    private static final String UPDATE_VEHICULE_QUERY = "UPDATE Vehicule SET constructeur = ?, modele = ?, nb_places = ? WHERE id = ?;";
    private static final String DELETE_VEHICULE_QUERY = "DELETE FROM Vehicule WHERE id=?;";

    @InjectMocks
    private VehiculeDao vehiculeDao;

    @Mock
    private DaoOutils daoOutils;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet result;

    @Test
    public void count_should_return_integer() throws DaoException, SQLException {
        // When
        initValidConnection(COUNT_VEHICULES, false);
        when(this.result.getInt("count")).thenReturn(10);

        // Then
        assertEquals(10, vehiculeDao.count());
    }

    @Test
    public void count_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(COUNT_VEHICULES, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.count());
    }

    @Test
    public void count_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(COUNT_VEHICULES, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.count());
    }

    @Test
    public void findById_should_return_client() throws DaoException, SQLException {
        // When
        initValidConnection(FIND_VEHICULE_QUERY, false);

        // Then
        assertTrue(Optional.of(initVehicule()).toString().equals(vehiculeDao.findById(1).toString()));
    }

    @Test
    public void findById_should_return_empty_client() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertEquals(Optional.empty(), vehiculeDao.findById(1));
    }

    @Test
    public void findById_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.findById(1));
    }

    @Test
    public void findAll_should_return_client_list() throws DaoException, SQLException {
        // When
        initValidConnection(FIND_VEHICULES_QUERY, true);
        List<Vehicule> vehicules = new ArrayList<>();
        vehicules.add(initVehicule());

        // Then
        assertTrue(vehicules.toString().equals(vehiculeDao.findAll().toString()));
    }

    @Test
    public void findAll_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_VEHICULES_QUERY, true)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.findAll());
    }

    @Test
    public void create_should_return_integer() throws DaoException, SQLException {
        // When
        initValidConnection(CREATE_VEHICULE_QUERY, true);
        when(this.stmt.getGeneratedKeys()).thenReturn(result);
        when(this.result.getInt(1)).thenReturn(1);

        // Then
        assertEquals(1, vehiculeDao.create(initVehicule()));
    }

    @Test
    public void create_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(CREATE_VEHICULE_QUERY, true)).thenReturn(stmt);
        when(this.stmt.getGeneratedKeys()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.create(initVehicule()));
    }

    @Test
    public void create_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(CREATE_VEHICULE_QUERY, true)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.create(initVehicule()));
    }

    @Test
    public void update_should_return_integer() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(1);

        // Then
        assertEquals(1, vehiculeDao.update(initVehicule()));
    }

    @Test
    public void update_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(2);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.update(initVehicule()));
    }

    @Test
    public void update_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.update(initVehicule()));
    }

    @Test
    public void delete_should_return_integer() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(1);

        // Then
        assertEquals(1, vehiculeDao.delete(initVehicule()));
    }

    @Test
    public void deletee_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(2);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.delete(initVehicule()));
    }

    @Test
    public void delete_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_VEHICULE_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> vehiculeDao.delete(initVehicule()));
    }

    public void initValidConnection(String query, boolean isGeneratedKeys) throws SQLException {
        when(this.daoOutils.initPreparedStatement(query, isGeneratedKeys)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(true).thenReturn(false);
        when(this.result.getInt("id")).thenReturn(1);
        when(this.result.getString("constructeur")).thenReturn("Foo");
        when(this.result.getString("modele")).thenReturn("Bar");
        when(this.result.getInt("nb_places")).thenReturn(4);
    }

    public Vehicule initVehicule() {
        return new Vehicule(1, "Foo", "Bar", 4);
    }
}
