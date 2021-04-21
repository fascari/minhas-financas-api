package com.fascari.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fascari.minhasfinancas.exceptions.AuthError;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.entity.Usuarios;
import com.fascari.minhasfinancas.model.repository.UsuariosRepository;
import com.fascari.minhasfinancas.service.impl.UsuariosServiceImpl;

import static com.fascari.minhasfinancas.service.impl.UsuariosServiceImpl.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuariosServiceTest {
    private static final String EMAIL_USUARIO = "email@email.com";
    private static final String NOME_USUARIO = "nome";
    private static final String SENHA_USUARIO = "senha";
    @MockBean
    private UsuariosRepository repository;
    @SpyBean
    private UsuariosServiceImpl service;

    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        Usuarios usuario = Usuarios.builder().email(EMAIL_USUARIO).senha(SENHA_USUARIO).id(1L).build();
        Mockito.when(repository.findByEmail(EMAIL_USUARIO)).thenReturn(Optional.of(usuario));
        Usuarios usuarioAutenticado = service.autenticar(EMAIL_USUARIO, SENHA_USUARIO);
        Assertions.assertThat(usuarioAutenticado).isNotNull();
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> service.validarEmail(EMAIL_USUARIO));
    }

    @Test
    public void deveLancarErroQUandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(() -> service.autenticar(EMAIL_USUARIO, SENHA_USUARIO));
        Assertions.assertThat(exception).isInstanceOf(AuthError.class).hasMessage(MSG_AUTH_ERROR_USUARIO_NAO_ENCONTRADO);
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        Usuarios usuario = Usuarios.builder().email(EMAIL_USUARIO).senha(SENHA_USUARIO).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
        Throwable exception = Assertions.catchThrowable(() -> service.autenticar(EMAIL_USUARIO, "123"));
        Assertions.assertThat(exception).isInstanceOf(AuthError.class).hasMessage(MSG_AUTH_ERROR_SENHA_INVALIDA);
    }

    @Test
    public void deveSalvarUmUsuario() {
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuarios usuario = Usuarios.builder().id(1L).nome(NOME_USUARIO).email(EMAIL_USUARIO).senha(SENHA_USUARIO).build();
        Mockito.when(repository.save(Mockito.any(Usuarios.class))).thenReturn(usuario);
        Usuarios usuarioSalvo = service.salvarUsuario(new Usuarios());
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo(NOME_USUARIO);
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo(EMAIL_USUARIO);
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo(SENHA_USUARIO);

    }

    @Test
    public void deveValidarEmail() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        service.validarEmail(EMAIL_USUARIO);
    }

    @Test
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
        Usuarios usuario = Usuarios.builder().email(EMAIL_USUARIO).build();
        Mockito.doThrow(BusinessException.class).when(service).validarEmail(EMAIL_USUARIO);
        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> service.salvarUsuario(usuario));
        Mockito.verify(repository, Mockito.never()).save(usuario);
    }
}
