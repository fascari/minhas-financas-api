package com.fascari.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fascari.minhasfinancas.model.entity.Lancamentos;

public interface LancamentosRepository extends JpaRepository<Lancamentos, Long> {
/*    @Query(value = " select sum(l.valor) from mf_owner.lancamentos l join l.usuario u "
            + " where u.id = :idUsuario and l.tipo =:tipo and l.status = :status group by u ")
    BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo,
            @Param("status") StatusLancamento status);*/
}
