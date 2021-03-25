package com.epf.rentmanager.servlets;

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
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.ui.servlets.HomeServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit test for home servlet.
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeServletTest {

    @InjectMocks
    private HomeServlet homeServlet;

    @Mock
    private ClientService clientService;
    @Mock
    private ReservationService reservationService;
    @Mock
    private VehiculeService vehiculeService;

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
    public void doGet_method_should_work() throws ServiceException, ServletException, IOException {
        // When
        when(request.getRequestDispatcher("WEB-INF/views/home.jsp")).thenReturn(dispatcher);
        when(this.clientService.count()).thenReturn(1);
        when(this.reservationService.count()).thenReturn(2);
        when(this.vehiculeService.count()).thenReturn(3);

        // Then
        homeServlet.doGet(request, response);
        verify(request).getRequestDispatcher("WEB-INF/views/home.jsp");
        verify(request).setAttribute("client_count", 1);
        verify(request).setAttribute("reservation_count", 2);
        verify(request).setAttribute("vehicule_count", 3);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_should_return_exception() throws ServiceException, ServletException, IOException {
        // When
        when(request.getRequestDispatcher("WEB-INF/views/home.jsp")).thenReturn(dispatcher);
        when(this.clientService.count()).thenThrow(ServiceException.class);

        // Then
        assertThrows(ServletException.class, () -> homeServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
