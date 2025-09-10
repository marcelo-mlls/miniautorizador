package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.dto.CardRequest;
import com.vr.miniautorizador.dto.CardResponse;
import com.vr.miniautorizador.service.CardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Tag(name = "cartoes")
@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest cardRequest) {
        CardResponse cardResponse = cardService.createCard(cardRequest);
        return new ResponseEntity<>(cardResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> getSaldo(@PathVariable String numeroCartao) {
        BigDecimal saldo = cardService.getCardBalance(numeroCartao);
        return ResponseEntity.ok(saldo);
    }
}