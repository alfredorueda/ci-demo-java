package com.example.portfoliodemo;

/** Raised when a sale would exceed the shares currently held for a ticker. */
public class InsufficientSharesException extends DomainException {
    public InsufficientSharesException(String message) {
        super(message);
    }
}
