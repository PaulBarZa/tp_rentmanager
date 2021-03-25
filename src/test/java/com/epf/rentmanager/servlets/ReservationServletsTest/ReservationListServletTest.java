package com.epf.rentmanager.servlets.ReservationServletsTest;

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
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.ui.servlets.ReservationServlets.ReservationListServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReservationListServletTest {
    @InjectMocks
    private ReservationListServlet reservationListServlet;

    @Mock
    private ClientService clientService;
    @Mock
    private VehiculeService vehiculeService;
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
    public void doGet_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse("10/10/2010", formatter);
        LocalDate endDate = LocalDate.parse("11/10/2010", formatter);

        Reservation reservation = new Reservation(1, 2, 2, startDate, endDate);
        Client client = new Client(0, "Nom", "Prenom", null, startDate);
        Vehicule vehicule = new Vehicule(0, "Constructeur", "Modele", 4);

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        // When
        when(request.getRequestDispatcher("WEB-INF/views/rents/list.jsp")).thenReturn(dispatcher);
        when(this.reservationService.findAll()).thenReturn(reservationList);
        when(this.clientService.findById(2)).thenReturn(client);
        when(this.vehiculeService.findById(2)).thenReturn(vehicule);

        // Then
        reservationListServlet.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException, ServiceException {
        // When
        when(request.getRequestDispatcher("WEB-INF/views/rents/list.jsp")).thenReturn(dispatcher);
        when(this.reservationService.findAll()).thenThrow(ServiceException.class);

        // Then
        assertThrows(ServletException.class, () -> reservationListServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
