package com.epf.rentmanager.ui.servlets.VehiculeServlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.VehiculeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/cars/delete")
public class VehiculeDeleteServlet extends HttpServlet {
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
     * Servlet for the http get request on the /cars/delete endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/vehicles/delete.jsp");

        try {

            int vehiculeId = Integer.parseInt(request.getParameter("id"));
            Vehicule vehicule = vehiculeService.findById(vehiculeId);
            vehiculeService.delete(vehicule);
            request.setAttribute("vehicule", vehicule);

        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        dispatcher.forward(request, response);
    }
}
