package com.Backend.AppBanco.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.AppBanco.dto.ContaDTO;
import com.Backend.AppBanco.dto.TransacaoDTO;
import com.Backend.AppBanco.entity.ContaEntity;
import com.Backend.AppBanco.service.ContaService;
import com.Backend.AppBanco.service.TransacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;
    private final TransacaoService transacaoService;

public ContaController(ContaService contaService, TransacaoService transacaoService) {
    this.contaService = contaService;
    this.transacaoService = transacaoService;
}

private ContaDTO toContaDTO(ContaEntity conta) {
        return new ContaDTO(
            conta.getIdConta(),
            conta.getNomeTitular(),
            conta.getSaldo(),
            conta.getStatus(),
            conta.getDataCriacao(),
            conta.getUsuario().getIdUsuario()
        );
    }
    @Operation(summary = "Criar nova conta")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Conta criada com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ContaDTO.class)) }), 
      @ApiResponse(responseCode = "400", description = "Requisição inválida", 
        content = @Content) })
    @PostMapping("/{idUsuario}")
    public ResponseEntity<ContaDTO> criarConta(@PathVariable Integer idUsuario, @RequestBody ContaDTO contaDTO) {
        ContaEntity conta = contaService.criarConta(contaDTO.getNomeTitular(), idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(toContaDTO(conta));
    }

    @Operation(summary = "Listar contas por usuário")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Contas listadas com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ContaDTO.class)) }) })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ContaDTO>> listarContasPorUsuario(@PathVariable Integer idUsuario) {
        List<ContaDTO> contas = contaService.listarContasPorUsuario(idUsuario).stream()
            .map(this::toContaDTO)
            .toList();
        return ResponseEntity.ok(contas);
    }

    @Operation(summary = "Buscar Conta")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Conta localizada com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ContaDTO.class)) }), 
      @ApiResponse(responseCode = "404", description = "Conta não encontrada", 
        content = @Content) })
    @GetMapping("/{idConta}")
    public ResponseEntity<ContaDTO> buscarConta(@PathVariable Integer idConta) {
        ContaEntity conta = contaService.buscarContaPorId(idConta);
        return ResponseEntity.ok(toContaDTO(conta));
    }

    @Operation(summary = "Consultar saldo")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Saldo da conta consultado com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = BigDecimal.class)) }), 
      @ApiResponse(responseCode = "404", description = "Conta não encontrada", 
        content = @Content) })
    @GetMapping("/{idConta}/saldo")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable Integer idConta) {
        BigDecimal saldo = contaService.consultarSaldo(idConta);
        return ResponseEntity.ok(saldo);
    }

    @Operation(summary = "Realizar Depósito")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Depósito efetuado com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ContaDTO.class)) }), 
      @ApiResponse(responseCode = "400", description = "Requisição inválida", 
        content = @Content) })
    @PostMapping("/{idConta}/deposito")
    public ResponseEntity<ContaDTO> realizarDeposito(@PathVariable Integer idConta, @RequestParam BigDecimal valor) {
        ContaEntity conta = contaService.realizarDeposito(idConta, valor);
        return ResponseEntity.ok(toContaDTO(conta));
    }

    @Operation(summary = "Realizar Saque")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Saque efetuado com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ContaDTO.class)) }), 
      @ApiResponse(responseCode = "400", description = "Requisição inválida", 
        content = @Content) })
    @PostMapping("/{idConta}/saque")
    public ResponseEntity<ContaDTO> realizarSaque(@PathVariable Integer idConta, @RequestParam BigDecimal valor) {
        ContaEntity conta = contaService.realizarSaque(idConta, valor);
        return ResponseEntity.ok(toContaDTO(conta));
    }

    @Operation(summary = "Mostrar Extrato")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Extrato carregado com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = TransacaoDTO.class)) }), 
      @ApiResponse(responseCode = "404", description = "Extrato não encontrado", 
        content = @Content) })
    @GetMapping("/{idConta}/extrato")
    public ResponseEntity<List<TransacaoDTO>> obterExtrato(@PathVariable Integer idConta) {
        List<TransacaoDTO> extrato = transacaoService.obterExtrato(idConta).stream()
            .map(transacao -> new TransacaoDTO(
                transacao.getIdTransacao(),
                transacao.getConta().getIdConta(),
                transacao.getTipo(),
                transacao.getValor(),
                transacao.getDataTransacao()
            ))
            .toList();
        return ResponseEntity.ok(extrato);
    }
}


