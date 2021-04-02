package com.fascari.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fascari.minhasfinancas.model.entity.Lancamentos;

public interface LancamentosRepository extends JpaRepository<Lancamentos, Long> {
}
