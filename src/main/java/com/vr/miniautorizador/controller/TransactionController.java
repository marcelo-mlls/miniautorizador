package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.TransactionRequest;
import com.vr.miniautorizador.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> performTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        transactionService.performTransaction(transactionRequest);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }
}
