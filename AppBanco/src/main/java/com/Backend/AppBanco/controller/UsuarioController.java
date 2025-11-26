package com.Backend.AppBanco.controller;

import java.security.Key;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.AppBanco.entity.UsuarioEntity;
import com.Backend.AppBanco.security.JwtUtil;
import com.Backend.AppBanco.service.UsuarioService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuarios")
@OpenAPIDefinition(
    info = @Info(
        title = "API de Gestão de Usuários do Banco",
        version = "1.0",
        description = "API para gerenciamento de usuários no sistema bancário"
        
    )
)
public class UsuarioController {

    @Autowired
    private Key secret;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Criar novo usuário")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = UsuarioEntity.class)) }), 
      @ApiResponse(responseCode = "400", description = "Requisição inválida", 
        content = @Content) })
    @PostMapping
    public ResponseEntity<UsuarioEntity> criarUsuario(@RequestBody UsuarioEntity usuario) {
        UsuarioEntity usuarioCriado = usuarioService.criarUsuario(usuario.getEmail(), usuario.getSenha());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @Operation(summary = "Buscar usuário por e-mail")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = UsuarioEntity.class)) }), 
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado", 
        content = @Content) })
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioEntity> buscarUsuario(@PathVariable String email) {
        UsuarioEntity usuario = usuarioService.buscarUsuarioPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Efetuar login")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = UsuarioEntity.class)) }), 
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas", 
        content = @Content) })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UsuarioEntity usuario) {
        UsuarioEntity usuarioLogado = usuarioService.login(usuario.getEmail(), usuario.getSenha());
        
        // Gerar o token JWT
        String token = JwtUtil.generateToken(usuarioLogado.getEmail(), secret);
        
        // Retornar o token no corpo da resposta
        return ResponseEntity.ok(Map.of(
            "token", token,
            "usuario", usuarioLogado
        ));
    }
}
