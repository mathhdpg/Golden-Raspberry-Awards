package com.mg.gra.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mg.gra.domain.gateways.ProducerGateway;
import com.mg.gra.infrastructure.gateways.ProducerGatewayImpl;
import com.mg.gra.infrastructure.mapper.ProducerMapper;
import com.mg.gra.infrastructure.repository.ProducerJpaRepository;

@Configuration
public class ProducerConfig {

    @Bean
    public ProducerGateway producerGateway(ProducerJpaRepository producerJpaRepository, ProducerMapper producerMapper) {
        return new ProducerGatewayImpl(producerJpaRepository, producerMapper);
    }
}
