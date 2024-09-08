package com.mg.gra.domain.gateways;

import java.util.Optional;

import com.mg.gra.domain.models.Producer;

public interface ProducerGateway {

    Producer save(Producer any);

    Optional<Producer> findById(Integer id);

    Optional<Producer> findByName(String producerName);

}
