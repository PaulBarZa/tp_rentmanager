package com.epf.rentmanager.servlets.ClientServletsTest;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.ui.servlets.ClientServlets.ClientListServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClientListServletTest {
    @InjectMocks
    private ClientListServlet clientListServlet;

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
    public void doGet_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("10/10/2010", formatter);
        Client client = new Client(1, "Foo", "Bar", "email", date);

        List<Client> clientList = new ArrayList<>();
        clientList.add(client);

        // When
        when(request.getRequestDispatcher("WEB-INF/views/users/list.jsp")).thenReturn(dispatcher);
        when(this.clientService.findAll()).thenReturn(clientList);

        // Then
        clientListServlet.doGet(request, response);
        verify(request).setAttribute("clients", clientList);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException, ServiceException {
        // When
        when(request.getRequestDispatcher("WEB-INF/views/users/list.jsp")).thenReturn(dispatcher);
        when(this.clientService.findAll()).thenThrow(ServiceException.class);

        // Then
        assertThrows(ServletException.class, () -> clientListServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
