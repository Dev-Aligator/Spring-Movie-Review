package com.dsi.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceAlreadyExists extends Exception {

    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExists(String message) {
        super(message);
    }
}
