package com.epf.rentmanager.ui.servlets.ClientServlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.ui.servlets.ClientServlets.utils.ClientServletsUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/users/create")
public class ClientCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;

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
     * Servlet for the http get request on the /users/create endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/users/create.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Servlet for the http post request on the /users/create endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            clientService.create(ClientServletsUtils.requestToClient(request, false));
        } catch (final Exception e) {
            // request.setAttribute("error_message", e.getMessage());
            throw new ServletException(e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/users");
    }
}
