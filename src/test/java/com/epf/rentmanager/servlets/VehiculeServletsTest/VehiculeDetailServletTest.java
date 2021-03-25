package com.epf.rentmanager.servlets.VehiculeServletsTest;

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
import com.epf.rentmanager.ui.servlets.VehiculeServlets.VehiculeDetailServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehiculeDetailServletTest {
    @InjectMocks
    private VehiculeDetailServlet vehiculeDetailServlet;

    @Mock
    private VehiculeService vehiculeService;
    @Mock
    private ClientService clientService;
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
        List<Reservation> reservationList = new ArrayList<>();
        List<Client> clients = new ArrayList<Client>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse("10/10/2010", formatter);
        LocalDate endDate = LocalDate.parse("11/10/2010", formatter);
        Client client = new Client(2, "Nom", "Prenom", "Email", startDate);
        Vehicule vehicule = new Vehicule(0, "Foo", "Bar", 4);
        Reservation reservation = new Reservation(1, 2, 0, startDate, endDate);

        reservationList.add(reservation);
        clients.add(client);

        // When
        when(request.getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.vehiculeService.findById(0)).thenReturn(vehicule);
        when(this.reservationService.findResaByVehicleId(0)).thenReturn(reservationList);
        when(this.clientService.findById(2)).thenReturn(client);

        // Then
        vehiculeDetailServlet.doGet(request, response);
        verify(request).setAttribute("vehicule", vehicule);
        verify(request).setAttribute("clients", clients);
        verify(request).setAttribute("reservations_count", 1);
        verify(request).setAttribute("clients_count", 1);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp")).thenReturn(dispatcher);

        // Then
        assertThrows(ServletException.class, () -> vehiculeDetailServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
