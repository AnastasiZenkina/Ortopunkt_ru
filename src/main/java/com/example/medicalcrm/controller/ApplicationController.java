package com.example.medicalcrm.controller;
import com.example.medicalcrm.dto.ApplicationRequestDto;
import com.example.medicalcrm.dto.ApplicationResponseDto;
import com.example.medicalcrm.entity.Application;
import com.example.medicalcrm.service.ApplicationService;
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
    public ApplicationController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @GetMapping
    public List <Application> getAllApplications(){
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id){
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto> create(@RequestBody @Valid ApplicationRequestDto dto) {
        Application saved = applicationService.create(dto);
        return ResponseEntity.ok(ApplicationResponseDto.fromEntity(saved));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id){
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

}
