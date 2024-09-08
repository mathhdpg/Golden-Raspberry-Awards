package com.mg.gra.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mg.gra.application.parser.MovieDataParser;
import com.mg.gra.application.useCase.ImportMoviesFromCSVUseCase;
import com.mg.gra.domain.gateways.MovieGateway;
import com.mg.gra.domain.gateways.ProducerGateway;
import com.mg.gra.infrastructure.gateways.MovieGatewayImpl;
import com.mg.gra.infrastructure.mapper.MovieMapper;
import com.mg.gra.infrastructure.parser.MovieDataParserImpl;
import com.mg.gra.infrastructure.repository.MovieJpaRepository;

@Configuration
public class MovieConfig {

    @Bean
    public ImportMoviesFromCSVUseCase importMoviesFromCSVUseCase(
            MovieGateway movieGateway,
            ProducerGateway producerGateway,
            MovieDataParser movieDataParser) {

        return new ImportMoviesFromCSVUseCase(movieGateway, producerGateway, movieDataParser());
    }

    @Bean
    public MovieDataParser movieDataParser() {
        return new MovieDataParserImpl();
    }

    @Bean
    public MovieGateway movieGateway(MovieJpaRepository movieJpaRepository, MovieMapper movieMapper) {
        return new MovieGatewayImpl(movieJpaRepository, movieMapper);
    }
}
