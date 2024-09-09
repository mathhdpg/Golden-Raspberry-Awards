package com.mg.gra.application.gateways;

import java.util.List;
import java.util.Optional;

import com.mg.gra.domain.entity.Movie;

public interface MovieGateway {

    Optional<Movie> findById(Integer id);

    List<Movie> findAll();

    Movie save(Movie any);

    boolean existsByTitle(String title);

}
