package com.vr.miniautorizador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.miniautorizador.dto.CardRequest;
import com.vr.miniautorizador.entity.Card;
import com.vr.miniautorizador.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "username", password = "password")
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardRepository cardRepository;

    @Test
    void createCard_shouldReturnCreated() throws Exception {
        CardRequest request = new CardRequest("1111222233334444", "1234");
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value(request.getNumeroCartao()))
                .andExpect(jsonPath("$.senha").value(request.getSenha()));
    }

    @Test
    void createCard_shouldReturnUnprocessableEntity_whenCardExists() throws Exception {
        CardRequest request = new CardRequest("1111222233334444", "1234");
        cardRepository.save(Card.builder().numeroCartao(request.getNumeroCartao()).senha(request.getSenha()).saldo(new BigDecimal("500")).build());

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getSaldo_shouldReturnOk() throws Exception {
        String numeroCartao = "1111222233334444";
        cardRepository.save(Card.builder().numeroCartao(numeroCartao).senha("1234").saldo(new BigDecimal("500.00")).build());

        mockMvc.perform(get("/cartoes/" + numeroCartao))
                .andExpect(status().isOk())
                .andExpect(content().string("500.00"));
    }

    @Test
    void getSaldo_shouldReturnNotFound_whenCardDoesNotExist() throws Exception {
        mockMvc.perform(get("/cartoes/1111222233334444"))
                .andExpect(status().isNotFound());
    }
}