package com.epf.rentmanager.servlets.ClientServletsTest;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.ui.servlets.ClientServlets.ClientDeleteServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClientDeleteServletTest {

    @InjectMocks
    private ClientDeleteServlet clientDeleteServlet;

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
    public void doGet_method_shoud_work() throws ServletException, IOException, ServiceException {
        // Given
        Client client = new Client();

        // When
        when(request.getRequestDispatcher("/WEB-INF/views/users/delete.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.clientService.findById(0)).thenReturn(client);
        when(this.clientService.delete(client)).thenReturn(1);

        // Then
        clientDeleteServlet.doGet(request, response);
        verify(request).setAttribute("client", client);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/users/delete.jsp")).thenReturn(dispatcher);

        // Then
        assertThrows(ServletException.class, () -> clientDeleteServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
