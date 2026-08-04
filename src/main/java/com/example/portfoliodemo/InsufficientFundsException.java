package com.example.portfoliodemo;

/** Raised when a purchase would cost more than the available cash balance. */
public class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
