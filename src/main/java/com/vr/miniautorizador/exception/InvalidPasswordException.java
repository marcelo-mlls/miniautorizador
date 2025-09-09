package com.vr.miniautorizador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("SENHA_INVALIDA");
    }
}

