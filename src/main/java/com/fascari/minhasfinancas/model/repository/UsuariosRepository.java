package com.fascari.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fascari.minhasfinancas.model.entity.Usuarios;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    boolean existsByEmail(String email);

    Optional<Usuarios> findByEmail(String email);
}
