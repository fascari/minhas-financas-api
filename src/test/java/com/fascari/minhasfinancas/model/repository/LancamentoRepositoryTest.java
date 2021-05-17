package com.fascari.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fascari.minhasfinancas.model.entity.Lancamentos;
import com.fascari.minhasfinancas.model.enums.StatusLancamento;
import com.fascari.minhasfinancas.model.enums.TipoLancamento;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
    private static final int EXERCICIO = 2021;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private LancamentosRepository repository;

    public static Lancamentos criarLancamento() {
        return Lancamentos.builder()
                .ano(EXERCICIO)
                .mes(1)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }

    @Test
    public void deveAtualizarUmLancamento() {
        Lancamentos lancamento = criarEPersistirUmLancamento();
        lancamento.setAno(EXERCICIO);
        lancamento.setDescricao("Teste Atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);
        repository.save(lancamento);
        Lancamentos lancamentoAtualizado = entityManager.find(Lancamentos.class, lancamento.getId());
        assertThat(lancamentoAtualizado.getAno()).isEqualTo(EXERCICIO);
        assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
        assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void deveBuscarUmLancamentoPorId() {
        Lancamentos lancamento = criarEPersistirUmLancamento();
        Optional<Lancamentos> lancamentoEncontrado = repository.findById(lancamento.getId());
        assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }

    @Test
    public void deveDeletarUmLancamento() {
        Lancamentos lancamento = criarEPersistirUmLancamento();
        lancamento = entityManager.find(Lancamentos.class, lancamento.getId());
        repository.delete(lancamento);
        Lancamentos lancamentoInexistente = entityManager.find(Lancamentos.class, lancamento.getId());
        assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void deveSalvarUmLancamento() {
        Lancamentos lancamento = criarLancamento();
        lancamento = repository.save(lancamento);
        assertThat(lancamento.getId()).isNotNull();
    }

    private Lancamentos criarEPersistirUmLancamento() {
        Lancamentos lancamento = criarLancamento();
        entityManager.persist(lancamento);
        return lancamento;
    }


}