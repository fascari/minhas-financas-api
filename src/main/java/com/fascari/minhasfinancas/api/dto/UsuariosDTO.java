package com.fascari.minhasfinancas.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosDTO {
    private String email;
    private String nome;
    private String senha;
}