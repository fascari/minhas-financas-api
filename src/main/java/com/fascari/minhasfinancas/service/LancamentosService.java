package com.fascari.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.fascari.minhasfinancas.model.entity.Lancamentos;
import com.fascari.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentosService {
    Lancamentos atualizar(Lancamentos lancamento);

    void atualizarStatus(Lancamentos lancamento, StatusLancamento status);

    List<Lancamentos> buscar(Lancamentos lancamentoFiltro);

    void deletar(Lancamentos lancamento);

    Optional<Lancamentos> obterPorId(Long id);

    BigDecimal obterSaldoPorUsuario(Long id);

    Lancamentos salvar(Lancamentos lancamento);

    void validar(Lancamentos lancamento);
}