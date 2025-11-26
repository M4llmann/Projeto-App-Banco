package com.Backend.AppBanco.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.AppBanco.entity.UsuarioEntity;
import com.Backend.AppBanco.exception.BusinessException;
import com.Backend.AppBanco.exception.ResourceNotFoundException;
import com.Backend.AppBanco.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UsuarioEntity criarUsuario(String email, String senha) {
        logger.info("Criando novo usuário com email: {}", email);
        
        // Verifica se o e-mail já está cadastrado
        Optional<UsuarioEntity> usuarioExistente = usuarioRepository.findByEmail(email);
        if (usuarioExistente.isPresent()) {
            logger.warn("Tentativa de cadastro com email já existente: {}", email);
            throw new BusinessException("E-mail já cadastrado!");
        }

        // Valida a senha
        if (senha == null || senha.length() < 6) {
            throw new BusinessException("A senha deve ter no mínimo 6 caracteres");
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(senha);
        
        // Cria o novo usuário e salva no repositório
        UsuarioEntity usuario = new UsuarioEntity(email, senhaCriptografada);
        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuario);
        
        logger.info("Usuário criado com sucesso. ID: {}", usuarioSalvo.getIdUsuario());
        return usuarioSalvo;
    }

    public UsuarioEntity buscarUsuarioPorEmail(String email) {
        logger.info("Buscando usuário por email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
    }
    
    public UsuarioEntity login(String email, String senha) {
        logger.info("Tentativa de login para email: {}", email);
        
        // Verifica se o usuário existe
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> {
                logger.warn("Tentativa de login com email não encontrado: {}", email);
                return new ResourceNotFoundException("E-mail ou senha incorretos.");
            });
        
        // Verifica se a senha está correta usando BCrypt
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            logger.warn("Tentativa de login com senha incorreta para email: {}", email);
            throw new BusinessException("E-mail ou senha incorretos.");
        }
    
        logger.info("Login realizado com sucesso para email: {}", email);
        return usuario;
    }
}
