package com.mg.gra.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mg.gra.infrastructure.model.MovieEntity;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, Integer> {

    boolean existsByTitleAndYearAndStudios(String title, int year, String studios);

}