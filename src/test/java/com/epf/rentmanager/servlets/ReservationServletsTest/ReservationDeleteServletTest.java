package com.epf.rentmanager.servlets.ReservationServletsTest;

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
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.ui.servlets.ReservationServlets.ReservationDeleteServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReservationDeleteServletTest {

    @InjectMocks
    private ReservationDeleteServlet reservationDeleteServlet;

    @Mock
    private ReservationService reservationService;

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
        Reservation reservation = new Reservation();

        // When
        when(request.getRequestDispatcher("/WEB-INF/views/rents/delete.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.reservationService.findById(0)).thenReturn(reservation);
        when(this.reservationService.delete(reservation)).thenReturn(1);

        // Then
        reservationDeleteServlet.doGet(request, response);
        verify(request).setAttribute("reservation", reservation);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/rents/delete.jsp")).thenReturn(dispatcher);

        // Then
        assertThrows(ServletException.class, () -> reservationDeleteServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
