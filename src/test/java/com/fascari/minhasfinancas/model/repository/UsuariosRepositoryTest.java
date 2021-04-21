package com.fascari.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fascari.minhasfinancas.model.entity.Usuarios;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuariosRepositoryTest {
    private static final String EMAIL_USUARIO = "usuario@email.com";
    private static final String NOME_USUARIO = "Usuário Teste";
    private static final String SENHA_USUARIO = "senha";
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UsuariosRepository repository;

    private static Usuarios criarUsuario() {
        return Usuarios.builder().nome(NOME_USUARIO).email(EMAIL_USUARIO).senha(SENHA_USUARIO).build();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail() {
        entityManager.persist(criarUsuario());
        Optional<Usuarios> usuario = repository.findByEmail(EMAIL_USUARIO);
        Assertions.assertThat(usuario.isPresent()).isTrue();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        Usuarios usuario = criarUsuario();
        Usuarios usuarioSalvo = repository.save(usuario);
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
        boolean hasEmail = repository.existsByEmail(EMAIL_USUARIO);
        Assertions.assertThat(hasEmail).isFalse();
    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
        Optional<Usuarios> usuario = repository.findByEmail(EMAIL_USUARIO);
        Assertions.assertThat(usuario.isPresent()).isFalse();
    }

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        entityManager.persist(criarUsuario());
        boolean hasEmail = repository.existsByEmail(EMAIL_USUARIO);
        Assertions.assertThat(hasEmail).isTrue();
    }

}
