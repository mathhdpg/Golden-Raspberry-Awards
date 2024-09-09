package com.mg.gra.domain.entity;

import java.util.List;

public class Movie {

    private Long id;

    private String title;

    private int year;

    private boolean winner;

    private List<Producer> producers;

    public Movie(String title, int year, boolean winner, List<Producer> producers) {
        this(null, title, year, winner, producers);
    }

    public Movie(Long id, String title, int year, boolean winner, List<Producer> producers) {
        this.id = id;
        this.title = title;
        this.year = year;
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

    public boolean isWinner() {
        return winner;
    }

    public List<Producer> getProducers() {
        return producers;
    }

}