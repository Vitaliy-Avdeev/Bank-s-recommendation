package org.skypro.recommendService.DTO;

import lombok.Getter;

import java.util.UUID;


@Getter
public class Recommendation {
    private final String name;
    private final UUID id;
    private final String text;

    public Recommendation(String name, UUID id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

}
