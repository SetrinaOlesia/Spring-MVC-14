package com.softserve.sprint14.exception;

public class CannotDeleteOwnerWithElementsException extends RuntimeException {
    public CannotDeleteOwnerWithElementsException(String message) {
        super(message);
    }
}