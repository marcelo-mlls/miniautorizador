package com.vr.miniautorizador.exception;

import com.vr.miniautorizador.dto.CardRequest;
import org.springframework.http.HttpStatus;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@Getter
public class CardAlreadyExistsException extends RuntimeException {

    private final CardRequest cardRequest;

    public CardAlreadyExistsException(CardRequest cardRequest) {
        this.cardRequest = cardRequest;
    }
}

