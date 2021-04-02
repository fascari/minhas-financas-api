package com.fascari.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fascari.minhasfinancas.model.entity.Usuarios;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
}
