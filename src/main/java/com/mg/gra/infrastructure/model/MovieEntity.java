package com.mg.gra.infrastructure.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "gra_movie")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gra_movie")
    @SequenceGenerator(name = "seq_gra_movie", sequenceName = "seq_gra_movie", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "movie_year", nullable = false)
    private int year;

    @Column(nullable = false)
    private boolean winner;

    @ManyToMany
    @JoinTable(name = "gra_movie_producer", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "producer_id"))
    private List<ProducerEntity> producers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public List<ProducerEntity> getProducers() {
        return producers;
    }

    public void setProducers(List<ProducerEntity> producers) {
        this.producers = producers;
    }

}