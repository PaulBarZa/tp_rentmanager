package com.epf.rentmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit test for client service.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientDao clientDao;
    @Mock
    private ReservationDao reservationDao;

    public Client initValidClient() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("10/10/1990", formatter);

        return new Client(1, "Foo1", "Bar1", "foo1@bar.com", date);
    }

    @Test
    public void count_should_return_integer_when_dao_return_integer() throws ServiceException, DaoException {
        // When
        when(this.clientDao.count()).thenReturn(1);

        // Then
        assertEquals(1, clientService.count());
    }

    @Test
    public void count_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.clientDao.count()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.count());
    }

    @Test
    public void findById_should_return_integer_when_dao_return_integer() throws ServiceException, DaoException {
        // When
        Client client = initValidClient();
        when(this.clientDao.findById(1)).thenReturn(Optional.of(client));

        // Then
        assertEquals(client, clientService.findById(1));
    }

    @Test
    public void findById_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.clientDao.findById(1)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.findById(1));
    }

    @Test
    public void findAll_should_fail_when_dao_throws_exception() throws DaoException {
        // When
        when(this.clientDao.findAll()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.findAll());
    }

    @Test
    public void findAll_should_return_clients_list_when_dao_return_clients_list()
            throws DaoException, ServiceException {
        // Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("10/10/1990", formatter);

        List<Client> clients = new ArrayList<>();
        clients.add(initValidClient());
        clients.add(new Client(2, "Foo2", "Bar2", "foo2@bar.com", date));

        // When
        when(this.clientDao.findAll()).thenReturn(clients);

        // Then
        assertEquals(clients, clientService.findAll());
    }

    @Test
    public void create_client_should_return_integer() throws ServiceException, DaoException {
        // When
        Client client = initValidClient();
        when(this.clientDao.create(client)).thenReturn(1);

        // Then
        assertEquals(1, clientService.create(client));
    }

    @Test
    public void create_client_should_throws_exception() throws DaoException {
        // When
        Client client = initValidClient();
        when(this.clientDao.create(client)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.create(client));
    }

    @Test
    public void update_client_should_return_integer() throws ServiceException, DaoException {
        // When
        Client client = initValidClient();
        when(this.clientDao.update(client)).thenReturn(1);

        // Then
        assertEquals(1, clientService.update(client));
    }

    @Test
    public void update_client_should_throws_exception() throws DaoException {
        // When
        Client client = initValidClient();
        when(this.clientDao.update(client)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.update(client));
    }

    @Test
    public void delete_client_should_return_integer() throws ServiceException, DaoException {
        // When
        Client client = initValidClient();
        when(this.clientDao.delete(client)).thenReturn(1);

        // Then
        assertEquals(1, clientService.delete(client));
        verify(this.reservationDao, times(1)).clientDeleted(client.getId());
    }

    @Test
    public void delete_client_should_throws_exception() throws DaoException {
        // When
        Client client = initValidClient();
        when(this.clientDao.delete(client)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.delete(client));
        verify(this.reservationDao, times(1)).clientDeleted(client.getId());
    }
}
