package com.neighbourhub.permissions.controller;


import com.neighbourhub.permissions.entity.Permission;
import com.neighbourhub.permissions.service.CommunityPermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Tag(name = "Управление правами", description = "API для работы с правами в сообществе")
@RequiredArgsConstructor
public class PermissionController {

    private final CommunityPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Permission>> getPermissions() {
        return ResponseEntity.ok(permissionService.getPermissions());
    }

}
