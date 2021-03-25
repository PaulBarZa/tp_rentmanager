package com.epf.rentmanager.servlets.VehiculeServletsTest;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.ui.servlets.VehiculeServlets.VehiculeListServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehiculeListServletTest {
    @InjectMocks
    private VehiculeListServlet vehiculeListServlet;

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
        // Given
        List<Vehicule> vehiculeList = new ArrayList<>();
        Vehicule vehicule = new Vehicule(1, "Foo", "Bar", 4);
        vehiculeList.add(vehicule);

        // When
        when(request.getRequestDispatcher("WEB-INF/views/vehicles/list.jsp")).thenReturn(dispatcher);
        when(this.vehiculeService.findAll()).thenReturn(vehiculeList);

        // Then
        vehiculeListServlet.doGet(request, response);
        verify(request).setAttribute("vehicules", vehiculeList);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doGet_method_shoud_return_error() throws ServletException, IOException, ServiceException {
        // When
        when(request.getRequestDispatcher("WEB-INF/views/vehicles/list.jsp")).thenReturn(dispatcher);
        when(this.vehiculeService.findAll()).thenThrow(ServiceException.class);

        // Then
        assertThrows(ServletException.class, () -> vehiculeListServlet.doGet(request, response));
        verify(dispatcher, times(0)).forward(request, response);
    }
}
