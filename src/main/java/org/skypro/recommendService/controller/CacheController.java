package org.skypro.recommendService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.skypro.recommendService.DTO.InfoDTO;
import org.skypro.recommendService.service.ManagementService;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Управление системой.", description = "В данном разделе можно узнать информацию о системе и сбросить кеш.")
@RestController
@RequestMapping("/management")
public class CacheController {
    private final ManagementService managementService;
    private final BuildProperties buildProperties;

    public CacheController(ManagementService managementService, BuildProperties buildProperties) {
        this.managementService = managementService;
        this.buildProperties = buildProperties;
    }

    @Operation(summary = "Сброс кеша рекомендаций.", description = "Данный эндпоинт позволяет очистить все " +
            "закешированные результаты, что означает обновление базы данных.")

    @PostMapping("/clear-caches")
    public List<Void> clearCaches() {
        managementService.clearCache();
        return List.of();
    }

    @Operation(summary = "Информация о системе.", description = "Данный эндпоинт позволяет получить информацию о " +
            "названии и версии системы.")

    @GetMapping("/info")
    public List<InfoDTO> getServiceInfo() {
        return Collections.singletonList(new InfoDTO(
                buildProperties.getName(),
                buildProperties.getVersion()
        ));
    }
}
