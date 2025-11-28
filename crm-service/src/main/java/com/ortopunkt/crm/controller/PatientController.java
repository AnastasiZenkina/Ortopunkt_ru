package com.ortopunkt.crm.controller;

import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.crm.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientResponseDto> getAllPatients() {
        return patientService.getAllPatients()
                .stream()
                .map(p -> {
                    PatientResponseDto dto = new PatientResponseDto();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setTgId(p.getTgId());
                    dto.setUsername(p.getUsername());
                    dto.setPaymentStatus(p.getPaymentStatus());
                    return dto;
                })
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable("id") Long id) {
        return patientService.getPatientById(id)
                .map(p -> {
                    PatientResponseDto dto = new PatientResponseDto();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setTgId(p.getTgId());
                    dto.setUsername(p.getUsername());
                    dto.setPaymentStatus(p.getPaymentStatus());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/payment-status")
    public ResponseEntity<Void> updatePaymentStatus(
            @PathVariable("id") Long id,
            @RequestParam("value") String value
    ) {
        return patientService.getPatientById(id)
                .map(p -> {
                    p.setPaymentStatus(value);
                    patientService.savePatient(p);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}