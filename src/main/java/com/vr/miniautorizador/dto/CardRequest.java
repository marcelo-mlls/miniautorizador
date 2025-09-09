package com.vr.miniautorizador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    @JsonProperty("numeroCartao")
    @NotEmpty(message = "O número do cartão é obrigatório.")
    @Size(min = 16, max = 16, message = "O número do cartão deve ter 16 dígitos.")
    private String numeroCartao;

    @JsonProperty("senha")
    @NotEmpty(message = "A senha é obrigatória.")
    private String senha;
}
