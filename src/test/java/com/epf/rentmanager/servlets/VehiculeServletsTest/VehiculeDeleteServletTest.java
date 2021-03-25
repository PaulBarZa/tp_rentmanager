package com.epf.rentmanager.servlets.VehiculeServletsTest;

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
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.ui.servlets.VehiculeServlets.VehiculeDeleteServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehiculeDeleteServletTest {

    @InjectMocks
    private VehiculeDeleteServlet vehiculeDeleteServlet;

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
    public void doGet_method_shoud_work() throws ServletException, IOException, ServiceException {
        // Given
        Vehicule vehicule = new Vehicule();

        // When
        when(request.getRequestDispatcher("/WEB-INF/views/vehicles/delete.jsp")).thenReturn(dispatcher);
        when(request.getParameter("id")).thenReturn("0");
        when(this.vehiculeService.findById(0)).thenReturn(vehicule);
        when(this.vehiculeService.delete(vehicule)).thenReturn(1);

        // Then
        vehiculeDeleteServlet.doGet(request, response);
        verify(request).setAttribute("vehicule", vehicule);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException {
        // When
        when(request.getRequestDispatcher("/WEB-INF/views/vehicles/delete.jsp")).thenReturn(dispatcher);

        // Then
        assertThrows(ServletException.class, () -> vehiculeDeleteServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
