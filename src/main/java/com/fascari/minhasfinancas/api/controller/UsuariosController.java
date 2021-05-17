package com.fascari.minhasfinancas.api.controller;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fascari.minhasfinancas.api.dto.UsuariosDTO;
import com.fascari.minhasfinancas.exceptions.AuthError;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.entity.Usuarios;
import com.fascari.minhasfinancas.service.LancamentosService;
import com.fascari.minhasfinancas.service.UsuariosService;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuariosController {
    private final LancamentosService lancamentosService;
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

    @GetMapping("{id}/saldo")
    public ResponseEntity<Object> obterSaldo(@PathVariable("id") Long id) {
        Optional<Usuarios> usuario = service.obterPorId(id);
        if (!usuario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        BigDecimal saldo = lancamentosService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
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