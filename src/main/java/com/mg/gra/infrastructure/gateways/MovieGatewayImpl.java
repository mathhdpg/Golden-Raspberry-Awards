package com.mg.gra.infrastructure.gateways;

import java.util.List;
import java.util.Optional;

import com.mg.gra.domain.gateways.MovieGateway;
import com.mg.gra.domain.models.Movie;
import com.mg.gra.infrastructure.mapper.MovieMapper;
import com.mg.gra.infrastructure.model.MovieEntity;
import com.mg.gra.infrastructure.repository.MovieJpaRepository;

public class MovieGatewayImpl implements MovieGateway {

    private final MovieJpaRepository movieJpaRepository;

    private final MovieMapper movieMapper;

    public MovieGatewayImpl(MovieJpaRepository movieJpaRepository, MovieMapper movieMapper) {
        this.movieJpaRepository = movieJpaRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public Optional<Movie> findById(Integer id) {
        return movieJpaRepository.findById(id).map(movieMapper::toDomain);
    }

    @Override
    public List<Movie> findAll() {
        return movieJpaRepository.findAll().stream().map(movieMapper::toDomain).toList();
    }

    @Override
    public Movie save(Movie movie) {
        MovieEntity entity = movieMapper.toEntity(movie);
        MovieEntity savedEntity = movieJpaRepository.save(entity);
        return movieMapper.toDomain(savedEntity);
    }

}
