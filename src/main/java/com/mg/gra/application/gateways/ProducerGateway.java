package com.mg.gra.application.gateways;

import java.util.List;
import java.util.Optional;

import com.mg.gra.domain.entity.Producer;
import com.mg.gra.domain.entity.ProducerAward;

public interface ProducerGateway {

    Producer save(Producer any);

    Optional<Producer> findById(Integer id);

    Optional<Producer> findByName(String producerName);

    List<ProducerAward> findAllWinningProducersOrderByProducerAndMovieYear();

}