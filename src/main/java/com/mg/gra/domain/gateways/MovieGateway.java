package com.mg.gra.domain.gateways;

import java.util.Optional;
import java.util.List;

import com.mg.gra.domain.models.Movie;

public interface MovieGateway {

    Optional<Movie> findById(Integer id);

    List<Movie> findAll();

    Movie save(Movie any);

    boolean existsByTitle(String title);

}
