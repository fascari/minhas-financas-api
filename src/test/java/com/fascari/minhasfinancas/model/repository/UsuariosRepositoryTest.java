package com.fascari.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fascari.minhasfinancas.model.entity.Usuarios;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuariosRepositoryTest {
    private static final String EMAIL_USUARIO = "usuario@email.com";
    private static final String NOME_USUARIO = "Usuário Teste";
    @Autowired
    private UsuariosRepository repository;

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
        repository.deleteAll();
        boolean hasEmail = repository.existsByEmail(EMAIL_USUARIO);
        Assertions.assertThat(hasEmail).isFalse();
    }

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        Usuarios usuarios = Usuarios.builder().nome(NOME_USUARIO).email(EMAIL_USUARIO).build();
        repository.save(usuarios);
        boolean hasEmail = repository.existsByEmail(EMAIL_USUARIO);
        Assertions.assertThat(hasEmail).isTrue();
    }
}
