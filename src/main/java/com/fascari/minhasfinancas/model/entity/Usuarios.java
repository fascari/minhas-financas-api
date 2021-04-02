package com.fascari.minhasfinancas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios", schema = "mf_owner")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios {

    @Column(name = "email")
    private String email;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "senha")
    @JsonIgnore
    private String senha;
}
