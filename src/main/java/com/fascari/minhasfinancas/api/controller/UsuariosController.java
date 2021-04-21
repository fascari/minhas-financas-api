package com.fascari.minhasfinancas.api.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fascari.minhasfinancas.api.dto.UsuariosDTO;
import com.fascari.minhasfinancas.exceptions.AuthError;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.entity.Usuarios;
import com.fascari.minhasfinancas.service.UsuariosService;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuariosController {
    private final UsuariosService service;

    @PostMapping("/autenticar")
    public ResponseEntity<Object> autenticar(@RequestBody UsuariosDTO dto) {
        try {
            Usuarios usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (AuthError e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody UsuariosDTO dto) {
        Usuarios usuarios = Usuarios.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
        try {
            Usuarios usuarioSalvo = service.salvarUsuario(usuarios);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}