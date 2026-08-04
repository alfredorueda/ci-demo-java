package com.example.portfoliodemo;

/** Base class for all portfolio domain errors. */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
