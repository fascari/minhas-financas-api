package com.fascari.minhasfinancas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentosDTO {
    private Integer ano;
    private String descricao;
    private Long id;
    private Integer mes;
    private String status;
    private String tipo;
    private Long usuarios;
    private BigDecimal valor;
}