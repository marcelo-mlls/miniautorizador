package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.CardRequest;
import com.vr.miniautorizador.dto.CardResponse;
import com.vr.miniautorizador.entity.Card;
import com.vr.miniautorizador.exception.CardAlreadyExistsException;
import com.vr.miniautorizador.exception.CardNotFoundException;
import com.vr.miniautorizador.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("500.00");

    @Override
    @Transactional
    public CardResponse createCard(CardRequest cardRequest) {
        cardRepository.findByNumeroCartao(cardRequest.getNumeroCartao())
                .ifPresent(card -> {
                    throw new CardAlreadyExistsException(cardRequest);
                });

        Card newCard = Card.builder()
                .numeroCartao(cardRequest.getNumeroCartao())
                .senha(cardRequest.getSenha())
                .saldo(INITIAL_BALANCE)
                .build();

        cardRepository.save(newCard);

        return CardResponse.builder()
                .numeroCartao(newCard.getNumeroCartao())
                .senha(newCard.getSenha())
                .build();
    }

    @Override
    public BigDecimal getCardBalance(String numeroCartao) {
        return cardRepository.findByNumeroCartao(numeroCartao)
                .map(Card::getSaldo)
                .orElseThrow(CardNotFoundException::new);
    }
}