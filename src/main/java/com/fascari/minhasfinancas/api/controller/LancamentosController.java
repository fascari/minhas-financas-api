package com.fascari.minhasfinancas.api.controller;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fascari.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.fascari.minhasfinancas.api.dto.LancamentosDTO;
import com.fascari.minhasfinancas.exceptions.BusinessException;
import com.fascari.minhasfinancas.model.entity.Lancamentos;
import com.fascari.minhasfinancas.model.entity.Usuarios;
import com.fascari.minhasfinancas.model.enums.StatusLancamento;
import com.fascari.minhasfinancas.model.enums.TipoLancamento;
import com.fascari.minhasfinancas.service.LancamentosService;
import com.fascari.minhasfinancas.service.UsuariosService;

@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentosController {
    private final LancamentosService service;
    private final UsuariosService usuarioService;

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentosDTO dto) {
        return service.obterPorId(id).map(entity -> {
            try {
                return ResponseEntity.ok(service.atualizar(toEntity(dto, entity)));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lancamentos não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
        return service.obterPorId(id).map(entity -> {
            if (dto.getStatus() == null) {
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
            }
            try {
                entity.setStatus(StatusLancamento.valueOf(dto.getStatus()));
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            } catch (BusinessException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lancamentos não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes, @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario) {
        Optional<Usuarios> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado.");
        }
        Lancamentos lancamentoFiltro = Lancamentos.builder().ano(ano).descricao(descricao).mes(mes).usuarios(usuario.get()).build();
        List<Lancamentos> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(entidade -> {
            service.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lancamentos não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("{id}")
    public ResponseEntity obterLancamento(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(lancamento -> new ResponseEntity(toDto(lancamento), HttpStatus.OK)).orElseGet(
                () -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentosDTO dto) {
        try {
            Lancamentos entidade = toEntity(dto, null);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Usuarios getUsuarios(LancamentosDTO dto) {
        Long idUsuario = dto.getUsuarios();
        if (idUsuario == null) {
            throw new BusinessException("Usuário não foi informado!");
        }
        return usuarioService.obterPorId(idUsuario).orElseThrow(() -> new BusinessException("Usuário não encontrado para o Id informado."));
    }

    private LancamentosDTO toDto(Lancamentos lancamento) {
        return LancamentosDTO.builder().id(lancamento.getId()).descricao(lancamento.getDescricao()).valor(lancamento.getValor()).mes(
                lancamento.getMes()).ano(lancamento.getAno()).status(lancamento.getStatus().name()).tipo(lancamento.getTipo().name()).usuarios(
                lancamento.getUsuarios().getId()).build();

    }

    private Lancamentos toEntity(LancamentosDTO dto, Lancamentos lancamentos) {
        Lancamentos.LancamentosBuilder lancamentosBuilder = Lancamentos.builder().id(dto.getId()).descricao(dto.getDescricao()).ano(dto.getAno()).mes(
                dto.getMes()).valor(dto.getValor()).usuarios(getUsuarios(dto)).dataCadastro(LocalDate.now());
        if (dto.getTipo() != null) {
            lancamentosBuilder.tipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if (dto.getStatus() != null) {
            lancamentosBuilder.status(StatusLancamento.valueOf(dto.getStatus()));
        }
        if (lancamentos != null) {
            lancamentosBuilder.id(lancamentos.getId());
        }
        return lancamentosBuilder.build();
    }
}