package com.mg.gra.infrastructure.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mg.gra.domain.models.Producer;
import com.mg.gra.infrastructure.model.ProducerEntity;

@Component
public class ProducerMapper {

    public Producer toDomain(ProducerEntity ProducerEntity) {
        return new Producer(
                ProducerEntity.getId(),
                ProducerEntity.getName());
    }

    public ProducerEntity toEntity(Producer Producer) {
        ProducerEntity entity = new ProducerEntity();
        entity.setId(Producer.getId());
        entity.setName(Producer.getName());
        return entity;
    }

    public List<Producer> toDomainList(List<ProducerEntity> producers) {
        return producers.stream().map(this::toDomain).toList();
    }

    public List<ProducerEntity> toEntityList(List<Producer> producers) {
        return producers.stream().map(this::toEntity).toList();
    }

}