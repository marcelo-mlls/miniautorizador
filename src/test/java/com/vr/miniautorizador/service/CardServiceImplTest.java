package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.CardRequest;
import com.vr.miniautorizador.dto.CardResponse;
import com.vr.miniautorizador.entity.Card;
import com.vr.miniautorizador.exception.CardAlreadyExistsException;
import com.vr.miniautorizador.exception.CardNotFoundException;
import com.vr.miniautorizador.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void createCard_shouldReturnCardResponse_whenCardDoesNotExist() {
        CardRequest request = new CardRequest("1234567890123456", "1234");
        when(cardRepository.findByNumeroCartao(request.getNumeroCartao())).thenReturn(Optional.empty());
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArguments()[0]);

        CardResponse response = cardService.createCard(request);

        assertNotNull(response);
        assertEquals(request.getNumeroCartao(), response.getNumeroCartao());
        assertEquals(request.getSenha(), response.getSenha());
    }

    @Test
    void createCard_shouldThrowCardAlreadyExistsException_whenCardExists() {
        CardRequest request = new CardRequest("1234567890123456", "1234");
        when(cardRepository.findByNumeroCartao(request.getNumeroCartao())).thenReturn(Optional.of(new Card()));

        assertThrows(CardAlreadyExistsException.class, () -> cardService.createCard(request));
    }

    @Test
    void getCardBalance_shouldReturnBalance_whenCardExists() {
        String numeroCartao = "1234567890123456";
        Card card = Card.builder().saldo(new BigDecimal("500.00")).build();
        when(cardRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(card));

        BigDecimal balance = cardService.getCardBalance(numeroCartao);

        assertEquals(card.getSaldo(), balance);
    }

    @Test
    void getCardBalance_shouldThrowCardNotFoundException_whenCardDoesNotExist() {
        String numeroCartao = "1234567890123456";
        when(cardRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCardBalance(numeroCartao));
    }
}