package com.epf.rentmanager.servlets.VehiculeServletsTest;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.refEq;
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
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.ui.servlets.VehiculeServlets.VehiculeUpdateServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehiculeUpdateServletTest {
    @InjectMocks
    private VehiculeUpdateServlet vehiculeUpdateServlet;

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
    public void doGet_method_should_work() throws ServletException, IOException, ServiceException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/vehicles/update.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.vehiculeService.findById(0)).thenReturn(new Vehicule(0, "Foo", "Bar", 4));

        // Then
        vehiculeUpdateServlet.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException, ServiceException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/vehicles/update.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.vehiculeService.findById(0)).thenThrow(ServiceException.class);

        // Then
        assertThrows(ServletException.class, () -> vehiculeUpdateServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }

    @Test
    public void doPost_method_should_work() throws ServletException, IOException, ServiceException {
        // Given
        Vehicule vehicule = new Vehicule(0, "Foo", "Bar", 4);

        // When
        when(request.getParameter("manufacturer")).thenReturn("Foo");
        when(request.getParameter("modele")).thenReturn("Bar");
        when(request.getParameter("seats")).thenReturn("4");
        when(request.getParameter("id")).thenReturn("0");
        when(this.vehiculeService.update(refEq(vehicule))).thenReturn(1);
        when(request.getContextPath()).thenReturn("rentmanager");

        // Then
        vehiculeUpdateServlet.doPost(request, response);
        verify(this.vehiculeService).update(refEq(vehicule));
        verify(response).sendRedirect("rentmanager/cars");
    }

    @Test
    public void doPost_method_shoud_return_error() throws ServletException, IOException {
        // Then
        assertThrows(ServletException.class, () -> vehiculeUpdateServlet.doPost(request, response));
        verify(response, times(0)).sendRedirect("rentmanager/cars");
    }
}
