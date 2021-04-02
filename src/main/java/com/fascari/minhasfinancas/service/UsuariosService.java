package com.fascari.minhasfinancas.service;

import java.util.Optional;

import com.fascari.minhasfinancas.model.entity.Usuarios;

public interface UsuariosService {

    Usuarios autenticar(String email, String senha);

    Optional<Usuarios> obterPorId(Long id);

    Usuarios salvarUsuario(Usuarios usuario);

    void validarEmail(String email);
}