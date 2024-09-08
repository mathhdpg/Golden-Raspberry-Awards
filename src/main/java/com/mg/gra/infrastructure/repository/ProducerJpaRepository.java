package com.mg.gra.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mg.gra.infrastructure.model.ProducerEntity;

public interface ProducerJpaRepository extends JpaRepository<ProducerEntity, Integer> {

    Optional<ProducerEntity> findByName(String producerName);

}