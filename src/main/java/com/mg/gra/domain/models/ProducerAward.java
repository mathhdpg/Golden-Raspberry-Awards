package com.mg.gra.domain.models;

public class ProducerAward {

    private final String producerName;
    private final int movieYear;

    public ProducerAward(String producerName, int movieYear) {
        this.producerName = producerName;
        this.movieYear = movieYear;
    }

    public String getProducerName() {
        return producerName;
    }

    public int getMovieYear() {
        return movieYear;
    }

}