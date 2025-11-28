package com.ortopunkt.crm.controller;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.crm.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public List<ApplicationResponseDto> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto> getApplicationById(@PathVariable("id") Long id) {
        return applicationService.getApplicationById(id)
                .map(applicationService::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto> create(@RequestBody @Valid ApplicationRequestDto dto) {
        return ResponseEntity.ok(applicationService.create(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status
    ) {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok().build();
    }
}