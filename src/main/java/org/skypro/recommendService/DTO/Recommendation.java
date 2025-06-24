package org.skypro.recommendService.DTO;

import java.util.UUID;


public class Recommendation {
    private final String name;
    private final UUID id;
    private final String text;

    public Recommendation(String name, UUID id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public UUID getId() {
        return id;
    }
}
