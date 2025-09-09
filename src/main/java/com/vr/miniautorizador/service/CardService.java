package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.CardRequest;
import com.vr.miniautorizador.dto.CardResponse;

import java.math.BigDecimal;

public interface CardService {

    CardResponse createCard(CardRequest cardRequest);

    BigDecimal getCardBalance(String numeroCartao);
}

