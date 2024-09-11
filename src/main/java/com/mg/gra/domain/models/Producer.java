package com.mg.gra.domain.models;

public class Producer {

    private Long id;

    private String name;

    public Producer(String name) {
        this(null, name);
    }

    public Producer(Long id, String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Producer name cannot be null or empty");

        this.id = id;
        this.name = name.trim();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
