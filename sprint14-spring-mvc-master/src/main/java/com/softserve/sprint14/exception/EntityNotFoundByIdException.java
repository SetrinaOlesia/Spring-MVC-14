package com.softserve.sprint14.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundByIdException extends RuntimeException {
    public EntityNotFoundByIdException(String message) {
        super(message);

    }
}
