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
import com.epf.rentmanager.ui.servlets.ReservationServlets.ReservationUpdateServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReservationUpdateServletTest {
    @InjectMocks
    private ReservationUpdateServlet reservationUpdateServlet;

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
    private LocalDate startDate;
    private LocalDate endDate;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        startDate = LocalDate.parse("10/10/2010", formatter);
        endDate = LocalDate.parse("11/10/2010", formatter);

    }

    @Test
    public void doGet_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        List<Client> clientList = new ArrayList<>();
        List<Vehicule> vehiculeList = new ArrayList<>();

        Reservation reservation = new Reservation(0, 0, 2, this.startDate, this.endDate);
        Client client = new Client(0, "Nom", "Prenom", null, this.startDate);
        Vehicule vehicule = new Vehicule(0, "Constructeur", "Modele", 4);

        clientList.add(client);
        vehiculeList.add(vehicule);

        // When
        when(request.getRequestDispatcher("/WEB-INF/views/rents/update.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.reservationService.findById(0)).thenReturn(reservation);
        when(this.vehiculeService.findAll()).thenReturn(vehiculeList);
        when(this.clientService.findAll()).thenReturn(clientList);

        // Then
        reservationUpdateServlet.doGet(request, response);
        verify(request).setAttribute("reserva", reservation);
        verify(request).setAttribute("vehicules", vehiculeList);
        verify(request).setAttribute("clients", clientList);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException, ServiceException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/rents/update.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.reservationService.findById(0)).thenThrow(ServiceException.class);

        // Then
        assertThrows(ServletException.class, () -> reservationUpdateServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }

    @Test
    public void doPost_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        Reservation reservation = new Reservation(0, 0, 2, this.startDate, this.endDate);

        // When
        when(request.getParameter("car")).thenReturn("2");
        when(request.getParameter("client")).thenReturn("0");
        when(request.getParameter("begin")).thenReturn("2010-10-10");
        when(request.getParameter("end")).thenReturn("2010-10-11");
        when(request.getParameter("id")).thenReturn("0");
        when(this.reservationService.update(refEq(reservation))).thenReturn(1);
        when(request.getContextPath()).thenReturn("rentmanager");

        // Then
        reservationUpdateServlet.doPost(request, response);
        verify(this.reservationService).update(refEq(reservation));
        verify(response).sendRedirect("rentmanager/rents");
    }

    @Test
    public void doPost_method_shoud_return_error() throws ServletException, IOException {
        // Then
        assertThrows(ServletException.class, () -> reservationUpdateServlet.doPost(request, response));
        verify(response, times(0)).sendRedirect("rentmanager/rents");
    }
}
