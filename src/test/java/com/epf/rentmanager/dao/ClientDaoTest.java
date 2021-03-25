package com.epf.rentmanager.dao;

import com.epf.rentmanager.dao.utils.DaoOutils;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Date;

/**
 * Unit test for client dao.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientDaoTest {

    private static final String COUNT_CLIENTS = "SELECT COUNT(id) AS count FROM Client;";
    private static final String FIND_CLIENT_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE id=?;";
    private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
    private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_CLIENT_QUERY = "UPDATE Client SET nom = ?, prenom = ?, email = ?, naissance = ? WHERE id = ?;";
    private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";

    @InjectMocks
    private ClientDao clientDao;

    @Mock
    private DaoOutils daoOutils;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet result;

    @Test
    public void count_should_return_integer() throws DaoException, SQLException {
        // When
        initValidConnection(COUNT_CLIENTS, null, false);
        when(this.result.getInt("count")).thenReturn(10);

        // Then
        assertEquals(10, clientDao.count());
    }

    @Test
    public void count_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(COUNT_CLIENTS, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertThrows(DaoException.class, () -> clientDao.count());
    }

    @Test
    public void count_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(COUNT_CLIENTS, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> clientDao.count());
    }

    @Test
    public void findById_should_return_client() throws DaoException, SQLException {
        // When
        Date date = new Date(0);
        initValidConnection(FIND_CLIENT_QUERY, date, false);

        // Then
        assertTrue(Optional.of(initClient(date)).toString().equals(clientDao.findById(1).toString()));
    }

    @Test
    public void findById_should_return_empty_client() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertEquals(Optional.empty(), clientDao.findById(1));
    }

    @Test
    public void findById_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> clientDao.findById(1));
    }

    @Test
    public void findAll_should_return_client_list() throws DaoException, SQLException {
        // When
        Date date = new Date(0);
        initValidConnection(FIND_CLIENTS_QUERY, date, false);
        List<Client> clients = new ArrayList<>();
        clients.add(initClient(date));

        // Then
        assertTrue(clients.toString().equals(clientDao.findAll().toString()));
    }

    @Test
    public void findAll_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(FIND_CLIENTS_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> clientDao.findAll());
    }

    @Test
    public void create_should_return_integer() throws DaoException, SQLException {
        // When
        initValidConnection(CREATE_CLIENT_QUERY, null, true);
        when(this.stmt.getGeneratedKeys()).thenReturn(result);
        when(this.result.getInt(1)).thenReturn(1);

        // Then
        assertEquals(1, clientDao.create(initClient(new Date(0))));
    }

    @Test
    public void create_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(CREATE_CLIENT_QUERY, true)).thenReturn(stmt);
        when(this.stmt.getGeneratedKeys()).thenReturn(result);
        when(this.result.next()).thenReturn(false);

        // Then
        assertThrows(DaoException.class, () -> clientDao.create(initClient(new Date(0))));
    }

    @Test
    public void create_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(CREATE_CLIENT_QUERY, true)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> clientDao.create(initClient(new Date(0))));
    }

    @Test
    public void update_should_return_integer() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(1);

        // Then
        assertEquals(1, clientDao.update(initClient(new Date(0))));
    }

    @Test
    public void update_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(2);

        // Then
        assertThrows(DaoException.class, () -> clientDao.update(initClient(new Date(0))));
    }

    @Test
    public void update_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(UPDATE_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> clientDao.update(initClient(new Date(0))));
    }

    @Test
    public void delete_should_return_integer() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(1);

        // Then
        assertEquals(1, clientDao.delete(initClient(new Date(0))));
    }

    @Test
    public void deletee_should_return_dao_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenReturn(2);

        // Then
        assertThrows(DaoException.class, () -> clientDao.delete(initClient(new Date(0))));
    }

    @Test
    public void delete_should_return_dao_exception_when_sql_exception() throws DaoException, SQLException {
        // When
        when(this.daoOutils.initPreparedStatement(DELETE_CLIENT_QUERY, false)).thenReturn(stmt);
        when(this.stmt.executeUpdate()).thenThrow(SQLException.class);

        // Then
        assertThrows(DaoException.class, () -> clientDao.delete(initClient(new Date(0))));
    }

    public void initValidConnection(String query, Date date, boolean isGeneratedKeys) throws SQLException {
        when(this.daoOutils.initPreparedStatement(query, isGeneratedKeys)).thenReturn(stmt);
        when(this.stmt.executeQuery()).thenReturn(result);
        when(this.result.next()).thenReturn(true).thenReturn(false);
        when(this.result.getInt("id")).thenReturn(1);
        when(this.result.getString("nom")).thenReturn("Nom");
        when(this.result.getString("prenom")).thenReturn("Prenom");
        when(this.result.getString("email")).thenReturn("Email");
        when(this.result.getDate("naissance")).thenReturn(date);
    }

    public Client initClient(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Client client = new Client(1, "Nom", "Prenom", "Email", LocalDate.parse(date.toString(), formatter));
        return client;
    }
}
