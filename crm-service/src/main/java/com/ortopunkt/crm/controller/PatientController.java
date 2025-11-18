package com.ortopunkt.crm.controller;

import com.ortopunkt.dto.request.PatientRequestDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.crm.entity.Patient;
import com.ortopunkt.crm.service.patient.PatientService;
import com.ortopunkt.crm.service.patient.PatientMessageService;
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
    private final PatientMessageService patientMessageService;

    @Autowired
    public PatientController(PatientService patientService,
                             PatientMessageService patientMessageService) {
        this.patientService = patientService;
        this.patientMessageService = patientMessageService;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> create(@RequestBody @Valid PatientRequestDto dto) {
        return ResponseEntity.ok(patientService.createDto(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/message")
    public ResponseEntity<Void> createPatientAndApplication(
            @RequestParam Long chatId,
            @RequestParam String fullName,
            @RequestParam(required = false) String username,
            @RequestParam String text
    ) {
        patientMessageService.createOrAttachApplication(chatId, fullName, username, text);
        return ResponseEntity.ok().build();
    }
}