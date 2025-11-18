package com.ortopunkt.crm.controller;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.service.ApplicationService;
import com.ortopunkt.crm.service.patient.PatientMessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final PatientMessageService patientMessageService;

    @Autowired
    public ApplicationController(ApplicationService applicationService,
                                 PatientMessageService patientMessageService) {
        this.applicationService = applicationService;
        this.patientMessageService = patientMessageService;
    }

    @GetMapping
    public List<ApplicationResponseDto> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto> getApplicationById(@PathVariable("id") Long id) {
        try {
            return applicationService.getApplicationById(id)
                    .map(app -> ResponseEntity.ok(applicationService.toResponseDto(app)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto> create(@RequestBody @Valid ApplicationRequestDto dto) {
        ApplicationResponseDto saved = applicationService.create(dto);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/from-message")
    public ResponseEntity<ApplicationResponseDto> createFromMessage(@RequestBody @Valid ApplicationRequestDto dto) {
        Application app = patientMessageService.processMessage(
                dto.getChatId(),
                dto.getUsername(),
                dto.getFullName(),
                dto.getText()
        );
        Application saved = applicationService.saveApplication(app);
        return ResponseEntity.ok(applicationService.toResponseDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable("id") Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public Long countApplications(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
        return applicationService.countApplications(fromDate);
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