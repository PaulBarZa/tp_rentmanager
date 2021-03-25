package com.epf.rentmanager.exception;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * ServiceException class constructor
     * 
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

}
