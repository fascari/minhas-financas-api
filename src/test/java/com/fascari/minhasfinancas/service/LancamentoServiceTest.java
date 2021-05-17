package com.fascari.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.entity.Lancamentos;
import com.fascari.minhasfinancas.model.entity.Usuarios;
import com.fascari.minhasfinancas.model.enums.StatusLancamento;
import com.fascari.minhasfinancas.model.enums.TipoLancamento;
import com.fascari.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.fascari.minhasfinancas.model.repository.LancamentosRepository;
import com.fascari.minhasfinancas.service.impl.LancamentosServiceImpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
    private static final long ID = 1L;
    @MockBean
    private LancamentosRepository repository;
    @SpyBean
    private LancamentosServiceImpl service;

    @Test
    public void deveAtualizarOStatusDeUmLancamento() {
        //cenário
        Lancamentos lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(ID);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        doReturn(lancamento).when(service).atualizar(lancamento);

        //execucao
        service.atualizarStatus(lancamento, novoStatus);

        //verificacoes
        assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        verify(service).atualizar(lancamento);

    }

    @Test
    public void deveAtualizarUmLancamento() {
        //cenário
        Lancamentos lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(ID);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        doNothing().when(service).validar(lancamentoSalvo);

        when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //execucao
        service.atualizar(lancamentoSalvo);

        //verificação
        verify(repository, times(1)).save(lancamentoSalvo);

    }

    @Test
    public void deveDeletarUmLancamento() {
        //cenário
        Lancamentos lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(ID);

        //execucao
        service.deletar(lancamento);

        //verificacao
        verify(repository).delete(lancamento);
    }

    @Test
    public void deveFiltrarLancamentos() {
        //cenário
        Lancamentos lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(ID);

        List<Lancamentos> lista = Arrays.asList(lancamento);
        when(repository.findAll(any(Example.class))).thenReturn(lista);

        //execucao
        List<Lancamentos> resultado = service.buscar(lancamento);

        //verificacoes
        assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);

    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
        //cenário
        Lancamentos lancamento = LancamentoRepositoryTest.criarLancamento();

        //execucao e verificacao
        catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);
        verify(repository, never()).save(lancamento);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
        //cenário
        Lancamentos lancamento = LancamentoRepositoryTest.criarLancamento();

        //execucao
        catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);

        //verificacao
        verify(repository, never()).delete(lancamento);
    }

    @Test
    public void deveLancarErrosAoValidarUmLancamento() {
        Lancamentos lancamento = new Lancamentos();

        Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("");

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("Salario");

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Mês válido.");

        lancamento.setAno(0);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Mês válido.");

        lancamento.setAno(13);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(1);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(202);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(2020);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Usuário.");

        lancamento.setUsuarios(new Usuarios());

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Usuário.");

        lancamento.getUsuarios().setId(ID);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.ZERO);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.valueOf(1));

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Informe um tipo de Lançamento.");

    }

    @Test
    public void deveObterSaldoPorUsuario() {
        //cenario
        Long idUsuario = ID;

        when(repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(idUsuario, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO)).thenReturn(
                BigDecimal.valueOf(100));

        when(repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(idUsuario, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO)).thenReturn(
                BigDecimal.valueOf(50));

        //execucao
        BigDecimal saldo = service.obterSaldoPorUsuario(idUsuario);

        //verificacao
        assertThat(saldo).isEqualTo(BigDecimal.valueOf(50));

    }

    @Test
    public void deveObterUmLancamentoPorID() {
        //cenário
        Long id = ID;

        Lancamentos lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        //execucao
        Optional<Lancamentos> resultado = service.obterPorId(id);

        //verificacao
        assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    public void deveREtornarVazioQuandoOLancamentoNaoExiste() {
        //cenário
        Long id = ID;

        Lancamentos lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        when(repository.findById(id)).thenReturn(Optional.empty());

        //execucao
        Optional<Lancamentos> resultado = service.obterPorId(id);

        //verificacao
        assertThat(resultado.isPresent()).isFalse();
    }

    @Test
    public void deveSalvarUmLancamento() {
        //cenário
        Lancamentos lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        doNothing().when(service).validar(lancamentoASalvar);

        Lancamentos lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(ID);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execucao
        Lancamentos lancamento = service.salvar(lancamentoASalvar);

        //verificação
        assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        //cenário
        Lancamentos lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        doThrow(BusinessException.class).when(service).validar(lancamentoASalvar);

        //execucao e verificacao
        catchThrowableOfType(() -> service.salvar(lancamentoASalvar), BusinessException.class);
        verify(repository, never()).save(lancamentoASalvar);
    }

}