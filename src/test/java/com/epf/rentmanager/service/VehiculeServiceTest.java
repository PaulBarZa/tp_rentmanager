package com.epf.rentmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehiculeDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehiculeServiceTest {

    @InjectMocks
    private VehiculeService vehiculeService;

    @Mock
    private VehiculeDao vehiculeDao;
    @Mock
    private ReservationDao reservationDao;

    public Vehicule initValidVehicule() {
        return new Vehicule(1, "Foo", "Bar", 4);
    }

    @Test
    public void count_should_return_integer_when_dao_return_integer() throws ServiceException, DaoException {
        // When
        when(this.vehiculeDao.count()).thenReturn(1);

        // Then
        assertEquals(1, vehiculeService.count());
    }

    @Test
    public void count_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.vehiculeDao.count()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> vehiculeService.count());
    }

    @Test
    public void findById_should_return_integer_when_dao_return_integer() throws ServiceException, DaoException {
        // When
        Vehicule vehicule = initValidVehicule();
        when(this.vehiculeDao.findById(1)).thenReturn(Optional.of(vehicule));

        // Then
        assertEquals(vehicule, vehiculeService.findById(1));
    }

    @Test
    public void findById_should_throws_exception_when_dao_throws_exception() throws DaoException {
        // When
        when(this.vehiculeDao.findById(1)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> vehiculeService.findById(1));
    }

    @Test
    public void findAll_should_fail_when_dao_throws_exception() throws DaoException {
        // When
        when(this.vehiculeDao.findAll()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> vehiculeService.findAll());
    }

    @Test
    public void findAll_should_return_vehicules_list_when_dao_return_vehicules_list()
            throws DaoException, ServiceException {
        // Given
        List<Vehicule> vehicules = new ArrayList<>();
        vehicules.add(initValidVehicule());
        vehicules.add(new Vehicule(2, "Foo2", "Bar2", 4));

        // When
        when(this.vehiculeDao.findAll()).thenReturn(vehicules);

        // Then
        assertEquals(vehicules, vehiculeService.findAll());
    }

    @Test
    public void create_vehicule_should_return_integer() throws ServiceException, DaoException {
        // When
        Vehicule vehicule = initValidVehicule();
        when(this.vehiculeDao.create(vehicule)).thenReturn(1);

        // Then
        assertEquals(1, vehiculeService.create(vehicule));
    }

    @Test
    public void create_vehicule_should_throws_exception() throws DaoException {
        // When
        Vehicule vehicule = initValidVehicule();
        when(this.vehiculeDao.create(vehicule)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> vehiculeService.create(vehicule));
    }

    @Test
    public void update_vehicule_should_return_integer() throws ServiceException, DaoException {
        // When
        Vehicule vehicule = initValidVehicule();
        when(this.vehiculeDao.update(vehicule)).thenReturn(1);

        // Then
        assertEquals(1, vehiculeService.update(vehicule));
    }

    @Test
    public void update_vehicule_should_throws_exception() throws DaoException {
        // When
        Vehicule vehicule = initValidVehicule();
        when(this.vehiculeDao.update(vehicule)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> vehiculeService.update(vehicule));
    }

    @Test
    public void delete_vehicule_should_return_integer() throws ServiceException, DaoException {
        // When
        Vehicule vehicule = initValidVehicule();
        when(this.vehiculeDao.delete(vehicule)).thenReturn(1);

        // Then
        assertEquals(1, vehiculeService.delete(vehicule));
        verify(this.reservationDao, times(1)).vehiculeDeleted(vehicule.getId());
    }

    @Test
    public void delete_vehicule_should_throws_exception() throws DaoException {
        // When
        Vehicule vehicule = initValidVehicule();
        when(this.vehiculeDao.delete(vehicule)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> vehiculeService.delete(vehicule));
        verify(this.reservationDao, times(1)).vehiculeDeleted(vehicule.getId());
    }
}
