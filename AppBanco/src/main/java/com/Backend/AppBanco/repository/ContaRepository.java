package com.Backend.AppBanco.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Backend.AppBanco.entity.ContaEntity;


public interface ContaRepository extends JpaRepository<ContaEntity, Integer> {

    Optional<ContaEntity> findByUsuario_IdUsuario(Integer idUsuario);
    
    List<ContaEntity> findAllByUsuario_IdUsuario(Integer idUsuario);

}
