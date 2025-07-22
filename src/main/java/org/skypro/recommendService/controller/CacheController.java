package org.skypro.recommendService.controller;

import org.skypro.recommendService.DTO.InfoDTO;
import org.skypro.recommendService.service.ManagementService;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/management")
public class CacheController {
    private final ManagementService managementService;
    private final BuildProperties buildProperties;

    public CacheController(ManagementService managementService, BuildProperties buildProperties) {
        this.managementService = managementService;
        this.buildProperties = buildProperties;
    }

    @PostMapping("/clear-caches")
    public List<Void> clearCaches() {
        managementService.clearCache();
        return List.of();
    }

    @GetMapping("/info")
    public List<InfoDTO> getServiceInfo() {
        return Collections.singletonList(new InfoDTO(
                buildProperties.getName(),
                buildProperties.getVersion()
        ));
    }
}
