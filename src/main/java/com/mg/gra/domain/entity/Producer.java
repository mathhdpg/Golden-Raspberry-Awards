package com.mg.gra.domain.entity;

public class Producer {

    private Long id;

    private String name;

    public Producer(String name) {
        this(null, name);
    }

    public Producer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
