package com.epf.rentmanager.ui.servlets.ReservationServlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.ReservationDetail;
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehiculeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/rents")
public class ReservationListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;
    @Autowired
    private VehiculeService vehiculeService;
    @Autowired
    private ReservationService reservationService;

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
     * Servlet for the http get request on the /rents endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/rents/list.jsp");
        try {

            List<Reservation> reservations = reservationService.findAll();
            List<ReservationDetail> reservationsDetail = new ArrayList<ReservationDetail>();

            for (Reservation reservation : reservations) {

                Client client = clientService.findById(reservation.getClient_id());
                Vehicule vehicule = vehiculeService.findById(reservation.getVehicule_id());
                reservationsDetail
                        .add(new ReservationDetail(reservation.getId(), client.getNom() + " " + client.getPrenom(),
                                vehicule.getConstructeur() + " " + vehicule.getModele(), reservation.getDebut(),
                                reservation.getFin()));

            }
            request.setAttribute("reservations", reservationsDetail);

        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        dispatcher.forward(request, response);
    }
}
