package com.vr.miniautorizador.service;

import com.vr.miniautorizador.dto.TransactionRequest;

public interface TransactionService {

    void performTransaction(TransactionRequest transactionRequest);
}

