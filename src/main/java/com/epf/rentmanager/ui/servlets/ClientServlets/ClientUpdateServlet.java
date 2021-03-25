package com.epf.rentmanager.ui.servlets.ClientServlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.ui.servlets.ClientServlets.utils.ClientServletsUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/users/update")
public class ClientUpdateServlet extends HttpServlet {
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
     * Servlet for the http get request on the /users/update endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/users/update.jsp");

        try {

            int clientId = Integer.parseInt(request.getParameter("id"));
            Client client = clientService.findById(clientId);
            request.setAttribute("client", client);
            request.setAttribute("birthday", client.transformNaissance());

        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }

        dispatcher.forward(request, response);
    }

    /**
     * Servlet for the http post request on the /users/update endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            clientService.update(ClientServletsUtils.requestToClient(request, true));
        } catch (final Exception e) {
            throw new ServletException(e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/users");
    }

}
