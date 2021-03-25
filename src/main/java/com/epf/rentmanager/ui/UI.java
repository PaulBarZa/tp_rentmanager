package com.epf.rentmanager.ui;

import java.time.LocalDate;
import java.util.List;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicule;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehiculeService;
import com.epf.rentmanager.utils.IOUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UI {

    private static ClientService clientService;
    private static VehiculeService vehiculeService;
    private static ReservationService reservationService;
    private ApplicationContext context;

    private UI() {
        context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        UI.clientService = context.getBean(ClientService.class);
        UI.vehiculeService = context.getBean(VehiculeService.class);
        UI.reservationService = context.getBean(ReservationService.class);
    }

    public static void main(String[] args) throws ServiceException {
        String leave = "y";

        while (leave.equals("y")) {
            new UI();

            System.out.println("Hello ! Please, choose one of these options:");
            System.out.println("");
            System.out.println("Client part");
            System.out.println(" - 1: List clients");
            System.out.println(" - 2: Find a client");
            System.out.println(" - 3: Create a client");
            System.out.println(" - 4: Update a client");
            System.out.println(" - 5: Delete a client");
            System.out.println("");
            System.out.println("Vehicule part");
            System.out.println(" - 6: List vehicules");
            System.out.println(" - 7: Find a vehicule");
            System.out.println(" - 8: Create a vehicule");
            System.out.println(" - 9: Update a vehicule");
            System.out.println(" - 10: Delete a vehicule");
            System.out.println("");
            System.out.println("Reservation part");
            System.out.println(" - 11: List reservations");
            System.out.println(" - 12: List reservations by vehicule id");
            System.out.println(" - 13: List reservations by client id");
            System.out.println(" - 14: Create a reservation");
            System.out.println(" - 15: Update a reservation");
            System.out.println(" - 16: Delete a reservation");

            int option = IOUtils.readInt("");

            switch (option) {

            case 1:
                System.out.println(" - All clients:");
                System.out.println("");

                List<Client> clients = clientService.findAll();
                for (Client client : clients) {
                    System.out.println(client);
                }
                break;

            case 2:
                int id = IOUtils.readInt("Please, enter the client ID: ");

                Client findClient = clientService.findById(id);

                System.out.println(findClient);
                break;

            case 3:
                int ID = clientService.findAll().size() + 1;
                String name = IOUtils.readString("Please, enter the client name: ", true);
                String surname = IOUtils.readString("Please, enter the client surname: ", true);
                String mail = IOUtils.readString("Please, enter the client mail: ", true);
                LocalDate date = IOUtils.readDate("Please, enter birthday date: ", true);

                Client customClient = new Client(ID, name, surname, mail, date);
                clientService.create(customClient);
                break;

            case 4:
                int clientId = IOUtils.readInt("Please, enter the client id : ");
                String updatedName = IOUtils.readString("Please, enter the updated client name: ", true);
                String updatedSurname = IOUtils.readString("Please, enter the updated client surname: ", true);
                String updatedMail = IOUtils.readString("Please, enter the updated client mail: ", true);
                LocalDate updatedDate = IOUtils.readDate("Please, enter the updated birthday date: ", true);

                Client updatedClient = new Client(clientId, updatedName, updatedSurname, updatedMail, updatedDate);
                clientService.update(updatedClient);
                break;

            case 5:
                int idClient = IOUtils.readInt("Please, enter the client ID: ");

                Client deleteClient = clientService.findById(idClient);

                clientService.delete(deleteClient);

                System.out.println("You have just deleted this client : ");
                System.out.println(deleteClient);
                break;

            case 6:
                System.out.println(" - All vehicules:");
                System.out.println("");

                List<Vehicule> vehicules = vehiculeService.findAll();
                for (Vehicule vehicule : vehicules) {
                    System.out.println(vehicule);
                }
                break;

            case 7:
                int vehiculeId = IOUtils.readInt("Please, enter the vehicule ID: ");

                Vehicule findVehicule = vehiculeService.findById(vehiculeId);

                System.out.println(findVehicule);
                break;

            case 8:
                int vehiculeID = vehiculeService.findAll().size() + 1;
                String constructeur = IOUtils.readString("Please, enter the vehicule constructeur: ", true);
                String modele = IOUtils.readString("Please, enter the vehicule modele: ", true);
                int nb_places = IOUtils.readInt("Please, enter the number of places: ");

                Vehicule vehicule = new Vehicule(vehiculeID, constructeur, modele, nb_places);
                vehiculeService.create(vehicule);
                break;

            case 9:
                int vehicleId = IOUtils.readInt("Please, enter the vehicule id : ");
                String updatedConstructeur = IOUtils.readString("Please, enter the updated vehicule constructeur: ",
                        true);
                String updatedModele = IOUtils.readString("Please, enter the updated vehicule modele: ", true);
                int updatedSeats = IOUtils.readInt("Please, enter the updated vehicule seats: ");

                Vehicule updatedVehicule = new Vehicule(vehicleId, updatedConstructeur, updatedModele, updatedSeats);
                vehiculeService.update(updatedVehicule);
                break;

            case 10:
                int idVehicule = IOUtils.readInt("Please, enter the vehicule ID: ");

                Vehicule deleteVehicule = vehiculeService.findById(idVehicule);

                vehiculeService.delete(deleteVehicule);

                System.out.println("You have just deleted this vehicule : ");
                System.out.println(deleteVehicule);
                break;

            case 11:
                System.out.println(" - All reservations:");
                System.out.println("");

                List<Reservation> reservations = reservationService.findAll();
                for (Reservation reservation : reservations) {
                    System.out.println(reservation);
                }
                break;

            case 12:
                int searchVehiculeId = IOUtils.readInt("Please, enter the vehicule ID: ");

                List<Reservation> reservaByVehicule = reservationService.findResaByVehicleId(searchVehiculeId);
                for (Reservation reservation : reservaByVehicule) {
                    System.out.println(reservation);
                }
                break;

            case 13:
                int searchClientId = IOUtils.readInt("Please, enter the client ID: ");

                List<Reservation> reservaByClient = reservationService.findResaByClientId(searchClientId);
                for (Reservation reservation : reservaByClient) {
                    System.out.println(reservation);
                }
                break;

            case 14:
                int creationID = reservationService.findAll().size() + 1;
                int client_id = IOUtils.readInt("Please, enter the reservation client id: ");
                int vehicule_id = IOUtils.readInt("Please, enter the reservation vehicule id: ");
                LocalDate debut = IOUtils.readDate("Please, enter the starting date: ", true);
                LocalDate fin = IOUtils.readDate("Please, enter the ending date: ", true);

                Reservation reserva = new Reservation(creationID, client_id, vehicule_id, debut, fin);
                reservationService.create(reserva);
                break;

            case 15:
                int reservaId = IOUtils.readInt("Please, enter the reservation id : ");
                int updatedClientId = IOUtils.readInt("Please, enter the updated reservation client id: ");
                int updatedVehiculeId = IOUtils.readInt("Please, enter the updated reservation vehicule id: ");
                LocalDate updatedBegin = IOUtils.readDate("Please, enter the updated reservation beginning: ", true);
                LocalDate updatedEnd = IOUtils.readDate("Please, enter the updated reservation end: ", true);

                Reservation updatedReserva = new Reservation(reservaId, updatedClientId, updatedVehiculeId,
                        updatedBegin, updatedEnd);
                reservationService.update(updatedReserva);
                break;

            case 16:
                int idReserva = IOUtils.readInt("Please, enter the reservation ID: ");

                Reservation deleteReserva = reservationService.findById(idReserva);

                reservationService.delete(deleteReserva);

                System.out.println("You have just deleted this reservation : ");
                System.out.println(deleteReserva);
                break;

            default:
                System.out.println(" - You must enter an integer between 1 and 8 !");
            }

            leave = IOUtils.readString(" - Do you want to do something else ? (y/n):", true);
        }
        ;
    }
}
