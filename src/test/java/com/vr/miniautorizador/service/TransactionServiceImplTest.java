package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.TransactionRequest;
import com.vr.miniautorizador.entity.Card;
import com.vr.miniautorizador.exception.InsufficientBalanceException;
import com.vr.miniautorizador.exception.InvalidPasswordException;
import com.vr.miniautorizador.exception.TransactionCardNotFoundException;
import com.vr.miniautorizador.repository.CardRepository;
import com.vr.miniautorizador.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void performTransaction_shouldSucceed_whenDataIsValid() {
        TransactionRequest request = new TransactionRequest("1234567890123456", "1234", new BigDecimal("10.00"));
        Card card = Card.builder().numeroCartao(request.getNumeroCartao()).senha("1234").saldo(new BigDecimal("500.00")).build();
        when(cardRepository.findByNumeroCartaoWithLock(request.getNumeroCartao())).thenReturn(Optional.of(card));

        transactionService.performTransaction(request);

        verify(cardRepository).save(card);
        assertEquals(new BigDecimal("490.00"), card.getSaldo());
    }

    @Test
    void performTransaction_shouldThrowTransactionCardNotFoundException_whenCardDoesNotExist() {
        TransactionRequest request = new TransactionRequest("1234567890123456", "1234", new BigDecimal("10.00"));
        when(cardRepository.findByNumeroCartaoWithLock(request.getNumeroCartao())).thenReturn(Optional.empty());

        assertThrows(TransactionCardNotFoundException.class, () -> transactionService.performTransaction(request));
    }

    @Test
    void performTransaction_shouldThrowInvalidPasswordException_whenPasswordIsIncorrect() {
        TransactionRequest request = new TransactionRequest("1234567890123456", "wrong-password", new BigDecimal("10.00"));
        Card card = Card.builder().numeroCartao(request.getNumeroCartao()).senha("1234").saldo(new BigDecimal("500.00")).build();
        when(cardRepository.findByNumeroCartaoWithLock(request.getNumeroCartao())).thenReturn(Optional.of(card));

        assertThrows(InvalidPasswordException.class, () -> transactionService.performTransaction(request));
    }

    @Test
    void performTransaction_shouldThrowInsufficientBalanceException_whenBalanceIsTooLow() {
        TransactionRequest request = new TransactionRequest("1234567890123456", "1234", new BigDecimal("600.00"));
        Card card = Card.builder().numeroCartao(request.getNumeroCartao()).senha("1234").saldo(new BigDecimal("500.00")).build();
        when(cardRepository.findByNumeroCartaoWithLock(request.getNumeroCartao())).thenReturn(Optional.of(card));

        assertThrows(InsufficientBalanceException.class, () -> transactionService.performTransaction(request));
    }
}
