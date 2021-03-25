package com.epf.rentmanager.servlets.ClientServletsTest;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.ui.servlets.ClientServlets.ClientCreateServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClientCreateServletTest {

    @InjectMocks
    private ClientCreateServlet clientCreateServlet;

    @Mock
    private ClientService clientService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void doGet_method_should_work() throws ServletException, IOException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/users/create.jsp")).thenReturn(dispatcher);

        // Then
        clientCreateServlet.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doPost_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("10/10/2010", formatter);
        Client client = new Client(0, "Nom", "Prenom", "Email", date);

        // When
        when(request.getParameter("last_name")).thenReturn("Nom");
        when(request.getParameter("first_name")).thenReturn("Prenom");
        when(request.getParameter("email")).thenReturn("Email");
        when(request.getParameter("birthday")).thenReturn("10/10/2010");
        when(this.clientService.create(refEq(client))).thenReturn(1);
        when(request.getContextPath()).thenReturn("rentmanager");

        // Then
        clientCreateServlet.doPost(request, response);
        verify(this.clientService).create(refEq(client));
        verify(response).sendRedirect("rentmanager/users");
    }

    @Test
    public void doPost_method_shoud_return_error() throws ServletException, IOException {
        // Then
        assertThrows(ServletException.class, () -> clientCreateServlet.doPost(request, response));
        verify(response, times(0)).sendRedirect("rentmanager/cars");
    }
}
