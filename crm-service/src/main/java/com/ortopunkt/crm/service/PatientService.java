package com.ortopunkt.crm.service;

import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.crm.entity.Patient;
import com.ortopunkt.crm.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient savePatient(Patient patient) {
        if (patient.getUsername() == null || patient.getUsername().isBlank()) {
            patient.setUsername("unknown");
        }
        if (patient.getName() == null || patient.getName().isBlank()) {
            patient.setName("Без имени");
        }
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public PatientResponseDto toResponseDto(Patient patient) {
        PatientResponseDto dto = new PatientResponseDto();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setTgId(patient.getTgId());
        dto.setUsername(patient.getUsername());
        dto.setPaymentStatus(patient.getPaymentStatus());
        return dto;
    }
}