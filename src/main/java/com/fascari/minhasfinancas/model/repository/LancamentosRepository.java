package com.fascari.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.fascari.minhasfinancas.model.entity.Lancamentos;
import com.fascari.minhasfinancas.model.enums.StatusLancamento;
import com.fascari.minhasfinancas.model.enums.TipoLancamento;

public interface LancamentosRepository extends JpaRepository<Lancamentos, Long> {
    @Query(value = " select sum(l.valor) from Lancamentos l join l.usuarios u "
            + " where u.id = :idUsuario and l.tipo =:tipo and l.status = :status group by u ")
    BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo,
            @Param("status") StatusLancamento status);
}
