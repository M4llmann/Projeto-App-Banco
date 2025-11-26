package com.Backend.AppBanco.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.AppBanco.entity.ContaEntity;
import com.Backend.AppBanco.entity.UsuarioEntity;
import com.Backend.AppBanco.exception.BusinessException;
import com.Backend.AppBanco.exception.ResourceNotFoundException;
import com.Backend.AppBanco.repository.ContaRepository;
import com.Backend.AppBanco.repository.UsuarioRepository;

@Service
public class ContaService {

    private static final Logger logger = LoggerFactory.getLogger(ContaService.class);

    @Autowired
    private ContaRepository contaRepository;
    
    @Autowired
    private TransacaoService transacaoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public ContaEntity criarConta(String nomeTitular, Integer idUsuario) {
        logger.info("Criando conta para usuário ID: {} com nome titular: {}", idUsuario, nomeTitular);
        
        // Valida o nome do titular
        if (nomeTitular == null || nomeTitular.trim().isEmpty()) {
            throw new BusinessException("O nome do titular é obrigatório");
        }
        
        // Encontra o usuário pelo ID fornecido
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Cria a conta, associando o usuário encontrado
        ContaEntity conta = new ContaEntity(nomeTitular.trim(), usuario);

        // Salva a conta no repositório
        ContaEntity contaSalva = contaRepository.save(conta);
        logger.info("Conta criada com sucesso. ID: {}", contaSalva.getIdConta());
        
        return contaSalva;
    }

    public ContaEntity buscarContaPorId(Integer idConta) {
        logger.debug("Buscando conta por ID: {}", idConta);
        return contaRepository.findById(idConta)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
    }

    public List<ContaEntity> listarContas() {
        logger.debug("Listando todas as contas");
        return contaRepository.findAll();
    }
    
    public List<ContaEntity> listarContasPorUsuario(Integer idUsuario) {
        logger.debug("Listando contas do usuário ID: {}", idUsuario);
        return contaRepository.findAllByUsuario_IdUsuario(idUsuario);
    }

    public BigDecimal consultarSaldo(Integer idConta) {
        logger.debug("Consultando saldo da conta ID: {}", idConta);
        ContaEntity conta = buscarContaPorId(idConta);
        return conta.getSaldo();
    }

    @Transactional
    public ContaEntity realizarDeposito(Integer idConta, BigDecimal valor) {
        logger.info("Realizando depósito de {} na conta ID: {}", valor, idConta);
        
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor do depósito deve ser maior que zero");
        }

        // Buscar a conta para realizar o depósito
        ContaEntity conta = buscarContaPorId(idConta);
        
        if (!conta.getStatus()) {
            throw new BusinessException("Não é possível realizar depósito em conta inativa");
        }
        
        conta.setSaldo(conta.getSaldo().add(valor));

        // Salvar a conta com o novo saldo
        contaRepository.save(conta);

        // Registrar a transação
        transacaoService.criarTransacao(idConta, "DEPÓSITO", valor);
        
        logger.info("Depósito realizado com sucesso. Novo saldo: {}", conta.getSaldo());
        return conta;
    }

    @Transactional
    public ContaEntity realizarSaque(Integer idConta, BigDecimal valor) {
        logger.info("Realizando saque de {} da conta ID: {}", valor, idConta);
        
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor do saque deve ser maior que zero");
        }

        // Buscar a conta
        ContaEntity conta = buscarContaPorId(idConta);
        
        if (!conta.getStatus()) {
            throw new BusinessException("Não é possível realizar saque em conta inativa");
        }

        // Verificar se o saldo é suficiente para o saque
        if (conta.getSaldo().compareTo(valor) < 0) {
            logger.warn("Tentativa de saque com saldo insuficiente. Saldo: {}, Valor solicitado: {}", 
                       conta.getSaldo(), valor);
            throw new BusinessException("Saldo insuficiente para realizar o saque");
        }

        // Subtrair o valor do saque do saldo da conta
        conta.setSaldo(conta.getSaldo().subtract(valor));

        // Salvar a conta com o saldo atualizado
        contaRepository.save(conta);

        // Registrar a transação
        transacaoService.criarTransacao(idConta, "SAQUE", valor);
        
        logger.info("Saque realizado com sucesso. Novo saldo: {}", conta.getSaldo());
        return conta;
    }
}
