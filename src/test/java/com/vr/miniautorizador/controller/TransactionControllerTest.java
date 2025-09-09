package com.vr.miniautorizador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.miniautorizador.dto.TransactionRequest;
import com.vr.miniautorizador.entity.Card;
import com.vr.miniautorizador.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "username", password = "password")
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardRepository cardRepository;

    private final String numeroCartao = "1111222233334444";
    private final String senha = "1234";

    @BeforeEach
    void setUp() {
        cardRepository.save(Card.builder().numeroCartao(numeroCartao).senha(senha).saldo(new BigDecimal("500.00")).build());
    }

    @Test
    void performTransaction_shouldReturnCreated() throws Exception {
        TransactionRequest request = new TransactionRequest(numeroCartao, senha, new BigDecimal("10.00"));
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));
    }

    @Test
    void performTransaction_shouldReturnUnprocessableEntity_whenPasswordIsInvalid() throws Exception {
        TransactionRequest request = new TransactionRequest(numeroCartao, "wrong-password", new BigDecimal("10.00"));
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SENHA_INVALIDA"));
    }

    @Test
    void performTransaction_shouldReturnUnprocessableEntity_whenBalanceIsInsufficient() throws Exception {
        TransactionRequest request = new TransactionRequest(numeroCartao, senha, new BigDecimal("600.00"));
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"));
    }

    @Test
    void performTransaction_shouldReturnUnprocessableEntity_whenCardDoesNotExist() throws Exception {
        TransactionRequest request = new TransactionRequest("0000000000000000", senha, new BigDecimal("10.00"));
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("CARTAO_INEXISTENTE"));
    }
}