package com.epf.rentmanager.utils;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

import org.junit.Test;

public class ClientsTest {

    public Client initValidClient() {
        Client validClient = new Client();
        validClient.setNom("Foo");
        validClient.setPrenom("Bar");
        validClient.setEmail("Foo@bar.com");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        validClient.setNaissance(LocalDate.parse("10/10/1990", formatter));

        return validClient;
    }

    @Test
    public void isClientValid_should_return_true_with_valid_client() throws Exception {
        // Given
        Client client = initValidClient();
        List<Client> clients = new ArrayList<>();

        // Then
        assertTrue(ClientService.isClientValid(clients, client));

    }

    @Test
    public void isClientValid_should_return_false_with_invalid_firstname() {
        // Given
        Client client = initValidClient();
        Client invalidClient = client;
        invalidClient.setPrenom("fo");
        List<Client> clients = new ArrayList<>();

        // Then
        assertThrows(ServiceException.class, () -> ClientService.isClientValid(clients, invalidClient));
    }

    @Test
    public void isClientValid_should_return_false_with_invalid_lastname() {
        // Given
        Client client = initValidClient();
        Client invalidClient = client;
        invalidClient.setNom("b");
        List<Client> clients = new ArrayList<>();

        // Then
        assertThrows(ServiceException.class, () -> ClientService.isClientValid(clients, invalidClient));

    }

    @Test
    public void isClientValid_should_return_false_with_invalid_email() {
        // Given
        Client client = initValidClient();
        Client invalidClient = client;
        invalidClient.setEmail("invalidEmail");
        List<Client> clients = new ArrayList<>();

        // Then
        assertThrows(Exception.class, () -> ClientService.isClientValid(clients, invalidClient));

    }

    @Test
    public void isClientValid_should_return_false_with_existing_email() {
        // Given
        Client client = initValidClient();
        List<Client> clients = new ArrayList<>();

        Client secondClient = new Client();
        secondClient.setNom("Name");
        secondClient.setPrenom("FirestName");
        secondClient.setEmail("Foo@bar.com");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        secondClient.setNaissance(LocalDate.parse("10/10/2010", formatter));

        clients.add(secondClient);

        // Then
        assertThrows(Exception.class, () -> ClientService.isClientValid(clients, client));

    }

    @Test
    public void isClientValid_should_return_false_with_invalid_birthday() {
        // Given
        Client client = initValidClient();
        Client invalidClient = client;
        List<Client> clients = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        invalidClient.setNaissance(LocalDate.parse("10/10/2005", formatter));

        // Then
        assertThrows(Exception.class, () -> ClientService.isClientValid(clients, invalidClient));

    }

}
