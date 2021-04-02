package com.fascari.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fascari.minhasfinancas.exceptions.AuthError;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.entity.Usuarios;
import com.fascari.minhasfinancas.model.repository.UsuariosRepository;
import com.fascari.minhasfinancas.service.UsuariosService;

@Service
public class UsuarioServiceImpl implements UsuariosService {

    private final UsuariosRepository repository;

    public UsuarioServiceImpl(UsuariosRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuarios autenticar(String email, String senha) {
        Optional<Usuarios> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()) {
            throw new AuthError("Usuário não encontrado para o email informado.");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new AuthError("Senha inválida.");
        }

        return usuario.get();
    }

    @Override
    public Optional<Usuarios> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuarios salvarUsuario(Usuarios usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean hasByEmail = repository.existsByEmail(email);
        if (hasByEmail) {
            throw new BusinessException("Já existe um usuário cadastrado com este email.");
        }
    }

}