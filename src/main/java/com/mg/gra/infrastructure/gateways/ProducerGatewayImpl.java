package com.mg.gra.infrastructure.gateways;

import java.util.List;
import java.util.Optional;

import com.mg.gra.domain.gateways.ProducerGateway;
import com.mg.gra.domain.models.Producer;
import com.mg.gra.domain.models.ProducerAward;
import com.mg.gra.infrastructure.mapper.ProducerMapper;
import com.mg.gra.infrastructure.model.ProducerEntity;
import com.mg.gra.infrastructure.repository.ProducerJpaRepository;

public class ProducerGatewayImpl implements ProducerGateway {

    private final ProducerJpaRepository producerJpaRepository;

    private final ProducerMapper producerMapper;

    public ProducerGatewayImpl(ProducerJpaRepository producerJpaRepository, ProducerMapper producerMapper) {
        this.producerJpaRepository = producerJpaRepository;
        this.producerMapper = producerMapper;
    }

    @Override
    public Optional<Producer> findById(Integer id) {
        return producerJpaRepository.findById(id).map(producerMapper::toDomain);
    }

    @Override
    public Producer save(Producer producer) {
        ProducerEntity entity = producerMapper.toEntity(producer);
        ProducerEntity savedEntity = producerJpaRepository.save(entity);
        return producerMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Producer> findByName(String producerName) {
        return producerJpaRepository.findByName(producerName).map(producerMapper::toDomain);
    }

    @Override
    public List<ProducerAward> findAllWinningProducersOrderByProducerAndMovieYear() {
        return producerJpaRepository.findAllWinningProducers()
                .stream()
                .map(producerAwardDTO -> {
                    return new ProducerAward(
                            producerAwardDTO.producerName(),
                            producerAwardDTO.movieYear());
                })
                .toList();
    }

}
