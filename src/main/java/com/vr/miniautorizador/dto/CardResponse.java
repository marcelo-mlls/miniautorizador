package com.vr.miniautorizador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    @JsonProperty("numeroCartao")
    private String numeroCartao;

    @JsonProperty("senha")
    private String senha;
}

