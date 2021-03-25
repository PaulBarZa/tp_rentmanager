package com.epf.rentmanager.ui.servlets.VehiculeServlets;

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

@WebServlet("/cars/details")
public class VehiculeDetailServlet extends HttpServlet {
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
     * Servlet for the http get request on the /cars/details endpoint
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp");
        try {

            int vehiculeId = Integer.parseInt(request.getParameter("id"));
            Vehicule vehicule = vehiculeService.findById(vehiculeId);
            List<Reservation> reservations = reservationService.findResaByVehicleId(vehiculeId);
            List<Client> clients = new ArrayList<Client>();
            List<ReservationDetail> reservationDetail = new ArrayList<ReservationDetail>();

            for (Reservation reservation : reservations) {
                Client client = clientService.findById(reservation.getClient_id());
                clients.add(client);
                reservationDetail
                        .add(new ReservationDetail(reservation.getId(), client.getNom() + " " + client.getPrenom(),
                                vehicule.getConstructeur() + " " + vehicule.getModele(), reservation.getDebut(),
                                reservation.getFin()));
            }

            request.setAttribute("vehicule", vehicule);
            request.setAttribute("clients", clients);
            request.setAttribute("reservations", reservationDetail);
            request.setAttribute("reservations_count", reservationDetail.size());
            request.setAttribute("clients_count", clients.size());

        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        dispatcher.forward(request, response);
    }
}
