package com.avijeet.launchpad.presentation.controller;




import com.avijeet.launchpad.application.service.ConfigLifeCycleService;
import com.avijeet.launchpad.domain.model.ConfigMaster;
import com.avijeet.launchpad.presentation.dto.ConfigRequest;
import com.avijeet.launchpad.presentation.dto.ConfigResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configs")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigLifeCycleService configService;

    @PutMapping("/{configKey}")
    public ResponseEntity<ConfigResponse> createOrUpdate(
            @PathVariable String configKey,
            @RequestBody ConfigRequest request,
            @RequestHeader("X-User-Id") String userId) {

        ConfigMaster updated = configService.createOrUpdate(
                configKey, request.payload(), request.apiEndpoint(), request.status(), userId);
        return ResponseEntity.ok(ConfigResponse.fromEntity(updated));
    }

    @GetMapping("/{configKey}")
    public ResponseEntity<ConfigResponse> getByKey(@PathVariable String configKey) {
        return ResponseEntity.ok(ConfigResponse.fromEntity(configService.getByKey(configKey)));
    }

    @GetMapping
    public ResponseEntity<List<ConfigResponse>> getAll() {
        List<ConfigResponse> responses = configService.getAllConfigs().stream()
                .map(ConfigResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{configKey}/diff")
    public ResponseEntity<JsonNode> generateDiff(
            @PathVariable String configKey,
            @RequestBody JsonNode proposedPayload) {
        return ResponseEntity.ok(configService.generateDiff(configKey, proposedPayload));
    }

    @PatchMapping("/{configKey}/status")
    public ResponseEntity<Void> toggleStatus(
            @PathVariable String configKey,
            @RequestParam String status,
            @RequestHeader("X-User-Id") String userId) {

        configService.toggleStatus(configKey, status, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{configKey}/history")
    public ResponseEntity<Object> getHistory(@PathVariable String configKey) {
        return ResponseEntity.ok(configService.getHistory(configKey));
    }

    @DeleteMapping("/{configKey}")
    public ResponseEntity<Void> deleteConfig(@PathVariable String configKey) {
        configService.deleteConfig(configKey);
        return ResponseEntity.noContent().build();
    }
}