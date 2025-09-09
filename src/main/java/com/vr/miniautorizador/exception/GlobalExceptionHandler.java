package com.vr.miniautorizador.exception;

import com.vr.miniautorizador.dto.CardRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CardAlreadyExistsException.class)
    public ResponseEntity<CardRequest> handleCardAlreadyExistsException(CardAlreadyExistsException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getCardRequest(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({InvalidPasswordException.class, InsufficientBalanceException.class, TransactionCardNotFoundException.class})
    public ResponseEntity<String> handleTransactionExceptions(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<Void> handleCardNotFoundException(CardNotFoundException ex, WebRequest request) {
        return ResponseEntity.notFound().build();
    }
}

