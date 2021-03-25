package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;

@Service
public class ClientService {

	private ClientDao clientDao;
	private ReservationDao reservationDao;

	@Autowired
	public ClientService(ClientDao clientDao, ReservationDao reservationDao) {
		this.clientDao = clientDao;
		this.reservationDao = reservationDao;
	}

	/**
	 * Count numbers of client in Client table
	 * 
	 * @return number of clients
	 * @throws ServiceException
	 */
	public int count() throws ServiceException {
		try {
			return clientDao.count();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Find a client in Client table think his id
	 * 
	 * @param id
	 * @return An optional client corresponding with the id
	 * @throws ServiceException
	 */
	public Client findById(long id) throws ServiceException {
		try {
			return clientDao.findById(id).get();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Return all clients from Client table
	 * 
	 * @return list of clients
	 * @throws ServiceException
	 */
	public List<Client> findAll() throws ServiceException {
		try {
			return clientDao.findAll();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Create a client in Client table
	 * 
	 * @param client
	 * @return client key
	 * @throws ServiceException
	 */
	public int create(Client client) throws ServiceException {
		try {

			isClientValid(clientDao.findAll(), client);
			client.setNom(client.getNom().toUpperCase());
			return clientDao.create(client);

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Update a client in Client table
	 * 
	 * @param client
	 * @return client key
	 * @throws ServiceException
	 */
	public int update(Client client) throws ServiceException {
		try {

			isClientValid(clientDao.findAll(), client);
			client.setNom(client.getNom().toUpperCase());
			return clientDao.update(client);

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Delete a client in Client table
	 * 
	 * @param client
	 * @return statement status (1 for ok, other value for error/empty)
	 * @throws ServiceException
	 */
	public int delete(Client client) throws ServiceException {
		try {
			reservationDao.clientDeleted(client.getId());
			return clientDao.delete(client);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Vérifie qu'un client soit valide
	 * 
	 * @param clients
	 * @param client
	 * @return boolean depends of client validity
	 * @throws ServiceException
	 */
	public static boolean isClientValid(List<Client> clients, Client client) throws ServiceException {
		if (!ClientService.isValidNames(client.getNom(), client.getPrenom())) {
			throw new ServiceException("Last name and first name must have 3 characters");
		}

		if (!ClientService.isValidEmail(clients, client.getEmail())) {
			throw new ServiceException("Invalid or already existing email");
		}

		if (!ClientService.isValidAge(client.getNaissance())) {
			throw new ServiceException("Client should have 18 years old");
		}

		return true;
	}

	/**
	 * Vérifie que le nom et prénom soient conformes
	 * 
	 * @param nom
	 * @param prenom
	 * @return boolean depends of names validity
	 */
	public static boolean isValidNames(String nom, String prenom) {
		if ((nom.trim().length() < 3) || (prenom.trim().length() < 3)) {
			return false;
		}
		return true;
	}

	/**
	 * Vérifie que l'email soit conforme et unique
	 * 
	 * @param email
	 * @param clients
	 * @return boolean depends of email validity
	 */
	public static boolean isValidEmail(List<Client> clients, String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);

		if (!pattern.matcher(email).matches()) {
			return false;
		}

		for (Client client : clients) {
			if (client.getEmail() == email) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Vérifie qu'un client soit agé de 18 ans
	 * 
	 * @param date
	 * @return boolean depends of age validity
	 */
	public static boolean isValidAge(LocalDate date) {

		long ageInMillis = (new Date(System.currentTimeMillis()).getTime() - Date.valueOf(date).getTime()) / 1000;
		long eighteenYearsOldInMillis = 567600000;

		if (ageInMillis < eighteenYearsOldInMillis) {
			return false;
		}

		return true;
	}

}
