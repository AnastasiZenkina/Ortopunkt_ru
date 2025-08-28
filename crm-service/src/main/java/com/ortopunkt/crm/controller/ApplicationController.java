package com.ortopunkt.crm.controller;
import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.service.ApplicationService;
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

    @Autowired
    public ApplicationController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @GetMapping
    public List <ApplicationResponseDto> getAllApplications(){
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
        ApplicationResponseDto saved = applicationService.create(dto);
        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id){
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public Long countApplications(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
        return applicationService.countApplications(fromDate);
    }

    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<Void> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String paymentStatus
    ) {
        applicationService.updatePaymentStatus(id, paymentStatus);
        return ResponseEntity.ok().build();
    }
}

