package com.mg.gra.domain.models;

import java.util.List;

public class Movie {

    private Long id;

    private String title;

    private int year;

    private String studios;

    private boolean winner;

    private List<Producer> producers;

    public Movie(String title, int year, String studios, boolean winner, List<Producer> producers) {
        this(null, title, year, studios, winner, producers);
    }

    public Movie(Long id, String title, int year, String studios, boolean winner, List<Producer> producers) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Movie title cannot be null or empty");

        if (studios == null || studios.isBlank())
            throw new IllegalArgumentException("Movie studios cannot be null or empty");

        this.id = id;
        this.title = title.trim();
        this.year = year;
        this.studios = studios.trim();
        this.winner = winner;
        this.producers = producers;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getStudios() {
        return studios;
    }

    public boolean isWinner() {
        return winner;
    }

    public List<Producer> getProducers() {
        return producers;
    }

}