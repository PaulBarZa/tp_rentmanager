package com.epf.rentmanager.servlets.ReservationServletsTest;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.refEq;
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
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.ui.servlets.ReservationServlets.ReservationCreateServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReservationCreateServletTest {

    @InjectMocks
    private ReservationCreateServlet reservationCreateServlet;

    @Mock
    private ClientService clientService;
    @Mock
    private VehiculeService vehiculeService;
    @Mock
    private ReservationService reservationService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private DateTimeFormatter formatter;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Test
    public void doGet_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        List<Vehicule> vehiculeList = new ArrayList<>();
        List<Client> clientList = new ArrayList<>();
        LocalDate date = LocalDate.parse("10/10/2010", this.formatter);

        vehiculeList.add(new Vehicule(1, "Foo", "Bar", 4));
        clientList.add(new Client(1, "Foo", "Bar", "email", date));

        // When
        when(request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp")).thenReturn(dispatcher);
        when(this.vehiculeService.findAll()).thenReturn(vehiculeList);
        when(this.clientService.findAll()).thenReturn(clientList);

        // Then
        reservationCreateServlet.doGet(request, response);
        verify(request).setAttribute("vehicules", vehiculeList);
        verify(request).setAttribute("clients", clientList);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doPost_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        LocalDate startDate = LocalDate.parse("10/10/2010", this.formatter);
        LocalDate endDate = LocalDate.parse("11/10/2010", this.formatter);
        Reservation reservation = new Reservation(0, 1, 1, startDate, endDate);

        // When
        when(request.getParameter("car")).thenReturn("1");
        when(request.getParameter("client")).thenReturn("1");
        when(request.getParameter("begin")).thenReturn("2010-10-10");
        when(request.getParameter("end")).thenReturn("2010-10-11");
        when(this.reservationService.create(refEq(reservation))).thenReturn(1);
        when(request.getContextPath()).thenReturn("rentmanager");

        // Then
        reservationCreateServlet.doPost(request, response);
        verify(this.reservationService).create(refEq(reservation));
        verify(response).sendRedirect("rentmanager/rents");
    }

    @Test
    public void doPost_method_shoud_return_error() throws ServletException, IOException {
        // Then
        assertThrows(ServletException.class, () -> reservationCreateServlet.doPost(request, response));
        verify(response, times(0)).sendRedirect("rentmanager/rents");
    }
}
