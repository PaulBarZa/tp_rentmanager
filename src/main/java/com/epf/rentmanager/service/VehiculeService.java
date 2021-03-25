package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehiculeDao;

@Service
public class VehiculeService {

    private VehiculeDao vehiculeDao;
    private ReservationDao reservationDao;

    @Autowired
    public VehiculeService(VehiculeDao vehiculeDao, ReservationDao reservationDao) {
        this.vehiculeDao = vehiculeDao;
        this.reservationDao = reservationDao;
    }

    /**
     * Count vehicules in Vehicule table
     * 
     * @return number of vehicules
     * @throws ServiceException
     */
    public int count() throws ServiceException {
        try {
            return vehiculeDao.count();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Find a vehicule in Vehicule table think his id
     * 
     * @param id
     * @return vehicule corresponding to the id
     * @throws ServiceException
     */
    public Vehicule findById(long id) throws ServiceException {
        try {
            return vehiculeDao.findById(id).get();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Return all vehicules from Vehicule table
     * 
     * @return list of vehicules
     * @throws ServiceException
     */
    public List<Vehicule> findAll() throws ServiceException {
        try {
            return vehiculeDao.findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Create a vehicule in Vehicule table
     * 
     * @param vehicule
     * @return vehicule key
     * @throws ServiceException
     */
    public int create(Vehicule vehicule) throws ServiceException {
        try {
            isVehiculeValid(vehicule);
            return vehiculeDao.create(vehicule);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Update a vehicule in vVhicule table
     * 
     * @param vehicule
     * @return vehicule key
     * @throws ServiceException
     */
    public int update(Vehicule vehicule) throws ServiceException {
        try {
            isVehiculeValid(vehicule);
            return vehiculeDao.update(vehicule);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Delete a vehicule in Vehicule table
     * 
     * @param vehicule
     * @return statement status (1 for ok, other value for error/empty)
     * @throws ServiceException
     */
    public int delete(Vehicule vehicule) throws ServiceException {
        try {
            reservationDao.vehiculeDeleted(vehicule.getId());
            return vehiculeDao.delete(vehicule);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Check if a vehicule is valid or not
     * 
     * @param vehicule
     * @return boolean depends on vehicule validity
     * @throws ServiceException
     */
    public static boolean isVehiculeValid(Vehicule vehicule) throws ServiceException {

        if (vehicule.getConstructeur().trim().isEmpty() || vehicule.getModele().trim().isEmpty()) {
            throw new ServiceException("A vehicule should have a constructeur and a modele");
        }

        if (vehicule.getNb_places() < 2 || vehicule.getNb_places() > 9) {
            throw new ServiceException("A vehicule should have between 2 and 9 places");
        }

        return true;
    }

}
