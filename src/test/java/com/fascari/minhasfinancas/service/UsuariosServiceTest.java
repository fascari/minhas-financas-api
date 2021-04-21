package com.fascari.minhasfinancas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.repository.UsuariosRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuariosServiceTest {
    private static final String EMAI_USUARIO = "email@email.com";
    @MockBean
    private UsuariosRepository repository;
    @SpyBean
    private UsuariosService service;

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        Assertions.assertThrows(BusinessException.class, () -> service.validarEmail(EMAI_USUARIO));
    }

    @Test
    public void deveValidarEmail() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        service.validarEmail(EMAI_USUARIO);
    }
}
