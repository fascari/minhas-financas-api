package com.fascari.minhasfinancas.api.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fascari.minhasfinancas.api.dto.UsuariosDTO;
import com.fascari.minhasfinancas.exceptions.AuthError;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.entity.Usuarios;
import com.fascari.minhasfinancas.service.LancamentosService;
import com.fascari.minhasfinancas.service.UsuariosService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuariosController.class)
@AutoConfigureMockMvc
public class UsuariosControllerTest {
    public static final String USUARIO_EMAIL = "usuario@email.com";
    public static final String USUARIO_SENHA = "123";
    private static final String API = "/api/usuarios";
    private static final long ID = 1L;
    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    @MockBean
    private LancamentosService lancamentoService;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UsuariosService service;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        UsuariosDTO dto = UsuariosDTO.builder().email(USUARIO_EMAIL).senha(USUARIO_SENHA).build();
        Usuarios usuario = Usuarios.builder().id(ID).email(USUARIO_EMAIL).senha(USUARIO_SENHA).build();
        Mockito.when(service.autenticar(USUARIO_EMAIL, USUARIO_SENHA)).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar")).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void deveCriarUmNovoUsuario() throws Exception {
        UsuariosDTO dto = UsuariosDTO.builder().email(USUARIO_EMAIL).senha(USUARIO_SENHA).build();
        Usuarios usuario = Usuarios.builder().id(ID).email(USUARIO_EMAIL).senha(USUARIO_SENHA).build();

        Mockito.when(service.salvarUsuario(Mockito.any(Usuarios.class))).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(
                MockMvcResultMatchers.jsonPath("id").value(usuario.getId())).andExpect(
                MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome())).andExpect(
                MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void deveObterOSaldoDoUsuario() throws Exception {
        //cenário
        BigDecimal saldo = BigDecimal.valueOf(10);
        Usuarios usuario = Usuarios.builder().id(ID).email(USUARIO_EMAIL).senha(USUARIO_SENHA).build();
        Mockito.when(service.obterPorId(ID)).thenReturn(Optional.of(usuario));
        Mockito.when(lancamentoService.obterSaldoPorUsuario(ID)).thenReturn(saldo);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API.concat("/1/saldo")).accept(JSON).contentType(JSON);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string("10"));
    }

    @Test
    public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
        UsuariosDTO dto = UsuariosDTO.builder().email(USUARIO_EMAIL).senha(USUARIO_SENHA).build();
        Mockito.when(service.autenticar(USUARIO_EMAIL, USUARIO_SENHA)).thenThrow(AuthError.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar")).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
        UsuariosDTO dto = UsuariosDTO.builder().email(USUARIO_EMAIL).senha(USUARIO_SENHA).build();

        Mockito.when(service.salvarUsuario(Mockito.any(Usuarios.class))).thenThrow(BusinessException.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON).contentType(JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deveRetornarResourceNotFoundQuandoUsuarioNaoExisteParaObterOSaldo() throws Exception {
        Mockito.when(service.obterPorId(ID)).thenReturn(Optional.empty());

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API.concat("/1/saldo")).accept(JSON).contentType(JSON);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

}