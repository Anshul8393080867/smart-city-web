package com.city.dao;

public class DataAccessException extends Exception {

    // Preserves custom error context along with the underlying cause
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    // Allows throwing data-tier validation or missing-record errors without an underlying SQLException
    public DataAccessException(String message) {
        super(message);
    }
}
