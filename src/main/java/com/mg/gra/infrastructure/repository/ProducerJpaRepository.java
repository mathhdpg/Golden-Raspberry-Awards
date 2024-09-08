package com.mg.gra.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mg.gra.infrastructure.dto.ProducerAwardDTO;
import com.mg.gra.infrastructure.model.ProducerEntity;

public interface ProducerJpaRepository extends JpaRepository<ProducerEntity, Integer> {

    Optional<ProducerEntity> findByName(String producerName);

    @Query(""
            + "select new com.mg.gra.infrastructure.dto.ProducerAwardDTO("
            + "         producer.name,"
            + "         movie.year"
            + "       )"
            + "  from MovieEntity movie"
            + " inner join movie.producers producer"
            + " where movie.winner = true"
            + " order by producer.name, movie.year")
    List<ProducerAwardDTO> findAllWinningProducers();

}