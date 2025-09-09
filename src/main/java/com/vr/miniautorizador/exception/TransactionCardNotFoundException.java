package com.vr.miniautorizador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class TransactionCardNotFoundException extends RuntimeException {
    public TransactionCardNotFoundException() {
        super("CARTAO_INEXISTENTE");
    }
}
