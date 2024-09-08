package com.mg.gra.infrastructure.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "gra_producer")
public class ProducerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gra_producer")
    @SequenceGenerator(name = "seq_gra_producer", sequenceName = "seq_gra_producer", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "producers")
    private List<MovieEntity> movies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MovieEntity> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieEntity> movies) {
        this.movies = movies;
    }

}
