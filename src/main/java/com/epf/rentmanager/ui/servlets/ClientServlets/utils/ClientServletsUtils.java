package com.epf.rentmanager.ui.servlets.ClientServlets.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import com.epf.rentmanager.model.Client;

public class ClientServletsUtils {

    /**
     * Transform a request to a client
     * 
     * @param request
     * @param isUpdate
     * @return client
     * @throws ParseException
     */
    public static Client requestToClient(HttpServletRequest request, boolean isUpdate) {

        Client client = new Client();
        client.setNom(request.getParameter("last_name"));
        client.setPrenom(request.getParameter("first_name"));
        client.setEmail(request.getParameter("email"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        client.setNaissance(LocalDate.parse(request.getParameter("birthday"), formatter));

        if (isUpdate) {
            client.setId(Integer.parseInt(request.getParameter("id")));
        }

        return client;
    }
}
