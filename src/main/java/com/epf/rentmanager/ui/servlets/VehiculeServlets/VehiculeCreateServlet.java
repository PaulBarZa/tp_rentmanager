package com.epf.rentmanager.ui.servlets.VehiculeServlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.ui.servlets.VehiculeServlets.utils.VehiculeServletsUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/cars/create")
public class VehiculeCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private VehiculeService vehiculeService;

    @Override
    /**
     * Override of the init HttpServlet init method
     * 
     * @throws ServletException
     */
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Servlet for the http get request on the /cars/create endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Servlet for the http post request on the /cars/create endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            vehiculeService.create(VehiculeServletsUtils.requestToVehicule(request, false));
        } catch (final Exception e) {
            throw new ServletException(e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/cars");
    }
}
