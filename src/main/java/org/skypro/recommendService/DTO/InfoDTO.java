package org.skypro.recommendService.DTO;

public class InfoDTO {
    private final String name;
    private final String version;

    public InfoDTO(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}


