package com.vr.miniautorizador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @JsonProperty("numeroCartao")
    @NotEmpty(message = "O número do cartão é obrigatório.")
    private String numeroCartao;

    @JsonProperty("senhaCartao")
    @NotEmpty(message = "A senha do cartão é obrigatória.")
    private String senhaCartao;

    @JsonProperty("valor")
    @NotNull(message = "O valor da transação é obrigatório.")
    @Positive(message = "O valor da transação deve ser positivo.")
    private BigDecimal valor;
}