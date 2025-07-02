package org.skypro.BankS.DTO;

import java.util.UUID;


public class RecommendationObject {
    private final UUID id;
    private final String name;
    private final String text;

    public RecommendationObject(UUID id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
