package com.mg.gra.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.mg.gra.domain.entity.Movie;
import com.mg.gra.infrastructure.model.MovieEntity;

@Component
public class MovieMapper {

    private final ProducerMapper producerMapper;

    public MovieMapper(ProducerMapper producerMapper) {
        this.producerMapper = producerMapper;
    }

    public Movie toDomain(MovieEntity entity) {
        return new Movie(
                entity.getId(),
                entity.getTitle(),
                entity.getYear(),
                entity.isWinner(),
                producerMapper.toDomainList(entity.getProducers()));
    }

    public MovieEntity toEntity(Movie movie) {
        MovieEntity entity = new MovieEntity();
        entity.setId(movie.getId());
        entity.setTitle(movie.getTitle());
        entity.setYear(movie.getYear());
        entity.setProducers(producerMapper.toEntityList(movie.getProducers()));
        entity.setWinner(movie.isWinner());
        return entity;
    }

}