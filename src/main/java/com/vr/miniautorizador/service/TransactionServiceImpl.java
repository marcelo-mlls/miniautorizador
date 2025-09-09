package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.TransactionRequest;
import com.vr.miniautorizador.entity.Card;
import com.vr.miniautorizador.exception.TransactionCardNotFoundException;
import com.vr.miniautorizador.exception.InsufficientBalanceException;
import com.vr.miniautorizador.exception.InvalidPasswordException;
import com.vr.miniautorizador.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CardRepository cardRepository;

    @Override
    @Transactional
    public void performTransaction(TransactionRequest transactionRequest) {
        Card card = cardRepository.findByNumeroCartaoWithLock(transactionRequest.getNumeroCartao())
                .orElseThrow(TransactionCardNotFoundException::new);

        validatePassword(card, transactionRequest.getSenhaCartao());
        validateBalance(card, transactionRequest.getValor());

        card.setSaldo(card.getSaldo().subtract(transactionRequest.getValor()));
        cardRepository.save(card);
    }

    private void validatePassword(Card card, String password) {
        Predicate<String> isPasswordValid = p -> card.getSenha().equals(p);
        if (!isPasswordValid.test(password)) {
            throw new InvalidPasswordException();
        }
    }

    private void validateBalance(Card card, BigDecimal amount) {
        Predicate<BigDecimal> hasSufficientBalance = b -> card.getSaldo().compareTo(b) >= 0;
        if (!hasSufficientBalance.test(amount)) {
            throw new InsufficientBalanceException();
        }
    }
}